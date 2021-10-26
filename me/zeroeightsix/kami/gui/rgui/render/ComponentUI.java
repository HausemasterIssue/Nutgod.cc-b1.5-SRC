package me.zeroeightsix.kami.gui.rgui.render;

import me.zeroeightsix.kami.gui.rgui.component.Component;
import me.zeroeightsix.kami.gui.rgui.component.container.Container;
import me.zeroeightsix.kami.gui.rgui.render.font.FontRenderer;

public interface ComponentUI {

    void renderComponent(Component component, FontRenderer fontrenderer);

    void handleMouseDown(Component component, int i, int j, int k);

    void handleMouseRelease(Component component, int i, int j, int k);

    void handleMouseDrag(Component component, int i, int j, int k);

    void handleScroll(Component component, int i, int j, int k, boolean flag);

    void handleKeyDown(Component component, int i);

    void handleKeyUp(Component component, int i);

    void handleAddComponent(Component component, Container container);

    void handleSizeComponent(Component component);

    Class getHandledClass();
}
