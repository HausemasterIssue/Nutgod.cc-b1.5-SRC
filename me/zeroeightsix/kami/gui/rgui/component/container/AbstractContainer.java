package me.zeroeightsix.kami.gui.rgui.component.container;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import me.zeroeightsix.kami.gui.rgui.component.AbstractComponent;
import me.zeroeightsix.kami.gui.rgui.component.Component;
import me.zeroeightsix.kami.gui.rgui.poof.PoofInfo;
import me.zeroeightsix.kami.gui.rgui.poof.use.AdditionPoof;
import me.zeroeightsix.kami.gui.rgui.render.theme.Theme;
import org.lwjgl.opengl.GL11;

public abstract class AbstractContainer extends AbstractComponent implements Container {

    protected ArrayList children = new ArrayList();
    int originoffsetX = 0;
    int originoffsetY = 0;

    public AbstractContainer(Theme theme) {
        this.setTheme(theme);
    }

    public ArrayList getChildren() {
        return this.children;
    }

    public Container addChild(Component... components) {
        Component[] acomponent = components;
        int i = components.length;

        for (int j = 0; j < i; ++j) {
            Component component = acomponent[j];

            if (!this.children.contains(component)) {
                component.setTheme(this.getTheme());
                component.setParent(this);
                component.getUI().handleAddComponent(component, this);
                component.getUI().handleSizeComponent(component);
                ArrayList arraylist = this.children;

                synchronized (this.children) {
                    this.children.add(component);
                    Collections.sort(this.children, new Comparator() {
                        public int compare(Component o1, Component o2) {
                            return o1.getPriority() - o2.getPriority();
                        }
                    });
                    component.callPoof(AdditionPoof.class, (PoofInfo) null);
                }
            }
        }

        return this;
    }

    public Container removeChild(Component component) {
        if (this.children.contains(component)) {
            this.children.remove(component);
        }

        return this;
    }

    public boolean hasChild(Component component) {
        return this.children.contains(component);
    }

    public void renderChildren() {
        Iterator iterator = this.getChildren().iterator();

        while (iterator.hasNext()) {
            Component c = (Component) iterator.next();

            if (c.isVisible()) {
                GL11.glPushMatrix();
                GL11.glTranslatef((float) c.getX(), (float) c.getY(), 0.0F);
                c.getRenderListeners().forEach(accept<invokedynamic>());
                c.getUI().renderComponent(c, this.getTheme().getFontRenderer());
                if (c instanceof Container) {
                    GL11.glTranslatef((float) ((Container) c).getOriginOffsetX(), (float) ((Container) c).getOriginOffsetY(), 0.0F);
                    ((Container) c).renderChildren();
                    GL11.glTranslatef((float) (-((Container) c).getOriginOffsetX()), (float) (-((Container) c).getOriginOffsetY()), 0.0F);
                }

                c.getRenderListeners().forEach(accept<invokedynamic>());
                GL11.glTranslatef((float) (-c.getX()), (float) (-c.getY()), 0.0F);
                GL11.glPopMatrix();
            }
        }

    }

    public Component getComponentAt(int x, int y) {
        for (int i = this.getChildren().size() - 1; i >= 0; --i) {
            Component c = (Component) this.getChildren().get(i);

            if (c.isVisible()) {
                int componentX = c.getX() + this.getOriginOffsetX();
                int componentY = c.getY() + this.getOriginOffsetY();
                int componentWidth = c.getWidth();
                int componentHeight = c.getHeight();
                Container container;

                if (c instanceof Container) {
                    container = (Container) c;
                    boolean hit = container.penetrateTest(x - this.getOriginOffsetX(), y - this.getOriginOffsetY());

                    if (!hit) {
                        continue;
                    }

                    Component a = ((Container) c).getComponentAt(x - componentX, y - componentY);

                    if (a != c) {
                        return a;
                    }
                }

                if (x >= componentX && y >= componentY && x <= componentX + componentWidth && y <= componentY + componentHeight) {
                    if (c instanceof Container) {
                        container = (Container) c;
                        Component component = container.getComponentAt(x - componentX, y - componentY);

                        return component;
                    }

                    return c;
                }
            }
        }

        return this;
    }

    public void setWidth(int width) {
        super.setWidth(width + this.getOriginOffsetX());
    }

    public void setHeight(int height) {
        super.setHeight(height + this.getOriginOffsetY());
    }

    public void kill() {
        Iterator iterator = this.children.iterator();

        while (iterator.hasNext()) {
            Component c = (Component) iterator.next();

            c.kill();
        }

        super.kill();
    }

    public int getOriginOffsetX() {
        return this.originoffsetX;
    }

    public int getOriginOffsetY() {
        return this.originoffsetY;
    }

    public void setOriginOffsetX(int originoffsetX) {
        this.originoffsetX = originoffsetX;
    }

    public void setOriginOffsetY(int originoffsetY) {
        this.originoffsetY = originoffsetY;
    }

    public boolean penetrateTest(int x, int y) {
        return true;
    }
}
