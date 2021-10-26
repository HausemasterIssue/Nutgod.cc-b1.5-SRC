package me.zeroeightsix.kami.module.modules.movement;

import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.util.math.MathHelper;

@Module.Info(
    name = "ElytraFlight",
    description = "Allows infinite elytra flying",
    category = Module.Category.MOVEMENT
)
public class ElytraFlight extends Module {

    private Setting mode;

    public ElytraFlight() {
        this.mode = this.register(Settings.e("Mode", ElytraFlight.ElytraFlightMode.BOOST));
    }

    public void onUpdate() {
        if (ElytraFlight.mc.player.isElytraFlying()) {
            switch ((ElytraFlight.ElytraFlightMode) this.mode.getValue()) {
            case BOOST:
                if (ElytraFlight.mc.player.isInWater()) {
                    ElytraFlight.mc.getConnection().sendPacket(new CPacketEntityAction(ElytraFlight.mc.player, Action.START_FALL_FLYING));
                    return;
                }

                if (ElytraFlight.mc.gameSettings.keyBindJump.isKeyDown()) {
                    ElytraFlight.mc.player.motionY += 0.08D;
                } else if (ElytraFlight.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    ElytraFlight.mc.player.motionY -= 0.04D;
                }

                float yaw;

                if (ElytraFlight.mc.gameSettings.keyBindForward.isKeyDown()) {
                    yaw = (float) Math.toRadians((double) ElytraFlight.mc.player.rotationYaw);
                    ElytraFlight.mc.player.motionX -= (double) (MathHelper.sin(yaw) * 0.05F);
                    ElytraFlight.mc.player.motionZ += (double) (MathHelper.cos(yaw) * 0.05F);
                } else if (ElytraFlight.mc.gameSettings.keyBindBack.isKeyDown()) {
                    yaw = (float) Math.toRadians((double) ElytraFlight.mc.player.rotationYaw);
                    ElytraFlight.mc.player.motionX += (double) (MathHelper.sin(yaw) * 0.05F);
                    ElytraFlight.mc.player.motionZ -= (double) (MathHelper.cos(yaw) * 0.05F);
                }
                break;

            case FLY:
                ElytraFlight.mc.player.capabilities.isFlying = true;
            }

        }
    }

    protected void onDisable() {
        if (!ElytraFlight.mc.player.capabilities.isCreativeMode) {
            ElytraFlight.mc.player.capabilities.isFlying = false;
        }
    }

    private static enum ElytraFlightMode {

        BOOST, FLY;
    }
}
