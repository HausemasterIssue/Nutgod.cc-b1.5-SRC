package me.zeroeightsix.kami.util.other;

import java.math.BigDecimal;
import java.math.RoundingMode;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public final class MathUtil {

    public static Vec3d interpolateEntity(Entity entity, float time) {
        return new Vec3d(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) time, entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) time, entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) time);
    }

    public static double radToDeg(double rad) {
        return rad * 57.295780181884766D;
    }

    public static double degToRad(double deg) {
        return deg * 0.01745329238474369D;
    }

    public static Vec3d direction(float yaw) {
        return new Vec3d(Math.cos(degToRad((double) (yaw + 90.0F))), 0.0D, Math.sin(degToRad((double) (yaw + 90.0F))));
    }

    public static float[] calcAngle(Vec3d from, Vec3d to) {
        double difX = to.x - from.x;
        double difY = (to.y - from.y) * -1.0D;
        double difZ = to.z - from.z;
        double dist = (double) MathHelper.sqrt(difX * difX + difZ * difZ);

        return new float[] { (float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0D), (float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difY, dist)))};
    }

    public static double[] directionSpeed(double speed) {
        Minecraft mc = Minecraft.getMinecraft();
        float forward = mc.player.movementInput.moveForward;
        float side = mc.player.movementInput.moveStrafe;
        float yaw = mc.player.prevRotationYaw + (mc.player.rotationYaw - mc.player.prevRotationYaw) * mc.getRenderPartialTicks();

        if (forward != 0.0F) {
            if (side > 0.0F) {
                yaw += (float) (forward > 0.0F ? -45 : 45);
            } else if (side < 0.0F) {
                yaw += (float) (forward > 0.0F ? 45 : -45);
            }

            side = 0.0F;
            if (forward > 0.0F) {
                forward = 1.0F;
            } else if (forward < 0.0F) {
                forward = -1.0F;
            }
        }

        double posX = (double) forward * speed * Math.cos(Math.toRadians((double) (yaw + 90.0F))) + (double) side * speed * Math.sin(Math.toRadians((double) (yaw + 90.0F)));
        double posZ = (double) forward * speed * Math.sin(Math.toRadians((double) (yaw + 90.0F))) - (double) side * speed * Math.cos(Math.toRadians((double) (yaw + 90.0F)));

        return new double[] { posX, posZ};
    }

    public static Vec3d mult(Vec3d factor, Vec3d multiplier) {
        return new Vec3d(factor.x * multiplier.x, factor.y * multiplier.y, factor.z * multiplier.z);
    }

    public static Vec3d mult(Vec3d factor, float multiplier) {
        return new Vec3d(factor.x * (double) multiplier, factor.y * (double) multiplier, factor.z * (double) multiplier);
    }

    public static Vec3d div(Vec3d factor, Vec3d divisor) {
        return new Vec3d(factor.x / divisor.x, factor.y / divisor.y, factor.z / divisor.z);
    }

    public static Vec3d div(Vec3d factor, float divisor) {
        return new Vec3d(factor.x / (double) divisor, factor.y / (double) divisor, factor.z / (double) divisor);
    }

    public static double round(double value, int places) {
        return places < 0 ? value : (new BigDecimal(value)).setScale(places, RoundingMode.HALF_UP).doubleValue();
    }

    public static float clamp(float val, float min, float max) {
        if (val <= min) {
            val = min;
        }

        if (val >= max) {
            val = max;
        }

        return val;
    }
}
