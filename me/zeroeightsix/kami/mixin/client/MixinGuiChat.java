package me.zeroeightsix.kami.mixin.client;

import me.zeroeightsix.kami.command.Command;
import me.zeroeightsix.kami.gui.mc.KamiGuiChat;
import me.zeroeightsix.kami.util.Wrapper;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiTextField;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ GuiChat.class})
public abstract class MixinGuiChat {

    @Shadow
    protected GuiTextField inputField;
    @Shadow
    public String historyBuffer;
    @Shadow
    public int sentHistoryCursor;

    @Shadow
    public abstract void initGui();

    @Inject(
        method = { "Lnet/minecraft/client/gui/GuiChat;keyTyped(CI)V"},
        at = {             @At("RETURN")}
    )
    public void returnKeyTyped(char typedChar, int keyCode, CallbackInfo info) {
        if (Wrapper.getMinecraft().currentScreen instanceof GuiChat && !(Wrapper.getMinecraft().currentScreen instanceof KamiGuiChat)) {
            if (this.inputField.getText().startsWith(Command.getCommandPrefix())) {
                Wrapper.getMinecraft().displayGuiScreen(new KamiGuiChat(this.inputField.getText(), this.historyBuffer, this.sentHistoryCursor));
            }

        }
    }
}
