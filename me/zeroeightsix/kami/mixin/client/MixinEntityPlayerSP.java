package me.zeroeightsix.kami.mixin.client;

import me.zeroeightsix.kami.KamiMod;
import me.zeroeightsix.kami.event.events.PlayerMoveEvent;
import me.zeroeightsix.kami.module.ModuleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.MoverType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ EntityPlayerSP.class})
public class MixinEntityPlayerSP {

    @Redirect(
        method = { "onLivingUpdate"},
        at =             @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/entity/EntityPlayerSP;closeScreen()V"
            )
    )
    public void closeScreen(EntityPlayerSP entityPlayerSP) {
        if (!ModuleManager.isModuleEnabled("PortalChat")) {
            ;
        }
    }

    @Redirect(
        method = { "onLivingUpdate"},
        at =             @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/Minecraft;displayGuiScreen(Lnet/minecraft/client/gui/GuiScreen;)V"
            )
    )
    public void closeScreen(Minecraft minecraft, GuiScreen screen) {
        if (!ModuleManager.isModuleEnabled("PortalChat")) {
            ;
        }
    }

    @Inject(
        method = { "move"},
        at = {             @At("HEAD")},
        cancellable = true
    )
    public void move(MoverType type, double x, double y, double z, CallbackInfo info) {
        PlayerMoveEvent event = new PlayerMoveEvent(type, x, y, z);

        KamiMod.EVENT_BUS.post(event);
        if (event.isCancelled()) {
            info.cancel();
        }

    }
}
