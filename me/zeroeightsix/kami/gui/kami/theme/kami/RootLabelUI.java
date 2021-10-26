package me.zeroeightsix.kami.gui.kami.theme.kami;

import me.zeroeightsix.kami.gui.rgui.component.AlignedComponent;
import me.zeroeightsix.kami.gui.rgui.component.use.Label;
import me.zeroeightsix.kami.gui.rgui.render.AbstractComponentUI;
import me.zeroeightsix.kami.gui.rgui.render.font.FontRenderer;
import org.lwjgl.opengl.GL11;

public class RootLabelUI extends AbstractComponentUI {

    public void renderComponent(Label component, FontRenderer a) {
        a = component.getFontRenderer();
        String[] lines = component.getLines();
        int y = 0;
        boolean shadow = component.isShadow();
        String[] astring = lines;
        int i = lines.length;

        for (int j = 0; j < i; ++j) {
            String s = astring[j];
            int x = 0;

            if (component.getAlignment() == AlignedComponent.Alignment.CENTER) {
                x = component.getWidth() / 2 - a.getStringWidth(s) / 2;
            } else if (component.getAlignment() == AlignedComponent.Alignment.RIGHT) {
                x = component.getWidth() - a.getStringWidth(s);
            }

            if (shadow) {
                a.drawStringWithShadow(x, y, 255, 255, 255, s);
            } else {
                a.drawString(x, y, s);
            }

            y += a.getFontHeight() + 3;
        }

        GL11.glDisable(3553);
        GL11.glDisable(3042);
    }

    public void handleSizeComponent(Label component) {
        String[] lines = component.getLines();
        int y = 0;
        int w = 0;
        String[] astring = lines;
        int i = lines.length;

        for (int j = 0; j < i; ++j) {
            String s = astring[j];

            w = Math.max(w, component.getFontRenderer().getStringWidth(s));
            y += component.getFontRenderer().getFontHeight() + 3;
        }

        component.setWidth(w);
        component.setHeight(y);
    }
}
