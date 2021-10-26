package me.zeroeightsix.kami.gui.kami.theme.kami;

import me.zeroeightsix.kami.gui.kami.RenderHelper;
import me.zeroeightsix.kami.gui.kami.component.SettingsPanel;
import me.zeroeightsix.kami.gui.rgui.render.AbstractComponentUI;
import me.zeroeightsix.kami.gui.rgui.render.font.FontRenderer;
import org.lwjgl.opengl.GL11;

public class RootSettingsPanelUI extends AbstractComponentUI {

    public void renderComponent(SettingsPanel component, FontRenderer fontRenderer) {
        GL11.glColor4f(1.0F, 0.33F, 0.33F, 0.2F);
        RenderHelper.drawOutlinedRoundedRectangle(0, 0, component.getWidth(), component.getHeight(), 6.0F, 0.14F, 0.14F, 0.14F, component.getOpacity(), 1.0F);
    }
}
