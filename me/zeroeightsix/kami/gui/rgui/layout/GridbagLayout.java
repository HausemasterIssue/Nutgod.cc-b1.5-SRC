package me.zeroeightsix.kami.gui.rgui.layout;

import java.util.ArrayList;
import java.util.Iterator;
import me.zeroeightsix.kami.gui.rgui.component.Component;
import me.zeroeightsix.kami.gui.rgui.component.container.Container;

public class GridbagLayout implements Layout {

    private static final int COMPONENT_OFFSET = 10;
    int blocks;
    int maxrows = -1;

    public GridbagLayout(int blocks) {
        this.blocks = blocks;
    }

    public GridbagLayout(int blocks, int fixrows) {
        this.blocks = blocks;
        this.maxrows = fixrows;
    }

    public void organiseContainer(Container container) {
        int width = 0;
        int height = 0;
        int i = 0;
        int w = 0;
        int h = 0;
        ArrayList children = container.getChildren();
        Iterator x = children.iterator();

        while (x.hasNext()) {
            Component y = (Component) x.next();

            if (y.doAffectLayout()) {
                w += y.getWidth() + 10;
                h = Math.max(h, y.getHeight());
                ++i;
                if (i >= this.blocks) {
                    width = Math.max(width, w);
                    height += h + 10;
                    i = 0;
                    h = 0;
                    w = 0;
                }
            }
        }

        int i = 0;
        int j = 0;
        Iterator iterator = children.iterator();

        while (iterator.hasNext()) {
            Component c = (Component) iterator.next();

            if (c.doAffectLayout()) {
                c.setX(i + 3);
                c.setY(j + 3);
                h = Math.max(c.getHeight(), h);
                i += width / this.blocks;
                if (i >= width) {
                    j += h + 10;
                    i = 0;
                }
            }
        }

        container.setWidth(width);
        container.setHeight(height);
    }
}
