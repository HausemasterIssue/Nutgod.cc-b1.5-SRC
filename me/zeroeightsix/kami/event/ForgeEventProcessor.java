package me.zeroeightsix.kami.event;

import java.util.function.Consumer;
import java.util.function.Predicate;
import me.zeroeightsix.kami.KamiMod;
import me.zeroeightsix.kami.command.Command;
import me.zeroeightsix.kami.command.commands.PeekCommand;
import me.zeroeightsix.kami.event.events.DisplaySizeChangedEvent;
import me.zeroeightsix.kami.gui.UIRenderer;
import me.zeroeightsix.kami.gui.kami.KamiGUI;
import me.zeroeightsix.kami.gui.rgui.component.Component;
import me.zeroeightsix.kami.gui.rgui.component.container.use.Frame;
import me.zeroeightsix.kami.module.ModuleManager;
import me.zeroeightsix.kami.module.modules.render.BossStack;
import me.zeroeightsix.kami.util.KamiTessellator;
import me.zeroeightsix.kami.util.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiShulkerBox;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Post;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Pre;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent.Start;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickBlock;
import net.minecraftforge.event.world.ChunkEvent.Load;
import net.minecraftforge.event.world.ChunkEvent.Unload;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class ForgeEventProcessor {

    private int displayWidth;
    private int displayHeight;

    @SubscribeEvent
    public void onUpdate(LivingUpdateEvent event) {
        if (!event.isCanceled()) {
            if (Minecraft.getMinecraft().displayWidth != this.displayWidth || Minecraft.getMinecraft().displayHeight != this.displayHeight) {
                KamiMod.EVENT_BUS.post(new DisplaySizeChangedEvent());
                this.displayWidth = Minecraft.getMinecraft().displayWidth;
                this.displayHeight = Minecraft.getMinecraft().displayHeight;
                KamiMod.getInstance().getGuiManager().getChildren().stream().filter((component) -> {
                    return component instanceof Frame;
                }).forEach((component) -> {
                    KamiGUI.dock((Frame) component);
                });
            }

            if (PeekCommand.sb != null) {
                ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());
                int i = scaledresolution.getScaledWidth();
                int j = scaledresolution.getScaledHeight();
                GuiShulkerBox gui = new GuiShulkerBox(Wrapper.getPlayer().inventory, PeekCommand.sb);

                gui.setWorldAndResolution(Wrapper.getMinecraft(), i, j);
                Minecraft.getMinecraft().displayGuiScreen(gui);
                PeekCommand.sb = null;
            }

        }
    }

    @SubscribeEvent
    public void onTick(ClientTickEvent event) {
        if (Wrapper.getPlayer() != null) {
            ModuleManager.onUpdate();
            KamiMod.getInstance().getGuiManager().callTick(KamiMod.getInstance().getGuiManager());
        }
    }

    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent event) {
        if (!event.isCanceled()) {
            ModuleManager.onWorldRender(event);
        }
    }

    @SubscribeEvent
    public void onRenderPre(Pre event) {
        if (event.getType() == ElementType.BOSSINFO && ModuleManager.isModuleEnabled("BossStack")) {
            event.setCanceled(true);
        }

    }

    @SubscribeEvent
    public void onRender(Post event) {
        if (!event.isCanceled()) {
            ElementType target = ElementType.EXPERIENCE;

            if (!Wrapper.getPlayer().isCreative() && Wrapper.getPlayer().getRidingEntity() instanceof AbstractHorse) {
                target = ElementType.HEALTHMOUNT;
            }

            if (event.getType() == target) {
                ModuleManager.onRender();
                GL11.glPushMatrix();
                UIRenderer.renderAndUpdateFrames();
                GL11.glPopMatrix();
                KamiTessellator.releaseGL();
            } else if (event.getType() == ElementType.BOSSINFO && ModuleManager.isModuleEnabled("BossStack")) {
                BossStack.render(event);
            }

        }
    }

    @SubscribeEvent(
        priority = EventPriority.NORMAL,
        receiveCanceled = true
    )
    public void onKeyInput(KeyInputEvent event) {
        if (Keyboard.getEventKeyState()) {
            ModuleManager.onBind(Keyboard.getEventKey());
        }

    }

    @SubscribeEvent(
        priority = EventPriority.HIGHEST
    )
    public void onChatSent(ClientChatEvent event) {
        if (event.getMessage().startsWith(Command.getCommandPrefix())) {
            event.setCanceled(true);

            try {
                Wrapper.getMinecraft().ingameGUI.getChatGUI().addToSentMessages(event.getMessage());
                if (event.getMessage().length() > 1) {
                    KamiMod.getInstance().commandManager.callCommand(event.getMessage().substring(Command.getCommandPrefix().length() - 1));
                } else {
                    Command.sendChatMessage("Please enter a command.");
                }
            } catch (Exception exception) {
                exception.printStackTrace();
                Command.sendChatMessage("Error occured while running command! (" + exception.getMessage() + ")");
            }

            event.setMessage("");
        }

    }

    @SubscribeEvent(
        priority = EventPriority.HIGHEST
    )
    public void onPlayerDrawn(net.minecraftforge.client.event.RenderPlayerEvent.Pre event) {
        KamiMod.EVENT_BUS.post(event);
    }

    @SubscribeEvent(
        priority = EventPriority.HIGHEST
    )
    public void onPlayerDrawn(net.minecraftforge.client.event.RenderPlayerEvent.Post event) {
        KamiMod.EVENT_BUS.post(event);
    }

    @SubscribeEvent
    public void onChunkLoaded(Load event) {
        KamiMod.EVENT_BUS.post(event);
    }

    @SubscribeEvent
    public void onChunkLoaded(Unload event) {
        KamiMod.EVENT_BUS.post(event);
    }

    @SubscribeEvent
    public void onInputUpdate(InputUpdateEvent event) {
        KamiMod.EVENT_BUS.post(event);
    }

    @SubscribeEvent
    public void onLivingEntityUseItemEventTick(Start entityUseItemEvent) {
        KamiMod.EVENT_BUS.post(entityUseItemEvent);
    }

    @SubscribeEvent
    public void onLivingDamageEvent(LivingDamageEvent event) {
        KamiMod.EVENT_BUS.post(event);
    }

    @SubscribeEvent
    public void onEntityJoinWorldEvent(EntityJoinWorldEvent entityJoinWorldEvent) {
        KamiMod.EVENT_BUS.post(entityJoinWorldEvent);
    }

    @SubscribeEvent
    public void onPlayerPush(PlayerSPPushOutOfBlocksEvent event) {
        KamiMod.EVENT_BUS.post(event);
    }

    @SubscribeEvent
    public void onLeftClickBlock(LeftClickBlock event) {
        KamiMod.EVENT_BUS.post(event);
    }

    @SubscribeEvent
    public void onAttackEntity(AttackEntityEvent entityEvent) {
        KamiMod.EVENT_BUS.post(entityEvent);
    }

    @SubscribeEvent
    public void onRenderBlockOverlay(RenderBlockOverlayEvent event) {
        KamiMod.EVENT_BUS.post(event);
    }
}
