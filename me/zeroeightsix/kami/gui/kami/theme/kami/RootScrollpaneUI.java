package me.zeroeightsix.kami.gui.kami.theme.kami;

import me.zeroeightsix.kami.gui.kami.RenderHelper;
import me.zeroeightsix.kami.gui.rgui.component.Component;
import me.zeroeightsix.kami.gui.rgui.component.container.Container;
import me.zeroeightsix.kami.gui.rgui.component.container.use.Scrollpane;
import me.zeroeightsix.kami.gui.rgui.component.listen.MouseListener;
import me.zeroeightsix.kami.gui.rgui.component.listen.RenderListener;
import me.zeroeightsix.kami.gui.rgui.render.AbstractComponentUI;
import me.zeroeightsix.kami.gui.rgui.render.font.FontRenderer;
import org.lwjgl.opengl.GL11;

public class RootScrollpaneUI extends AbstractComponentUI {

    long lastScroll = 0L;
    Component scrollComponent = null;
    float barLife = 1220.0F;
    boolean dragBar = false;
    int dY = 0;
    double hovering = 0.0D;

    public void renderComponent(Scrollpane component, FontRenderer fontRenderer) {}

    public void handleAddComponent(final Scrollpane component, Container container) {
        component.addMouseListener(new MouseListener() {
            public void onMouseDown(MouseListener.MouseButtonEvent event) {
                if ((float) (System.currentTimeMillis() - RootScrollpaneUI.this.lastScroll) < RootScrollpaneUI.this.barLife && RootScrollpaneUI.this.scrollComponent.liesIn(component) && component.canScrollY()) {
                    double progress = (double) component.getScrolledY() / (double) component.getMaxScrollY();
                    byte barHeight = 30;
                    int y = (int) ((double) (component.getHeight() - barHeight) * progress);

                    if (event.getX() > component.getWidth() - 10 && event.getY() > y && event.getY() < y + barHeight) {
                        RootScrollpaneUI.this.dragBar = true;
                        RootScrollpaneUI.this.dY = event.getY() - y;
                        event.cancel();
                    }
                }

            }

            public void onMouseRelease(MouseListener.MouseButtonEvent event) {
                RootScrollpaneUI.this.dragBar = false;
            }

            public void onMouseDrag(MouseListener.MouseButtonEvent event) {
                if (RootScrollpaneUI.this.dragBar) {
                    double progress = (double) event.getY() / (double) component.getHeight();

                    progress = Math.max(Math.min(progress, 1.0D), 0.0D);
                    component.setScrolledY((int) ((double) component.getMaxScrollY() * progress));
                    event.cancel();
                }

            }

            public void onMouseMove(MouseListener.MouseMoveEvent event) {}

            public void onScroll(MouseListener.MouseScrollEvent event) {
                RootScrollpaneUI.this.lastScroll = System.currentTimeMillis();
                RootScrollpaneUI.this.scrollComponent = event.getComponent();
            }
        });
        component.addRenderListener(new RenderListener() {
            public void onPreRender() {}

            public void onPostRender() {
                if (RootScrollpaneUI.this.dragBar) {
                    RootScrollpaneUI.this.lastScroll = System.currentTimeMillis();
                }

                if ((float) (System.currentTimeMillis() - RootScrollpaneUI.this.lastScroll) < RootScrollpaneUI.this.barLife && RootScrollpaneUI.this.scrollComponent.liesIn(component) && component.canScrollY()) {
                    float alpha = Math.min(1.0F, (RootScrollpaneUI.this.barLife - (float) (System.currentTimeMillis() - RootScrollpaneUI.this.lastScroll)) / 100.0F) / 3.0F;

                    if (RootScrollpaneUI.this.dragBar) {
                        alpha = 0.4F;
                    }

                    GL11.glColor4f(1.0F, 0.22F, 0.22F, alpha);
                    GL11.glDisable(3553);
                    byte barHeight = 30;
                    double progress = (double) component.getScrolledY() / (double) component.getMaxScrollY();
                    int y = (int) ((double) (component.getHeight() - barHeight) * progress);

                    RenderHelper.drawRoundedRectangle((float) (component.getWidth() - 6), (float) y, 4.0F, (float) barHeight, 1.0F);
                }

            }
        });
    }
}
