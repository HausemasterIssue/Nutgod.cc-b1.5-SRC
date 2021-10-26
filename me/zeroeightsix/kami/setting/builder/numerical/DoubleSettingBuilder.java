package me.zeroeightsix.kami.setting.builder.numerical;

import java.util.function.BiConsumer;
import java.util.function.Predicate;
import me.zeroeightsix.kami.setting.impl.numerical.DoubleSetting;
import me.zeroeightsix.kami.setting.impl.numerical.NumberSetting;

public class DoubleSettingBuilder extends NumericalSettingBuilder {

    public NumberSetting build() {
        return new DoubleSetting((Double) this.initialValue, this.predicate(), this.consumer(), this.name, this.visibilityPredicate(), (Double) this.min, (Double) this.max);
    }

    public DoubleSettingBuilder withVisibility(Predicate predicate) {
        return (DoubleSettingBuilder) super.withVisibility(predicate);
    }

    public DoubleSettingBuilder withRestriction(Predicate predicate) {
        return (DoubleSettingBuilder) super.withRestriction(predicate);
    }

    public DoubleSettingBuilder withConsumer(BiConsumer consumer) {
        return (DoubleSettingBuilder) super.withConsumer(consumer);
    }

    public DoubleSettingBuilder withValue(Double value) {
        return (DoubleSettingBuilder) super.withValue((Number) value);
    }

    public DoubleSettingBuilder withRange(Double minimum, Double maximum) {
        return (DoubleSettingBuilder) super.withRange(minimum, maximum);
    }

    public DoubleSettingBuilder withMaximum(Double maximum) {
        return (DoubleSettingBuilder) super.withMaximum(maximum);
    }

    public DoubleSettingBuilder withListener(BiConsumer consumer) {
        return (DoubleSettingBuilder) super.withListener(consumer);
    }

    public DoubleSettingBuilder withName(String name) {
        return (DoubleSettingBuilder) super.withName(name);
    }

    public DoubleSettingBuilder withMinimum(Double minimum) {
        return (DoubleSettingBuilder) super.withMinimum(minimum);
    }
}
