package net.torode;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.extras.MojangAuth;
import net.torode.command.StartCommand;
import net.torode.command.StopCommand;
import net.torode.game.SquidGameManager;
import net.torode.listeners.PlayerConfig;
import net.torode.player.GamePlayer;
import net.torode.util.Scheduler;

public final class Main
{
    private static final int PORT = 25565;

    private static void initCommands()
    {
        CommandManager manager = MinecraftServer.getCommandManager();

        manager.register(new StopCommand(), new StartCommand());
    }

    private static void initEvents()
    {
        GlobalEventHandler handler = MinecraftServer.getGlobalEventHandler();

        handler.addListener(AsyncPlayerConfigurationEvent.class, PlayerConfig::configEvent)
               .addListener(PlayerSpawnEvent.class, SquidGameManager::onSpawn);
    }

    private static void shutdown()
    {
        MinecraftServer.LOGGER.info("Executing shutdown hook...");
        Scheduler.shutdownIO();
        Scheduler.shutdownGeneral();
    }

    public static void main(String[] args)
    {
        MinecraftServer server = MinecraftServer.init();

        MinecraftServer.getConnectionManager().setPlayerProvider(GamePlayer::new);
        MojangAuth.init();
        MinecraftServer.getSchedulerManager().buildShutdownTask(Main::shutdown);

        initCommands();
        initEvents();

        server.start("0.0.0.0", PORT);
    }
}
