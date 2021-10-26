package me.zeroeightsix.kami.setting.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Map.Entry;
import me.zeroeightsix.kami.KamiMod;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.SettingsRegister;
import me.zeroeightsix.kami.setting.converter.Convertable;

public class Configuration {

    public static JsonObject produceConfig() {
        return produceConfig(SettingsRegister.ROOT);
    }

    private static JsonObject produceConfig(SettingsRegister register) {
        JsonObject object = new JsonObject();
        Iterator iterator = register.registerHashMap.entrySet().iterator();

        Entry entry;

        while (iterator.hasNext()) {
            entry = (Entry) iterator.next();
            object.add((String) entry.getKey(), produceConfig((SettingsRegister) entry.getValue()));
        }

        iterator = register.settingHashMap.entrySet().iterator();

        while (iterator.hasNext()) {
            entry = (Entry) iterator.next();
            Setting setting = (Setting) entry.getValue();

            if (setting instanceof Convertable) {
                object.add((String) entry.getKey(), (JsonElement) setting.converter().convert(setting.getValue()));
            }
        }

        return object;
    }

    public static void saveConfiguration(Path path) throws IOException {
        saveConfiguration(Files.newOutputStream(path, new OpenOption[0]));
    }

    public static void saveConfiguration(OutputStream stream) throws IOException {
        Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
        String json = gson.toJson(produceConfig());
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream));

        writer.write(json);
        writer.close();
    }

    public static void loadConfiguration(Path path) throws IOException {
        InputStream stream = Files.newInputStream(path, new OpenOption[0]);

        loadConfiguration(stream);
        stream.close();
    }

    public static void loadConfiguration(InputStream stream) {
        try {
            loadConfiguration((new JsonParser()).parse(new InputStreamReader(stream)).getAsJsonObject());
        } catch (IllegalStateException illegalstateexception) {
            KamiMod.log.error("KAMI Config malformed: resetting.");
            loadConfiguration(new JsonObject());
        }

    }

    public static void loadConfiguration(JsonObject input) {
        loadConfiguration(SettingsRegister.ROOT, input);
    }

    private static void loadConfiguration(SettingsRegister register, JsonObject input) {
        Iterator iterator = input.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();
            String key = (String) entry.getKey();
            JsonElement element = (JsonElement) entry.getValue();

            if (register.registerHashMap.containsKey(key)) {
                loadConfiguration(register.subregister(key), element.getAsJsonObject());
            } else {
                Setting setting = register.getSetting(key);

                if (setting != null) {
                    setting.setValue(setting.converter().reverse().convert(element));
                }
            }
        }

    }
}
