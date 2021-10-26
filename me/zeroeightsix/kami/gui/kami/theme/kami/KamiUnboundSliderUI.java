package me.zeroeightsix.kami.gui.kami.theme.kami;

import me.zeroeightsix.kami.gui.kami.component.UnboundSlider;
import me.zeroeightsix.kami.gui.rgui.component.container.Container;
import me.zeroeightsix.kami.gui.rgui.render.AbstractComponentUI;
import me.zeroeightsix.kami.gui.rgui.render.font.FontRenderer;
import org.lwjgl.opengl.GL11;

public class KamiUnboundSliderUI extends AbstractComponentUI {

    public void renderComponent(UnboundSlider component, FontRenderer fontRenderer) {
        String s = component.getText() + ": " + component.getValue();
        int c = component.isPressed() ? 11184810 : 14540253;

        if (component.isHovered()) {
            c = (c & 8355711) << 1;
        }

        fontRenderer.drawString(component.getWidth() / 2 - fontRenderer.getStringWidth(s) / 2, component.getHeight() - fontRenderer.getFontHeight() / 2 - 4, c, s);
        GL11.glDisable(3042);
    }

    public void handleAddComponent(UnboundSlider component, Container container) {
        component.setHeight(component.getTheme().getFontRenderer().getFontHeight());
        component.setWidth(component.getTheme().getFontRenderer().getStringWidth(component.getText()));
    }
}
