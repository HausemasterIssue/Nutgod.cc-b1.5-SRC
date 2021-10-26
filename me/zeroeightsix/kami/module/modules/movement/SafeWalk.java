package me.zeroeightsix.kami.module.modules.movement;

import me.zeroeightsix.kami.module.Module;

@Module.Info(
    name = "SafeWalk",
    category = Module.Category.MOVEMENT,
    description = "Keeps you from walking off edges"
)
public class SafeWalk extends Module {

    private static SafeWalk INSTANCE;

    public SafeWalk() {
        SafeWalk.INSTANCE = this;
    }

    public static boolean shouldSafewalk() {
        return SafeWalk.INSTANCE.isEnabled();
    }
}
