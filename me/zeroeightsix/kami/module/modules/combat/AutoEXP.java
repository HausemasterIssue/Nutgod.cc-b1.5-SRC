package me.zeroeightsix.kami.module.modules.combat;

import me.zeroeightsix.kami.module.Module;
import net.minecraft.item.ItemExpBottle;

@Module.Info(
    name = "Auto XP",
    category = Module.Category.COMBAT
)
public class AutoEXP extends Module {

    public void onUpdate() {
        if (AutoEXP.mc.player.inventory.getCurrentItem().getItem() instanceof ItemExpBottle) {
            AutoEXP.mc.rightClickDelayTimer = 0;
            AutoEXP.mc.rightClickMouse();
        }

    }
}
