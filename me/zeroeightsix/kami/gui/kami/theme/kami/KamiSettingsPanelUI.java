package me.zeroeightsix.kami.gui.kami.theme.kami;

import me.zeroeightsix.kami.gui.kami.RenderHelper;
import me.zeroeightsix.kami.gui.kami.component.SettingsPanel;
import me.zeroeightsix.kami.gui.rgui.render.AbstractComponentUI;
import me.zeroeightsix.kami.gui.rgui.render.font.FontRenderer;
import org.lwjgl.opengl.GL11;

public class KamiSettingsPanelUI extends AbstractComponentUI {

    public void renderComponent(SettingsPanel component, FontRenderer fontRenderer) {
        super.renderComponent(component, fontRenderer);
        GL11.glLineWidth(2.0F);
        GL11.glColor4f(0.65F, 0.0F, 0.6F, 0.7F);
        RenderHelper.drawFilledRectangle(0.0F, 0.0F, (float) component.getWidth(), (float) component.getHeight());
        GL11.glColor3f(1.0F, 1.0F, 1.0F);
        GL11.glLineWidth(1.5F);
        RenderHelper.drawRectangle(0.0F, 0.0F, (float) component.getWidth(), (float) component.getHeight());
    }
}
