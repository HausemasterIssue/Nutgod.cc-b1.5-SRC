package me.zeroeightsix.kami.module.modules.combat;

import java.util.Iterator;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.module.ModuleManager;
import me.zeroeightsix.kami.module.modules.misc.AutoTool;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import me.zeroeightsix.kami.util.EntityUtil;
import me.zeroeightsix.kami.util.Friends;
import me.zeroeightsix.kami.util.LagCompensator;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;

@Module.Info(
    name = "Aura",
    category = Module.Category.COMBAT,
    description = "Hits entities around you"
)
public class Aura extends Module {

    private Setting players = this.register(Settings.b("Players", true));
    private Setting animals = this.register(Settings.b("Animals", false));
    private Setting mobs = this.register(Settings.b("Mobs", false));
    private Setting range = this.register(Settings.d("Range", 5.5D));
    private Setting wait = this.register(Settings.b("Wait", true));
    private Setting walls = this.register(Settings.b("Walls", false));
    private Setting sharpness = this.register(Settings.b("32k Switch", false));

    public void onUpdate() {
        if (!Aura.mc.player.isDead) {
            boolean shield = Aura.mc.player.getHeldItemOffhand().getItem().equals(Items.SHIELD) && Aura.mc.player.getActiveHand() == EnumHand.OFF_HAND;

            if (!Aura.mc.player.isHandActive() || shield) {
                if (((Boolean) this.wait.getValue()).booleanValue()) {
                    if (Aura.mc.player.getCooledAttackStrength(this.getLagComp()) < 1.0F) {
                        return;
                    }

                    if (Aura.mc.player.ticksExisted % 2 != 0) {
                        return;
                    }
                }

                Iterator entityIterator = Minecraft.getMinecraft().world.loadedEntityList.iterator();

                Entity target;

                while (true) {
                    do {
                        do {
                            do {
                                do {
                                    do {
                                        do {
                                            if (!entityIterator.hasNext()) {
                                                return;
                                            }

                                            target = (Entity) entityIterator.next();
                                        } while (!EntityUtil.isLiving(target));
                                    } while (target == Aura.mc.player);
                                } while ((double) Aura.mc.player.getDistance(target) > ((Double) this.range.getValue()).doubleValue());
                            } while (((EntityLivingBase) target).getHealth() <= 0.0F);
                        } while (((EntityLivingBase) target).hurtTime != 0 && ((Boolean) this.wait.getValue()).booleanValue());
                    } while (!((Boolean) this.walls.getValue()).booleanValue() && !Aura.mc.player.canEntityBeSeen(target) && !this.canEntityFeetBeSeen(target));

                    if (((Boolean) this.players.getValue()).booleanValue() && target instanceof EntityPlayer && !Friends.isFriend(target.getName())) {
                        this.attack(target);
                        return;
                    }

                    if (EntityUtil.isPassive(target)) {
                        if (((Boolean) this.animals.getValue()).booleanValue()) {
                            break;
                        }
                    } else if (EntityUtil.isMobAggressive(target) && ((Boolean) this.mobs.getValue()).booleanValue()) {
                        break;
                    }
                }

                if (ModuleManager.isModuleEnabled("AutoTool")) {
                    AutoTool.equipBestWeapon();
                }

                this.attack(target);
            }
        }
    }

    private boolean checkSharpness(ItemStack stack) {
        if (stack.getTagCompound() == null) {
            return false;
        } else {
            NBTTagList enchants = (NBTTagList) stack.getTagCompound().getTag("ench");

            if (enchants == null) {
                return false;
            } else {
                for (int i = 0; i < enchants.tagCount(); ++i) {
                    NBTTagCompound enchant = enchants.getCompoundTagAt(i);

                    if (enchant.getInteger("id") == 16) {
                        int lvl = enchant.getInteger("lvl");

                        if (lvl >= 16) {
                            return true;
                        }
                        break;
                    }
                }

                return false;
            }
        }
    }

    private void attack(Entity e) {
        if (((Boolean) this.sharpness.getValue()).booleanValue() && !this.checkSharpness(Aura.mc.player.getHeldItemMainhand())) {
            int newSlot = -1;

            for (int i = 0; i < 9; ++i) {
                ItemStack stack = Aura.mc.player.inventory.getStackInSlot(i);

                if (stack != ItemStack.EMPTY && this.checkSharpness(stack)) {
                    newSlot = i;
                    break;
                }
            }

            if (newSlot != -1) {
                Aura.mc.player.inventory.currentItem = newSlot;
            }
        }

        Aura.mc.playerController.attackEntity(Aura.mc.player, e);
        Aura.mc.player.swingArm(EnumHand.MAIN_HAND);
    }

    private float getLagComp() {
        return ((Boolean) this.wait.getValue()).booleanValue() ? -(20.0F - LagCompensator.INSTANCE.getTickRate()) : 0.0F;
    }

    private boolean canEntityFeetBeSeen(Entity entityIn) {
        return Aura.mc.world.rayTraceBlocks(new Vec3d(Aura.mc.player.posX, Aura.mc.player.posX + (double) Aura.mc.player.getEyeHeight(), Aura.mc.player.posZ), new Vec3d(entityIn.posX, entityIn.posY, entityIn.posZ), false, true, false) == null;
    }
}
