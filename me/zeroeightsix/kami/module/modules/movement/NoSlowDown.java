package me.zeroeightsix.kami.module.modules.movement;

import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.EventHook;
import me.zero.alpine.listener.Listener;
import me.zeroeightsix.kami.module.Module;
import net.minecraft.util.MovementInput;
import net.minecraftforge.client.event.InputUpdateEvent;

@Module.Info(
    name = "NoSlowDown",
    category = Module.Category.MOVEMENT
)
public class NoSlowDown extends Module {

    @EventHandler
    private Listener eventListener = new Listener((event) -> {
        if (NoSlowDown.mc.player.isHandActive() && !NoSlowDown.mc.player.isRiding()) {
            MovementInput movementinput = event.getMovementInput();

            movementinput.moveStrafe *= 5.0F;
            movementinput = event.getMovementInput();
            movementinput.moveForward *= 5.0F;
        }

    }, new Predicate[0]);
}
