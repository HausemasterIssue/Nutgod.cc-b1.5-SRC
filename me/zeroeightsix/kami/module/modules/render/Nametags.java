package me.zeroeightsix.kami.module.modules.render;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import me.zeroeightsix.kami.command.Command;
import me.zeroeightsix.kami.event.events.RenderEvent;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import me.zeroeightsix.kami.util.EntityUtil;
import me.zeroeightsix.kami.util.Friends;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

@Module.Info(
    name = "Nametags",
    description = "Draws descriptive nametags above entities",
    category = Module.Category.RENDER
)
public class Nametags extends Module {

    private Setting players = this.register(Settings.b("Players", true));
    private Setting animals = this.register(Settings.b("Animals", false));
    private Setting mobs = this.register(Settings.b("Mobs", false));
    private Setting range = this.register(Settings.d("Range", 200.0D));
    private Setting scale = this.register((Setting) Settings.floatBuilder("Scale").withMinimum(Float.valueOf(0.5F)).withMaximum(Float.valueOf(10.0F)).withValue((Number) Float.valueOf(1.0F)).build());
    private Setting health = this.register(Settings.b("Health", true));
    RenderItem itemRenderer;

    public Nametags() {
        this.itemRenderer = Nametags.mc.getRenderItem();
    }

    public void onWorldRender(RenderEvent event) {
        if (Nametags.mc.getRenderManager().options != null) {
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            Minecraft.getMinecraft().world.loadedEntityList.stream().filter(EntityUtil::isLiving).filter((entity) -> {
                return !EntityUtil.isFakeLocalPlayer(entity);
            }).filter((entity) -> {
                return entity instanceof EntityPlayer ? ((Boolean) this.players.getValue()).booleanValue() && Nametags.mc.player != entity : (EntityUtil.isPassive(entity) ? ((Boolean) this.animals.getValue()).booleanValue() : ((Boolean) this.mobs.getValue()).booleanValue());
            }).filter((entity) -> {
                return (double) Nametags.mc.player.getDistance(entity) < ((Double) this.range.getValue()).doubleValue();
            }).sorted(Comparator.comparing((entity) -> {
                return Float.valueOf(-Nametags.mc.player.getDistance(entity));
            })).forEach(this::drawNametag);
            GlStateManager.disableTexture2D();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
        }
    }

    private void drawNametag(Entity entityIn) {
        GlStateManager.pushMatrix();
        Vec3d interp = EntityUtil.getInterpolatedRenderPos(entityIn, Nametags.mc.getRenderPartialTicks());
        float yAdd = entityIn.height + 0.5F - (entityIn.isSneaking() ? 0.25F : 0.0F);
        double x = interp.x;
        double y = interp.y + (double) yAdd;
        double z = interp.z;
        float viewerYaw = Nametags.mc.getRenderManager().playerViewY;
        float viewerPitch = Nametags.mc.getRenderManager().playerViewX;
        boolean isThirdPersonFrontal = Nametags.mc.getRenderManager().options.thirdPersonView == 2;

        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(-viewerYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((float) (isThirdPersonFrontal ? -1 : 1) * viewerPitch, 1.0F, 0.0F, 0.0F);
        float f = Nametags.mc.player.getDistance(entityIn);
        float m = f / 8.0F * (float) Math.pow(1.258925437927246D, (double) ((Float) this.scale.getValue()).floatValue());

        GlStateManager.scale(m, m, m);
        FontRenderer fontRendererIn = Nametags.mc.fontRenderer;

        GlStateManager.scale(-0.025F, -0.025F, 0.025F);
        String str = entityIn.getName() + (((Boolean) this.health.getValue()).booleanValue() ? " " + Command.SECTIONSIGN() + "c" + Math.round(((EntityLivingBase) entityIn).getHealth() + (entityIn instanceof EntityPlayer ? ((EntityPlayer) entityIn).getAbsorptionAmount() : 0.0F)) : "");
        int i = fontRendererIn.getStringWidth(str) / 2;

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
        GlStateManager.disableTexture2D();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        GL11.glTranslatef(0.0F, -20.0F, 0.0F);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double) (-i - 1), 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.5F).endVertex();
        bufferbuilder.pos((double) (-i - 1), 19.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.5F).endVertex();
        bufferbuilder.pos((double) (i + 1), 19.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.5F).endVertex();
        bufferbuilder.pos((double) (i + 1), 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.5F).endVertex();
        tessellator.draw();
        bufferbuilder.begin(2, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double) (-i - 1), 8.0D, 0.0D).color(0.1F, 0.1F, 0.1F, 0.1F).endVertex();
        bufferbuilder.pos((double) (-i - 1), 19.0D, 0.0D).color(0.1F, 0.1F, 0.1F, 0.1F).endVertex();
        bufferbuilder.pos((double) (i + 1), 19.0D, 0.0D).color(0.1F, 0.1F, 0.1F, 0.1F).endVertex();
        bufferbuilder.pos((double) (i + 1), 8.0D, 0.0D).color(0.1F, 0.1F, 0.1F, 0.1F).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
        fontRendererIn.drawString(str, -i, 10, entityIn instanceof EntityPlayer ? (Friends.isFriend(entityIn.getName()) ? 1175057 : 16777215) : 16777215);
        GlStateManager.glNormal3f(0.0F, 0.0F, 0.0F);
        GL11.glTranslatef(0.0F, 20.0F, 0.0F);
        GlStateManager.scale(-40.0F, -40.0F, 40.0F);
        ArrayList equipment = new ArrayList();

        entityIn.getHeldEquipment().forEach((itemStack) -> {
            if (itemStack != null) {
                equipment.add(itemStack);
            }

        });
        ArrayList armour = new ArrayList();

        entityIn.getArmorInventoryList().forEach((itemStack) -> {
            if (itemStack != null) {
                armour.add(itemStack);
            }

        });
        Collections.reverse(armour);
        equipment.addAll(armour);
        if (equipment.size() == 0) {
            GlStateManager.popMatrix();
        } else {
            Collection a = (Collection) equipment.stream().filter((itemStack) -> {
                return !itemStack.isEmpty();
            }).collect(Collectors.toList());

            GlStateManager.translate((double) ((float) (a.size() - 1) / 2.0F * 0.5F), 0.6D, 0.0D);
            a.forEach((itemStack) -> {
                GlStateManager.pushAttrib();
                RenderHelper.enableStandardItemLighting();
                GlStateManager.scale(0.5D, 0.5D, 0.0D);
                GlStateManager.disableLighting();
                this.itemRenderer.zLevel = -5.0F;
                this.itemRenderer.renderItem(itemStack, itemStack.getItem() == Items.SHIELD ? TransformType.FIXED : TransformType.NONE);
                this.itemRenderer.zLevel = 0.0F;
                GlStateManager.scale(2.0F, 2.0F, 0.0F);
                GlStateManager.popAttrib();
                GlStateManager.translate(-0.5F, 0.0F, 0.0F);
            });
            GlStateManager.popMatrix();
        }
    }
}
