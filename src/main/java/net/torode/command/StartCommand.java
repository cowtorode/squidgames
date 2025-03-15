package net.torode.command;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.torode.game.SquidGameManager;

public final class StartCommand extends Command
{
    public StartCommand()
    {
        super("start");

        setDefaultExecutor(StartCommand::onStart);
    }

    private static void onStart(CommandSender sender, CommandContext context)
    {
        SquidGameManager.start();
    }
}
