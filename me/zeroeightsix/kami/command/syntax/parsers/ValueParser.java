package me.zeroeightsix.kami.command.syntax.parsers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import me.zeroeightsix.kami.command.syntax.SyntaxChunk;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.module.ModuleManager;
import me.zeroeightsix.kami.setting.Setting;

public class ValueParser extends AbstractParser {

    int moduleIndex;

    public ValueParser(int moduleIndex) {
        this.moduleIndex = moduleIndex;
    }

    public String getChunk(SyntaxChunk[] chunks, SyntaxChunk thisChunk, String[] values, String chunkValue) {
        if (this.moduleIndex <= values.length - 1 && chunkValue != null) {
            String module = values[this.moduleIndex];
            Module m = ModuleManager.getModuleByName(module);

            if (m == null) {
                return "";
            } else {
                HashMap possibilities = new HashMap();
                Iterator p = m.settingList.iterator();

                Setting aV;

                while (p.hasNext()) {
                    aV = (Setting) p.next();
                    if (aV.getName().toLowerCase().startsWith(chunkValue.toLowerCase())) {
                        possibilities.put(aV.getName(), aV);
                    }
                }

                if (possibilities.isEmpty()) {
                    return "";
                } else {
                    TreeMap p1 = new TreeMap(possibilities);

                    aV = (Setting) p1.firstEntry().getValue();
                    return aV.getName().substring(chunkValue.length());
                }
            }
        } else {
            return this.getDefaultChunk(thisChunk);
        }
    }
}
