package me.zeroeightsix.kami.util;

import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class EntityUtil {

    public static boolean isPassive(Entity e) {
        return e instanceof EntityWolf && ((EntityWolf) e).isAngry() ? false : (!(e instanceof EntityAnimal) && !(e instanceof EntityAgeable) && !(e instanceof EntityTameable) && !(e instanceof EntityAmbientCreature) && !(e instanceof EntitySquid) ? e instanceof EntityIronGolem && ((EntityIronGolem) e).getRevengeTarget() == null : true);
    }

    public static boolean isLiving(Entity e) {
        return e instanceof EntityLivingBase;
    }

    public static boolean isFakeLocalPlayer(Entity entity) {
        return entity != null && entity.getEntityId() == -100 && Wrapper.getPlayer() != entity;
    }

    public static Vec3d getInterpolatedAmount(Entity entity, double x, double y, double z) {
        return new Vec3d((entity.posX - entity.lastTickPosX) * x, (entity.posY - entity.lastTickPosY) * y, (entity.posZ - entity.lastTickPosZ) * z);
    }

    public static Vec3d getInterpolatedAmount(Entity entity, Vec3d vec) {
        return getInterpolatedAmount(entity, vec.x, vec.y, vec.z);
    }

    public static Vec3d getInterpolatedAmount(Entity entity, double ticks) {
        return getInterpolatedAmount(entity, ticks, ticks, ticks);
    }

    public static boolean isMobAggressive(Entity entity) {
        if (entity instanceof EntityPigZombie) {
            if (((EntityPigZombie) entity).isArmsRaised() || ((EntityPigZombie) entity).isAngry()) {
                return true;
            }
        } else {
            if (entity instanceof EntityWolf) {
                return ((EntityWolf) entity).isAngry() && !Wrapper.getPlayer().equals(((EntityWolf) entity).getOwner());
            }

            if (entity instanceof EntityEnderman) {
                return ((EntityEnderman) entity).isScreaming();
            }
        }

        return isHostileMob(entity);
    }

    public static boolean isNeutralMob(Entity entity) {
        return entity instanceof EntityPigZombie || entity instanceof EntityWolf || entity instanceof EntityEnderman;
    }

    public static boolean isFriendlyMob(Entity entity) {
        return entity.isCreatureType(EnumCreatureType.CREATURE, false) && !isNeutralMob(entity) || entity.isCreatureType(EnumCreatureType.AMBIENT, false) || entity instanceof EntityVillager || entity instanceof EntityIronGolem || isNeutralMob(entity) && !isMobAggressive(entity);
    }

    public static boolean isHostileMob(Entity entity) {
        return entity.isCreatureType(EnumCreatureType.MONSTER, false) && !isNeutralMob(entity);
    }

    public static Vec3d getInterpolatedPos(Entity entity, float ticks) {
        return (new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ)).add(getInterpolatedAmount(entity, (double) ticks));
    }

    public static Vec3d getInterpolatedRenderPos(Entity entity, float ticks) {
        return getInterpolatedPos(entity, ticks).subtract(Wrapper.getMinecraft().getRenderManager().renderPosX, Wrapper.getMinecraft().getRenderManager().renderPosY, Wrapper.getMinecraft().getRenderManager().renderPosZ);
    }

    public static boolean isInWater(Entity entity) {
        if (entity == null) {
            return false;
        } else {
            double y = entity.posY + 0.01D;

            for (int x = MathHelper.floor(entity.posX); x < MathHelper.ceil(entity.posX); ++x) {
                for (int z = MathHelper.floor(entity.posZ); z < MathHelper.ceil(entity.posZ); ++z) {
                    BlockPos pos = new BlockPos(x, (int) y, z);

                    if (Wrapper.getWorld().getBlockState(pos).getBlock() instanceof BlockLiquid) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    public static boolean isDrivenByPlayer(Entity entityIn) {
        return Wrapper.getPlayer() != null && entityIn != null && entityIn.equals(Wrapper.getPlayer().getRidingEntity());
    }

    public static boolean isAboveWater(Entity entity) {
        return isAboveWater(entity, false);
    }

    public static boolean isAboveWater(Entity entity, boolean packet) {
        if (entity == null) {
            return false;
        } else {
            double y = entity.posY - (packet ? 0.03D : (isPlayer(entity) ? 0.2D : 0.5D));

            for (int x = MathHelper.floor(entity.posX); x < MathHelper.ceil(entity.posX); ++x) {
                for (int z = MathHelper.floor(entity.posZ); z < MathHelper.ceil(entity.posZ); ++z) {
                    BlockPos pos = new BlockPos(x, MathHelper.floor(y), z);

                    if (Wrapper.getWorld().getBlockState(pos).getBlock() instanceof BlockLiquid) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    public static double[] calculateLookAt(double px, double py, double pz, EntityPlayer me) {
        double dirx = me.posX - px;
        double diry = me.posY - py;
        double dirz = me.posZ - pz;
        double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);

        dirx /= len;
        diry /= len;
        dirz /= len;
        double pitch = Math.asin(diry);
        double yaw = Math.atan2(dirz, dirx);

        pitch = pitch * 180.0D / 3.141592653589793D;
        yaw = yaw * 180.0D / 3.141592653589793D;
        yaw += 90.0D;
        return new double[] { yaw, pitch};
    }

    public static boolean isPlayer(Entity entity) {
        return entity instanceof EntityPlayer;
    }

    public static double getRelativeX(float yaw) {
        return (double) MathHelper.sin(-yaw * 0.017453292F);
    }

    public static double getRelativeZ(float yaw) {
        return (double) MathHelper.cos(yaw * 0.017453292F);
    }
}