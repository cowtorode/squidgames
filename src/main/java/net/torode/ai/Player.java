package net.torode.ai;

import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.*;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.play.EntityMetaDataPacket;
import net.minestom.server.network.packet.server.play.PlayerInfoUpdatePacket;
import net.torode.GamePlayer;

import java.util.ArrayList;
import java.util.Map;

public class Player extends Entity
{
    private final String name;

    public Player(String name, Instance instance, Pos location)
    {
        super(EntityType.PLAYER);

        this.name = name;

        scheduleNextTick(entity -> teleport(location));
        setBoundingBox(0, 0, 0);
        setNoGravity(true);
        setInstance(instance);
        setCustomName(Component.text(name));
        setCustomNameVisible(true);
    }

    public Player(GamePlayer player)
    {
        this(player.getUsername(), player.getInstance(), player.getPosition());
    }

    @Override
    public void updateNewViewer(net.minestom.server.entity.Player player) {
        var properties = new ArrayList<PlayerInfoUpdatePacket.Property>();
        var entry = new PlayerInfoUpdatePacket.Entry(getUuid(), name, properties, false, 0, GameMode.CREATIVE, null, null, 1);
        player.sendPacket(new PlayerInfoUpdatePacket(PlayerInfoUpdatePacket.Action.ADD_PLAYER, entry));
        super.updateNewViewer(player);
        player.sendPacket(new EntityMetaDataPacket(getEntityId(), Map.of(17, Metadata.Byte((byte) 127))));
    }
}
