package me.zeroeightsix.kami.gui.kami;

import java.util.ArrayList;
import java.util.Iterator;
import me.zeroeightsix.kami.gui.rgui.component.Component;
import me.zeroeightsix.kami.gui.rgui.component.container.Container;
import me.zeroeightsix.kami.gui.rgui.layout.Layout;

public class Stretcherlayout implements Layout {

    public int COMPONENT_OFFSET_X = 10;
    public int COMPONENT_OFFSET_Y = 4;
    int blocks;
    int maxrows = -1;

    public Stretcherlayout(int blocks) {
        this.blocks = blocks;
    }

    public Stretcherlayout(int blocks, int fixrows) {
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
                w += y.getWidth() + this.COMPONENT_OFFSET_X;
                h = Math.max(h, y.getHeight());
                ++i;
                if (i >= this.blocks) {
                    width = Math.max(width, w);
                    height += h + this.COMPONENT_OFFSET_Y;
                    i = 0;
                    h = 0;
                    w = 0;
                }
            }
        }

        int i = 0;
        int j = 0;
        Iterator iterator = children.iterator();

        Component c;

        while (iterator.hasNext()) {
            c = (Component) iterator.next();
            if (c.doAffectLayout()) {
                c.setX(i + this.COMPONENT_OFFSET_X / 3);
                c.setY(j + this.COMPONENT_OFFSET_Y / 3);
                h = Math.max(c.getHeight(), h);
                i += width / this.blocks;
                if (i >= width) {
                    j += h + this.COMPONENT_OFFSET_Y;
                    i = 0;
                }
            }
        }

        container.setWidth(width);
        container.setHeight(height);
        width -= this.COMPONENT_OFFSET_X;
        iterator = children.iterator();

        while (iterator.hasNext()) {
            c = (Component) iterator.next();
            if (!c.doAffectLayout()) {
                return;
            }

            c.setWidth(width);
        }

    }

    public void setComponentOffsetWidth(int componentOffset) {
        this.COMPONENT_OFFSET_X = componentOffset;
    }

    public void setComponentOffsetHeight(int componentOffset) {
        this.COMPONENT_OFFSET_Y = componentOffset;
    }
}
