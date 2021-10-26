package me.zeroeightsix.kami.setting.impl;

import java.util.function.BiConsumer;
import java.util.function.Predicate;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.converter.BooleanConverter;

public class BooleanSetting extends Setting {

    private static final BooleanConverter converter = new BooleanConverter();

    public BooleanSetting(Boolean value, Predicate restriction, BiConsumer consumer, String name, Predicate visibilityPredicate) {
        super(value, restriction, consumer, name, visibilityPredicate);
    }

    public BooleanConverter converter() {
        return BooleanSetting.converter;
    }
}
