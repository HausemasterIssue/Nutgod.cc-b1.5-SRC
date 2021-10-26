package me.zeroeightsix.kami.event;

import me.zero.alpine.type.Cancellable;
import me.zeroeightsix.kami.util.Wrapper;

public class KamiEvent extends Cancellable {

    private KamiEvent.Era era;
    private final float partialTicks;

    public KamiEvent() {
        this.era = KamiEvent.Era.PRE;
        this.partialTicks = Wrapper.getMinecraft().getRenderPartialTicks();
    }

    public KamiEvent.Era getEra() {
        return this.era;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }

    public static enum Era {

        PRE, PERI, POST;
    }
}
