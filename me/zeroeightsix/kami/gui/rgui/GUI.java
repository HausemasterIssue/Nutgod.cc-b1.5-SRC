package me.zeroeightsix.kami.gui.rgui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import me.zeroeightsix.kami.gui.rgui.component.Component;
import me.zeroeightsix.kami.gui.rgui.component.container.AbstractContainer;
import me.zeroeightsix.kami.gui.rgui.component.container.Container;
import me.zeroeightsix.kami.gui.rgui.component.listen.KeyListener;
import me.zeroeightsix.kami.gui.rgui.component.listen.MouseListener;
import me.zeroeightsix.kami.gui.rgui.component.listen.TickListener;
import me.zeroeightsix.kami.gui.rgui.render.theme.Theme;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public abstract class GUI extends AbstractContainer {

    Component focus = null;
    boolean press = false;
    int x = 0;
    int y = 0;
    int button = 0;
    int mx = 0;
    int my = 0;
    long lastMS = System.currentTimeMillis();

    public GUI(Theme theme) {
        super(theme);
    }

    public abstract void initializeGUI();

    public abstract void destroyGUI();

    public void updateGUI() {
        this.catchMouse();
        this.catchKey();
    }

    public void handleKeyDown(int key) {
        if (this.focus != null) {
            this.focus.getTheme().getUIForComponent(this.focus).handleKeyDown(this.focus, key);
            ArrayList l = new ArrayList();

            for (Object p = this.focus; p != null; p = ((Component) p).getParent()) {
                l.add(0, p);
            }

            KeyListener.KeyEvent event = new KeyListener.KeyEvent(key);
            Iterator iterator = l.iterator();

            while (iterator.hasNext()) {
                Component a = (Component) iterator.next();

                a.getKeyListeners().forEach(accept<invokedynamic>(event));
            }

        }
    }

    public void handleKeyUp(int key) {
        if (this.focus != null) {
            this.focus.getTheme().getUIForComponent(this.focus).handleKeyUp(this.focus, key);
            ArrayList l = new ArrayList();

            for (Object p = this.focus; p != null; p = ((Component) p).getParent()) {
                l.add(0, p);
            }

            KeyListener.KeyEvent event = new KeyListener.KeyEvent(key);
            Iterator iterator = l.iterator();

            while (iterator.hasNext()) {
                Component a = (Component) iterator.next();

                a.getKeyListeners().forEach(accept<invokedynamic>(event));
            }

        }
    }

    public void catchKey() {
        if (this.focus != null) {
            while (Keyboard.next()) {
                if (Keyboard.getEventKeyState()) {
                    this.handleKeyDown(Keyboard.getEventKey());
                } else {
                    this.handleKeyUp(Keyboard.getEventKey());
                }
            }

        }
    }

    public void handleMouseDown(int x, int y) {
        Component c = this.getComponentAt(x, y);
        int[] real = calculateRealPosition(c);

        if (this.focus != null) {
            this.focus.setFocussed(false);
        }

        this.focus = c;
        if (!c.equals(this)) {
            Object l;

            for (l = c; !this.hasChild((Component) l); l = ((Component) l).getParent()) {
                ;
            }

            this.children.remove(l);
            this.children.add(l);
            Collections.sort(this.children, new Comparator() {
                public int compare(Component o1, Component o2) {
                    return o1.getPriority() - o2.getPriority();
                }
            });
        }

        this.focus.setFocussed(true);
        this.press = true;
        this.x = x;
        this.y = y;
        this.button = Mouse.getEventButton();
        this.getTheme().getUIForComponent(c).handleMouseDown(c, x - real[0], y - real[1], Mouse.getEventButton());
        ArrayList l1 = new ArrayList();

        for (Object p = this.focus; p != null; p = ((Component) p).getParent()) {
            l1.add(0, p);
        }

        MouseListener.MouseButtonEvent event = new MouseListener.MouseButtonEvent(x, y, this.button, this.focus);
        Iterator iterator = l1.iterator();

        while (iterator.hasNext()) {
            Component a = (Component) iterator.next();

            event.setX(event.getX() - a.getX());
            event.setY(event.getY() - a.getY());
            if (a instanceof Container) {
                event.setX(event.getX() - ((Container) a).getOriginOffsetX());
                event.setY(event.getY() - ((Container) a).getOriginOffsetY());
            }

            a.getMouseListeners().forEach(accept<invokedynamic>(event));
            if (event.isCancelled()) {
                break;
            }
        }

    }

    public void handleMouseRelease(int x, int y) {
        int button = Mouse.getEventButton();

        if (this.focus != null && button != -1) {
            int[] c1 = calculateRealPosition(this.focus);

            this.getTheme().getUIForComponent(this.focus).handleMouseRelease(this.focus, x - c1[0], y - c1[1], button);
            ArrayList real1 = new ArrayList();

            for (Object l1 = this.focus; l1 != null; l1 = ((Component) l1).getParent()) {
                real1.add(0, l1);
            }

            MouseListener.MouseButtonEvent ey = new MouseListener.MouseButtonEvent(x, y, button, this.focus);
            Iterator event1 = real1.iterator();

            while (event1.hasNext()) {
                Component a2 = (Component) event1.next();

                ey.setX(ey.getX() - a2.getX());
                ey.setY(ey.getY() - a2.getY());
                if (a2 instanceof Container) {
                    ey.setX(ey.getX() - ((Container) a2).getOriginOffsetX());
                    ey.setY(ey.getY() - ((Container) a2).getOriginOffsetY());
                }

                a2.getMouseListeners().forEach(accept<invokedynamic>(ey));
                if (ey.isCancelled()) {
                    break;
                }
            }

            this.press = false;
        } else {
            if (button != -1) {
                Component c = this.getComponentAt(x, y);
                int[] real = calculateRealPosition(c);

                this.getTheme().getUIForComponent(c).handleMouseRelease(c, x - real[0], y - real[1], button);
                ArrayList l = new ArrayList();

                for (Object p = c; p != null; p = ((Component) p).getParent()) {
                    l.add(0, p);
                }

                MouseListener.MouseButtonEvent event = new MouseListener.MouseButtonEvent(x, y, button, c);
                Iterator a = l.iterator();

                while (a.hasNext()) {
                    Component a1 = (Component) a.next();

                    event.setX(event.getX() - a1.getX());
                    event.setY(event.getY() - a1.getY());
                    if (a1 instanceof Container) {
                        event.setX(event.getX() - ((Container) a1).getOriginOffsetX());
                        event.setY(event.getY() - ((Container) a1).getOriginOffsetY());
                    }

                    a1.getMouseListeners().forEach(accept<invokedynamic>(event));
                    if (event.isCancelled()) {
                        break;
                    }
                }

                this.press = false;
            }

        }
    }

    public void handleWheel(int x, int y, int step) {
        if (step != 0) {
            Component c = this.getComponentAt(x, y);
            int[] real = calculateRealPosition(c);

            this.getTheme().getUIForComponent(c).handleScroll(c, x - real[0], y - real[1], step, step > 0);
            ArrayList l = new ArrayList();

            for (Object p = c; p != null; p = ((Component) p).getParent()) {
                l.add(0, p);
            }

            MouseListener.MouseScrollEvent event = new MouseListener.MouseScrollEvent(x, y, step > 0, c);
            Iterator iterator = l.iterator();

            while (iterator.hasNext()) {
                Component a = (Component) iterator.next();

                event.setX(event.getX() - a.getX());
                event.setY(event.getY() - a.getY());
                if (a instanceof Container) {
                    event.setX(event.getX() - ((Container) a).getOriginOffsetX());
                    event.setY(event.getY() - ((Container) a).getOriginOffsetY());
                }

                a.getMouseListeners().forEach(accept<invokedynamic>(event));
                if (event.isCancelled()) {
                    break;
                }
            }

        }
    }

    public void handleMouseDrag(int x, int y) {
        int[] real = calculateRealPosition(this.focus);
        int ex = x - real[0];
        int ey = y - real[1];

        this.getTheme().getUIForComponent(this.focus).handleMouseDrag(this.focus, ex, ey, this.button);
        ArrayList l = new ArrayList();

        for (Object p = this.focus; p != null; p = ((Component) p).getParent()) {
            l.add(0, p);
        }

        MouseListener.MouseButtonEvent event = new MouseListener.MouseButtonEvent(x, y, this.button, this.focus);
        Iterator iterator = l.iterator();

        while (iterator.hasNext()) {
            Component a = (Component) iterator.next();

            event.setX(event.getX() - a.getX());
            event.setY(event.getY() - a.getY());
            if (a instanceof Container) {
                event.setX(event.getX() - ((Container) a).getOriginOffsetX());
                event.setY(event.getY() - ((Container) a).getOriginOffsetY());
            }

            a.getMouseListeners().forEach(accept<invokedynamic>(event));
            if (event.isCancelled()) {
                break;
            }
        }

    }

    private void catchMouse() {
        while (Mouse.next()) {
            int x = Mouse.getX();
            int y = Mouse.getY();

            y = Display.getHeight() - y;
            if (this.press && this.focus != null && (this.x != x || this.y != y)) {
                this.handleMouseDrag(x, y);
            }

            if (Mouse.getEventButtonState()) {
                this.handleMouseDown(x, y);
            } else {
                this.handleMouseRelease(x, y);
            }

            if (Mouse.hasWheel()) {
                this.handleWheel(x, y, Mouse.getDWheel());
            }
        }

    }

    public void callTick(Container container) {
        container.getTickListeners().forEach(accept<invokedynamic>());
        Iterator iterator = container.getChildren().iterator();

        while (iterator.hasNext()) {
            Component c = (Component) iterator.next();

            if (c instanceof Container) {
                this.callTick((Container) c);
            } else {
                c.getTickListeners().forEach(accept<invokedynamic>());
            }
        }

    }

    public void update() {
        if (System.currentTimeMillis() - this.lastMS > 50L) {
            this.callTick(this);
            this.lastMS = System.currentTimeMillis();
        }

    }

    public void drawGUI() {
        this.renderChildren();
    }

    public Component getFocus() {
        return this.focus;
    }

    public static int[] calculateRealPosition(Component c) {
        int realX = c.getX();
        int realY = c.getY();

        if (c instanceof Container) {
            realX += ((Container) c).getOriginOffsetX();
            realY += ((Container) c).getOriginOffsetY();
        }

        for (Container parent = c.getParent(); parent != null; parent = parent.getParent()) {
            realX += parent.getX();
            realY += parent.getY();
            if (parent instanceof Container) {
                realX += ((Container) parent).getOriginOffsetX();
                realY += ((Container) parent).getOriginOffsetY();
            }
        }

        return new int[] { realX, realY};
    }

    private static void lambda$callTick$8(TickListener tickListener) {
        tickListener.onTick();
    }

    private static void lambda$callTick$7(TickListener tickListener) {
        tickListener.onTick();
    }

    private static void lambda$handleMouseDrag$6(MouseListener.MouseButtonEvent event, MouseListener listener) {
        listener.onMouseDrag(event);
    }

    private static void lambda$handleWheel$5(MouseListener.MouseScrollEvent event, MouseListener listener) {
        listener.onScroll(event);
    }

    private static void lambda$handleMouseRelease$4(MouseListener.MouseButtonEvent event, MouseListener listener) {
        listener.onMouseRelease(event);
    }

    private static void lambda$handleMouseRelease$3(MouseListener.MouseButtonEvent event, MouseListener listener) {
        listener.onMouseRelease(event);
    }

    private static void lambda$handleMouseDown$2(MouseListener.MouseButtonEvent event, MouseListener listener) {
        listener.onMouseDown(event);
    }

    private static void lambda$handleKeyUp$1(KeyListener.KeyEvent event, KeyListener keyListener) {
        keyListener.onKeyUp(event);
    }

    private static void lambda$handleKeyDown$0(KeyListener.KeyEvent event, KeyListener keyListener) {
        keyListener.onKeyDown(event);
    }
}
