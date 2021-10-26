package me.zeroeightsix.kami.module.modules.render;

import java.util.Stack;
import java.util.function.Function;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;

@Module.Info(
    name = "Brightness",
    description = "Makes everything brighter!",
    category = Module.Category.RENDER
)
public class Brightness extends Module {

    private Setting transition = this.register(Settings.b("Transition", true));
    private Setting seconds = this.register(Settings.floatBuilder("Seconds").withMinimum(Float.valueOf(0.0F)).withMaximum(Float.valueOf(10.0F)).withValue((Number) Float.valueOf(1.0F)).withVisibility(test<invokedynamic>(this)).build());
    private Setting mode;
    private Stack transitionStack;
    private static float currentBrightness = 0.0F;
    private static boolean inTransition = false;

    public Brightness() {
        this.mode = this.register(Settings.enumBuilder(Brightness.Transition.class).withName("Mode").withValue(Brightness.Transition.SINE).withVisibility(test<invokedynamic>(this)).build());
        this.transitionStack = new Stack();
    }

    private void addTransition(boolean isUpwards) {
        if (((Boolean) this.transition.getValue()).booleanValue()) {
            int length = (int) (((Float) this.seconds.getValue()).floatValue() * 20.0F);
            float[] values;

            switch ((Brightness.Transition) this.mode.getValue()) {
            case LINEAR:
                values = this.linear(length, isUpwards);
                break;

            case SINE:
                values = this.sine(length, isUpwards);
                break;

            default:
                values = new float[] { 0.0F};
            }

            float[] afloat = values;
            int i = values.length;

            for (int j = 0; j < i; ++j) {
                float v = afloat[j];

                this.transitionStack.add(Float.valueOf(v));
            }

            Brightness.inTransition = true;
        }

    }

    protected void onEnable() {
        super.onEnable();
        this.addTransition(true);
    }

    protected void onDisable() {
        this.setAlwaysListening(true);
        super.onDisable();
        this.addTransition(false);
    }

    public void onUpdate() {
        if (Brightness.inTransition) {
            if (this.transitionStack.isEmpty()) {
                Brightness.inTransition = false;
                this.setAlwaysListening(false);
                Brightness.currentBrightness = this.isEnabled() ? 1.0F : 0.0F;
            } else {
                Brightness.currentBrightness = ((Float) this.transitionStack.pop()).floatValue();
            }
        }

    }

    private float[] createTransition(int length, boolean upwards, Function function) {
        float[] transition = new float[length];

        for (int i = 0; i < length; ++i) {
            float v = ((Float) function.apply(Float.valueOf((float) i / (float) length))).floatValue();

            if (upwards) {
                v = 1.0F - v;
            }

            transition[i] = v;
        }

        return transition;
    }

    private float[] linear(int length, boolean polarity) {
        return this.createTransition(length, polarity, apply<invokedynamic>());
    }

    private float sine(float x) {
        return ((float) Math.sin(3.141592653589793D * (double) x - 1.5707963267948966D) + 1.0F) / 2.0F;
    }

    private float[] sine(int length, boolean polarity) {
        return this.createTransition(length, polarity, apply<invokedynamic>(this));
    }

    public static float getCurrentBrightness() {
        return Brightness.currentBrightness;
    }

    public static boolean isInTransition() {
        return Brightness.inTransition;
    }

    public static boolean shouldBeActive() {
        return isInTransition() || Brightness.currentBrightness == 1.0F;
    }

    private static Float lambda$linear$2(Float d) {
        return d;
    }

    private boolean lambda$new$1(Object o) {
        return ((Boolean) this.transition.getValue()).booleanValue();
    }

    private boolean lambda$new$0(Float o) {
        return ((Boolean) this.transition.getValue()).booleanValue();
    }

    public static enum Transition {

        LINEAR, SINE;
    }
}
