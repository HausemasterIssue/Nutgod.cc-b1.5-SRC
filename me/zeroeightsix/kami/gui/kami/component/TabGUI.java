package me.zeroeightsix.kami.gui.kami.component;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import me.zeroeightsix.kami.gui.rgui.component.AbstractComponent;
import me.zeroeightsix.kami.gui.rgui.component.container.Container;
import me.zeroeightsix.kami.gui.rgui.component.container.use.Frame;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.module.ModuleManager;
import me.zeroeightsix.kami.util.Wrapper;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import org.lwjgl.input.Keyboard;

public class TabGUI extends AbstractComponent implements EventListener {

    public final ArrayList tabs = new ArrayList();
    public int width;
    public int height;
    public int selected;
    public float selectedLerpY;
    public boolean tabOpened;

    public TabGUI() {
        FMLCommonHandler.instance().bus().register(this);
        LinkedHashMap tabMap = new LinkedHashMap();
        Module.Category[] features = Module.Category.values();
        int iterator = features.length;

        for (int entry = 0; entry < iterator; ++entry) {
            Module.Category category = features[entry];

            tabMap.put(category, new TabGUI.Tab(category.getName()));
        }

        ArrayList arraylist = new ArrayList();

        arraylist.addAll(ModuleManager.getModules());
        Iterator iterator = arraylist.iterator();

        while (iterator.hasNext()) {
            Module module = (Module) iterator.next();

            if (module.getCategory() != null && !module.getCategory().isHidden()) {
                ((TabGUI.Tab) tabMap.get(module.getCategory())).add(module);
            }
        }

        iterator = tabMap.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            if (((TabGUI.Tab) entry.getValue()).features.isEmpty()) {
                iterator.remove();
            }
        }

        this.tabs.addAll(tabMap.values());
        this.tabs.forEach(accept<invokedynamic>());
        this.updateSize();
    }

    @SubscribeEvent
    public void onKeyPress(KeyInputEvent event) {
        if (Keyboard.getEventKeyState()) {
            Container framep;

            for (framep = this.getParent(); !(framep instanceof Frame); framep = framep.getParent()) {
                ;
            }

            if (((Frame) framep).isPinned()) {
                if (this.tabOpened) {
                    switch (Keyboard.getEventKey()) {
                    case 203:
                        this.tabOpened = false;
                        break;

                    default:
                        ((TabGUI.Tab) this.tabs.get(this.selected)).onKeyPress(Keyboard.getEventKey());
                    }
                } else {
                    switch (Keyboard.getEventKey()) {
                    case 200:
                        if (this.selected > 0) {
                            --this.selected;
                        } else {
                            this.selected = this.tabs.size() - 1;
                        }
                        break;

                    case 205:
                        this.tabOpened = true;
                        break;

                    case 208:
                        if (this.selected < this.tabs.size() - 1) {
                            ++this.selected;
                        } else {
                            this.selected = 0;
                        }
                    }
                }

            }
        }
    }

    private void updateSize() {
        this.width = 64;
        Iterator iterator = this.tabs.iterator();

        while (iterator.hasNext()) {
            TabGUI.Tab tab = (TabGUI.Tab) iterator.next();
            int tabWidth = Wrapper.getFontRenderer().getStringWidth(tab.name) + 10;

            if (tabWidth > this.width) {
                this.width = tabWidth;
            }
        }

        this.height = this.tabs.size() * 10;
    }

    private static void lambda$new$0(TabGUI.Tab tab) {
        tab.updateSize();
    }

    public static final class Tab {

        public final String name;
        public final ArrayList features = new ArrayList();
        public int width;
        public int height;
        public int selected;
        public float lerpSelectY = 0.0F;

        public Tab(String name) {
            this.name = name;
        }

        public void updateSize() {
            this.width = 64;
            Iterator iterator = this.features.iterator();

            while (iterator.hasNext()) {
                Module feature = (Module) iterator.next();
                int fWidth = Wrapper.getFontRenderer().getStringWidth(feature.getName()) + 10;

                if (fWidth > this.width) {
                    this.width = fWidth;
                }
            }

            this.height = this.features.size() * 10;
        }

        public void onKeyPress(int keyCode) {
            switch (keyCode) {
            case 200:
                if (this.selected > 0) {
                    --this.selected;
                } else {
                    this.selected = this.features.size() - 1;
                }
                break;

            case 205:
                ((Module) this.features.get(this.selected)).toggle();
                break;

            case 208:
                if (this.selected < this.features.size() - 1) {
                    ++this.selected;
                } else {
                    this.selected = 0;
                }
            }

        }

        public void add(Module feature) {
            this.features.add(feature);
        }
    }
}
