package me.zeroeightsix.kami.mixin.client;

import me.zeroeightsix.kami.KamiMod;
import me.zeroeightsix.kami.event.events.GuiScreenEvent;
import me.zeroeightsix.kami.util.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ Minecraft.class})
public class MixinMinecraft {

    @Shadow
    WorldClient world;
    @Shadow
    EntityPlayerSP player;
    @Shadow
    GuiScreen currentScreen;
    @Shadow
    GameSettings gameSettings;
    @Shadow
    GuiIngame ingameGUI;
    @Shadow
    boolean skipRenderWorld;
    @Shadow
    SoundHandler soundHandler;

    @Inject(
        method = { "displayGuiScreen"},
        at = {             @At("HEAD")},
        cancellable = true
    )
    public void displayGuiScreen(GuiScreen guiScreenIn, CallbackInfo info) {
        GuiScreenEvent.Closed screenEvent = new GuiScreenEvent.Closed(Wrapper.getMinecraft().currentScreen);

        KamiMod.EVENT_BUS.post(screenEvent);
        GuiScreenEvent.Displayed screenEvent1 = new GuiScreenEvent.Displayed(guiScreenIn);

        KamiMod.EVENT_BUS.post(screenEvent1);
        Object guiScreenIn1 = screenEvent1.getScreen();

        if (guiScreenIn1 == null && this.world == null) {
            guiScreenIn1 = new GuiMainMenu();
        } else if (guiScreenIn1 == null && this.player.getHealth() <= 0.0F) {
            guiScreenIn1 = new GuiGameOver((ITextComponent) null);
        }

        GuiScreen old = this.currentScreen;
        GuiOpenEvent event = new GuiOpenEvent((GuiScreen) guiScreenIn1);

        if (!MinecraftForge.EVENT_BUS.post(event)) {
            guiScreenIn = event.getGui();
            if (old != null && guiScreenIn != old) {
                old.onGuiClosed();
            }

            if (guiScreenIn instanceof GuiMainMenu || guiScreenIn instanceof GuiMultiplayer) {
                this.gameSettings.showDebugInfo = false;
                this.ingameGUI.getChatGUI().clearChatMessages(true);
            }

            this.currentScreen = guiScreenIn;
            if (guiScreenIn != null) {
                Minecraft.getMinecraft().setIngameNotInFocus();
                KeyBinding.unPressAllKeys();

                while (true) {
                    if (!Mouse.next()) {
                        while (Keyboard.next()) {
                            ;
                        }

                        ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());
                        int i = scaledresolution.getScaledWidth();
                        int j = scaledresolution.getScaledHeight();

                        guiScreenIn.setWorldAndResolution(Minecraft.getMinecraft(), i, j);
                        this.skipRenderWorld = false;
                        break;
                    }
                }
            } else {
                this.soundHandler.resumeSounds();
                Minecraft.getMinecraft().setIngameFocus();
            }

            info.cancel();
        }
    }

    @Redirect(
        method = { "run"},
        at =             @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/Minecraft;displayCrashReport(Lnet/minecraft/crash/CrashReport;)V"
            )
    )
    public void displayCrashReport(Minecraft minecraft, CrashReport crashReport) {
        this.save();
    }

    @Inject(
        method = { "shutdown"},
        at = {             @At("HEAD")}
    )
    public void shutdown(CallbackInfo info) {
        this.save();
    }

    private void save() {
        System.out.println("Shutting down: saving KAMI configuration");
        KamiMod.saveConfiguration();
        System.out.println("Configuration saved.");
    }
}
