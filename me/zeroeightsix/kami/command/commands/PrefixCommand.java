package me.zeroeightsix.kami.command.commands;

import me.zeroeightsix.kami.command.Command;
import me.zeroeightsix.kami.command.syntax.ChunkBuilder;

public class PrefixCommand extends Command {

    public PrefixCommand() {
        super("prefix", (new ChunkBuilder()).append("character").build());
    }

    public void call(String[] args) {
        if (args.length == 0) {
            Command.sendChatMessage("Please specify a new prefix!");
        } else {
            Command.commandPrefix.setValue(args[0]);
            Command.sendChatMessage("Prefix set to &b" + (String) Command.commandPrefix.getValue());
        }
    }
}
