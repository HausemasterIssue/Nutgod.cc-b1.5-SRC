package me.zeroeightsix.kami.module.modules.render;

import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import me.zeroeightsix.kami.util.EntityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

@Module.Info(
    name = "Chams",
    category = Module.Category.RENDER,
    description = "See entities through walls"
)
public class Chams extends Module {

    private static Setting players = Settings.b("Players", true);
    private static Setting animals = Settings.b("Animals", false);
    private static Setting mobs = Settings.b("Mobs", false);

    public Chams() {
        this.registerAll(new Setting[] { Chams.players, Chams.animals, Chams.mobs});
    }

    public static boolean renderChams(Entity entity) {
        return entity instanceof EntityPlayer ? ((Boolean) Chams.players.getValue()).booleanValue() : (EntityUtil.isPassive(entity) ? ((Boolean) Chams.animals.getValue()).booleanValue() : ((Boolean) Chams.mobs.getValue()).booleanValue());
    }
}
