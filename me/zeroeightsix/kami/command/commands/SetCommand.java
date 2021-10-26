package me.zeroeightsix.kami.command.commands;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import me.zeroeightsix.kami.command.Command;
import me.zeroeightsix.kami.command.syntax.ChunkBuilder;
import me.zeroeightsix.kami.command.syntax.parsers.ModuleParser;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.module.ModuleManager;
import me.zeroeightsix.kami.setting.ISettingUnknown;
import me.zeroeightsix.kami.setting.Setting;

public class SetCommand extends Command {

    public SetCommand() {
        super("set", (new ChunkBuilder()).append("module", true, new ModuleParser()).append("setting", true).append("value", true).build());
    }

    public void call(String[] args) {
        if (args[0] == null) {
            Command.sendChatMessage("Please specify a module!");
        } else {
            Module m = ModuleManager.getModuleByName(args[0]);

            if (m == null) {
                Command.sendChatMessage("Unknown module &b" + args[0] + "&r!");
            } else if (args[1] == null) {
                String optionalSetting1 = String.join(", ", (Iterable) m.settingList.stream().map((setting) -> {
                    return setting.getName();
                }).collect(Collectors.toList()));

                if (optionalSetting1.isEmpty()) {
                    Command.sendChatMessage("Module &b" + m.getName() + "&r has no settings.");
                } else {
                    Command.sendStringChatMessage(new String[] { "Please specify a setting! Choose one of the following:", optionalSetting1});
                }

            } else {
                Optional optionalSetting = m.settingList.stream().filter((setting1) -> {
                    return setting1.getName().equalsIgnoreCase(args[1]);
                }).findFirst();

                if (!optionalSetting.isPresent()) {
                    Command.sendChatMessage("Unknown setting &b" + args[1] + "&r in &b" + m.getName() + "&r!");
                } else {
                    ISettingUnknown setting = (ISettingUnknown) optionalSetting.get();

                    if (args[2] == null) {
                        Command.sendChatMessage("&b" + setting.getName() + "&r is a &3" + setting.getValueClass().getSimpleName() + "&r. Its current value is &3" + setting.getValueAsString());
                    } else {
                        try {
                            setting.setValueFromString(args[2]);
                            Command.sendChatMessage("Set &b" + setting.getName() + "&r to &3" + args[2] + "&r.");
                        } catch (Exception exception) {
                            exception.printStackTrace();
                            Command.sendChatMessage("Unable to set value! &6" + exception.getMessage());
                        }

                    }
                }
            }
        }
    }
}
