package me.zeroeightsix.kami.gui.rgui.component;

import java.util.ArrayList;
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

public interface Component {

    int getX();

    int getY();

    int getWidth();

    int getHeight();

    void setX(int i);

    void setY(int i);

    void setWidth(int i);

    void setHeight(int i);

    Component setMinimumWidth(int i);

    Component setMaximumWidth(int i);

    Component setMinimumHeight(int i);

    Component setMaximumHeight(int i);

    int getMinimumWidth();

    int getMaximumWidth();

    int getMinimumHeight();

    int getMaximumHeight();

    float getOpacity();

    void setOpacity(float f);

    boolean doAffectLayout();

    void setAffectLayout(boolean flag);

    Container getParent();

    void setParent(Container container);

    boolean liesIn(Component component);

    boolean isVisible();

    void setVisible(boolean flag);

    void setFocussed(boolean flag);

    boolean isFocussed();

    ComponentUI getUI();

    Theme getTheme();

    void setTheme(Theme theme);

    boolean isHovered();

    boolean isPressed();

    ArrayList getMouseListeners();

    void addMouseListener(MouseListener mouselistener);

    ArrayList getRenderListeners();

    void addRenderListener(RenderListener renderlistener);

    ArrayList getKeyListeners();

    void addKeyListener(KeyListener keylistener);

    ArrayList getUpdateListeners();

    void addUpdateListener(UpdateListener updatelistener);

    ArrayList getTickListeners();

    void addTickListener(TickListener ticklistener);

    void addPoof(IPoof ipoof);

    void callPoof(Class oclass, PoofInfo poofinfo);

    int getPriority();

    void kill();
}
