package me.zeroeightsix.kami.setting.builder.numerical;

import java.util.function.BiConsumer;
import java.util.function.Predicate;
import me.zeroeightsix.kami.setting.impl.numerical.IntegerSetting;
import me.zeroeightsix.kami.setting.impl.numerical.NumberSetting;

public class IntegerSettingBuilder extends NumericalSettingBuilder {

    public NumberSetting build() {
        return new IntegerSetting((Integer) this.initialValue, this.predicate(), this.consumer(), this.name, this.visibilityPredicate(), (Integer) this.min, (Integer) this.max);
    }

    public IntegerSettingBuilder withMinimum(Integer minimum) {
        return (IntegerSettingBuilder) super.withMinimum(minimum);
    }

    public NumericalSettingBuilder withName(String name) {
        return (IntegerSettingBuilder) super.withName(name);
    }

    public IntegerSettingBuilder withListener(BiConsumer consumer) {
        return (IntegerSettingBuilder) super.withListener(consumer);
    }

    public IntegerSettingBuilder withMaximum(Integer maximum) {
        return (IntegerSettingBuilder) super.withMaximum(maximum);
    }

    public IntegerSettingBuilder withRange(Integer minimum, Integer maximum) {
        return (IntegerSettingBuilder) super.withRange(minimum, maximum);
    }

    public IntegerSettingBuilder withValue(Integer value) {
        return (IntegerSettingBuilder) super.withValue((Number) value);
    }

    public IntegerSettingBuilder withConsumer(BiConsumer consumer) {
        return (IntegerSettingBuilder) super.withConsumer(consumer);
    }

    public IntegerSettingBuilder withRestriction(Predicate predicate) {
        return (IntegerSettingBuilder) super.withRestriction(predicate);
    }

    public IntegerSettingBuilder withVisibility(Predicate predicate) {
        return (IntegerSettingBuilder) super.withVisibility(predicate);
    }
}
