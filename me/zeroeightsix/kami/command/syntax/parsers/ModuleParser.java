package me.zeroeightsix.kami.command.syntax.parsers;

import java.util.function.Predicate;
import me.zeroeightsix.kami.command.syntax.SyntaxChunk;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.module.ModuleManager;

public class ModuleParser extends AbstractParser {

    public String getChunk(SyntaxChunk[] chunks, SyntaxChunk thisChunk, String[] values, String chunkValue) {
        if (chunkValue == null) {
            return this.getDefaultChunk(thisChunk);
        } else {
            Module chosen = (Module) ModuleManager.getModules().stream().filter((module) -> {
                return module.getName().toLowerCase().startsWith(chunkValue.toLowerCase());
            }).findFirst().orElse((Object) null);

            return chosen == null ? null : chosen.getName().substring(chunkValue.length());
        }
    }
}
