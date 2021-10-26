package me.zeroeightsix.kami.module.modules.combat;

import me.zeroeightsix.kami.module.Module;
import net.minecraft.item.ItemExpBottle;

@Module.Info(
    name = "Fast Exp",
    category = Module.Category.COMBAT
)
public class FastEXP extends Module {

    public void onUpdate() {
        if (FastEXP.mc.player.inventory.getCurrentItem().getItem() instanceof ItemExpBottle) {
            FastEXP.mc.rightClickDelayTimer = 0;
        }

    }
}
