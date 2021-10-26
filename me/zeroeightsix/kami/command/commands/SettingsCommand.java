package me.zeroeightsix.kami.command.commands;

import java.util.List;
import me.zeroeightsix.kami.command.Command;
import me.zeroeightsix.kami.command.syntax.ChunkBuilder;
import me.zeroeightsix.kami.command.syntax.parsers.ModuleParser;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.module.ModuleManager;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.impl.EnumSetting;

public class SettingsCommand extends Command {

    public SettingsCommand() {
        super("settings", (new ChunkBuilder()).append("module", true, new ModuleParser()).build());
    }

    public void call(String[] args) {
        if (args[0] == null) {
            Command.sendChatMessage("Please specify a module to display the settings of.");
        } else {
            Module m = ModuleManager.getModuleByName(args[0]);

            if (m == null) {
                Command.sendChatMessage("Couldn\'t find a module &b" + args[0] + "!");
            } else {
                List settings = m.settingList;
                String[] result = new String[settings.size()];

                for (int i = 0; i < settings.size(); ++i) {
                    Setting setting = (Setting) settings.get(i);

                    result[i] = "&b" + setting.getName() + "&3(=" + setting.getValue() + ")  &ftype: &3" + setting.getValue().getClass().getSimpleName();
                    if (setting instanceof EnumSetting) {
                        result[i] = result[i] + "  (";
                        Enum[] enums = (Enum[]) ((Enum[]) ((EnumSetting) setting).clazz.getEnumConstants());
                        Enum[] aenum = enums;
                        int i = enums.length;

                        for (int j = 0; j < i; ++j) {
                            Enum e = aenum[j];

                            result[i] = result[i] + e.name() + ", ";
                        }

                        result[i] = result[i].substring(0, result[i].length() - 2) + ")";
                    }
                }

                Command.sendStringChatMessage(result);
            }
        }
    }
}
