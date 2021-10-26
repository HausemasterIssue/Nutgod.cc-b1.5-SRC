package me.zeroeightsix.kami;

import com.google.common.base.Converter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.stream.Stream;
import me.zero.alpine.EventBus;
import me.zero.alpine.EventManager;
import me.zeroeightsix.kami.command.Command;
import me.zeroeightsix.kami.command.CommandManager;
import me.zeroeightsix.kami.event.ForgeEventProcessor;
import me.zeroeightsix.kami.gui.kami.KamiGUI;
import me.zeroeightsix.kami.gui.rgui.component.AlignedComponent;
import me.zeroeightsix.kami.gui.rgui.component.Component;
import me.zeroeightsix.kami.gui.rgui.component.container.use.Frame;
import me.zeroeightsix.kami.gui.rgui.util.ContainerHelper;
import me.zeroeightsix.kami.gui.rgui.util.Docking;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.module.ModuleManager;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import me.zeroeightsix.kami.setting.SettingsRegister;
import me.zeroeightsix.kami.setting.config.Configuration;
import me.zeroeightsix.kami.util.Friends;
import me.zeroeightsix.kami.util.LagCompensator;
import me.zeroeightsix.kami.util.Wrapper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
    modid = "kami",
    name = "KAMI",
    version = "b9"
)
public class KamiMod {

    public static final String MODID = "kami";
    public static final String MODNAME = "KAMI";
    public static final String MODVER = "b9";
    public static final String KAMI_HIRAGANA = "ã?‹ã?¿";
    public static final String KAMI_KATAKANA = "ã‚«ãƒŸ";
    public static final String KAMI_KANJI = "ç¥ž";
    private static final String KAMI_CONFIG_NAME_DEFAULT = "NutgodConfig.json";
    public static final Logger log = LogManager.getLogger("KAMI");
    public static final EventBus EVENT_BUS = new EventManager();
    @Instance
    private static KamiMod INSTANCE;
    public KamiGUI guiManager;
    public CommandManager commandManager;
    private Setting guiStateSetting = Settings.custom("gui", new JsonObject(), new Converter() {
        protected JsonObject doForward(JsonObject jsonObject) {
            return jsonObject;
        }

        protected JsonObject doBackward(JsonObject jsonObject) {
            return jsonObject;
        }
    }).buildAndRegister("");

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {}

    @EventHandler
    public void init(FMLInitializationEvent event) {
        KamiMod.log.info("\n\nInitializing KAMI b9");
        ModuleManager.initialize();
        Stream stream = ModuleManager.getModules().stream().filter(test<invokedynamic>());
        EventBus eventbus = KamiMod.EVENT_BUS;

        KamiMod.EVENT_BUS.getClass();
        stream.forEach(accept<invokedynamic>(eventbus));
        MinecraftForge.EVENT_BUS.register(new ForgeEventProcessor());
        LagCompensator.INSTANCE = new LagCompensator();
        Wrapper.init();
        this.guiManager = new KamiGUI();
        this.guiManager.initializeGUI();
        this.commandManager = new CommandManager();
        Friends.initFriends();
        SettingsRegister.register("commandPrefix", Command.commandPrefix);
        loadConfiguration();
        KamiMod.log.info("Settings loaded");
        ModuleManager.updateLookup();
        ModuleManager.getModules().stream().filter(test<invokedynamic>()).forEach(accept<invokedynamic>());
        KamiMod.log.info("KAMI Mod initialized!\n");
    }

    public static String getConfigName() {
        Path config = Paths.get("KAMILastConfig.txt", new String[0]);
        String kamiConfigName = "NutgodConfig.json";

        try {
            BufferedReader e = Files.newBufferedReader(config);
            Throwable e11 = null;

            try {
                kamiConfigName = e.readLine();
                if (!isFilenameValid(kamiConfigName)) {
                    kamiConfigName = "NutgodConfig.json";
                }
            } catch (Throwable throwable) {
                e11 = throwable;
                throw throwable;
            } finally {
                if (e != null) {
                    if (e11 != null) {
                        try {
                            e.close();
                        } catch (Throwable throwable1) {
                            e11.addSuppressed(throwable1);
                        }
                    } else {
                        e.close();
                    }
                }

            }
        } catch (NoSuchFileException nosuchfileexception) {
            try {
                BufferedWriter e1 = Files.newBufferedWriter(config, new OpenOption[0]);
                Throwable throwable2 = null;

                try {
                    e1.write("NutgodConfig.json");
                } catch (Throwable throwable3) {
                    throwable2 = throwable3;
                    throw throwable3;
                } finally {
                    if (e1 != null) {
                        if (throwable2 != null) {
                            try {
                                e1.close();
                            } catch (Throwable throwable4) {
                                throwable2.addSuppressed(throwable4);
                            }
                        } else {
                            e1.close();
                        }
                    }

                }
            } catch (IOException ioexception) {
                ioexception.printStackTrace();
            }
        } catch (IOException ioexception1) {
            ioexception1.printStackTrace();
        }

        return kamiConfigName;
    }

    public static void loadConfiguration() {
        try {
            loadConfigurationUnsafe();
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
        }

    }

    public static void loadConfigurationUnsafe() throws IOException {
        String kamiConfigName = getConfigName();
        Path kamiConfig = Paths.get(kamiConfigName, new String[0]);

        if (Files.exists(kamiConfig, new LinkOption[0])) {
            Configuration.loadConfiguration(kamiConfig);
            JsonObject gui = (JsonObject) KamiMod.INSTANCE.guiStateSetting.getValue();
            Iterator iterator = gui.entrySet().iterator();

            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();
                Optional optional = KamiMod.INSTANCE.guiManager.getChildren().stream().filter(test<invokedynamic>()).filter(test<invokedynamic>(entry)).findFirst();

                if (optional.isPresent()) {
                    JsonObject object = ((JsonElement) entry.getValue()).getAsJsonObject();
                    Frame frame = (Frame) optional.get();

                    frame.setX(object.get("x").getAsInt());
                    frame.setY(object.get("y").getAsInt());
                    Docking docking = Docking.values()[object.get("docking").getAsInt()];

                    if (docking.isLeft()) {
                        ContainerHelper.setAlignment(frame, AlignedComponent.Alignment.LEFT);
                    } else if (docking.isRight()) {
                        ContainerHelper.setAlignment(frame, AlignedComponent.Alignment.RIGHT);
                    } else if (docking.isCenterVertical()) {
                        ContainerHelper.setAlignment(frame, AlignedComponent.Alignment.CENTER);
                    }

                    frame.setDocking(docking);
                    frame.setMinimized(object.get("minimized").getAsBoolean());
                    frame.setPinned(object.get("pinned").getAsBoolean());
                } else {
                    System.err.println("Found GUI config entry for " + (String) entry.getKey() + ", but found no frame with that name");
                }
            }

            getInstance().getGuiManager().getChildren().stream().filter(test<invokedynamic>()).forEach(accept<invokedynamic>());
        }
    }

    public static void saveConfiguration() {
        try {
            saveConfigurationUnsafe();
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
        }

    }

    public static void saveConfigurationUnsafe() throws IOException {
        JsonObject object = new JsonObject();

        KamiMod.INSTANCE.guiManager.getChildren().stream().filter(test<invokedynamic>()).map(apply<invokedynamic>()).forEach(accept<invokedynamic>(object));
        KamiMod.INSTANCE.guiStateSetting.setValue(object);
        Path outputFile = Paths.get(getConfigName(), new String[0]);

        if (!Files.exists(outputFile, new LinkOption[0])) {
            Files.createFile(outputFile, new FileAttribute[0]);
        }

        Configuration.saveConfiguration(outputFile);
        ModuleManager.getModules().forEach(accept<invokedynamic>());
    }

    public static boolean isFilenameValid(String file) {
        File f = new File(file);

        try {
            f.getCanonicalPath();
            return true;
        } catch (IOException ioexception) {
            return false;
        }
    }

    public static KamiMod getInstance() {
        return KamiMod.INSTANCE;
    }

    public KamiGUI getGuiManager() {
        return this.guiManager;
    }

    public CommandManager getCommandManager() {
        return this.commandManager;
    }

    private static void lambda$saveConfigurationUnsafe$7(JsonObject object, Frame frame) {
        JsonObject frameObject = new JsonObject();

        frameObject.add("x", new JsonPrimitive(Integer.valueOf(frame.getX())));
        frameObject.add("y", new JsonPrimitive(Integer.valueOf(frame.getY())));
        frameObject.add("docking", new JsonPrimitive(Integer.valueOf(Arrays.asList(Docking.values()).indexOf(frame.getDocking()))));
        frameObject.add("minimized", new JsonPrimitive(Boolean.valueOf(frame.isMinimized())));
        frameObject.add("pinned", new JsonPrimitive(Boolean.valueOf(frame.isPinned())));
        object.add(frame.getTitle(), frameObject);
    }

    private static Frame lambda$saveConfigurationUnsafe$6(Component component) {
        return (Frame) component;
    }

    private static boolean lambda$saveConfigurationUnsafe$5(Component component) {
        return component instanceof Frame;
    }

    private static void lambda$loadConfigurationUnsafe$4(Component component) {
        component.setOpacity(0.0F);
    }

    private static boolean lambda$loadConfigurationUnsafe$3(Component component) {
        return component instanceof Frame && ((Frame) component).isPinneable() && component.isVisible();
    }

    private static boolean lambda$loadConfigurationUnsafe$2(Entry entry, Component component) {
        return ((Frame) component).getTitle().equals(entry.getKey());
    }

    private static boolean lambda$loadConfigurationUnsafe$1(Component component) {
        return component instanceof Frame;
    }

    private static boolean lambda$init$0(Module module) {
        return module.alwaysListening;
    }
}
