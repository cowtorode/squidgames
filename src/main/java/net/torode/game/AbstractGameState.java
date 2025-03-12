package net.torode.game;

import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;

public interface AbstractGameState
{
    void spawnEvent(PlayerSpawnEvent event);

    void disconnectEvent(PlayerDisconnectEvent event);
}
