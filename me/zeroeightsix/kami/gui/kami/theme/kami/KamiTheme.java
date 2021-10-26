package me.zeroeightsix.kami.gui.kami.theme.kami;

import me.zeroeightsix.kami.gui.kami.KamiGUI;
import me.zeroeightsix.kami.gui.kami.theme.staticui.RadarUI;
import me.zeroeightsix.kami.gui.kami.theme.staticui.TabGuiUI;
import me.zeroeightsix.kami.gui.rgui.render.AbstractComponentUI;
import me.zeroeightsix.kami.gui.rgui.render.font.FontRenderer;
import me.zeroeightsix.kami.gui.rgui.render.theme.AbstractTheme;

public class KamiTheme extends AbstractTheme {

    FontRenderer fontRenderer;

    public KamiTheme() {
        this.installUI(new RootButtonUI());
        this.installUI(new KamiTheme.GUIUI());
        this.installUI(new RootGroupboxUI());
        this.installUI(new KamiFrameUI());
        this.installUI(new RootScrollpaneUI());
        this.installUI(new RootInputFieldUI());
        this.installUI(new RootLabelUI());
        this.installUI(new RootChatUI());
        this.installUI(new RootCheckButtonUI());
        this.installUI(new KamiActiveModulesUI());
        this.installUI(new KamiSettingsPanelUI());
        this.installUI(new RootSliderUI());
        this.installUI(new KamiEnumbuttonUI());
        this.installUI(new RootColorizedCheckButtonUI());
        this.installUI(new KamiUnboundSliderUI());
        this.installUI(new RadarUI());
        this.installUI(new TabGuiUI());
        this.fontRenderer = KamiGUI.fontRenderer;
    }

    public FontRenderer getFontRenderer() {
        return this.fontRenderer;
    }

    public class GUIUI extends AbstractComponentUI {

    }
}
