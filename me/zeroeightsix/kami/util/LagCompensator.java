package me.zeroeightsix.kami.util;

import java.util.Arrays;
import java.util.EventListener;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.EventHook;
import me.zeroeightsix.kami.KamiMod;
import me.zeroeightsix.kami.event.events.PacketEvent;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.util.math.MathHelper;

public class LagCompensator implements EventListener {

    public static LagCompensator INSTANCE;
    private final float[] tickRates = new float[20];
    private int nextIndex = 0;
    private long timeLastTimeUpdate;
    @EventHandler
    me.zero.alpine.listener.Listener packetEventListener = new me.zero.alpine.listener.Listener((event) -> {
        if (event.getPacket() instanceof SPacketTimeUpdate) {
            LagCompensator.INSTANCE.onTimeUpdate();
        }

    }, new Predicate[0]);

    public LagCompensator() {
        KamiMod.EVENT_BUS.subscribe((Object) this);
        this.reset();
    }

    public void reset() {
        this.nextIndex = 0;
        this.timeLastTimeUpdate = -1L;
        Arrays.fill(this.tickRates, 0.0F);
    }

    public float getTickRate() {
        float numTicks = 0.0F;
        float sumTickRates = 0.0F;
        float[] afloat = this.tickRates;
        int i = afloat.length;

        for (int j = 0; j < i; ++j) {
            float tickRate = afloat[j];

            if (tickRate > 0.0F) {
                sumTickRates += tickRate;
                ++numTicks;
            }
        }

        return MathHelper.clamp(sumTickRates / numTicks, 0.0F, 20.0F);
    }

    public void onTimeUpdate() {
        if (this.timeLastTimeUpdate != -1L) {
            float timeElapsed = (float) (System.currentTimeMillis() - this.timeLastTimeUpdate) / 1000.0F;

            this.tickRates[this.nextIndex % this.tickRates.length] = MathHelper.clamp(20.0F / timeElapsed, 0.0F, 20.0F);
            ++this.nextIndex;
        }

        this.timeLastTimeUpdate = System.currentTimeMillis();
    }
}
