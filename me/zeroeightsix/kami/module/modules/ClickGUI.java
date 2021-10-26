package me.zeroeightsix.kami.module.modules;

import me.zeroeightsix.kami.gui.kami.DisplayGuiScreen;
import me.zeroeightsix.kami.module.Module;

@Module.Info(
    name = "clickGUI",
    description = "Opens the Click GUI",
    category = Module.Category.HIDDEN
)
public class ClickGUI extends Module {

    public ClickGUI() {
        this.getBind().setKey(25);
    }

    protected void onEnable() {
        if (!(ClickGUI.mc.currentScreen instanceof DisplayGuiScreen)) {
            ClickGUI.mc.displayGuiScreen(new DisplayGuiScreen(ClickGUI.mc.currentScreen));
        }

        this.disable();
    }
}
