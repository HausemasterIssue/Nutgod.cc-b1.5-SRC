package me.zeroeightsix.kami.setting.impl.numerical;

import java.util.function.BiConsumer;
import java.util.function.Predicate;
import me.zeroeightsix.kami.setting.converter.AbstractBoxedNumberConverter;
import me.zeroeightsix.kami.setting.converter.BoxedIntegerConverter;

public class IntegerSetting extends NumberSetting {

    private static final BoxedIntegerConverter converter = new BoxedIntegerConverter();

    public IntegerSetting(Integer value, Predicate restriction, BiConsumer consumer, String name, Predicate visibilityPredicate, Integer min, Integer max) {
        super(value, restriction, consumer, name, visibilityPredicate, min, max);
    }

    public AbstractBoxedNumberConverter converter() {
        return IntegerSetting.converter;
    }
}
