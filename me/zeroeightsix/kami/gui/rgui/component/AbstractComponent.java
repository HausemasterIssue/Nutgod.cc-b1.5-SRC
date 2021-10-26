package me.zeroeightsix.kami.gui.rgui.component;

import java.util.ArrayList;
import java.util.Iterator;
import me.zeroeightsix.kami.gui.kami.DisplayGuiScreen;
import me.zeroeightsix.kami.gui.rgui.GUI;
import me.zeroeightsix.kami.gui.rgui.component.container.Container;
import me.zeroeightsix.kami.gui.rgui.component.listen.KeyListener;
import me.zeroeightsix.kami.gui.rgui.component.listen.MouseListener;
import me.zeroeightsix.kami.gui.rgui.component.listen.RenderListener;
import me.zeroeightsix.kami.gui.rgui.component.listen.TickListener;
import me.zeroeightsix.kami.gui.rgui.component.listen.UpdateListener;
import me.zeroeightsix.kami.gui.rgui.poof.IPoof;
import me.zeroeightsix.kami.gui.rgui.poof.PoofInfo;
import me.zeroeightsix.kami.gui.rgui.render.ComponentUI;
import me.zeroeightsix.kami.gui.rgui.render.theme.Theme;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;

public abstract class AbstractComponent implements Component {

    int x;
    int y;
    int width;
    int height;
    int minWidth = Integer.MIN_VALUE;
    int minHeight = Integer.MIN_VALUE;
    int maxWidth = Integer.MAX_VALUE;
    int maxHeight = Integer.MAX_VALUE;
    protected int priority = 0;
    private Setting visible = Settings.b("Visible", true);
    float opacity = 1.0F;
    private boolean focus = false;
    ComponentUI ui;
    Theme theme;
    Container parent;
    boolean hover = false;
    boolean press = false;
    boolean drag = false;
    boolean affectlayout = true;
    ArrayList mouseListeners = new ArrayList();
    ArrayList renderListeners = new ArrayList();
    ArrayList keyListeners = new ArrayList();
    ArrayList updateListeners = new ArrayList();
    ArrayList tickListeners = new ArrayList();
    ArrayList poofs = new ArrayList();
    boolean workingy = false;
    boolean workingx = false;

    public AbstractComponent() {
        this.addMouseListener(new MouseListener() {
            public void onMouseDown(MouseListener.MouseButtonEvent event) {
                AbstractComponent.this.press = true;
            }

            public void onMouseRelease(MouseListener.MouseButtonEvent event) {
                AbstractComponent.this.press = false;
                AbstractComponent.this.drag = false;
            }

            public void onMouseDrag(MouseListener.MouseButtonEvent event) {
                AbstractComponent.this.drag = true;
            }

            public void onMouseMove(MouseListener.MouseMoveEvent event) {}

            public void onScroll(MouseListener.MouseScrollEvent event) {}
        });
    }

    public ComponentUI getUI() {
        if (this.ui == null) {
            this.ui = this.getTheme().getUIForComponent(this);
        }

        return this.ui;
    }

    public Container getParent() {
        return this.parent;
    }

    public void setParent(Container parent) {
        this.parent = parent;
    }

    public Theme getTheme() {
        return this.theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public void setFocussed(boolean focus) {
        this.focus = focus;
    }

    public boolean isFocussed() {
        return this.focus;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setY(int y) {
        int oldX = this.getX();
        int oldY = this.getY();

        this.y = y;
        if (!this.workingy) {
            this.workingy = true;
            this.getUpdateListeners().forEach(accept<invokedynamic>(this, oldX, oldY));
            if (this.getParent() != null) {
                this.getParent().getUpdateListeners().forEach(accept<invokedynamic>(this, oldX, oldY));
            }

            this.workingy = false;
        }

    }

    public void setX(int x) {
        int oldX = this.getX();
        int oldY = this.getY();

        this.x = x;
        if (!this.workingx) {
            this.workingx = true;
            this.getUpdateListeners().forEach(accept<invokedynamic>(this, oldX, oldY));
            if (this.getParent() != null) {
                this.getParent().getUpdateListeners().forEach(accept<invokedynamic>(this, oldX, oldY));
            }

            this.workingx = false;
        }

    }

    public void setWidth(int width) {
        width = Math.max(this.getMinimumWidth(), Math.min(width, this.getMaximumWidth()));
        int oldWidth = this.getWidth();
        int oldHeight = this.getHeight();

        this.width = width;
        this.getUpdateListeners().forEach(accept<invokedynamic>(this, oldWidth, oldHeight));
        if (this.getParent() != null) {
            this.getParent().getUpdateListeners().forEach(accept<invokedynamic>(this, oldWidth, oldHeight));
        }

    }

    public void setHeight(int height) {
        height = Math.max(this.getMinimumHeight(), Math.min(height, this.getMaximumHeight()));
        int oldWidth = this.getWidth();
        int oldHeight = this.getHeight();

        this.height = height;
        this.getUpdateListeners().forEach(accept<invokedynamic>(this, oldWidth, oldHeight));
        if (this.getParent() != null) {
            this.getParent().getUpdateListeners().forEach(accept<invokedynamic>(this, oldWidth, oldHeight));
        }

    }

    public boolean isVisible() {
        return ((Boolean) this.visible.getValue()).booleanValue();
    }

    public void setVisible(boolean visible) {
        this.visible.setValue(Boolean.valueOf(visible));
    }

    public int getPriority() {
        return this.priority;
    }

    public void kill() {
        this.setVisible(false);
    }

    private boolean isMouseOver() {
        int[] real = GUI.calculateRealPosition(this);
        int mx = DisplayGuiScreen.mouseX;
        int my = DisplayGuiScreen.mouseY;

        return real[0] <= mx && real[1] <= my && real[0] + this.getWidth() >= mx && real[1] + this.getHeight() >= my;
    }

    public boolean isHovered() {
        return this.isMouseOver() && !this.press;
    }

    public boolean isPressed() {
        return this.press;
    }

    public ArrayList getMouseListeners() {
        return this.mouseListeners;
    }

    public void addMouseListener(MouseListener listener) {
        if (!this.mouseListeners.contains(listener)) {
            this.mouseListeners.add(listener);
        }

    }

    public ArrayList getRenderListeners() {
        return this.renderListeners;
    }

    public void addRenderListener(RenderListener listener) {
        if (!this.renderListeners.contains(listener)) {
            this.renderListeners.add(listener);
        }

    }

    public ArrayList getKeyListeners() {
        return this.keyListeners;
    }

    public void addKeyListener(KeyListener listener) {
        if (!this.keyListeners.contains(listener)) {
            this.keyListeners.add(listener);
        }

    }

    public ArrayList getUpdateListeners() {
        return this.updateListeners;
    }

    public void addUpdateListener(UpdateListener listener) {
        if (!this.updateListeners.contains(listener)) {
            this.updateListeners.add(listener);
        }

    }

    public ArrayList getTickListeners() {
        return this.tickListeners;
    }

    public void addTickListener(TickListener listener) {
        if (!this.tickListeners.contains(listener)) {
            this.tickListeners.add(listener);
        }

    }

    public void addPoof(IPoof poof) {
        this.poofs.add(poof);
    }

    public void callPoof(Class target, PoofInfo info) {
        Iterator iterator = this.poofs.iterator();

        while (iterator.hasNext()) {
            IPoof poof = (IPoof) iterator.next();

            if (target.isAssignableFrom(poof.getClass()) && poof.getComponentClass().isAssignableFrom(this.getClass())) {
                poof.execute(this, info);
            }
        }

    }

    public boolean liesIn(Component container) {
        if (container.equals(this)) {
            return true;
        } else if (container instanceof Container) {
            Iterator iterator = ((Container) container).getChildren().iterator();

            boolean liesin;

            do {
                if (!iterator.hasNext()) {
                    return false;
                }

                Component component = (Component) iterator.next();

                if (component.equals(this)) {
                    return true;
                }

                liesin = false;
                if (component instanceof Container) {
                    liesin = this.liesIn((Container) component);
                }
            } while (!liesin);

            return true;
        } else {
            return false;
        }
    }

    public float getOpacity() {
        return this.opacity;
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    public int getMaximumHeight() {
        return this.maxHeight;
    }

    public int getMaximumWidth() {
        return this.maxWidth;
    }

    public int getMinimumHeight() {
        return this.minHeight;
    }

    public int getMinimumWidth() {
        return this.minWidth;
    }

    public Component setMaximumWidth(int width) {
        this.maxWidth = width;
        return this;
    }

    public Component setMaximumHeight(int height) {
        this.maxHeight = height;
        return this;
    }

    public Component setMinimumWidth(int width) {
        this.minWidth = width;
        return this;
    }

    public Component setMinimumHeight(int height) {
        this.minHeight = height;
        return this;
    }

    public boolean doAffectLayout() {
        return this.affectlayout;
    }

    public void setAffectLayout(boolean flag) {
        this.affectlayout = flag;
    }

    private void lambda$setHeight$7(int oldWidth, int oldHeight, UpdateListener listener) {
        listener.updateSize(this, oldWidth, oldHeight);
    }

    private void lambda$setHeight$6(int oldWidth, int oldHeight, UpdateListener listener) {
        listener.updateSize(this, oldWidth, oldHeight);
    }

    private void lambda$setWidth$5(int oldWidth, int oldHeight, UpdateListener listener) {
        listener.updateSize(this, oldWidth, oldHeight);
    }

    private void lambda$setWidth$4(int oldWidth, int oldHeight, UpdateListener listener) {
        listener.updateSize(this, oldWidth, oldHeight);
    }

    private void lambda$setX$3(int oldX, int oldY, UpdateListener listener) {
        listener.updateLocation(this, oldX, oldY);
    }

    private void lambda$setX$2(int oldX, int oldY, UpdateListener listener) {
        listener.updateLocation(this, oldX, oldY);
    }

    private void lambda$setY$1(int oldX, int oldY, UpdateListener listener) {
        listener.updateLocation(this, oldX, oldY);
    }

    private void lambda$setY$0(int oldX, int oldY, UpdateListener listener) {
        listener.updateLocation(this, oldX, oldY);
    }
}
