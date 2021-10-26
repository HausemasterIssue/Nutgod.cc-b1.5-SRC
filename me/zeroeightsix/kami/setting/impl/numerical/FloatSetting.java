package me.zeroeightsix.kami.setting.impl.numerical;

import java.util.function.BiConsumer;
import java.util.function.Predicate;
import me.zeroeightsix.kami.setting.converter.AbstractBoxedNumberConverter;
import me.zeroeightsix.kami.setting.converter.BoxedFloatConverter;

public class FloatSetting extends NumberSetting {

    private static final BoxedFloatConverter converter = new BoxedFloatConverter();

    public FloatSetting(Float value, Predicate restriction, BiConsumer consumer, String name, Predicate visibilityPredicate, Float min, Float max) {
        super(value, restriction, consumer, name, visibilityPredicate, min, max);
    }

    public AbstractBoxedNumberConverter converter() {
        return FloatSetting.converter;
    }
}
