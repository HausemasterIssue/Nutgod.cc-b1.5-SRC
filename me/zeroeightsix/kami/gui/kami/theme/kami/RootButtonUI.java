package me.zeroeightsix.kami.gui.kami.theme.kami;

import java.awt.Color;
import me.zeroeightsix.kami.gui.kami.KamiGUI;
import me.zeroeightsix.kami.gui.kami.RenderHelper;
import me.zeroeightsix.kami.gui.rgui.component.container.Container;
import me.zeroeightsix.kami.gui.rgui.component.use.Button;
import me.zeroeightsix.kami.gui.rgui.render.AbstractComponentUI;
import me.zeroeightsix.kami.gui.rgui.render.font.FontRenderer;
import org.lwjgl.opengl.GL11;

public class RootButtonUI extends AbstractComponentUI {

    protected Color idleColour = new Color(163, 163, 163);
    protected Color downColour = new Color(255, 255, 255);

    public void renderComponent(Button component, FontRenderer ff) {
        GL11.glColor3f(0.22F, 0.22F, 0.22F);
        if (component.isHovered() || component.isPressed()) {
            GL11.glColor3f(0.26F, 0.26F, 0.26F);
        }

        RenderHelper.drawRoundedRectangle(0.0F, 0.0F, (float) component.getWidth(), (float) component.getHeight(), 3.0F);
        GL11.glColor3f(1.0F, 1.0F, 1.0F);
        GL11.glEnable(3553);
        KamiGUI.fontRenderer.drawString(component.getWidth() / 2 - KamiGUI.fontRenderer.getStringWidth(component.getName()) / 2, 0, component.isPressed() ? this.downColour : this.idleColour, component.getName());
        GL11.glDisable(3553);
        GL11.glDisable(3042);
    }

    public void handleAddComponent(Button component, Container container) {
        component.setWidth(KamiGUI.fontRenderer.getStringWidth(component.getName()) + 28);
        component.setHeight(KamiGUI.fontRenderer.getFontHeight() + 2);
    }
}
