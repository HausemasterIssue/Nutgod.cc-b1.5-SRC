package me.zeroeightsix.kami.module.modules.misc;

import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.EventHook;
import me.zero.alpine.listener.Listener;
import me.zeroeightsix.kami.module.Module;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickBlock;

@Module.Info(
    name = "AutoTool",
    description = "Automatically switch to the best tools when mining or attacking",
    category = Module.Category.MISC
)
public class AutoTool extends Module {

    @EventHandler
    private Listener leftClickListener = new Listener((event) -> {
        this.equipBestTool(AutoTool.mc.world.getBlockState(event.getPos()));
    }, new Predicate[0]);
    @EventHandler
    private Listener attackListener = new Listener((event) -> {
        equipBestWeapon();
    }, new Predicate[0]);

    private void equipBestTool(IBlockState blockState) {
        int bestSlot = -1;
        double max = 0.0D;

        for (int i = 0; i < 9; ++i) {
            ItemStack stack = AutoTool.mc.player.inventory.getStackInSlot(i);

            if (!stack.isEmpty) {
                float speed = stack.getDestroySpeed(blockState);

                if (speed > 1.0F) {
                    int eff;

                    speed = (float) ((double) speed + ((eff = EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack)) > 0 ? Math.pow((double) eff, 2.0D) + 1.0D : 0.0D));
                    if ((double) speed > max) {
                        max = (double) speed;
                        bestSlot = i;
                    }
                }
            }
        }

        if (bestSlot != -1) {
            equip(bestSlot);
        }

    }

    public static void equipBestWeapon() {
        int bestSlot = -1;
        double maxDamage = 0.0D;

        for (int i = 0; i < 9; ++i) {
            ItemStack stack = AutoTool.mc.player.inventory.getStackInSlot(i);

            if (!stack.isEmpty) {
                double damage;

                if (stack.getItem() instanceof ItemTool) {
                    damage = (double) ((ItemTool) stack.getItem()).attackDamage + (double) EnchantmentHelper.getModifierForCreature(stack, EnumCreatureAttribute.UNDEFINED);
                    if (damage > maxDamage) {
                        maxDamage = damage;
                        bestSlot = i;
                    }
                } else if (stack.getItem() instanceof ItemSword) {
                    damage = (double) ((ItemSword) stack.getItem()).getAttackDamage() + (double) EnchantmentHelper.getModifierForCreature(stack, EnumCreatureAttribute.UNDEFINED);
                    if (damage > maxDamage) {
                        maxDamage = damage;
                        bestSlot = i;
                    }
                }
            }
        }

        if (bestSlot != -1) {
            equip(bestSlot);
        }

    }

    private static void equip(int slot) {
        AutoTool.mc.player.inventory.currentItem = slot;
        AutoTool.mc.playerController.syncCurrentPlayItem();
    }
}
