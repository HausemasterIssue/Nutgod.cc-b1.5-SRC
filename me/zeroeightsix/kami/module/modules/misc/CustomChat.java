package me.zeroeightsix.kami.module.modules.misc;

import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.EventHook;
import me.zero.alpine.listener.Listener;
import me.zeroeightsix.kami.event.events.PacketEvent;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import net.minecraft.network.play.client.CPacketChatMessage;

@Module.Info(
    name = "Nutgod.cc chat",
    category = Module.Category.MISC,
    description = "Modifies your chat messages"
)
public class CustomChat extends Module {

    private Setting commands = this.register(Settings.b("Commands", false));
    private final String KAMI_SUFFIX = " �?? ɴᴜᴛɢ�?ᴅ.ᴄᴄ ࿉";
    @EventHandler
    public Listener listener = new Listener((event) -> {
        if (event.getPacket() instanceof CPacketChatMessage) {
            String s = ((CPacketChatMessage) event.getPacket()).getMessage();

            if (s.startsWith("/") && !((Boolean) this.commands.getValue()).booleanValue()) {
                return;
            }

            s = s + " �?? ɴᴜᴛɢ�?ᴅ.ᴄᴄ ࿉";
            if (s.length() >= 256) {
                s = s.substring(0, 256);
            }

            ((CPacketChatMessage) event.getPacket()).message = s;
        }

    }, new Predicate[0]);
}
