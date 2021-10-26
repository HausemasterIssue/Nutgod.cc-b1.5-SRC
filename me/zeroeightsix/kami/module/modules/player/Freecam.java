package me.zeroeightsix.kami.module.modules.player;

import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.EventHook;
import me.zero.alpine.listener.Listener;
import me.zeroeightsix.kami.event.events.PacketEvent;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;

@Module.Info(
    name = "Freecam",
    category = Module.Category.PLAYER,
    description = "Leave your body and trascend into the realm of the gods"
)
public class Freecam extends Module {

    private Setting speed = this.register(Settings.i("Speed", 5));
    private double posX;
    private double posY;
    private double posZ;
    private float pitch;
    private float yaw;
    private EntityOtherPlayerMP clonedPlayer;
    private boolean isRidingEntity;
    private Entity ridingEntity;
    @EventHandler
    private Listener moveListener = new Listener((event) -> {
        Freecam.mc.player.noClip = true;
    }, new Predicate[0]);
    @EventHandler
    private Listener pushListener = new Listener((event) -> {
        event.setCanceled(true);
    }, new Predicate[0]);
    @EventHandler
    private Listener sendListener = new Listener((event) -> {
        if (event.getPacket() instanceof CPacketPlayer || event.getPacket() instanceof CPacketInput) {
            event.cancel();
        }

    }, new Predicate[0]);

    protected void onEnable() {
        if (Freecam.mc.player != null) {
            this.isRidingEntity = Freecam.mc.player.getRidingEntity() != null;
            if (Freecam.mc.player.getRidingEntity() == null) {
                this.posX = Freecam.mc.player.posX;
                this.posY = Freecam.mc.player.posY;
                this.posZ = Freecam.mc.player.posZ;
            } else {
                this.ridingEntity = Freecam.mc.player.getRidingEntity();
                Freecam.mc.player.dismountRidingEntity();
            }

            this.pitch = Freecam.mc.player.rotationPitch;
            this.yaw = Freecam.mc.player.rotationYaw;
            this.clonedPlayer = new EntityOtherPlayerMP(Freecam.mc.world, Freecam.mc.getSession().getProfile());
            this.clonedPlayer.copyLocationAndAnglesFrom(Freecam.mc.player);
            this.clonedPlayer.rotationYawHead = Freecam.mc.player.rotationYawHead;
            Freecam.mc.world.addEntityToWorld(-100, this.clonedPlayer);
            Freecam.mc.player.capabilities.isFlying = true;
            Freecam.mc.player.capabilities.setFlySpeed((float) ((Integer) this.speed.getValue()).intValue() / 100.0F);
            Freecam.mc.player.noClip = true;
        }

    }

    protected void onDisable() {
        EntityPlayerSP localPlayer = Freecam.mc.player;

        if (localPlayer != null) {
            Freecam.mc.player.setPositionAndRotation(this.posX, this.posY, this.posZ, this.yaw, this.pitch);
            Freecam.mc.world.removeEntityFromWorld(-100);
            this.clonedPlayer = null;
            this.posX = this.posY = this.posZ = 0.0D;
            this.pitch = this.yaw = 0.0F;
            Freecam.mc.player.capabilities.isFlying = false;
            Freecam.mc.player.capabilities.setFlySpeed(0.05F);
            Freecam.mc.player.noClip = false;
            Freecam.mc.player.motionX = Freecam.mc.player.motionY = Freecam.mc.player.motionZ = 0.0D;
            if (this.isRidingEntity) {
                Freecam.mc.player.startRiding(this.ridingEntity, true);
            }
        }

    }

    public void onUpdate() {
        Freecam.mc.player.capabilities.isFlying = true;
        Freecam.mc.player.capabilities.setFlySpeed((float) ((Integer) this.speed.getValue()).intValue() / 100.0F);
        Freecam.mc.player.noClip = true;
        Freecam.mc.player.onGround = false;
        Freecam.mc.player.fallDistance = 0.0F;
    }
}
