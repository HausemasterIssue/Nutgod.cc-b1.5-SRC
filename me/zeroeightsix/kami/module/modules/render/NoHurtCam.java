package me.zeroeightsix.kami.module.modules.render;

import me.zeroeightsix.kami.module.Module;

@Module.Info(
    name = "NoHurtCam",
    category = Module.Category.RENDER,
    description = "Disables the \'hurt\' camera effect"
)
public class NoHurtCam extends Module {

    private static NoHurtCam INSTANCE;

    public NoHurtCam() {
        NoHurtCam.INSTANCE = this;
    }

    public static boolean shouldDisable() {
        return NoHurtCam.INSTANCE != null && NoHurtCam.INSTANCE.isEnabled();
    }
}
