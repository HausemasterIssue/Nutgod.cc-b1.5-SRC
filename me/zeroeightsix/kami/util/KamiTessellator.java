package me.zeroeightsix.kami.util;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

public class KamiTessellator extends Tessellator {

    public static KamiTessellator INSTANCE = new KamiTessellator();

    public KamiTessellator() {
        super(2097152);
    }

    public static void prepare(int mode) {
        prepareGL();
        begin(mode);
    }

    public static void prepareGL() {
        GL11.glBlendFunc(770, 771);
        GlStateManager.tryBlendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
        GlStateManager.glLineWidth(1.5F);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0F, 1.0F, 1.0F);
    }

    public static void begin(int mode) {
        KamiTessellator.INSTANCE.getBuffer().begin(mode, DefaultVertexFormats.POSITION_COLOR);
    }

    public static void release() {
        render();
        releaseGL();
    }

    public static void render() {
        KamiTessellator.INSTANCE.draw();
    }

    public static void releaseGL() {
        GlStateManager.enableCull();
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.enableDepth();
    }

    public static void drawBox(BlockPos blockPos, int argb, int sides) {
        int a = argb >>> 24 & 255;
        int r = argb >>> 16 & 255;
        int g = argb >>> 8 & 255;
        int b = argb & 255;

        drawBox(blockPos, r, g, b, a, sides);
    }

    public static void drawBox(float x, float y, float z, int argb, int sides) {
        int a = argb >>> 24 & 255;
        int r = argb >>> 16 & 255;
        int g = argb >>> 8 & 255;
        int b = argb & 255;

        drawBox(KamiTessellator.INSTANCE.getBuffer(), x, y, z, 1.0F, 1.0F, 1.0F, r, g, b, a, sides);
    }

    public static void drawBox(BlockPos blockPos, int r, int g, int b, int a, int sides) {
        drawBox(KamiTessellator.INSTANCE.getBuffer(), (float) blockPos.x, (float) blockPos.y, (float) blockPos.z, 1.0F, 1.0F, 1.0F, r, g, b, a, sides);
    }

    public static BufferBuilder getBufferBuilder() {
        return KamiTessellator.INSTANCE.getBuffer();
    }

    public static void drawBox(BufferBuilder buffer, float x, float y, float z, float w, float h, float d, int r, int g, int b, int a, int sides) {
        if ((sides & 1) != 0) {
            buffer.pos((double) (x + w), (double) y, (double) z).color(r, g, b, a).endVertex();
            buffer.pos((double) (x + w), (double) y, (double) (z + d)).color(r, g, b, a).endVertex();
            buffer.pos((double) x, (double) y, (double) (z + d)).color(r, g, b, a).endVertex();
            buffer.pos((double) x, (double) y, (double) z).color(r, g, b, a).endVertex();
        }

        if ((sides & 2) != 0) {
            buffer.pos((double) (x + w), (double) (y + h), (double) z).color(r, g, b, a).endVertex();
            buffer.pos((double) x, (double) (y + h), (double) z).color(r, g, b, a).endVertex();
            buffer.pos((double) x, (double) (y + h), (double) (z + d)).color(r, g, b, a).endVertex();
            buffer.pos((double) (x + w), (double) (y + h), (double) (z + d)).color(r, g, b, a).endVertex();
        }

        if ((sides & 4) != 0) {
            buffer.pos((double) (x + w), (double) y, (double) z).color(r, g, b, a).endVertex();
            buffer.pos((double) x, (double) y, (double) z).color(r, g, b, a).endVertex();
            buffer.pos((double) x, (double) (y + h), (double) z).color(r, g, b, a).endVertex();
            buffer.pos((double) (x + w), (double) (y + h), (double) z).color(r, g, b, a).endVertex();
        }

        if ((sides & 8) != 0) {
            buffer.pos((double) x, (double) y, (double) (z + d)).color(r, g, b, a).endVertex();
            buffer.pos((double) (x + w), (double) y, (double) (z + d)).color(r, g, b, a).endVertex();
            buffer.pos((double) (x + w), (double) (y + h), (double) (z + d)).color(r, g, b, a).endVertex();
            buffer.pos((double) x, (double) (y + h), (double) (z + d)).color(r, g, b, a).endVertex();
        }

        if ((sides & 16) != 0) {
            buffer.pos((double) x, (double) y, (double) z).color(r, g, b, a).endVertex();
            buffer.pos((double) x, (double) y, (double) (z + d)).color(r, g, b, a).endVertex();
            buffer.pos((double) x, (double) (y + h), (double) (z + d)).color(r, g, b, a).endVertex();
            buffer.pos((double) x, (double) (y + h), (double) z).color(r, g, b, a).endVertex();
        }

        if ((sides & 32) != 0) {
            buffer.pos((double) (x + w), (double) y, (double) (z + d)).color(r, g, b, a).endVertex();
            buffer.pos((double) (x + w), (double) y, (double) z).color(r, g, b, a).endVertex();
            buffer.pos((double) (x + w), (double) (y + h), (double) z).color(r, g, b, a).endVertex();
            buffer.pos((double) (x + w), (double) (y + h), (double) (z + d)).color(r, g, b, a).endVertex();
        }

    }

    public static void drawLines(BufferBuilder buffer, float x, float y, float z, float w, float h, float d, int r, int g, int b, int a, int sides) {
        if ((sides & 17) != 0) {
            buffer.pos((double) x, (double) y, (double) z).color(r, g, b, a).endVertex();
            buffer.pos((double) x, (double) y, (double) (z + d)).color(r, g, b, a).endVertex();
        }

        if ((sides & 18) != 0) {
            buffer.pos((double) x, (double) (y + h), (double) z).color(r, g, b, a).endVertex();
            buffer.pos((double) x, (double) (y + h), (double) (z + d)).color(r, g, b, a).endVertex();
        }

        if ((sides & 33) != 0) {
            buffer.pos((double) (x + w), (double) y, (double) z).color(r, g, b, a).endVertex();
            buffer.pos((double) (x + w), (double) y, (double) (z + d)).color(r, g, b, a).endVertex();
        }

        if ((sides & 34) != 0) {
            buffer.pos((double) (x + w), (double) (y + h), (double) z).color(r, g, b, a).endVertex();
            buffer.pos((double) (x + w), (double) (y + h), (double) (z + d)).color(r, g, b, a).endVertex();
        }

        if ((sides & 5) != 0) {
            buffer.pos((double) x, (double) y, (double) z).color(r, g, b, a).endVertex();
            buffer.pos((double) (x + w), (double) y, (double) z).color(r, g, b, a).endVertex();
        }

        if ((sides & 6) != 0) {
            buffer.pos((double) x, (double) (y + h), (double) z).color(r, g, b, a).endVertex();
            buffer.pos((double) (x + w), (double) (y + h), (double) z).color(r, g, b, a).endVertex();
        }

        if ((sides & 9) != 0) {
            buffer.pos((double) x, (double) y, (double) (z + d)).color(r, g, b, a).endVertex();
            buffer.pos((double) (x + w), (double) y, (double) (z + d)).color(r, g, b, a).endVertex();
        }

        if ((sides & 10) != 0) {
            buffer.pos((double) x, (double) (y + h), (double) (z + d)).color(r, g, b, a).endVertex();
            buffer.pos((double) (x + w), (double) (y + h), (double) (z + d)).color(r, g, b, a).endVertex();
        }

        if ((sides & 20) != 0) {
            buffer.pos((double) x, (double) y, (double) z).color(r, g, b, a).endVertex();
            buffer.pos((double) x, (double) (y + h), (double) z).color(r, g, b, a).endVertex();
        }

        if ((sides & 36) != 0) {
            buffer.pos((double) (x + w), (double) y, (double) z).color(r, g, b, a).endVertex();
            buffer.pos((double) (x + w), (double) (y + h), (double) z).color(r, g, b, a).endVertex();
        }

        if ((sides & 24) != 0) {
            buffer.pos((double) x, (double) y, (double) (z + d)).color(r, g, b, a).endVertex();
            buffer.pos((double) x, (double) (y + h), (double) (z + d)).color(r, g, b, a).endVertex();
        }

        if ((sides & 40) != 0) {
            buffer.pos((double) (x + w), (double) y, (double) (z + d)).color(r, g, b, a).endVertex();
            buffer.pos((double) (x + w), (double) (y + h), (double) (z + d)).color(r, g, b, a).endVertex();
        }

    }
}
