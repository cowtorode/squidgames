package net.torode.util;

import net.minestom.server.MinecraftServer;

import java.util.concurrent.*;

public final class Scheduler
{
    private static final ForkJoinPool generalWorker;
    private static final ExecutorService ioWorker;

    private Scheduler()
    {
    }

    static
    {
        generalWorker = new ForkJoinPool(Math.max(Runtime.getRuntime().availableProcessors() / 3, 3));
        ioWorker = Executors.newSingleThreadExecutor();
    }

    public static Executor async()
    {
        return generalWorker;
    }

    public static Executor ioAsync()
    {
        return ioWorker;
    }

    public static Executor sync()
    {
        return MinecraftServer.getSchedulerManager();
    }

    public static void runAsync(Runnable runnable)
    {
        async().execute(runnable);
    }

    public static void runSync(Runnable runnable)
    {
        sync().execute(runnable);
    }

    public static void shutdownGeneral()
    {
        generalWorker.shutdown(); // Initiates an orderly shutdown
        try
        {
            if (!generalWorker.awaitTermination(60, TimeUnit.SECONDS))
            {
                generalWorker.shutdownNow(); // Forces shutdown if tasks are still running
                MinecraftServer.LOGGER.error("Timed out waiting for the general worker thread(s) to shutdown.");
            }
        } catch (InterruptedException e)
        {
            MinecraftServer.LOGGER.error("General worker thread shutdown interrupted", e);
        }
    }

    public static void shutdownIO()
    {
        ioWorker.shutdown(); // Initiates an orderly shutdown
        try
        {
            if (!ioWorker.awaitTermination(60, TimeUnit.SECONDS))
            {
                ioWorker.shutdownNow(); // Forces shutdown if tasks are still running
                MinecraftServer.LOGGER.error("Timed out waiting for the io worker thread(s) to shutdown.");
            }
        } catch (InterruptedException e)
        {
            MinecraftServer.LOGGER.error("General worker thread shutdown interrupted", e);
        }
    }
}
