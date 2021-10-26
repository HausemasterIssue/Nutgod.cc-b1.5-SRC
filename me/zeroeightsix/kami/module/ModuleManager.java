package me.zeroeightsix.kami.module;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import me.zeroeightsix.kami.KamiMod;
import me.zeroeightsix.kami.event.events.RenderEvent;
import me.zeroeightsix.kami.module.modules.ClickGUI;
import me.zeroeightsix.kami.util.ClassFinder;
import me.zeroeightsix.kami.util.EntityUtil;
import me.zeroeightsix.kami.util.KamiTessellator;
import me.zeroeightsix.kami.util.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class ModuleManager {

    public static ArrayList modules = new ArrayList();
    static HashMap lookup = new HashMap();

    public static void updateLookup() {
        ModuleManager.lookup.clear();
        Iterator iterator = ModuleManager.modules.iterator();

        while (iterator.hasNext()) {
            Module m = (Module) iterator.next();

            ModuleManager.lookup.put(m.getName().toLowerCase(), m);
        }

    }

    public static void initialize() {
        Set classList = ClassFinder.findClasses(ClickGUI.class.getPackage().getName(), Module.class);

        classList.forEach((aClass) -> {
            try {
                Module e = (Module) aClass.getConstructor(new Class[0]).newInstance(new Object[0]);

                ModuleManager.modules.add(e);
            } catch (InvocationTargetException invocationtargetexception) {
                invocationtargetexception.getCause().printStackTrace();
                System.err.println("Couldn\'t initiate module " + aClass.getSimpleName() + "! Err: " + invocationtargetexception.getClass().getSimpleName() + ", message: " + invocationtargetexception.getMessage());
            } catch (Exception exception) {
                exception.printStackTrace();
                System.err.println("Couldn\'t initiate module " + aClass.getSimpleName() + "! Err: " + exception.getClass().getSimpleName() + ", message: " + exception.getMessage());
            }

        });
        KamiMod.log.info("Modules initialised");
        getModules().sort(Comparator.comparing(Module::getName));
    }

    public static void onUpdate() {
        ModuleManager.modules.stream().filter((module) -> {
            return module.alwaysListening || module.isEnabled();
        }).forEach((module) -> {
            module.onUpdate();
        });
    }

    public static void onRender() {
        ModuleManager.modules.stream().filter((module) -> {
            return module.alwaysListening || module.isEnabled();
        }).forEach((module) -> {
            module.onRender();
        });
    }

    public static void onWorldRender(RenderWorldLastEvent event) {
        Minecraft.getMinecraft().profiler.startSection("kami");
        Minecraft.getMinecraft().profiler.startSection("setup");
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        GlStateManager.disableDepth();
        GlStateManager.glLineWidth(1.0F);
        Vec3d renderPos = EntityUtil.getInterpolatedPos(Wrapper.getPlayer(), event.getPartialTicks());
        RenderEvent e = new RenderEvent(KamiTessellator.INSTANCE, renderPos);

        e.resetTranslation();
        Minecraft.getMinecraft().profiler.endSection();
        ModuleManager.modules.stream().filter((module) -> {
            return module.alwaysListening || module.isEnabled();
        }).forEach((module) -> {
            Minecraft.getMinecraft().profiler.startSection(module.getName());
            module.onWorldRender(e);
            Minecraft.getMinecraft().profiler.endSection();
        });
        Minecraft.getMinecraft().profiler.startSection("release");
        GlStateManager.glLineWidth(1.0F);
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.enableCull();
        KamiTessellator.releaseGL();
        Minecraft.getMinecraft().profiler.endSection();
        Minecraft.getMinecraft().profiler.endSection();
    }

    public static void onBind(int eventKey) {
        if (eventKey != 0) {
            ModuleManager.modules.forEach((module) -> {
                if (module.getBind().isDown(eventKey)) {
                    module.toggle();
                }

            });
        }
    }

    public static ArrayList getModules() {
        return ModuleManager.modules;
    }

    public static Module getModuleByName(String name) {
        return (Module) ModuleManager.lookup.get(name.toLowerCase());
    }

    public static boolean isModuleEnabled(String moduleName) {
        Module m = getModuleByName(moduleName);

        return m == null ? false : m.isEnabled();
    }
}
