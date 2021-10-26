package me.zeroeightsix.kami.gui.kami.theme.kami;

import java.awt.Color;
import me.zeroeightsix.kami.gui.kami.RootSmallFontRenderer;
import me.zeroeightsix.kami.gui.kami.component.EnumButton;
import me.zeroeightsix.kami.gui.rgui.component.container.Container;
import me.zeroeightsix.kami.gui.rgui.render.AbstractComponentUI;
import me.zeroeightsix.kami.gui.rgui.render.font.FontRenderer;
import org.lwjgl.opengl.GL11;

public class KamiEnumbuttonUI extends AbstractComponentUI {

    RootSmallFontRenderer smallFontRenderer = new RootSmallFontRenderer();
    protected Color idleColour = new Color(163, 163, 163);
    protected Color downColour = new Color(255, 255, 255);
    EnumButton modeComponent;
    long lastMS = System.currentTimeMillis();

    public void renderComponent(EnumButton component, FontRenderer aa) {
        if (System.currentTimeMillis() - this.lastMS > 3000L && this.modeComponent != null) {
            this.modeComponent = null;
        }

        int c = component.isPressed() ? 11184810 : 14540253;

        if (component.isHovered()) {
            c = (c & 8355711) << 1;
        }

        GL11.glColor3f(1.0F, 1.0F, 1.0F);
        GL11.glEnable(3553);
        int parts = component.getModes().length;
        double step = (double) component.getWidth() / (double) parts;
        double startX = step * (double) component.getIndex();
        double endX = step * (double) (component.getIndex() + 1);
        int height = component.getHeight();
        float downscale = 1.1F;

        GL11.glDisable(3553);
        GL11.glColor3f(0.59F, 0.05F, 0.11F);
        GL11.glBegin(1);
        GL11.glVertex2d(startX, (double) ((float) height / downscale));
        GL11.glVertex2d(endX, (double) ((float) height / downscale));
        GL11.glEnd();
        if (this.modeComponent != null && this.modeComponent.equals(component)) {
            this.smallFontRenderer.drawString(component.getWidth() / 2 - this.smallFontRenderer.getStringWidth(component.getIndexMode()) / 2, 0, c, component.getIndexMode());
        } else {
            this.smallFontRenderer.drawString(0, 0, c, component.getName());
            this.smallFontRenderer.drawString(component.getWidth() - this.smallFontRenderer.getStringWidth(component.getIndexMode()), 0, c, component.getIndexMode());
        }

        GL11.glDisable(3042);
    }

    public void handleSizeComponent(EnumButton component) {
        int width = 0;
        String[] astring = component.getModes();
        int i = astring.length;

        for (int j = 0; j < i; ++j) {
            String s = astring[j];

            width = Math.max(width, this.smallFontRenderer.getStringWidth(s));
        }

        component.setWidth(this.smallFontRenderer.getStringWidth(component.getName()) + width + 1);
        component.setHeight(this.smallFontRenderer.getFontHeight() + 2);
    }

    public void handleAddComponent(EnumButton component, Container container) {
        component.addPoof(new EnumButton.EnumbuttonIndexPoof() {
            public void execute(EnumButton component, EnumButton.EnumbuttonIndexPoof.EnumbuttonInfo info) {
                KamiEnumbuttonUI.this.modeComponent = component;
                KamiEnumbuttonUI.this.lastMS = System.currentTimeMillis();
            }
        });
    }
}
