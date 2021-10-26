package me.zeroeightsix.kami.gui.kami.theme.kami;

import me.zeroeightsix.kami.gui.rgui.component.container.Container;
import me.zeroeightsix.kami.gui.rgui.component.container.use.Groupbox;
import me.zeroeightsix.kami.gui.rgui.render.AbstractComponentUI;
import me.zeroeightsix.kami.gui.rgui.render.font.FontRenderer;
import org.lwjgl.opengl.GL11;

public class RootGroupboxUI extends AbstractComponentUI {

    public void renderComponent(Groupbox component, FontRenderer fontRenderer) {
        GL11.glLineWidth(1.0F);
        fontRenderer.drawString(1, 1, component.getName());
        GL11.glColor3f(1.0F, 0.0F, 0.0F);
        GL11.glDisable(3553);
        GL11.glBegin(1);
        GL11.glVertex2d(0.0D, 0.0D);
        GL11.glVertex2d((double) component.getWidth(), 0.0D);
        GL11.glVertex2d((double) component.getWidth(), 0.0D);
        GL11.glVertex2d((double) component.getWidth(), (double) component.getHeight());
        GL11.glVertex2d((double) component.getWidth(), (double) component.getHeight());
        GL11.glVertex2d(0.0D, (double) component.getHeight());
        GL11.glVertex2d(0.0D, (double) component.getHeight());
        GL11.glVertex2d(0.0D, 0.0D);
        GL11.glEnd();
    }

    public void handleMouseDown(Groupbox component, int x, int y, int button) {}

    public void handleAddComponent(Groupbox component, Container container) {
        component.setWidth(100);
        component.setHeight(100);
        component.setOriginOffsetY(component.getTheme().getFontRenderer().getFontHeight() + 3);
    }
}
