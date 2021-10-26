package me.zeroeightsix.kami.module.modules.player;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.EventHook;
import me.zero.alpine.listener.Listener;
import me.zeroeightsix.kami.event.events.PacketEvent;
import me.zeroeightsix.kami.module.Module;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;

@Module.Info(
    name = "Blink",
    category = Module.Category.PLAYER
)
public class Blink extends Module {

    Queue packets = new LinkedList();
    @EventHandler
    public Listener listener = new Listener((event) -> {
        if (this.isEnabled() && event.getPacket() instanceof CPacketPlayer) {
            event.cancel();
            this.packets.add((CPacketPlayer) event.getPacket());
        }

    }, new Predicate[0]);
    private EntityOtherPlayerMP clonedPlayer;

    protected void onEnable() {
        if (Blink.mc.player != null) {
            this.clonedPlayer = new EntityOtherPlayerMP(Blink.mc.world, Blink.mc.getSession().getProfile());
            this.clonedPlayer.copyLocationAndAnglesFrom(Blink.mc.player);
            this.clonedPlayer.rotationYawHead = Blink.mc.player.rotationYawHead;
            Blink.mc.world.addEntityToWorld(-100, this.clonedPlayer);
        }

    }

    protected void onDisable() {
        while (!this.packets.isEmpty()) {
            Blink.mc.player.connection.sendPacket((Packet) this.packets.poll());
        }

        EntityPlayerSP localPlayer = Blink.mc.player;

        if (localPlayer != null) {
            Blink.mc.world.removeEntityFromWorld(-100);
            this.clonedPlayer = null;
        }

    }

    public String getHudInfo() {
        return String.valueOf(this.packets.size());
    }
}
