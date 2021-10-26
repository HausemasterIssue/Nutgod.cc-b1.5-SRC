package me.zeroeightsix.kami.gui.kami.theme.kami;

import me.zeroeightsix.kami.gui.kami.RenderHelper;
import me.zeroeightsix.kami.gui.rgui.component.container.Container;
import me.zeroeightsix.kami.gui.rgui.component.use.InputField;
import me.zeroeightsix.kami.gui.rgui.render.AbstractComponentUI;
import me.zeroeightsix.kami.gui.rgui.render.font.FontRenderer;
import org.lwjgl.opengl.GL11;

public class RootInputFieldUI extends AbstractComponentUI {

    public void renderComponent(InputField component, FontRenderer fontRenderer) {
        GL11.glColor3f(0.33F, 0.22F, 0.22F);
        RenderHelper.drawFilledRectangle(0.0F, 0.0F, (float) component.getWidth(), (float) component.getHeight());
        GL11.glLineWidth(1.5F);
        GL11.glColor4f(1.0F, 0.33F, 0.33F, 0.6F);
        RenderHelper.drawRectangle(0.0F, 0.0F, (float) component.getWidth(), (float) component.getHeight());
    }

    public void handleAddComponent(InputField component, Container container) {
        component.setWidth(200);
        component.setHeight(component.getTheme().getFontRenderer().getFontHeight());
    }
}
