package me.zeroeightsix.kami.module.modules.movement;

import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import me.zeroeightsix.kami.util.EntityUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayer.PositionRotation;

@Module.Info(
    category = Module.Category.MOVEMENT,
    description = "Makes the player fly",
    name = "Flight"
)
public class Flight extends Module {

    private Setting speed = this.register(Settings.f("Speed", 10.0F));
    private Setting mode;

    public Flight() {
        this.mode = this.register(Settings.e("Mode", Flight.FlightMode.VANILLA));
    }

    protected void onEnable() {
        if (Flight.mc.player != null) {
            switch ((Flight.FlightMode) this.mode.getValue()) {
            case VANILLA:
                Flight.mc.player.capabilities.isFlying = true;
                if (Flight.mc.player.capabilities.isCreativeMode) {
                    return;
                } else {
                    Flight.mc.player.capabilities.allowFlying = true;
                }

            default:
            }
        }
    }

    public void onUpdate() {
        switch ((Flight.FlightMode) this.mode.getValue()) {
        case VANILLA:
            Flight.mc.player.capabilities.setFlySpeed(((Float) this.speed.getValue()).floatValue() / 100.0F);
            Flight.mc.player.capabilities.isFlying = true;
            if (Flight.mc.player.capabilities.isCreativeMode) {
                return;
            }

            Flight.mc.player.capabilities.allowFlying = true;
            break;

        case STATIC:
            Flight.mc.player.capabilities.isFlying = false;
            Flight.mc.player.motionX = 0.0D;
            Flight.mc.player.motionY = 0.0D;
            Flight.mc.player.motionZ = 0.0D;
            Flight.mc.player.jumpMovementFactor = ((Float) this.speed.getValue()).floatValue();
            if (Flight.mc.gameSettings.keyBindJump.isKeyDown()) {
                Flight.mc.player.motionY += (double) ((Float) this.speed.getValue()).floatValue();
            }

            if (Flight.mc.gameSettings.keyBindSneak.isKeyDown()) {
                Flight.mc.player.motionY -= (double) ((Float) this.speed.getValue()).floatValue();
            }
            break;

        case PACKET:
            boolean forward = Flight.mc.gameSettings.keyBindForward.isKeyDown();
            boolean left = Flight.mc.gameSettings.keyBindLeft.isKeyDown();
            boolean right = Flight.mc.gameSettings.keyBindRight.isKeyDown();
            boolean back = Flight.mc.gameSettings.keyBindBack.isKeyDown();
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

            if (angle != -1 && (forward || left || right || back)) {
                float yaw = Flight.mc.player.rotationYaw + (float) angle;

                Flight.mc.player.motionX = EntityUtil.getRelativeX(yaw) * 0.20000000298023224D;
                Flight.mc.player.motionZ = EntityUtil.getRelativeZ(yaw) * 0.20000000298023224D;
            }

            Flight.mc.player.motionY = 0.0D;
            Flight.mc.player.connection.sendPacket(new PositionRotation(Flight.mc.player.posX + Flight.mc.player.motionX, Flight.mc.player.posY + (Minecraft.getMinecraft().gameSettings.keyBindJump.isKeyDown() ? 0.0622D : 0.0D) - (Minecraft.getMinecraft().gameSettings.keyBindSneak.isKeyDown() ? 0.0622D : 0.0D), Flight.mc.player.posZ + Flight.mc.player.motionZ, Flight.mc.player.rotationYaw, Flight.mc.player.rotationPitch, false));
            Flight.mc.player.connection.sendPacket(new PositionRotation(Flight.mc.player.posX + Flight.mc.player.motionX, Flight.mc.player.posY - 42069.0D, Flight.mc.player.posZ + Flight.mc.player.motionZ, Flight.mc.player.rotationYaw, Flight.mc.player.rotationPitch, true));
        }

    }

    protected void onDisable() {
        switch ((Flight.FlightMode) this.mode.getValue()) {
        case VANILLA:
            Flight.mc.player.capabilities.isFlying = false;
            Flight.mc.player.capabilities.setFlySpeed(0.05F);
            if (Flight.mc.player.capabilities.isCreativeMode) {
                return;
            } else {
                Flight.mc.player.capabilities.allowFlying = false;
            }

        default:
        }
    }

    public double[] moveLooking() {
        return new double[] { (double) (Flight.mc.player.rotationYaw * 360.0F / 360.0F * 180.0F / 180.0F), 0.0D};
    }

    public static enum FlightMode {

        VANILLA, STATIC, PACKET;
    }
}
