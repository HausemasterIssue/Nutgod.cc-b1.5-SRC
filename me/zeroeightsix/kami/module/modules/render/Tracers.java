package me.zeroeightsix.kami.module.modules.render;

import java.util.function.Consumer;
import java.util.function.Predicate;
import me.zeroeightsix.kami.event.events.RenderEvent;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import me.zeroeightsix.kami.setting.builder.SettingBuilder;
import me.zeroeightsix.kami.util.ColourUtils;
import me.zeroeightsix.kami.util.EntityUtil;
import me.zeroeightsix.kami.util.Friends;
import me.zeroeightsix.kami.util.HueCycler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

@Module.Info(
    name = "Tracers",
    description = "Draws lines to other living entities",
    category = Module.Category.RENDER
)
public class Tracers extends Module {

    private Setting players = this.register(Settings.b("Players", true));
    private Setting friends = this.register(Settings.b("Friends", true));
    private Setting animals = this.register(Settings.b("Animals", false));
    private Setting mobs = this.register(Settings.b("Mobs", false));
    private Setting range = this.register(Settings.d("Range", 200.0D));
    private Setting opacity = this.register((SettingBuilder) Settings.floatBuilder("Opacity").withRange(Float.valueOf(0.0F), Float.valueOf(1.0F)).withValue((Number) Float.valueOf(1.0F)));
    HueCycler cycler = new HueCycler(3600);

    public void onWorldRender(RenderEvent event) {
        GlStateManager.pushMatrix();
        Minecraft.getMinecraft().world.loadedEntityList.stream().filter(EntityUtil::isLiving).filter((entity) -> {
            return !EntityUtil.isFakeLocalPlayer(entity);
        }).filter((entity) -> {
            return entity instanceof EntityPlayer ? ((Boolean) this.players.getValue()).booleanValue() && Tracers.mc.player != entity : (EntityUtil.isPassive(entity) ? ((Boolean) this.animals.getValue()).booleanValue() : ((Boolean) this.mobs.getValue()).booleanValue());
        }).filter((entity) -> {
            return (double) Tracers.mc.player.getDistance(entity) < ((Double) this.range.getValue()).doubleValue();
        }).forEach((entity) -> {
            int colour = this.getColour(entity);

            if (colour == Integer.MIN_VALUE) {
                if (!((Boolean) this.friends.getValue()).booleanValue()) {
                    return;
                }

                colour = this.cycler.current();
            }

            float r = 1.0F;
            float g = 0.0F;
            float b = 1.0F;

            drawLineToEntity(entity, 1.0F, 0.0F, 1.0F, ((Float) this.opacity.getValue()).floatValue());
        });
        GlStateManager.popMatrix();
    }

    public void onUpdate() {
        this.cycler.next();
    }

    private void drawRainbowToEntity(Entity entity, float opacity) {
        Vec3d eyes = (new Vec3d(0.0D, 0.0D, 1.0D)).rotatePitch(-((float) Math.toRadians((double) Minecraft.getMinecraft().player.rotationPitch))).rotateYaw(-((float) Math.toRadians((double) Minecraft.getMinecraft().player.rotationYaw)));
        double[] xyz = interpolate(entity);
        double posx = xyz[0];
        double posy = xyz[1];
        double posz = xyz[2];
        double posx2 = eyes.x;
        double posy2 = eyes.y + (double) Tracers.mc.player.getEyeHeight();
        double posz2 = eyes.z;

        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(1.5F);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        this.cycler.reset();
        this.cycler.setNext(opacity);
        GlStateManager.disableLighting();
        GL11.glLoadIdentity();
        Tracers.mc.entityRenderer.orientCamera(Tracers.mc.getRenderPartialTicks());
        GL11.glBegin(1);
        GL11.glVertex3d(posx, posy, posz);
        GL11.glVertex3d(posx2, posy2, posz2);
        this.cycler.setNext(opacity);
        GL11.glVertex3d(posx2, posy2, posz2);
        GL11.glVertex3d(posx2, posy2, posz2);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glColor3d(1.0D, 0.0D, 1.0D);
        GlStateManager.enableLighting();
    }

    private int getColour(Entity entity) {
        return entity instanceof EntityPlayer ? (Friends.isFriend(entity.getName()) ? Integer.MIN_VALUE : ColourUtils.Colors.WHITE) : (EntityUtil.isPassive(entity) ? ColourUtils.Colors.GREEN : ColourUtils.Colors.RED);
    }

    public static double interpolate(double now, double then) {
        return then + (now - then) * (double) Tracers.mc.getRenderPartialTicks();
    }

    public static double[] interpolate(Entity entity) {
        double posX = interpolate(entity.posX, entity.lastTickPosX) - Tracers.mc.getRenderManager().renderPosX;
        double posY = interpolate(entity.posY, entity.lastTickPosY) - Tracers.mc.getRenderManager().renderPosY;
        double posZ = interpolate(entity.posZ, entity.lastTickPosZ) - Tracers.mc.getRenderManager().renderPosZ;

        return new double[] { posX, posY, posZ};
    }

    public static void drawLineToEntity(Entity e, float red, float green, float blue, float opacity) {
        double[] xyz = interpolate(e);

        drawLine(xyz[0], xyz[1], xyz[2], (double) e.height, red, green, blue, opacity);
    }

    public static void drawLine(double posx, double posy, double posz, double up, float red, float green, float blue, float opacity) {
        Vec3d eyes = (new Vec3d(0.0D, 0.0D, 1.0D)).rotatePitch(-((float) Math.toRadians((double) Minecraft.getMinecraft().player.rotationPitch))).rotateYaw(-((float) Math.toRadians((double) Minecraft.getMinecraft().player.rotationYaw)));

        drawLineFromPosToPos(eyes.x, eyes.y + (double) Tracers.mc.player.getEyeHeight(), eyes.z, posx, posy, posz, up, red, green, blue, opacity);
    }

    public static void drawLineFromPosToPos(double posx, double posy, double posz, double posx2, double posy2, double posz2, double up, float red, float green, float blue, float opacity) {
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(1.0F);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(red, green, blue, opacity);
        GlStateManager.disableLighting();
        GL11.glLoadIdentity();
        Tracers.mc.entityRenderer.orientCamera(Tracers.mc.getRenderPartialTicks());
        GL11.glBegin(1);
        GL11.glVertex3d(posx, posy, posz);
        GL11.glVertex3d(posx2, posy2, posz2);
        GL11.glVertex3d(posx2, posy2, posz2);
        GL11.glVertex3d(posx2, posy2 + up, posz2);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glColor3d(1.0D, 0.0D, 1.0D);
        GlStateManager.enableLighting();
    }
}
