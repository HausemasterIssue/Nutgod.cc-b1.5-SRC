package me.zeroeightsix.kami.setting.builder.primitive;

import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.builder.SettingBuilder;
import me.zeroeightsix.kami.setting.impl.EnumSetting;

public class EnumSettingBuilder extends SettingBuilder {

    Class clazz;

    public EnumSettingBuilder(Class clazz) {
        this.clazz = clazz;
    }

    public Setting build() {
        return new EnumSetting((Enum) this.initialValue, this.predicate(), this.consumer(), this.name, this.visibilityPredicate(), this.clazz);
    }
}
