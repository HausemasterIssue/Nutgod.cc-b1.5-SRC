package me.zeroeightsix.kami.module.modules.render;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import me.zeroeightsix.kami.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.BossInfoClient;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Post;
import org.lwjgl.opengl.GL11;

@Module.Info(
    name = "BossStack",
    description = "Modify the boss health GUI to take up less space",
    category = Module.Category.MISC
)
public class BossStack extends Module {

    private static Setting mode = Settings.e("Mode", BossStack.BossStackMode.STACK);
    private static Setting scale = Settings.d("Scale", 0.5D);
    private static final ResourceLocation GUI_BARS_TEXTURES = new ResourceLocation("textures/gui/bars.png");

    public BossStack() {
        this.registerAll(new Setting[] { BossStack.mode, BossStack.scale});
    }

    public static void render(Post event) {
        Map map;
        int i;
        String text;

        if (BossStack.mode.getValue() == BossStack.BossStackMode.MINIMIZE) {
            map = Minecraft.getMinecraft().ingameGUI.getBossOverlay().mapBossInfos;
            if (map == null) {
                return;
            }

            ScaledResolution to = new ScaledResolution(Minecraft.getMinecraft());
            int scaledresolution = to.getScaledWidth();

            i = 12;

            for (Iterator j = map.entrySet().iterator(); j.hasNext(); i += 10 + Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT) {
                Entry p = (Entry) j.next();
                BossInfoClient entry = (BossInfoClient) p.getValue();

                text = entry.getName().getFormattedText();
                int info = (int) ((double) scaledresolution / ((Double) BossStack.scale.getValue()).doubleValue() / 2.0D - 91.0D);

                GL11.glScaled(((Double) BossStack.scale.getValue()).doubleValue(), ((Double) BossStack.scale.getValue()).doubleValue(), 1.0D);
                if (!event.isCanceled()) {
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    Minecraft.getMinecraft().getTextureManager().bindTexture(BossStack.GUI_BARS_TEXTURES);
                    Minecraft.getMinecraft().ingameGUI.getBossOverlay().render(info, i, entry);
                    Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(text, (float) ((double) scaledresolution / ((Double) BossStack.scale.getValue()).doubleValue() / 2.0D - (double) (Minecraft.getMinecraft().fontRenderer.getStringWidth(text) / 2)), (float) (i - 9), 16777215);
                }

                GL11.glScaled(1.0D / ((Double) BossStack.scale.getValue()).doubleValue(), 1.0D / ((Double) BossStack.scale.getValue()).doubleValue(), 1.0D);
            }
        } else if (BossStack.mode.getValue() == BossStack.BossStackMode.STACK) {
            map = Minecraft.getMinecraft().ingameGUI.getBossOverlay().mapBossInfos;
            HashMap to1 = new HashMap();
            Iterator scaledresolution1 = map.entrySet().iterator();

            while (scaledresolution1.hasNext()) {
                Entry i1 = (Entry) scaledresolution1.next();
                String j1 = ((BossInfoClient) i1.getValue()).getName().getFormattedText();
                Pair p1;

                if (to1.containsKey(j1)) {
                    p1 = (Pair) to1.get(j1);
                    p1 = new Pair(p1.getKey(), Integer.valueOf(((Integer) p1.getValue()).intValue() + 1));
                    to1.put(j1, p1);
                } else {
                    p1 = new Pair(i1.getValue(), Integer.valueOf(1));
                    to1.put(j1, p1);
                }
            }

            ScaledResolution scaledresolution2 = new ScaledResolution(Minecraft.getMinecraft());

            i = scaledresolution2.getScaledWidth();
            int j2 = 12;

            for (Iterator p2 = to1.entrySet().iterator(); p2.hasNext(); j2 += 10 + Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT) {
                Entry entry1 = (Entry) p2.next();

                text = (String) entry1.getKey();
                BossInfoClient info1 = (BossInfoClient) ((Pair) entry1.getValue()).getKey();
                int a = ((Integer) ((Pair) entry1.getValue()).getValue()).intValue();

                text = text + " x" + a;
                int k = (int) ((double) i / ((Double) BossStack.scale.getValue()).doubleValue() / 2.0D - 91.0D);

                GL11.glScaled(((Double) BossStack.scale.getValue()).doubleValue(), ((Double) BossStack.scale.getValue()).doubleValue(), 1.0D);
                if (!event.isCanceled()) {
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    Minecraft.getMinecraft().getTextureManager().bindTexture(BossStack.GUI_BARS_TEXTURES);
                    Minecraft.getMinecraft().ingameGUI.getBossOverlay().render(k, j2, info1);
                    Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(text, (float) ((double) i / ((Double) BossStack.scale.getValue()).doubleValue() / 2.0D - (double) (Minecraft.getMinecraft().fontRenderer.getStringWidth(text) / 2)), (float) (j2 - 9), 16777215);
                }

                GL11.glScaled(1.0D / ((Double) BossStack.scale.getValue()).doubleValue(), 1.0D / ((Double) BossStack.scale.getValue()).doubleValue(), 1.0D);
            }
        }

    }

    private static enum BossStackMode {

        REMOVE, STACK, MINIMIZE;
    }
}
