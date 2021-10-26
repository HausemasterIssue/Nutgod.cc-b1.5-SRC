package me.zeroeightsix.kami.command.syntax;

public class SyntaxChunk {

    boolean headless;
    String head;
    String type;
    private boolean necessary;
    private SyntaxParser parser;
    public static final SyntaxChunk[] EMPTY = new SyntaxChunk[0];

    public SyntaxChunk(String head, String type, boolean necessary) {
        this.headless = false;
        this.head = head;
        this.type = type;
        this.necessary = necessary;
        this.parser = (chunks, thisChunk, values, chunkValue) -> {
            return chunkValue != null ? null : head + (this.isNecessary() ? "<" : "[") + type + (this.isNecessary() ? ">" : "]");
        };
    }

    public SyntaxChunk(String type, boolean necessary) {
        this("", type, necessary);
        this.headless = true;
    }

    public String getHead() {
        return this.head;
    }

    public boolean isHeadless() {
        return this.headless;
    }

    public boolean isNecessary() {
        return this.necessary;
    }

    public String getChunk(SyntaxChunk[] chunks, SyntaxChunk thisChunk, String[] args, String chunkValue) {
        String s = this.parser.getChunk(chunks, thisChunk, args, chunkValue);

        return s == null ? "" : s;
    }

    public String getType() {
        return this.type;
    }

    public void setParser(SyntaxParser parser) {
        this.parser = parser;
    }
}
