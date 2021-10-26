package me.zeroeightsix.kami.module.modules.render;

import java.util.Iterator;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import me.zeroeightsix.kami.util.ColourHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;

@Module.Info(
    name = "ArmourHUD",
    category = Module.Category.RENDER
)
public class ArmourHUD extends Module {

    private static RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();
    private Setting damage = this.register(Settings.b("Damage", false));

    public void onRender() {
        GlStateManager.enableTexture2D();
        ScaledResolution resolution = new ScaledResolution(ArmourHUD.mc);
        int i = resolution.getScaledWidth() / 2;
        int iteration = 0;
        int y = resolution.getScaledHeight() - 55 - (ArmourHUD.mc.player.isInWater() ? 10 : 0);
        Iterator iterator = ArmourHUD.mc.player.inventory.armorInventory.iterator();

        while (iterator.hasNext()) {
            ItemStack is = (ItemStack) iterator.next();

            ++iteration;
            if (!is.isEmpty()) {
                int x = i - 90 + (9 - iteration) * 20 + 2;

                GlStateManager.enableDepth();
                ArmourHUD.itemRender.zLevel = 200.0F;
                ArmourHUD.itemRender.renderItemAndEffectIntoGUI(is, x, y);
                ArmourHUD.itemRender.renderItemOverlayIntoGUI(ArmourHUD.mc.fontRenderer, is, x, y, "");
                ArmourHUD.itemRender.zLevel = 0.0F;
                GlStateManager.enableTexture2D();
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                String s = is.getCount() > 1 ? is.getCount() + "" : "";

                ArmourHUD.mc.fontRenderer.drawStringWithShadow(s, (float) (x + 19 - 2 - ArmourHUD.mc.fontRenderer.getStringWidth(s)), (float) (y + 9), 16777215);
                if (((Boolean) this.damage.getValue()).booleanValue()) {
                    float green = ((float) is.getMaxDamage() - (float) is.getItemDamage()) / (float) is.getMaxDamage();
                    float red = 1.0F - green;
                    int dmg = 100 - (int) (red * 100.0F);

                    ArmourHUD.mc.fontRenderer.drawStringWithShadow(dmg + "", (float) (x + 8 - ArmourHUD.mc.fontRenderer.getStringWidth(dmg + "") / 2), (float) (y - 11), ColourHolder.toHex((int) (red * 255.0F), (int) (green * 255.0F), 0));
                }
            }
        }

        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
    }
}
