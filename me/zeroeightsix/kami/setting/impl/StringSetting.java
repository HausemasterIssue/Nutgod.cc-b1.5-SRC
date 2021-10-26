package me.zeroeightsix.kami.setting.impl;

import java.util.function.BiConsumer;
import java.util.function.Predicate;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.converter.StringConverter;

public class StringSetting extends Setting {

    private static final StringConverter converter = new StringConverter();

    public StringSetting(String value, Predicate restriction, BiConsumer consumer, String name, Predicate visibilityPredicate) {
        super(value, restriction, consumer, name, visibilityPredicate);
    }

    public StringConverter converter() {
        return StringSetting.converter;
    }
}
