package me.zeroeightsix.kami.setting.builder.primitive;

import me.zeroeightsix.kami.setting.builder.SettingBuilder;
import me.zeroeightsix.kami.setting.impl.StringSetting;

public class StringSettingBuilder extends SettingBuilder {

    public StringSetting build() {
        return new StringSetting((String) this.initialValue, this.predicate(), this.consumer(), this.name, this.visibilityPredicate());
    }
}
