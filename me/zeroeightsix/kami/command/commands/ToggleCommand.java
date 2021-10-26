package me.zeroeightsix.kami.command.commands;

import me.zeroeightsix.kami.command.Command;
import me.zeroeightsix.kami.command.syntax.ChunkBuilder;
import me.zeroeightsix.kami.command.syntax.parsers.ModuleParser;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.module.ModuleManager;

public class ToggleCommand extends Command {

    public ToggleCommand() {
        super("toggle", (new ChunkBuilder()).append("module", true, new ModuleParser()).build());
    }

    public void call(String[] args) {
        if (args.length == 0) {
            Command.sendChatMessage("Please specify a module!");
        } else {
            Module m = ModuleManager.getModuleByName(args[0]);

            if (m == null) {
                Command.sendChatMessage("Unknown module \'" + args[0] + "\'");
            } else {
                m.toggle();
                Command.sendChatMessage(m.getName() + (m.isEnabled() ? " &aenabled" : " &cdisabled"));
            }
        }
    }
}
