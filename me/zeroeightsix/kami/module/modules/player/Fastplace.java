package me.zeroeightsix.kami.module.modules.player;

import me.zeroeightsix.kami.module.Module;

@Module.Info(
    name = "Fastplace",
    category = Module.Category.PLAYER,
    description = "Nullifies block place delay"
)
public class Fastplace extends Module {

    public void onUpdate() {
        Fastplace.mc.rightClickDelayTimer = 0;
    }
}
