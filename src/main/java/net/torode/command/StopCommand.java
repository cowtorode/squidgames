package net.torode.command;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;

public final class StopCommand extends Command
{
    public StopCommand()
    {
        super("stop");

        setDefaultExecutor(StopCommand::onStop);
    }

    private static void onStop(CommandSender sender, CommandContext context)
    {
        sender.sendMessage("Stopping...");

        // fixme permission
        MinecraftServer.getSchedulerManager().scheduleEndOfTick(MinecraftServer::stopCleanly);
    }
}
