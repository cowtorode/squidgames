package net.torode.game;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.timer.TaskSchedule;
import net.torode.player.GamePlayer;

import java.util.ArrayList;
import java.util.List;

import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.torode.game.GameState.*;

public final class SquidGameManager
{
    public static final int AUTO_START_THRESHOLD = 1;
    public static final int MAX_PLAYERS = 456;

    private static final List<GamePlayer> players;
    private static final List<GamePlayer> spectators;
    private static Game game;

    private static GameState state = WAIT;

    static
    {
        players = new ArrayList<>();
        spectators = new ArrayList<>();

        MinecraftServer.getSchedulerManager().scheduleTask(() ->
        {
            for (GamePlayer player : spectators)
            {
                player.sendActionBar(Component.text("You are a spectator.", GRAY));
            }
        }, TaskSchedule.seconds(0), TaskSchedule.seconds(2));
    }

    public static void start()
    {
        if (state != WAIT)
        {
            throw new IllegalStateException("Games are already started.");
        }

        state = PLAY;

        // set sleeping (or something similar) & teleport to spawn
        // guards come in & dialogue (w/ voice)
        // debt video
        // piggy bank
        // signature
        // go into spirally confusing room
        // picture taken
        // red light green light

        game = new RedLightGreenLight();
        game.enable();
        game.start();
    }

    private static void addSpectator(GamePlayer player)
    {
        player.sendActionBar(Component.text("You are a spectator.", GRAY));
        player.setSpectatorTrue();

        // Make it so non spectator players can't see this player
        for (Player online : MinecraftServer.getConnectionManager().getOnlinePlayers())
        {
            if (!((GamePlayer) online).isSpectator())
            {
                player.removeViewer(online);
            }
        }

        spectators.add(player);
    }

    private static void removeSpectator(GamePlayer player)
    {
        player.setSpectatorFalse();

        // Make it so everyone can see the spectator
        for (Player online : MinecraftServer.getConnectionManager().getOnlinePlayers())
        {
            player.addViewer(online);
        }

        spectators.remove(player);
    }

    public static void eliminatePlayer(GamePlayer player)
    {
        addSpectator(player);

        game.eliminatePlayer(player);
    }

    private static void addPlayer(GamePlayer player)
    {
        players.add(player);

        if (players.size() >= AUTO_START_THRESHOLD)
        {

        }
    }

    private static void removePlayer(GamePlayer player)
    {
        players.remove(player);
    }

    public static void spawnWhileWaiting(PlayerSpawnEvent event)
    {
        GamePlayer player = (GamePlayer) event.getPlayer();

        // number of players currently waiting before we add this new player
        int count = players.size();

        if (count > MAX_PLAYERS)
        {
            throw new IllegalStateException("Player count exceeded maximum of " + MAX_PLAYERS);
        }

        if (count == MAX_PLAYERS)
        {
            addSpectator(player);
        } else
        {
            addPlayer(player);
        }
    }

    public static void disconnectWhileWaiting(PlayerDisconnectEvent event)
    {
        GamePlayer player = (GamePlayer) event.getPlayer();

        if (player.isSpectator())
        {
            removeSpectator(player);
        } else
        {
            removePlayer(player);
        }
    }

    public static void onSpawn(PlayerSpawnEvent event)
    {
        // Player joins the server
        switch (state)
        {
            case WAIT:
                spawnWhileWaiting(event);
                break;
            case PLAY:
                addSpectator((GamePlayer) event.getPlayer());
                break;
        }
    }

    public static void onDisconnect(PlayerDisconnectEvent event)
    {
        switch (state)
        {
            case WAIT:
                disconnectWhileWaiting(event);
                break;
            case PLAY:

                break;
        }
    }
}
