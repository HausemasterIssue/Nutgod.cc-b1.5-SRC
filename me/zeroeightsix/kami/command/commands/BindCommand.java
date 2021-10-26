package me.zeroeightsix.kami.command.commands;

import me.zeroeightsix.kami.command.Command;
import me.zeroeightsix.kami.command.syntax.ChunkBuilder;
import me.zeroeightsix.kami.command.syntax.parsers.ModuleParser;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.module.ModuleManager;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import me.zeroeightsix.kami.setting.builder.SettingBuilder;
import me.zeroeightsix.kami.util.Wrapper;

public class BindCommand extends Command {

    public static Setting modifiersEnabled = SettingBuilder.register(Settings.b("modifiersEnabled", false), "binds");

    public BindCommand() {
        super("bind", (new ChunkBuilder()).append("[module]|modifiers", true, new ModuleParser()).append("[key]|[on|off]", true).build());
    }

    public void call(String[] args) {
        if (args.length == 1) {
            Command.sendChatMessage("Please specify a module.");
        } else {
            String module = args[0];
            String rkey = args[1];

            if (module.equalsIgnoreCase("modifiers")) {
                if (rkey == null) {
                    sendChatMessage("Expected: on or off");
                } else {
                    if (rkey.equalsIgnoreCase("on")) {
                        BindCommand.modifiersEnabled.setValue(Boolean.valueOf(true));
                        sendChatMessage("Turned modifiers on.");
                    } else if (rkey.equalsIgnoreCase("off")) {
                        BindCommand.modifiersEnabled.setValue(Boolean.valueOf(false));
                        sendChatMessage("Turned modifiers off.");
                    } else {
                        sendChatMessage("Expected: on or off");
                    }

                }
            } else {
                Module m = ModuleManager.getModuleByName(module);

                if (m == null) {
                    sendChatMessage("Unknown module \'" + module + "\'!");
                } else if (rkey == null) {
                    sendChatMessage(m.getName() + " is bound to &b" + m.getBindName());
                } else {
                    int key = Wrapper.getKey(rkey);

                    if (rkey.equalsIgnoreCase("none")) {
                        key = -1;
                    }

                    if (key == 0) {
                        sendChatMessage("Unknown key \'" + rkey + "\'!");
                    } else {
                        m.getBind().setKey(key);
                        sendChatMessage("Bind for &b" + m.getName() + "&r set to &b" + rkey.toUpperCase());
                    }
                }
            }
        }
    }
}
