package me.zeroeightsix.kami.command.syntax.parsers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import me.zeroeightsix.kami.command.syntax.SyntaxChunk;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

public class BlockParser extends AbstractParser {

    private static HashMap blockNames = new HashMap();

    public BlockParser() {
        if (BlockParser.blockNames.isEmpty()) {
            Iterator iterator = Block.REGISTRY.getKeys().iterator();

            while (iterator.hasNext()) {
                ResourceLocation resourceLocation = (ResourceLocation) iterator.next();

                BlockParser.blockNames.put(resourceLocation.toString().replace("minecraft:", "").replace("_", ""), Block.REGISTRY.getObject(resourceLocation));
            }

        }
    }

    public String getChunk(SyntaxChunk[] chunks, SyntaxChunk thisChunk, String[] values, String chunkValue) {
        try {
            if (chunkValue == null) {
                return (thisChunk.isHeadless() ? "" : thisChunk.getHead()) + (thisChunk.isNecessary() ? "<" : "[") + thisChunk.getType() + (thisChunk.isNecessary() ? ">" : "]");
            } else {
                HashMap e = new HashMap();
                Iterator p = BlockParser.blockNames.keySet().iterator();

                while (p.hasNext()) {
                    String e1 = (String) p.next();

                    if (e1.toLowerCase().startsWith(chunkValue.toLowerCase().replace("minecraft:", "").replace("_", ""))) {
                        e.put(e1, BlockParser.blockNames.get(e1));
                    }
                }

                if (e.isEmpty()) {
                    return "";
                } else {
                    TreeMap p1 = new TreeMap(e);
                    Entry e2 = p1.firstEntry();

                    return ((String) e2.getKey()).substring(chunkValue.length());
                }
            }
        } catch (Exception exception) {
            return "";
        }
    }

    public static Block getBlockFromName(String name) {
        return !BlockParser.blockNames.containsKey(name) ? null : (Block) BlockParser.blockNames.get(name);
    }

    public static Object getKeyFromValue(Map hm, Object value) {
        Iterator iterator = hm.keySet().iterator();

        Object o;

        do {
            if (!iterator.hasNext()) {
                return null;
            }

            o = iterator.next();
        } while (!hm.get(o).equals(value));

        return o;
    }

    public static String getNameFromBlock(Block b) {
        return !BlockParser.blockNames.containsValue(b) ? null : (String) getKeyFromValue(BlockParser.blockNames, b);
    }
}
