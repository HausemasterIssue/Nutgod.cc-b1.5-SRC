package me.zeroeightsix.kami.module.modules.movement;

import me.zeroeightsix.kami.module.Module;

@Module.Info(
    name = "Sprint",
    description = "Automatically makes the player sprint",
    category = Module.Category.MOVEMENT
)
public class Sprint extends Module {

    public void onUpdate() {
        try {
            if (!Sprint.mc.player.collidedHorizontally && Sprint.mc.player.moveForward > 0.0F) {
                Sprint.mc.player.setSprinting(true);
            } else {
                Sprint.mc.player.setSprinting(false);
            }
        } catch (Exception exception) {
            ;
        }

    }
}
