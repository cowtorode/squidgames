package net.torode.listeners;

import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.torode.player.GamePlayer;
import net.torode.WorldManager;

public final class PlayerConfig
{
    private PlayerConfig()
    {}

    public static void configEvent(AsyncPlayerConfigurationEvent event)
    {
        GamePlayer player = (GamePlayer) event.getPlayer();

        event.setSpawningInstance(WorldManager.getWorld());
        player.setRespawnPoint(WorldManager.getSpawn());
    }
}
