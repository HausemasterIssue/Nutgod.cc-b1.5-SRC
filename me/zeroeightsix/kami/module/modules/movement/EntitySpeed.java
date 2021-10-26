package me.zeroeightsix.kami.module.modules.movement;

import java.util.function.Predicate;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import me.zeroeightsix.kami.util.EntityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.util.MovementInput;
import net.minecraft.world.chunk.EmptyChunk;

@Module.Info(
    name = "EntitySpeed",
    category = Module.Category.MOVEMENT,
    description = "Abuse client-sided movement to shape sound barrier breaking rideables"
)
public class EntitySpeed extends Module {

    private Setting speed = this.register(Settings.f("Speed", 1.0F));
    private Setting antiStuck = this.register(Settings.b("AntiStuck"));
    private Setting flight = this.register(Settings.b("Flight", false));
    private Setting wobble = this.register(Settings.booleanBuilder("Wobble").withValue(Boolean.valueOf(true)).withVisibility((b) -> {
        return ((Boolean) this.flight.getValue()).booleanValue();
    }).build());
    private static Setting opacity = Settings.f("Boat opacity", 0.5F);

    public EntitySpeed() {
        this.register(EntitySpeed.opacity);
    }

    public void onUpdate() {
        if (EntitySpeed.mc.world != null && EntitySpeed.mc.player.getRidingEntity() != null) {
            Entity riding = EntitySpeed.mc.player.getRidingEntity();

            if (!(riding instanceof EntityPig) && !(riding instanceof AbstractHorse)) {
                if (riding instanceof EntityBoat) {
                    this.steerBoat(this.getBoat());
                }
            } else {
                this.steerEntity(riding);
            }
        }

    }

    private void steerEntity(Entity entity) {
        if (!((Boolean) this.flight.getValue()).booleanValue()) {
            entity.motionY = -0.4D;
        }

        if (((Boolean) this.flight.getValue()).booleanValue()) {
            if (EntitySpeed.mc.gameSettings.keyBindJump.isKeyDown()) {
                entity.motionY = (double) ((Float) this.speed.getValue()).floatValue();
            } else if (EntitySpeed.mc.gameSettings.keyBindForward.isKeyDown() || EntitySpeed.mc.gameSettings.keyBindBack.isKeyDown()) {
                entity.motionY = ((Boolean) this.wobble.getValue()).booleanValue() ? Math.sin((double) EntitySpeed.mc.player.ticksExisted) : 0.0D;
            }
        }

        this.moveForward(entity, (double) ((Float) this.speed.getValue()).floatValue() * 3.8D);
        if (entity instanceof EntityHorse) {
            entity.rotationYaw = EntitySpeed.mc.player.rotationYaw;
        }

    }

    private void steerBoat(EntityBoat boat) {
        if (boat != null) {
            boolean forward = EntitySpeed.mc.gameSettings.keyBindForward.isKeyDown();
            boolean left = EntitySpeed.mc.gameSettings.keyBindLeft.isKeyDown();
            boolean right = EntitySpeed.mc.gameSettings.keyBindRight.isKeyDown();
            boolean back = EntitySpeed.mc.gameSettings.keyBindBack.isKeyDown();

            if (!forward || !back) {
                boat.motionY = 0.0D;
            }

            if (EntitySpeed.mc.gameSettings.keyBindJump.isKeyDown()) {
                boat.motionY += (double) (((Float) this.speed.getValue()).floatValue() / 2.0F);
            }

            if (forward || left || right || back) {
                int angle;

                if (left && right) {
                    angle = forward ? 0 : (back ? 180 : -1);
                } else if (forward && back) {
                    angle = left ? -90 : (right ? 90 : -1);
                } else {
                    angle = left ? -90 : (right ? 90 : 0);
                    if (forward) {
                        angle /= 2;
                    } else if (back) {
                        angle = 180 - angle / 2;
                    }
                }

                if (angle != -1) {
                    float yaw = EntitySpeed.mc.player.rotationYaw + (float) angle;

                    boat.motionX = EntityUtil.getRelativeX(yaw) * (double) ((Float) this.speed.getValue()).floatValue();
                    boat.motionZ = EntityUtil.getRelativeZ(yaw) * (double) ((Float) this.speed.getValue()).floatValue();
                }
            }
        }
    }

    public void onRender() {
        EntityBoat boat = this.getBoat();

        if (boat != null) {
            boat.rotationYaw = EntitySpeed.mc.player.rotationYaw;
            boat.updateInputs(false, false, false, false);
        }
    }

    private EntityBoat getBoat() {
        return EntitySpeed.mc.player.getRidingEntity() != null && EntitySpeed.mc.player.getRidingEntity() instanceof EntityBoat ? (EntityBoat) EntitySpeed.mc.player.getRidingEntity() : null;
    }

    private void moveForward(Entity entity, double speed) {
        if (entity != null) {
            MovementInput movementInput = EntitySpeed.mc.player.movementInput;
            double forward = (double) movementInput.moveForward;
            double strafe = (double) movementInput.moveStrafe;
            boolean movingForward = forward != 0.0D;
            boolean movingStrafe = strafe != 0.0D;
            float yaw = EntitySpeed.mc.player.rotationYaw;

            if (!movingForward && !movingStrafe) {
                this.setEntitySpeed(entity, 0.0D, 0.0D);
            } else {
                if (forward != 0.0D) {
                    if (strafe > 0.0D) {
                        yaw += (float) (forward > 0.0D ? -45 : 45);
                    } else if (strafe < 0.0D) {
                        yaw += (float) (forward > 0.0D ? 45 : -45);
                    }

                    strafe = 0.0D;
                    if (forward > 0.0D) {
                        forward = 1.0D;
                    } else {
                        forward = -1.0D;
                    }
                }

                double motX = forward * speed * Math.cos(Math.toRadians((double) (yaw + 90.0F))) + strafe * speed * Math.sin(Math.toRadians((double) (yaw + 90.0F)));
                double motZ = forward * speed * Math.sin(Math.toRadians((double) (yaw + 90.0F))) - strafe * speed * Math.cos(Math.toRadians((double) (yaw + 90.0F)));

                if (this.isBorderingChunk(entity, motX, motZ)) {
                    motZ = 0.0D;
                    motX = 0.0D;
                }

                this.setEntitySpeed(entity, motX, motZ);
            }
        }

    }

    private void setEntitySpeed(Entity entity, double motX, double motZ) {
        entity.motionX = motX;
        entity.motionZ = motZ;
    }

    private boolean isBorderingChunk(Entity entity, double motX, double motZ) {
        return ((Boolean) this.antiStuck.getValue()).booleanValue() && EntitySpeed.mc.world.getChunk((int) (entity.posX + motX) >> 4, (int) (entity.posZ + motZ) >> 4) instanceof EmptyChunk;
    }

    public static float getOpacity() {
        return ((Float) EntitySpeed.opacity.getValue()).floatValue();
    }
}
