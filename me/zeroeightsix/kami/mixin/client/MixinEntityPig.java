package me.zeroeightsix.kami.mixin.client;

import me.zeroeightsix.kami.module.ModuleManager;
import net.minecraft.entity.passive.EntityPig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ EntityPig.class})
public class MixinEntityPig {

    @Inject(
        method = { "canBeSteered"},
        at = {             @At("HEAD")},
        cancellable = true
    )
    public void canBeSteered(CallbackInfoReturnable returnable) {
        if (ModuleManager.isModuleEnabled("EntitySpeed")) {
            returnable.setReturnValue(Boolean.valueOf(true));
            returnable.cancel();
        }

    }
}
