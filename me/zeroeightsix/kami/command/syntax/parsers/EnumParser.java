package me.zeroeightsix.kami.command.syntax.parsers;

import java.util.ArrayList;
import java.util.Collections;
import me.zeroeightsix.kami.command.syntax.SyntaxChunk;

public class EnumParser extends AbstractParser {

    String[] modes;

    public EnumParser(String[] modes) {
        this.modes = modes;
    }

    public String getChunk(SyntaxChunk[] chunks, SyntaxChunk thisChunk, String[] values, String chunkValue) {
        String[] s;
        int i;
        int j;
        String s1;

        if (chunkValue != null) {
            ArrayList arraylist = new ArrayList();

            s = this.modes;
            i = s.length;

            for (j = 0; j < i; ++j) {
                s1 = s[j];
                if (s1.toLowerCase().startsWith(chunkValue.toLowerCase())) {
                    arraylist.add(s1);
                }
            }

            if (arraylist.isEmpty()) {
                return "";
            } else {
                Collections.sort(arraylist);
                String s = (String) arraylist.get(0);

                return s.substring(chunkValue.length());
            }
        } else {
            String possibilities = "";

            s = this.modes;
            i = s.length;

            for (j = 0; j < i; ++j) {
                s1 = s[j];
                possibilities = possibilities + s1 + ":";
            }

            possibilities = possibilities.substring(0, possibilities.length() - 1);
            return (thisChunk.isHeadless() ? "" : thisChunk.getHead()) + (thisChunk.isNecessary() ? "<" : "[") + possibilities + (thisChunk.isNecessary() ? ">" : "]");
        }
    }
}
