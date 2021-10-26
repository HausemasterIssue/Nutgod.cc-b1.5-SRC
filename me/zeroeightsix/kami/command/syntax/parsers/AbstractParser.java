package me.zeroeightsix.kami.command.syntax.parsers;

import me.zeroeightsix.kami.command.syntax.SyntaxChunk;
import me.zeroeightsix.kami.command.syntax.SyntaxParser;

public abstract class AbstractParser implements SyntaxParser {

    public abstract String getChunk(SyntaxChunk[] asyntaxchunk, SyntaxChunk syntaxchunk, String[] astring, String s);

    protected String getDefaultChunk(SyntaxChunk chunk) {
        return (chunk.isHeadless() ? "" : chunk.getHead()) + (chunk.isNecessary() ? "<" : "[") + chunk.getType() + (chunk.isNecessary() ? ">" : "]");
    }
}
