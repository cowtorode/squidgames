package net.torode;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.extras.MojangAuth;
import net.torode.game.SquidGameManager;

public final class Main
{
    private static final int PORT = 25565;

    private static void initEvents()
    {
        GlobalEventHandler handler = MinecraftServer.getGlobalEventHandler();

        handler.addListener(AsyncPlayerConfigurationEvent.class, PlayerConfigEvent::configEvent);
        handler.addListener(PlayerSpawnEvent.class, SquidGameManager::onSpawn);
    }

    private static void shutdown()
    {

    }

    public static void main(String[] args)
    {
        MinecraftServer server = MinecraftServer.init();

        MinecraftServer.getConnectionManager().setPlayerProvider(GamePlayer::new);
        MojangAuth.init();
        MinecraftServer.getSchedulerManager().buildShutdownTask(Main::shutdown);

        initEvents();

        server.start("0.0.0.0", PORT);
    }
}
