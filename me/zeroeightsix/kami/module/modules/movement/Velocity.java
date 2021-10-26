package me.zeroeightsix.kami.module.modules.movement;

import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.EventHook;
import me.zero.alpine.listener.Listener;
import me.zeroeightsix.kami.event.KamiEvent;
import me.zeroeightsix.kami.event.events.EntityEvent;
import me.zeroeightsix.kami.event.events.PacketEvent;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;

@Module.Info(
    name = "Velocity",
    description = "Modify knockback impact",
    category = Module.Category.MOVEMENT
)
public class Velocity extends Module {

    private Setting horizontal = this.register(Settings.f("Horizontal", 0.0F));
    private Setting vertical = this.register(Settings.f("Vertical", 0.0F));
    @EventHandler
    private Listener packetEventListener = new Listener((event) -> {
        if (event.getEra() == KamiEvent.Era.PRE) {
            if (event.getPacket() instanceof SPacketEntityVelocity) {
                SPacketEntityVelocity velocity = (SPacketEntityVelocity) event.getPacket();

                if (velocity.getEntityID() == Velocity.mc.player.entityId) {
                    if (((Float) this.horizontal.getValue()).floatValue() == 0.0F && ((Float) this.vertical.getValue()).floatValue() == 0.0F) {
                        event.cancel();
                    }

                    velocity.motionX = (int) ((float) velocity.motionX * ((Float) this.horizontal.getValue()).floatValue());
                    velocity.motionY = (int) ((float) velocity.motionY * ((Float) this.vertical.getValue()).floatValue());
                    velocity.motionZ = (int) ((float) velocity.motionZ * ((Float) this.horizontal.getValue()).floatValue());
                }
            } else if (event.getPacket() instanceof SPacketExplosion) {
                if (((Float) this.horizontal.getValue()).floatValue() == 0.0F && ((Float) this.vertical.getValue()).floatValue() == 0.0F) {
                    event.cancel();
                }

                SPacketExplosion velocity1 = (SPacketExplosion) event.getPacket();

                velocity1.motionX *= ((Float) this.horizontal.getValue()).floatValue();
                velocity1.motionY *= ((Float) this.vertical.getValue()).floatValue();
                velocity1.motionZ *= ((Float) this.horizontal.getValue()).floatValue();
            }
        }

    }, new Predicate[0]);
    @EventHandler
    private Listener entityCollisionListener = new Listener((event) -> {
        if (event.getEntity() == Velocity.mc.player) {
            if (((Float) this.horizontal.getValue()).floatValue() == 0.0F && ((Float) this.vertical.getValue()).floatValue() == 0.0F) {
                event.cancel();
                return;
            }

            event.setX(-event.getX() * (double) ((Float) this.horizontal.getValue()).floatValue());
            event.setY(0.0D);
            event.setZ(-event.getZ() * (double) ((Float) this.horizontal.getValue()).floatValue());
        }

    }, new Predicate[0]);
}
