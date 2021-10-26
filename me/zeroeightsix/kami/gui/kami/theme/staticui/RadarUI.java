package me.zeroeightsix.kami.gui.kami.theme.staticui;

import java.util.Iterator;
import me.zeroeightsix.kami.gui.kami.RenderHelper;
import me.zeroeightsix.kami.gui.kami.component.Radar;
import me.zeroeightsix.kami.gui.rgui.render.AbstractComponentUI;
import me.zeroeightsix.kami.gui.rgui.render.font.FontRenderer;
import me.zeroeightsix.kami.util.EntityUtil;
import me.zeroeightsix.kami.util.Wrapper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import org.lwjgl.opengl.GL11;

public class RadarUI extends AbstractComponentUI {

    float scale = 2.0F;
    public static final int radius = 45;

    public void handleSizeComponent(Radar component) {
        component.setWidth(90);
        component.setHeight(90);
    }

    public void renderComponent(Radar component, FontRenderer fontRenderer) {
        this.scale = 2.0F;
        GL11.glTranslated((double) (component.getWidth() / 2), (double) (component.getHeight() / 2), 0.0D);
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.disableCull();
        GlStateManager.pushMatrix();
        GL11.glColor4f(0.11F, 0.11F, 0.11F, 0.6F);
        RenderHelper.drawCircle(0.0F, 0.0F, 45.0F);
        GL11.glRotatef(Wrapper.getPlayer().rotationYaw + 180.0F, 0.0F, 0.0F, -1.0F);
        Iterator iterator = Wrapper.getWorld().loadedEntityList.iterator();

        while (iterator.hasNext()) {
            Entity e = (Entity) iterator.next();

            if (e instanceof EntityLiving) {
                float red = 1.0F;
                float green = 1.0F;

                if (EntityUtil.isPassive(e)) {
                    red = 0.0F;
                } else {
                    green = 0.0F;
                }

                double dX = e.posX - Wrapper.getPlayer().posX;
                double dZ = e.posZ - Wrapper.getPlayer().posZ;
                double distance = Math.sqrt(Math.pow(dX, 2.0D) + Math.pow(dZ, 2.0D));

                if (distance <= (double) (45.0F * this.scale) && Math.abs(Wrapper.getPlayer().posY - e.posY) <= 30.0D) {
                    GL11.glColor4f(0.65F, 0.0F, 0.6F, 0.5F);
                    RenderHelper.drawCircle((float) ((int) dX) / this.scale, (float) ((int) dZ) / this.scale, 2.5F / this.scale);
                }
            }
        }

        GL11.glColor3f(255.0F, 255.0F, 255.0F);
        RenderHelper.drawCircle(0.0F, 0.0F, 3.0F / this.scale);
        GL11.glLineWidth(1.8F);
        GL11.glColor3f(0.6F, 0.09F, 0.6F);
        GL11.glEnable(2848);
        RenderHelper.drawCircleOutline(0.0F, 0.0F, 45.0F);
        GL11.glDisable(2848);
        component.getTheme().getFontRenderer().drawString(-component.getTheme().getFontRenderer().getStringWidth("+z") / 2, 45 - component.getTheme().getFontRenderer().getFontHeight(), "§7z+");
        GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
        component.getTheme().getFontRenderer().drawString(-component.getTheme().getFontRenderer().getStringWidth("+x") / 2, 45 - component.getTheme().getFontRenderer().getFontHeight(), "§7x-");
        GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
        component.getTheme().getFontRenderer().drawString(-component.getTheme().getFontRenderer().getStringWidth("-z") / 2, 45 - component.getTheme().getFontRenderer().getFontHeight(), "§7z-");
        GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
        component.getTheme().getFontRenderer().drawString(-component.getTheme().getFontRenderer().getStringWidth("+x") / 2, 45 - component.getTheme().getFontRenderer().getFontHeight(), "§7x+");
        GlStateManager.popMatrix();
        GlStateManager.enableTexture2D();
        GL11.glTranslated((double) (-component.getWidth() / 2), (double) (-component.getHeight() / 2), 0.0D);
    }
}
