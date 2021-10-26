package me.zeroeightsix.kami.module.modules.player;

import me.zeroeightsix.kami.module.Module;

@Module.Info(
    name = "Fastbreak",
    category = Module.Category.PLAYER,
    description = "Nullifies block hit delay"
)
public class Fastbreak extends Module {

    public void onUpdate() {
        Fastbreak.mc.playerController.blockHitDelay = 0;
    }
}
