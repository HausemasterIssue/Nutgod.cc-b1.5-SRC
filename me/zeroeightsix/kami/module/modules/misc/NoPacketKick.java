package me.zeroeightsix.kami.module.modules.misc;

import me.zeroeightsix.kami.module.Module;

@Module.Info(
    name = "NoPacketKick",
    category = Module.Category.MISC,
    description = "Prevent large packets from kicking you"
)
public class NoPacketKick {

    private static NoPacketKick INSTANCE;

    public NoPacketKick() {
        NoPacketKick.INSTANCE = this;
    }

    public static boolean isEnabled() {
        NoPacketKick nopacketkick = NoPacketKick.INSTANCE;

        return isEnabled();
    }
}
