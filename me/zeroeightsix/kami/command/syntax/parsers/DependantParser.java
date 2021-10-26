package me.zeroeightsix.kami.command.syntax.parsers;

import me.zeroeightsix.kami.command.syntax.SyntaxChunk;

public class DependantParser extends AbstractParser {

    int dependantIndex;
    private DependantParser.Dependency dependancy;

    public DependantParser(int dependantIndex, DependantParser.Dependency dependancy) {
        this.dependantIndex = dependantIndex;
        this.dependancy = dependancy;
    }

    protected String getDefaultChunk(SyntaxChunk chunk) {
        return this.dependancy.getEscape();
    }

    public String getChunk(SyntaxChunk[] chunks, SyntaxChunk thisChunk, String[] values, String chunkValue) {
        return chunkValue != null && !chunkValue.equals("") ? "" : (values.length <= this.dependantIndex ? this.getDefaultChunk(thisChunk) : (values[this.dependantIndex] != null && !values[this.dependantIndex].equals("") ? this.dependancy.feed(values[this.dependantIndex]) : ""));
    }

    public static class Dependency {

        String[][] map = new String[0][];
        String escape;

        public Dependency(String[][] map, String escape) {
            this.map = map;
            this.escape = escape;
        }

        private String[] containsKey(String[][] map, String key) {
            String[][] astring = map;
            int i = map.length;

            for (int j = 0; j < i; ++j) {
                String[] s = astring[j];

                if (s[0].equals(key)) {
                    return s;
                }
            }

            return null;
        }

        public String feed(String food) {
            String[] entry = this.containsKey(this.map, food);

            return entry != null ? entry[1] : this.getEscape();
        }

        public String[][] getMap() {
            return this.map;
        }

        public String getEscape() {
            return this.escape;
        }
    }
}
