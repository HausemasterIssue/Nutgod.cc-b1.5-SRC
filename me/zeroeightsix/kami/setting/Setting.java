package me.zeroeightsix.kami.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import me.zeroeightsix.kami.setting.converter.Convertable;

public abstract class Setting implements ISettingUnknown, Convertable {

    String name;
    Object value;
    private Predicate restriction;
    private Predicate visibilityPredicate;
    private BiConsumer consumer;
    private final Class valueType;

    public Setting(Object value, Predicate restriction, BiConsumer consumer, String name, Predicate visibilityPredicate) {
        this.value = value;
        this.valueType = value.getClass();
        this.restriction = restriction;
        this.consumer = consumer;
        this.name = name;
        this.visibilityPredicate = visibilityPredicate;
    }

    public String getName() {
        return this.name;
    }

    public Object getValue() {
        return this.value;
    }

    public Class getValueClass() {
        return this.valueType;
    }

    public boolean setValue(Object value) {
        Object old = this.getValue();

        if (!this.restriction.test(value)) {
            return false;
        } else {
            this.value = value;
            this.consumer.accept(old, value);
            return true;
        }
    }

    public boolean isVisible() {
        return this.visibilityPredicate.test(this.getValue());
    }

    public BiConsumer changeListener() {
        return this.consumer;
    }

    public void setValueFromString(String value) {
        JsonParser jp = new JsonParser();

        this.setValue(this.converter().reverse().convert(jp.parse(value)));
    }

    public String getValueAsString() {
        return ((JsonElement) this.converter().convert(this.getValue())).toString();
    }
}
