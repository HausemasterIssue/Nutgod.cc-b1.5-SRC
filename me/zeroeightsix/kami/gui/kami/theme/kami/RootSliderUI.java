package me.zeroeightsix.kami.gui.kami.theme.kami;

import me.zeroeightsix.kami.gui.kami.RenderHelper;
import me.zeroeightsix.kami.gui.kami.RootSmallFontRenderer;
import me.zeroeightsix.kami.gui.rgui.component.container.Container;
import me.zeroeightsix.kami.gui.rgui.component.use.Slider;
import me.zeroeightsix.kami.gui.rgui.render.AbstractComponentUI;
import me.zeroeightsix.kami.gui.rgui.render.font.FontRenderer;
import org.lwjgl.opengl.GL11;

public class RootSliderUI extends AbstractComponentUI {

    RootSmallFontRenderer smallFontRenderer = new RootSmallFontRenderer();

    public void renderComponent(Slider component, FontRenderer aa) {
        GL11.glColor4f(1.0F, 0.33F, 0.33F, component.getOpacity());
        GL11.glLineWidth(2.5F);
        int height = component.getHeight();
        double w = (double) component.getWidth() * (component.getValue() / component.getMaximum());
        float downscale = 1.1F;

        GL11.glBegin(1);
        GL11.glVertex2d(0.0D, (double) ((float) height / downscale));
        GL11.glVertex2d(w, (double) ((float) height / downscale));
        GL11.glColor3f(0.33F, 0.33F, 0.33F);
        GL11.glVertex2d(w, (double) ((float) height / downscale));
        GL11.glVertex2d((double) component.getWidth(), (double) ((float) height / downscale));
        GL11.glEnd();
        GL11.glColor3f(1.0F, 0.33F, 0.33F);
        RenderHelper.drawCircle((float) ((int) w), (float) height / downscale, 2.0F);
        String s = "";

        if (Math.floor(component.getValue()) == component.getValue()) {
            s = s + (int) component.getValue();
        } else {
            s = s + component.getValue();
        }

        if (component.isPressed()) {
            w -= (double) (this.smallFontRenderer.getStringWidth(s) / 2);
            w = Math.max(0.0D, Math.min(w, (double) (component.getWidth() - this.smallFontRenderer.getStringWidth(s))));
            this.smallFontRenderer.drawString((int) w, 0, s);
        } else {
            this.smallFontRenderer.drawString(0, 0, component.getText());
            this.smallFontRenderer.drawString(component.getWidth() - this.smallFontRenderer.getStringWidth(s), 0, s);
        }

        GL11.glDisable(3553);
    }

    public void handleAddComponent(Slider component, Container container) {
        component.setHeight(component.getTheme().getFontRenderer().getFontHeight() + 2);
        component.setWidth(this.smallFontRenderer.getStringWidth(component.getText()) + this.smallFontRenderer.getStringWidth(component.getMaximum() + "") + 3);
    }
}
