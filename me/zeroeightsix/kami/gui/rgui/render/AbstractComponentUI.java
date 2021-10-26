package me.zeroeightsix.kami.gui.rgui.render;

import java.lang.reflect.ParameterizedType;
import me.zeroeightsix.kami.gui.rgui.component.Component;
import me.zeroeightsix.kami.gui.rgui.component.container.Container;
import me.zeroeightsix.kami.gui.rgui.render.font.FontRenderer;

public abstract class AbstractComponentUI implements ComponentUI {

    private Class persistentClass = (Class) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    public void renderComponent(Component component, FontRenderer fontRenderer) {}

    public void handleMouseDown(Component component, int x, int y, int button) {}

    public void handleMouseRelease(Component component, int x, int y, int button) {}

    public void handleMouseDrag(Component component, int x, int y, int button) {}

    public void handleScroll(Component component, int x, int y, int amount, boolean up) {}

    public void handleAddComponent(Component component, Container container) {}

    public void handleKeyDown(Component component, int key) {}

    public void handleKeyUp(Component component, int key) {}

    public void handleSizeComponent(Component component) {}

    public Class getHandledClass() {
        return this.persistentClass;
    }
}
