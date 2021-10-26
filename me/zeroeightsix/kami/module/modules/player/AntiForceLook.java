package me.zeroeightsix.kami.module.modules.player;

import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.EventHook;
import me.zero.alpine.listener.Listener;
import me.zeroeightsix.kami.event.events.PacketEvent;
import me.zeroeightsix.kami.module.Module;
import net.minecraft.network.play.server.SPacketPlayerPosLook;

@Module.Info(
    name = "AntiForceLook",
    category = Module.Category.PLAYER
)
public class AntiForceLook extends Module {

    @EventHandler
    Listener receiveListener = new Listener((event) -> {
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            SPacketPlayerPosLook packet = (SPacketPlayerPosLook) event.getPacket();

            packet.yaw = AntiForceLook.mc.player.rotationYaw;
            packet.pitch = AntiForceLook.mc.player.rotationPitch;
        }

    }, new Predicate[0]);
}
