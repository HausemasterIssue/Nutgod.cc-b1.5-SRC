package me.zeroeightsix.kami.gui.kami.theme.kami;

import java.awt.Color;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import me.zeroeightsix.kami.command.Command;
import me.zeroeightsix.kami.gui.kami.component.ActiveModules;
import me.zeroeightsix.kami.gui.rgui.component.AlignedComponent;
import me.zeroeightsix.kami.gui.rgui.render.AbstractComponentUI;
import me.zeroeightsix.kami.gui.rgui.render.font.FontRenderer;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.module.ModuleManager;
import me.zeroeightsix.kami.util.Wrapper;
import org.lwjgl.opengl.GL11;

public class KamiActiveModulesUI extends AbstractComponentUI {

    public void renderComponent(ActiveModules component, FontRenderer f) {
        GL11.glDisable(2884);
        GL11.glEnable(3042);
        GL11.glEnable(3553);
        FontRenderer renderer = Wrapper.getFontRenderer();
        List mods = (List) ModuleManager.getModules().stream().filter(test<invokedynamic>()).sorted(Comparator.comparing(apply<invokedynamic>(renderer, component))).collect(Collectors.toList());
        int[] y = new int[] { 2};

        if (component.getParent().getY() < 26 && Wrapper.getPlayer().getActivePotionEffects().size() > 0 && component.getParent().getOpacity() == 0.0F) {
            y[0] = Math.max(component.getParent().getY(), 26 - component.getParent().getY());
        }

        float[] hue = new float[] { (float) (System.currentTimeMillis() % 11520L) / 11520.0F};
        boolean lAlign = component.getAlignment() == AlignedComponent.Alignment.LEFT;
        Function xFunc;

        switch (component.getAlignment()) {
        case RIGHT:
            xFunc = apply<invokedynamic>(component);
            break;

        case CENTER:
            xFunc = apply<invokedynamic>(component);
            break;

        case LEFT:
        default:
            xFunc = apply<invokedynamic>();
        }

        mods.stream().forEach(accept<invokedynamic>(hue, renderer, xFunc, y));
        component.setHeight(y[0]);
        GL11.glEnable(2884);
        GL11.glDisable(3042);
    }

    public void handleSizeComponent(ActiveModules component) {
        component.setWidth(100);
        component.setHeight(100);
    }

    private static void lambda$renderComponent$4(float[] hue, FontRenderer renderer, Function xFunc, int[] y, Module module) {
        int rgb = Color.HSBtoRGB(hue[0], 1.0F, 1.0F);
        String s = module.getHudInfo();
        String text = module.getName() + (s == null ? "" : " " + Command.SECTIONSIGN() + "7" + s);
        int textwidth = renderer.getStringWidth(text);
        int textheight = renderer.getFontHeight() + 1;
        short red = 255;
        byte green = 0;
        short blue = 255;

        renderer.drawStringWithShadow(((Integer) xFunc.apply(Integer.valueOf(textwidth))).intValue(), y[0], red, green, blue, text);
        hue[0] += 0.02F;
        y[0] += textheight;
    }

    private static Integer lambda$renderComponent$3(Integer i) {
        return Integer.valueOf(0);
    }

    private static Integer lambda$renderComponent$2(ActiveModules component, Integer i) {
        return Integer.valueOf(component.getWidth() / 2 - i.intValue() / 2);
    }

    private static Integer lambda$renderComponent$1(ActiveModules component, Integer i) {
        return Integer.valueOf(component.getWidth() - i.intValue());
    }

    private static Integer lambda$renderComponent$0(FontRenderer renderer, ActiveModules component, Module module) {
        return Integer.valueOf(renderer.getStringWidth(module.getName() + (module.getHudInfo() == null ? "" : module.getHudInfo() + " ")) * (component.sort_up ? -1 : 1));
    }
}
