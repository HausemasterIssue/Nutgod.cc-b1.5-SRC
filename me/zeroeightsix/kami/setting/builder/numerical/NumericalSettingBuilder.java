package me.zeroeightsix.kami.setting.builder.numerical;

import java.util.function.BiConsumer;
import java.util.function.Predicate;
import me.zeroeightsix.kami.setting.builder.SettingBuilder;
import me.zeroeightsix.kami.setting.impl.numerical.NumberSetting;

public abstract class NumericalSettingBuilder extends SettingBuilder {

    protected Number min;
    protected Number max;

    public NumericalSettingBuilder withMinimum(Number minimum) {
        this.predicateList.add((t) -> {
            return t.doubleValue() >= minimum.doubleValue();
        });
        if (this.min == null || minimum.doubleValue() > this.min.doubleValue()) {
            this.min = minimum;
        }

        return this;
    }

    public NumericalSettingBuilder withMaximum(Number maximum) {
        this.predicateList.add((t) -> {
            return t.doubleValue() <= maximum.doubleValue();
        });
        if (this.max == null || maximum.doubleValue() < this.max.doubleValue()) {
            this.max = maximum;
        }

        return this;
    }

    public NumericalSettingBuilder withRange(Number minimum, Number maximum) {
        this.predicateList.add((t) -> {
            double doubleValue = t.doubleValue();

            return doubleValue >= minimum.doubleValue() && doubleValue <= maximum.doubleValue();
        });
        if (this.min == null || minimum.doubleValue() > this.min.doubleValue()) {
            this.min = minimum;
        }

        if (this.max == null || maximum.doubleValue() < this.max.doubleValue()) {
            this.max = maximum;
        }

        return this;
    }

    public NumericalSettingBuilder withListener(BiConsumer consumer) {
        this.consumer = consumer;
        return this;
    }

    public NumericalSettingBuilder withValue(Number value) {
        return (NumericalSettingBuilder) super.withValue(value);
    }

    public NumericalSettingBuilder withName(String name) {
        return (NumericalSettingBuilder) super.withName(name);
    }

    public abstract NumberSetting build();
}
