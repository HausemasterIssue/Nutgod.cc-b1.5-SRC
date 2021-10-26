package me.zeroeightsix.kami.module.modules.render;

import me.zeroeightsix.kami.command.Command;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import me.zeroeightsix.kami.util.Friends;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;

@Module.Info(
    name = "ExtraTab",
    description = "Expands the player tab menu",
    category = Module.Category.RENDER
)
public class ExtraTab extends Module {

    public Setting tabSize = this.register((Setting) Settings.integerBuilder("Players").withMinimum(Integer.valueOf(1)).withValue((Number) Integer.valueOf(80)).build());
    public static ExtraTab INSTANCE;

    public ExtraTab() {
        ExtraTab.INSTANCE = this;
    }

    public static String getPlayerName(NetworkPlayerInfo networkPlayerInfoIn) {
        String dname = networkPlayerInfoIn.getDisplayName() != null ? networkPlayerInfoIn.getDisplayName().getFormattedText() : ScorePlayerTeam.formatPlayerName(networkPlayerInfoIn.getPlayerTeam(), networkPlayerInfoIn.getGameProfile().getName());

        return Friends.isFriend(dname) ? String.format("%sa%s", new Object[] { Character.valueOf(Command.SECTIONSIGN()), dname}) : dname;
    }
}
