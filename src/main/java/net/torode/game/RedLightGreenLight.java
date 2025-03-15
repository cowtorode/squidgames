package net.torode.game;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.adventure.audience.Audiences;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.utils.NamespaceID;
import net.torode.player.GamePlayer;
import net.torode.player.meta.RedLightGreenLightMeta;
import net.torode.util.MathUtil;
import net.torode.util.Scheduler;
import net.torode.util.Util;

import static java.lang.Math.abs;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.minestom.server.MinecraftServer.LOGGER;

public final class RedLightGreenLight extends Game
{
    /**
     * True if players can walk, false if not.
     */
    private volatile boolean greenLight;
    private EventNode<Event> events = EventNode.all("rlgl");

    private static final long GREEN_LIGHT_LOWER_BOUND = 40 * 50;
    private static final long GREEN_LIGHT_UPPER_BOUND = 70 * 50;
    private static final long RED_LIGHT_LOWER_BOUND = 30 * 50;
    private static final long RED_LIGHT_UPPER_BOUND = 60 * 50;

    /**
     * How much time the player has to react to a red light
     * (250 ms)
     */
    private static final long RED_LIGHT_SAFETY_BUFFER = 14 * 50;
    /**
     * The amount of movement that players are allowed during red light before flagging.
     */
    private static final Pos marginOfError = new Pos(0.5, 0.5, 0.5, 15, 15);

    public RedLightGreenLight()
    {
        events.addListener(PlayerMoveEvent.class, this::onMove);
    }

    public void onMove(PlayerMoveEvent event)
    {
        GamePlayer player = (GamePlayer) event.getPlayer();

        // if the player is a spectator, they're already eliminated, they didn't join in time, or they're staff
        if (!player.isSpectator() && !greenLight)
        {
            Pos p0 = player.getPosition();
            Pos p1 = ((RedLightGreenLightMeta) player.getMeta()).getRedLightPos();

            double dx = abs(p0.x() - p1.x());
            double dy = abs(p0.y() - p1.y());
            double dz = abs(p0.z() - p1.z());
            double dyaw = abs(p0.yaw() - p1.yaw());
            double dpitch = abs(p0.pitch() - p1.pitch());

            if (dx > marginOfError.x() ||
                    dy > marginOfError.y() ||
                    dz > marginOfError.z() ||
                    dyaw > marginOfError.yaw() ||
                    dpitch > marginOfError.pitch())
            {
                player.sendMessage("difference (" + dx + ", " + dy + ", " + dz + ") " + dyaw + ", " + dpitch);

                SquidGameManager.eliminatePlayer(player);
            }
        }
    }

    @Override
    public void enable()
    {
        // Register red light green light event listeners
        MinecraftServer.getGlobalEventHandler().addChild(events);
    }

    @Override
    public void disable()
    {
        // Unregister red light green light event listeners (when the game is over)
        MinecraftServer.getGlobalEventHandler().removeChild(events);
    }

    private static final Particle BLOOD = Particle.BLOCK_CRUMBLE.withBlock(Block.REDSTONE_BLOCK);

    @Override
    public void eliminatePlayer(GamePlayer player)
    {
        player.sendMessage("eliminated");

        Pos pos = player.getPosition();

        Util.fireGunshot(player.getInstance(), pos.add(25, 10, 25), pos.add(0, 1.5, 0));

        // blood
        player.sendPacket(new ParticlePacket(BLOOD, false, false, pos.add(0, 1, 2), new Pos(0.1, 0.1, 0.1), 0.1f, 5));
    }

    /**
     * Allows players to move and blocks for the duration of that green light
     */
    private void greenLight()
    {
        Audiences.players().sendMessage(Component.text("Ged light", GREEN));

        synchronized (this)
        {
            greenLight = true;
        }

        try
        {
            Thread.sleep(MathUtil.randomLong(GREEN_LIGHT_LOWER_BOUND, GREEN_LIGHT_UPPER_BOUND));
        } catch (InterruptedException ie)
        {
            LOGGER.warn("Call to Thread#sleep failed", ie);
        }
    }

    /**
     * Disallows players to move and blocks for the duration of that red light
     */
    private void redLight()
    {
        Audiences.players().sendMessage(Component.text("Red light", RED));

        try
        {
            Thread.sleep(RED_LIGHT_SAFETY_BUFFER);
        } catch (InterruptedException ie)
        {
            LOGGER.warn("Call to Thread#sleep failed", ie);
        }

        for (Player player : MinecraftServer.getConnectionManager().getOnlinePlayers())
        {
            // fixme only run for alive players of the game
            ((RedLightGreenLightMeta) ((GamePlayer) player).getMeta()).setRedLightPos(player.getPosition());
        }

        synchronized (this)
        {
            greenLight = false;
        }

        try
        {
            Thread.sleep(MathUtil.randomLong(RED_LIGHT_LOWER_BOUND, RED_LIGHT_UPPER_BOUND));
        } catch (InterruptedException ie)
        {
            LOGGER.warn("Call to Thread#sleep failed", ie);
        }
    }

    public void start()
    {
        Scheduler.runAsync(() ->
        {
            for (int i = 0; i < 10; ++i)
            {
                greenLight();
                redLight();
            }
        });
    }
}
