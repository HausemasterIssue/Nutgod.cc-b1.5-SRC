package me.zeroeightsix.kami.setting.impl.numerical;

import java.util.function.BiConsumer;
import java.util.function.Predicate;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.converter.AbstractBoxedNumberConverter;

public abstract class NumberSetting extends Setting {

    private final Number min;
    private final Number max;

    public NumberSetting(Number value, Predicate restriction, BiConsumer consumer, String name, Predicate visibilityPredicate, Number min, Number max) {
        super(value, restriction, consumer, name, visibilityPredicate);
        this.min = min;
        this.max = max;
    }

    public boolean isBound() {
        return this.min != null && this.max != null;
    }

    public abstract AbstractBoxedNumberConverter converter();

    public Number getValue() {
        return (Number) super.getValue();
    }

    public Number getMax() {
        return this.max;
    }

    public Number getMin() {
        return this.min;
    }
}
