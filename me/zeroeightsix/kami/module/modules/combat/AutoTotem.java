package me.zeroeightsix.kami.module.modules.combat;

import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;

@Module.Info(
    name = "AutoTotem",
    category = Module.Category.COMBAT
)
public class AutoTotem extends Module {

    int totems;
    boolean moving = false;
    boolean returnI = false;
    private Setting soft = this.register(Settings.b("Soft"));

    public void onUpdate() {
        if (!(AutoTotem.mc.currentScreen instanceof GuiContainer)) {
            int t;
            int i;

            if (this.returnI) {
                t = -1;

                for (i = 0; i < 45; ++i) {
                    if (AutoTotem.mc.player.inventory.getStackInSlot(i).isEmpty) {
                        t = i;
                        break;
                    }
                }

                if (t == -1) {
                    return;
                }

                AutoTotem.mc.playerController.windowClick(0, t < 9 ? t + 36 : t, 0, ClickType.PICKUP, AutoTotem.mc.player);
                this.returnI = false;
            }

            this.totems = AutoTotem.mc.player.inventory.mainInventory.stream().filter((itemStack) -> {
                return itemStack.getItem() == Items.TOTEM_OF_UNDYING;
            }).mapToInt(ItemStack::getCount).sum();
            if (AutoTotem.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
                ++this.totems;
            } else {
                if (((Boolean) this.soft.getValue()).booleanValue() && !AutoTotem.mc.player.getHeldItemOffhand().isEmpty) {
                    return;
                }

                if (this.moving) {
                    AutoTotem.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, AutoTotem.mc.player);
                    this.moving = false;
                    if (!AutoTotem.mc.player.inventory.itemStack.isEmpty()) {
                        this.returnI = true;
                    }

                    return;
                }

                if (AutoTotem.mc.player.inventory.itemStack.isEmpty()) {
                    if (this.totems == 0) {
                        return;
                    }

                    t = -1;

                    for (i = 0; i < 45; ++i) {
                        if (AutoTotem.mc.player.inventory.getStackInSlot(i).getItem() == Items.TOTEM_OF_UNDYING) {
                            t = i;
                            break;
                        }
                    }

                    if (t == -1) {
                        return;
                    }

                    AutoTotem.mc.playerController.windowClick(0, t < 9 ? t + 36 : t, 0, ClickType.PICKUP, AutoTotem.mc.player);
                    this.moving = true;
                } else if (!((Boolean) this.soft.getValue()).booleanValue()) {
                    t = -1;

                    for (i = 0; i < 45; ++i) {
                        if (AutoTotem.mc.player.inventory.getStackInSlot(i).isEmpty) {
                            t = i;
                            break;
                        }
                    }

                    if (t == -1) {
                        return;
                    }

                    AutoTotem.mc.playerController.windowClick(0, t < 9 ? t + 36 : t, 0, ClickType.PICKUP, AutoTotem.mc.player);
                }
            }

        }
    }

    public String getHudInfo() {
        return String.valueOf(this.totems);
    }
}
