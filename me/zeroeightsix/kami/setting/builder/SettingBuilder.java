package me.zeroeightsix.kami.setting.builder;

import com.google.common.base.MoreObjects;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.SettingsRegister;

public abstract class SettingBuilder {

    protected String name;
    protected Object initialValue;
    protected BiConsumer consumer;
    protected List predicateList = new ArrayList();
    private Predicate visibilityPredicate;

    public SettingBuilder withValue(Object value) {
        this.initialValue = value;
        return this;
    }

    protected Predicate predicate() {
        return this.predicateList.isEmpty() ? (t) -> {
            return true;
        } : (t) -> {
            return this.predicateList.stream().allMatch((tPredicate) -> {
                return tPredicate.test(t);
            });
        };
    }

    protected Predicate visibilityPredicate() {
        return (Predicate) MoreObjects.firstNonNull(this.visibilityPredicate, (t) -> {
            return true;
        });
    }

    protected BiConsumer consumer() {
        return (BiConsumer) MoreObjects.firstNonNull(this.consumer, (a, b) -> {
        });
    }

    public SettingBuilder withConsumer(BiConsumer consumer) {
        this.consumer = consumer;
        return this;
    }

    public SettingBuilder withVisibility(Predicate predicate) {
        this.visibilityPredicate = predicate;
        return this;
    }

    public SettingBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public SettingBuilder withRestriction(Predicate predicate) {
        this.predicateList.add(predicate);
        return this;
    }

    public abstract Setting build();

    public final Setting buildAndRegister(String group) {
        return register(this.build(), group);
    }

    public static Setting register(Setting setting, String group) {
        String name = setting.getName();

        if (name != null && !name.isEmpty()) {
            SettingsRegister.register(group + "." + name, setting);
            return setting;
        } else {
            throw new RuntimeException("Can\'t register nameless setting");
        }
    }
}
