package me.zeroeightsix.kami.module.modules.misc;

import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;

@Module.Info(
    name = "NoEntityTrace",
    category = Module.Category.MISC,
    description = "Blocks entities from stopping you from mining"
)
public class NoEntityTrace extends Module {

    private Setting mode;
    private static NoEntityTrace INSTANCE;

    public NoEntityTrace() {
        this.mode = this.register(Settings.e("Mode", NoEntityTrace.TraceMode.DYNAMIC));
        NoEntityTrace.INSTANCE = this;
    }

    public static boolean shouldBlock() {
        return NoEntityTrace.INSTANCE.isEnabled() && (NoEntityTrace.INSTANCE.mode.getValue() == NoEntityTrace.TraceMode.STATIC || NoEntityTrace.mc.playerController.isHittingBlock);
    }

    private static enum TraceMode {

        STATIC, DYNAMIC;
    }
}
