package me.zeroeightsix.kami.command.commands;

import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Function;
import me.zeroeightsix.kami.KamiMod;
import me.zeroeightsix.kami.command.Command;
import me.zeroeightsix.kami.command.syntax.SyntaxChunk;

public class CommandsCommand extends Command {

    public CommandsCommand() {
        super("commands", SyntaxChunk.EMPTY);
    }

    public void call(String[] args) {
        KamiMod.getInstance().getCommandManager().getCommands().stream().sorted(Comparator.comparing((command) -> {
            return command.getLabel();
        })).forEach((command) -> {
            Command.sendChatMessage("&7" + Command.getCommandPrefix() + command.getLabel() + "&r ~ &8" + command.getDescription());
        });
    }
}
