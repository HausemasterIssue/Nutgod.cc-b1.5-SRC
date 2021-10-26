package me.zeroeightsix.kami.setting.impl;

import com.google.common.base.Converter;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.converter.EnumConverter;

public class EnumSetting extends Setting {

    private EnumConverter converter;
    public final Class clazz;

    public EnumSetting(Enum value, Predicate restriction, BiConsumer consumer, String name, Predicate visibilityPredicate, Class clazz) {
        super(value, restriction, consumer, name, visibilityPredicate);
        this.converter = new EnumConverter(clazz);
        this.clazz = clazz;
    }

    public Converter converter() {
        return this.converter;
    }
}
