package me.zeroeightsix.kami.gui.kami.theme.staticui;

import me.zeroeightsix.kami.gui.kami.KamiGUI;
import me.zeroeightsix.kami.gui.kami.component.TabGUI;
import me.zeroeightsix.kami.gui.rgui.render.AbstractComponentUI;
import me.zeroeightsix.kami.gui.rgui.render.font.FontRenderer;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.util.Wrapper;
import org.lwjgl.opengl.GL11;

public class TabGuiUI extends AbstractComponentUI {

    long lastms = System.currentTimeMillis();

    public void renderComponent(TabGUI component, FontRenderer fontRenderer) {
        boolean updatelerp = false;
        float difference = (float) (System.currentTimeMillis() - this.lastms);

        if (difference > 2.0F) {
            component.selectedLerpY += ((float) (component.selected * 10) - component.selectedLerpY) * difference * 0.02F;
            updatelerp = true;
            this.lastms = System.currentTimeMillis();
        }

        GL11.glDisable(2884);
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        byte x = 2;
        byte y = 2;

        GL11.glTranslatef((float) x, (float) y, 0.0F);
        this.drawBox(0, 0, component.width, component.height);
        KamiGUI.primaryColour.setGLColour();
        GL11.glColor3f(0.59F, 0.05F, 0.11F);
        GL11.glBegin(7);
        GL11.glVertex2d(0.0D, (double) component.selectedLerpY);
        GL11.glVertex2d(0.0D, (double) (component.selectedLerpY + 10.0F));
        GL11.glVertex2d((double) component.width, (double) (component.selectedLerpY + 10.0F));
        GL11.glVertex2d((double) component.width, (double) component.selectedLerpY);
        GL11.glEnd();
        int textY = 1;

        for (int tab = 0; tab < component.tabs.size(); ++tab) {
            String tabTextY = ((TabGUI.Tab) component.tabs.get(tab)).name;

            GL11.glEnable(3553);
            GL11.glColor3f(1.0F, 1.0F, 1.0F);
            Wrapper.getFontRenderer().drawStringWithShadow(2, textY, 255, 255, 255, "§7" + tabTextY);
            textY += 10;
        }

        if (component.tabOpened) {
            GL11.glPushMatrix();
            GL11.glDisable(3553);
            TabGUI.Tab tabgui_tab = (TabGUI.Tab) component.tabs.get(component.selected);

            GL11.glTranslatef((float) (component.width + 2), 0.0F, 0.0F);
            this.drawBox(0, 0, tabgui_tab.width, tabgui_tab.height);
            if (updatelerp) {
                tabgui_tab.lerpSelectY += ((float) (tabgui_tab.selected * 10) - tabgui_tab.lerpSelectY) * difference * 0.02F;
            }

            GL11.glColor3f(0.59F, 0.05F, 0.11F);
            GL11.glBegin(7);
            GL11.glVertex2d(0.0D, (double) tabgui_tab.lerpSelectY);
            GL11.glVertex2d(0.0D, (double) (tabgui_tab.lerpSelectY + 10.0F));
            GL11.glVertex2d((double) tabgui_tab.width, (double) (tabgui_tab.lerpSelectY + 10.0F));
            GL11.glVertex2d((double) tabgui_tab.width, (double) tabgui_tab.lerpSelectY);
            GL11.glEnd();
            int i = 1;

            for (int i = 0; i < tabgui_tab.features.size(); ++i) {
                Module feature = (Module) tabgui_tab.features.get(i);
                String fName = (feature.isEnabled() ? "§c" : "§7") + feature.getName();

                GL11.glEnable(3553);
                GL11.glColor3f(1.0F, 1.0F, 1.0F);
                Wrapper.getFontRenderer().drawStringWithShadow(2, i, 255, 255, 255, fName);
                i += 10;
            }

            GL11.glDisable(3089);
            GL11.glPopMatrix();
        }

        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glEnable(2884);
    }

    private void drawBox(int x1, int y1, int x2, int y2) {
        GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.6F);
        GL11.glBegin(7);
        GL11.glVertex2i(x1, y1);
        GL11.glVertex2i(x2, y1);
        GL11.glVertex2i(x2, y2);
        GL11.glVertex2i(x1, y2);
        GL11.glEnd();
        double xi1 = (double) x1 - 0.1D;
        double xi2 = (double) x2 + 0.1D;
        double yi1 = (double) y1 - 0.1D;
        double yi2 = (double) y2 + 0.1D;

        GL11.glLineWidth(1.5F);
        GL11.glColor3f(0.59F, 0.05F, 0.11F);
        GL11.glBegin(2);
        GL11.glVertex2d(xi1, yi1);
        GL11.glVertex2d(xi2, yi1);
        GL11.glVertex2d(xi2, yi2);
        GL11.glVertex2d(xi1, yi2);
        GL11.glEnd();
        xi1 -= 0.9D;
        xi2 += 0.9D;
        yi1 -= 0.9D;
        yi2 += 0.9D;
        GL11.glBegin(9);
        GL11.glColor4f(0.125F, 0.125F, 0.125F, 0.75F);
        GL11.glVertex2d((double) x1, (double) y1);
        GL11.glVertex2d((double) x2, (double) y1);
        GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.0F);
        GL11.glVertex2d(xi2, yi1);
        GL11.glVertex2d(xi1, yi1);
        GL11.glVertex2d(xi1, yi2);
        GL11.glColor4f(0.125F, 0.125F, 0.125F, 0.75F);
        GL11.glVertex2d((double) x1, (double) y2);
        GL11.glEnd();
        GL11.glBegin(9);
        GL11.glVertex2d((double) x2, (double) y2);
        GL11.glVertex2d((double) x2, (double) y1);
        GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.0F);
        GL11.glVertex2d(xi2, yi1);
        GL11.glVertex2d(xi2, yi2);
        GL11.glVertex2d(xi1, yi2);
        GL11.glColor4f(0.125F, 0.125F, 0.125F, 0.75F);
        GL11.glVertex2d((double) x1, (double) y2);
        GL11.glEnd();
    }
}
