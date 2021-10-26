package me.zeroeightsix.kami.gui.kami.theme.kami;

import java.awt.Color;
import me.zeroeightsix.kami.gui.kami.KamiGUI;
import me.zeroeightsix.kami.gui.rgui.component.container.Container;
import me.zeroeightsix.kami.gui.rgui.component.use.CheckButton;
import me.zeroeightsix.kami.gui.rgui.render.AbstractComponentUI;
import me.zeroeightsix.kami.gui.rgui.render.font.FontRenderer;
import org.lwjgl.opengl.GL11;

public class RootCheckButtonUI extends AbstractComponentUI {

    protected Color backgroundColour = new Color(200, 56, 56);
    protected Color backgroundColourHover = new Color(255, 66, 66);
    protected Color idleColourNormal = new Color(200, 200, 200);
    protected Color downColourNormal = new Color(190, 190, 190);
    protected Color idleColourToggle = new Color(250, 120, 120);
    protected Color downColourToggle;

    public RootCheckButtonUI() {
        this.downColourToggle = this.idleColourToggle.brighter();
    }

    public void renderComponent(CheckButton component, FontRenderer ff) {
        GL11.glColor4f((float) this.backgroundColour.getRed() / 255.0F, (float) this.backgroundColour.getGreen() / 255.0F, (float) this.backgroundColour.getBlue() / 255.0F, component.getOpacity());
        if (component.isToggled()) {
            GL11.glColor3f(0.9F, (float) this.backgroundColour.getGreen() / 255.0F, (float) this.backgroundColour.getBlue() / 255.0F);
        }

        if (component.isHovered() || component.isPressed()) {
            GL11.glColor4f((float) this.backgroundColourHover.getRed() / 255.0F, (float) this.backgroundColourHover.getGreen() / 255.0F, (float) this.backgroundColourHover.getBlue() / 255.0F, component.getOpacity());
        }

        String text = component.getName();
        int c = component.isPressed() ? 11184810 : (component.isToggled() ? 16724787 : 14540253);

        if (component.isHovered()) {
            c = (c & 8355711) << 1;
        }

        GL11.glColor3f(1.0F, 1.0F, 1.0F);
        GL11.glEnable(3553);
        KamiGUI.fontRenderer.drawString(component.getWidth() / 2 - KamiGUI.fontRenderer.getStringWidth(text) / 2, KamiGUI.fontRenderer.getFontHeight() / 2 - 2, c, text);
        GL11.glDisable(3553);
        GL11.glDisable(3042);
    }

    public void handleAddComponent(CheckButton component, Container container) {
        component.setWidth(KamiGUI.fontRenderer.getStringWidth(component.getName()) + 28);
        component.setHeight(KamiGUI.fontRenderer.getFontHeight() + 2);
    }
}
