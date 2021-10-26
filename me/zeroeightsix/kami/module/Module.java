package me.zeroeightsix.kami.module;

import com.google.common.base.Converter;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import me.zeroeightsix.kami.KamiMod;
import me.zeroeightsix.kami.event.events.RenderEvent;
import me.zeroeightsix.kami.module.modules.movement.Sprint;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import me.zeroeightsix.kami.setting.builder.SettingBuilder;
import me.zeroeightsix.kami.util.Bind;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class Module {

    private final String originalName = this.getAnnotation().name();
    private final Setting name;
    private final String description;
    private final Module.Category category;
    private Setting bind;
    private Setting enabled;
    public boolean alwaysListening;
    protected static final Minecraft mc = Minecraft.getMinecraft();
    public List settingList;

    public Module() {
        this.name = this.register(Settings.s("Name", this.originalName));
        this.description = this.getAnnotation().description();
        this.category = this.getAnnotation().category();
        this.bind = this.register(Settings.custom("Bind", Bind.none(), new Module.BindConverter(null)).build());
        this.enabled = this.register(Settings.booleanBuilder("Enabled").withVisibility(test<invokedynamic>()).withValue(Boolean.valueOf(false)).build());
        this.settingList = new ArrayList();
        this.alwaysListening = this.getAnnotation().alwaysListening();
        this.registerAll(new Setting[] { this.bind, this.enabled});
    }

    private Module.Info getAnnotation() {
        return this.getClass().isAnnotationPresent(Module.Info.class) ? (Module.Info) this.getClass().getAnnotation(Module.Info.class) : (Module.Info) Sprint.class.getAnnotation(Module.Info.class);
    }

    public void onUpdate() {}

    public void onRender() {}

    public void onWorldRender(RenderEvent event) {}

    public Bind getBind() {
        return (Bind) this.bind.getValue();
    }

    public String getBindName() {
        return ((Bind) this.bind.getValue()).toString();
    }

    public void setName(String name) {
        this.name.setValue(name);
        ModuleManager.updateLookup();
    }

    public String getOriginalName() {
        return this.originalName;
    }

    public String getName() {
        return (String) this.name.getValue();
    }

    public String getDescription() {
        return this.description;
    }

    public Module.Category getCategory() {
        return this.category;
    }

    public boolean isEnabled() {
        return ((Boolean) this.enabled.getValue()).booleanValue();
    }

    protected void onEnable() {}

    protected void onDisable() {}

    public void toggle() {
        this.setEnabled(!this.isEnabled());
    }

    public void enable() {
        this.enabled.setValue(Boolean.valueOf(true));
        this.onEnable();
        if (!this.alwaysListening) {
            KamiMod.EVENT_BUS.subscribe((Object) this);
        }

    }

    public void disable() {
        this.enabled.setValue(Boolean.valueOf(false));
        this.onDisable();
        if (!this.alwaysListening) {
            KamiMod.EVENT_BUS.unsubscribe((Object) this);
        }

    }

    public boolean isDisabled() {
        return !this.isEnabled();
    }

    public void setEnabled(boolean enabled) {
        boolean prev = ((Boolean) this.enabled.getValue()).booleanValue();

        if (prev != enabled) {
            if (enabled) {
                this.enable();
            } else {
                this.disable();
            }
        }

    }

    public String getHudInfo() {
        return null;
    }

    protected final void setAlwaysListening(boolean alwaysListening) {
        this.alwaysListening = alwaysListening;
        if (alwaysListening) {
            KamiMod.EVENT_BUS.subscribe((Object) this);
        }

        if (!alwaysListening && this.isDisabled()) {
            KamiMod.EVENT_BUS.unsubscribe((Object) this);
        }

    }

    public void destroy() {}

    protected void registerAll(Setting... settings) {
        Setting[] asetting = settings;
        int i = settings.length;

        for (int j = 0; j < i; ++j) {
            Setting setting = asetting[j];

            this.register(setting);
        }

    }

    protected Setting register(Setting setting) {
        if (this.settingList == null) {
            this.settingList = new ArrayList();
        }

        this.settingList.add(setting);
        return SettingBuilder.register(setting, "modules." + this.originalName);
    }

    protected Setting register(SettingBuilder builder) {
        if (this.settingList == null) {
            this.settingList = new ArrayList();
        }

        Setting setting = builder.buildAndRegister("modules." + this.name);

        this.settingList.add(setting);
        return setting;
    }

    private static boolean lambda$new$0(Boolean aBoolean) {
        return false;
    }

    private class BindConverter extends Converter {

        private BindConverter() {}

        protected JsonElement doForward(Bind bind) {
            return new JsonPrimitive(bind.toString());
        }

        protected Bind doBackward(JsonElement jsonElement) {
            String s = jsonElement.getAsString();

            if (s.equalsIgnoreCase("None")) {
                return Bind.none();
            } else {
                boolean ctrl = false;
                boolean alt = false;
                boolean shift = false;

                if (s.startsWith("Ctrl+")) {
                    ctrl = true;
                    s = s.substring(5);
                }

                if (s.startsWith("Alt+")) {
                    alt = true;
                    s = s.substring(4);
                }

                if (s.startsWith("Shift+")) {
                    shift = true;
                    s = s.substring(6);
                }

                int key = -1;

                try {
                    key = Keyboard.getKeyIndex(s.toUpperCase());
                } catch (Exception exception) {
                    ;
                }

                return key == 0 ? Bind.none() : new Bind(ctrl, alt, shift, key);
            }
        }

        BindConverter(Object x1) {
            this();
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Info {

        String name();

        String description() default "Descriptionless";

        Module.Category category();

        boolean alwaysListening() default false;
    }

    public static enum Category {

        COMBAT("Combat", false), EXPLOITS("Exploits", false), RENDER("Render", false), MISC("Misc", false), PLAYER("Player", false), MOVEMENT("Movement", false), HIDDEN("Hidden", true);

        boolean hidden;
        String name;

        private Category(String name, boolean hidden) {
            this.name = name;
            this.hidden = hidden;
        }

        public boolean isHidden() {
            return this.hidden;
        }

        public String getName() {
            return this.name;
        }
    }
}
