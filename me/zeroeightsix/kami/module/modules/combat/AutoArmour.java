package me.zeroeightsix.kami.module.modules.combat;

import me.zeroeightsix.kami.module.Module;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

@Module.Info(
    name = "AutoArmour",
    category = Module.Category.COMBAT
)
public class AutoArmour extends Module {

    public void onUpdate() {
        if (AutoArmour.mc.player.ticksExisted % 2 != 0) {
            if (!(AutoArmour.mc.currentScreen instanceof GuiContainer) || AutoArmour.mc.currentScreen instanceof InventoryEffectRenderer) {
                int[] bestArmorSlots = new int[4];
                int[] bestArmorValues = new int[4];

                int armorType;
                ItemStack slot;

                for (armorType = 0; armorType < 4; ++armorType) {
                    slot = AutoArmour.mc.player.inventory.armorItemInSlot(armorType);
                    if (slot != null && slot.getItem() instanceof ItemArmor) {
                        bestArmorValues[armorType] = ((ItemArmor) slot.getItem()).damageReduceAmount;
                    }

                    bestArmorSlots[armorType] = -1;
                }

                for (armorType = 0; armorType < 36; ++armorType) {
                    slot = AutoArmour.mc.player.inventory.getStackInSlot(armorType);
                    if (slot.getCount() <= 1 && slot != null && slot.getItem() instanceof ItemArmor) {
                        ItemArmor oldArmor = (ItemArmor) slot.getItem();
                        int armorType1 = oldArmor.armorType.ordinal() - 2;

                        if (armorType1 != 2 || !AutoArmour.mc.player.inventory.armorItemInSlot(armorType1).getItem().equals(Items.ELYTRA)) {
                            int armorValue = oldArmor.damageReduceAmount;

                            if (armorValue > bestArmorValues[armorType1]) {
                                bestArmorSlots[armorType1] = armorType;
                                bestArmorValues[armorType1] = armorValue;
                            }
                        }
                    }
                }

                for (armorType = 0; armorType < 4; ++armorType) {
                    int i = bestArmorSlots[armorType];

                    if (i != -1) {
                        ItemStack itemstack = AutoArmour.mc.player.inventory.armorItemInSlot(armorType);

                        if (itemstack == null || itemstack != ItemStack.EMPTY || AutoArmour.mc.player.inventory.getFirstEmptyStack() != -1) {
                            if (i < 9) {
                                i += 36;
                            }

                            AutoArmour.mc.playerController.windowClick(0, 8 - armorType, 0, ClickType.QUICK_MOVE, AutoArmour.mc.player);
                            AutoArmour.mc.playerController.windowClick(0, i, 0, ClickType.QUICK_MOVE, AutoArmour.mc.player);
                            break;
                        }
                    }
                }

            }
        }
    }
}
