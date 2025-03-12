package net.torode;

import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;

public final class PlayerConfigEvent
{
    private PlayerConfigEvent()
    {}

    public static void configEvent(AsyncPlayerConfigurationEvent event)
    {
        GamePlayer player = (GamePlayer) event.getPlayer();

        event.setSpawningInstance(WorldManager.getWorld());
        player.setRespawnPoint(WorldManager.getSpawn());
    }
}
