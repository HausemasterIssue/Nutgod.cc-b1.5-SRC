package me.zeroeightsix.kami.gui.kami;

import java.awt.Color;
import me.zeroeightsix.kami.gui.rgui.render.font.FontRenderer;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class RootFontRenderer implements FontRenderer {

    private final float fontsize;
    private final net.minecraft.client.gui.FontRenderer fontRenderer;

    public RootFontRenderer(float fontsize) {
        this.fontRenderer = Minecraft.getMinecraft().fontRenderer;
        this.fontsize = fontsize;
    }

    public int getFontHeight() {
        return (int) ((float) Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT * this.fontsize);
    }

    public int getStringHeight(String text) {
        return this.getFontHeight();
    }

    public int getStringWidth(String text) {
        return (int) ((float) this.fontRenderer.getStringWidth(text) * this.fontsize);
    }

    public void drawString(int x, int y, String text) {
        this.drawString(x, y, 255, 255, 255, text);
    }

    public void drawString(int x, int y, int r, int g, int b, String text) {
        this.drawString(x, y, -16777216 | (r & 255) << 16 | (g & 255) << 8 | b & 255, text);
    }

    public void drawString(int x, int y, Color color, String text) {
        this.drawString(x, y, color.getRGB(), text);
    }

    public void drawString(int x, int y, int colour, String text) {
        this.drawString(x, y, colour, text, true);
    }

    public void drawString(int x, int y, int colour, String text, boolean shadow) {
        this.prepare(x, y);
        Minecraft.getMinecraft().fontRenderer.drawString(text, 0.0F, 0.0F, colour, shadow);
        this.pop(x, y);
    }

    public void drawStringWithShadow(int x, int y, int r, int g, int b, String text) {
        this.drawString(x, y, -16777216 | (r & 255) << 16 | (g & 255) << 8 | b & 255, text, true);
    }

    private void prepare(int x, int y) {
        GL11.glEnable(3553);
        GL11.glEnable(3042);
        GL11.glTranslatef((float) x, (float) y, 0.0F);
        GL11.glScalef(this.fontsize, this.fontsize, 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void pop(int x, int y) {
        GL11.glScalef(1.0F / this.fontsize, 1.0F / this.fontsize, 1.0F);
        GL11.glTranslatef((float) (-x), (float) (-y), 0.0F);
        GL11.glDisable(3553);
    }
}
