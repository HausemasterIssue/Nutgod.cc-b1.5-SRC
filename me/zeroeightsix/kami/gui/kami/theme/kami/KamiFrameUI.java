package me.zeroeightsix.kami.gui.kami.theme.kami;

import java.util.Iterator;
import me.zeroeightsix.kami.KamiMod;
import me.zeroeightsix.kami.gui.kami.DisplayGuiScreen;
import me.zeroeightsix.kami.gui.kami.KamiGUI;
import me.zeroeightsix.kami.gui.kami.RenderHelper;
import me.zeroeightsix.kami.gui.kami.RootFontRenderer;
import me.zeroeightsix.kami.gui.kami.RootLargeFontRenderer;
import me.zeroeightsix.kami.gui.rgui.GUI;
import me.zeroeightsix.kami.gui.rgui.component.AlignedComponent;
import me.zeroeightsix.kami.gui.rgui.component.Component;
import me.zeroeightsix.kami.gui.rgui.component.container.Container;
import me.zeroeightsix.kami.gui.rgui.component.container.use.Frame;
import me.zeroeightsix.kami.gui.rgui.component.listen.MouseListener;
import me.zeroeightsix.kami.gui.rgui.component.listen.UpdateListener;
import me.zeroeightsix.kami.gui.rgui.poof.use.FramePoof;
import me.zeroeightsix.kami.gui.rgui.render.AbstractComponentUI;
import me.zeroeightsix.kami.gui.rgui.render.font.FontRenderer;
import me.zeroeightsix.kami.gui.rgui.util.ContainerHelper;
import me.zeroeightsix.kami.gui.rgui.util.Docking;
import me.zeroeightsix.kami.util.Bind;
import me.zeroeightsix.kami.util.ColourHolder;
import me.zeroeightsix.kami.util.Wrapper;
import org.lwjgl.opengl.GL11;

public class KamiFrameUI extends AbstractComponentUI {

    ColourHolder frameColour;
    ColourHolder outlineColour;
    Component yLineComponent;
    Component xLineComponent;
    Component centerXComponent;
    Component centerYComponent;
    boolean centerX;
    boolean centerY;
    int xLineOffset;
    private static final RootFontRenderer ff = new RootLargeFontRenderer();

    public KamiFrameUI() {
        this.frameColour = KamiGUI.primaryColour.setA(100);
        this.outlineColour = this.frameColour.darker();
        this.yLineComponent = null;
        this.xLineComponent = null;
        this.centerXComponent = null;
        this.centerYComponent = null;
        this.centerX = false;
        this.centerY = false;
        this.xLineOffset = 0;
    }

    public void renderComponent(Frame component, FontRenderer fontRenderer) {
        if (component.getOpacity() != 0.0F) {
            GL11.glDisable(3553);
            GL11.glColor4f(0.65F, 0.0F, 0.6F, 0.7F);
            RenderHelper.drawFilledRectangle(0.0F, 0.0F, (float) component.getWidth(), (float) component.getHeight());
            GL11.glColor3f(255.0F, 255.0F, 255.0F);
            GL11.glLineWidth(2.0F);
            RenderHelper.drawRectangle(0.0F, 0.0F, (float) component.getWidth(), (float) component.getHeight());
            GL11.glColor3f(230.0F, 1.0F, 230.0F);
            KamiFrameUI.ff.drawString(component.getWidth() / 2 - KamiFrameUI.ff.getStringWidth(component.getTitle()) / 2, 1, component.getTitle());
            int top_y = 5;
            int bottom_y = component.getTheme().getFontRenderer().getFontHeight() - 9;

            if (component.isCloseable() && component.isMinimizeable()) {
                top_y -= 4;
                bottom_y -= 4;
            }

            if (component.isCloseable()) {
                GL11.glLineWidth(2.0F);
                GL11.glColor3f(255.0F, 255.0F, 255.0F);
                GL11.glBegin(1);
                GL11.glVertex2d((double) (component.getWidth() - 20), (double) top_y);
                GL11.glVertex2d((double) (component.getWidth() - 10), (double) bottom_y);
                GL11.glVertex2d((double) (component.getWidth() - 10), (double) top_y);
                GL11.glVertex2d((double) (component.getWidth() - 20), (double) bottom_y);
                GL11.glEnd();
            }

            if (component.isCloseable() && component.isMinimizeable()) {
                top_y += 12;
                bottom_y += 12;
            }

            if (component.isMinimizeable()) {
                GL11.glLineWidth(2.0F);
                GL11.glColor3f(255.0F, 0.0F, 255.0F);
                if (component.isMinimized()) {
                    GL11.glBegin(2);
                    GL11.glVertex2d((double) (component.getWidth() - 15), (double) (top_y + 2));
                    GL11.glVertex2d((double) (component.getWidth() - 15), (double) (bottom_y + 3));
                    GL11.glVertex2d((double) (component.getWidth() - 10), (double) (bottom_y + 3));
                    GL11.glVertex2d((double) (component.getWidth() - 10), (double) (top_y + 2));
                    GL11.glEnd();
                } else {
                    GL11.glBegin(1);
                    GL11.glVertex2d((double) (component.getWidth() - 15), (double) (bottom_y + 4));
                    GL11.glVertex2d((double) (component.getWidth() - 10), (double) (bottom_y + 4));
                    GL11.glEnd();
                }
            }

            if (component.isPinneable()) {
                if (component.isPinned()) {
                    GL11.glColor3f(255.0F, 0.0F, 0.0F);
                } else {
                    GL11.glColor3f(255.0F, 1.0F, 1.0F);
                }

                RenderHelper.drawCircle(7.0F, 4.0F, 2.0F);
                GL11.glLineWidth(3.0F);
                GL11.glBegin(1);
                GL11.glVertex2d(7.0D, 4.0D);
                GL11.glVertex2d(4.0D, 8.0D);
                GL11.glEnd();
            }

            if (component.equals(this.xLineComponent)) {
                GL11.glColor3f(1.0F, 0.0F, 1.0F);
                GL11.glLineWidth(1.0F);
                GL11.glBegin(1);
                GL11.glVertex2d((double) this.xLineOffset, (double) (-GUI.calculateRealPosition(component)[1]));
                GL11.glVertex2d((double) this.xLineOffset, (double) Wrapper.getMinecraft().displayHeight);
                GL11.glEnd();
            }

            double y;

            if (component == this.centerXComponent && this.centerX) {
                GL11.glColor3f(1.0F, 0.0F, 1.0F);
                GL11.glLineWidth(1.0F);
                GL11.glBegin(1);
                y = (double) (component.getWidth() / 2);
                GL11.glVertex2d(y, (double) (-GUI.calculateRealPosition(component)[1]));
                GL11.glVertex2d(y, (double) Wrapper.getMinecraft().displayHeight);
                GL11.glEnd();
            }

            if (component.equals(this.yLineComponent)) {
                GL11.glColor3f(1.0F, 0.0F, 1.0F);
                GL11.glLineWidth(1.0F);
                GL11.glBegin(1);
                GL11.glVertex2d((double) (-GUI.calculateRealPosition(component)[0]), 0.0D);
                GL11.glVertex2d((double) Wrapper.getMinecraft().displayWidth, 0.0D);
                GL11.glEnd();
            }

            if (component == this.centerYComponent && this.centerY) {
                GL11.glColor3f(1.0F, 0.0F, 1.0F);
                GL11.glLineWidth(1.0F);
                GL11.glBegin(1);
                y = (double) (component.getHeight() / 2);
                GL11.glVertex2d((double) (-GUI.calculateRealPosition(component)[0]), y);
                GL11.glVertex2d((double) Wrapper.getMinecraft().displayWidth, y);
                GL11.glEnd();
            }

            GL11.glDisable(3042);
        }
    }

    public void handleMouseRelease(Frame component, int x, int y, int button) {
        this.yLineComponent = null;
        this.xLineComponent = null;
        this.centerXComponent = null;
        this.centerYComponent = null;
    }

    public void handleMouseDrag(Frame component, int x, int y, int button) {
        super.handleMouseDrag(component, x, y, button);
    }

    public void handleAddComponent(final Frame component, Container container) {
        super.handleAddComponent(component, container);
        component.setOriginOffsetY(component.getTheme().getFontRenderer().getFontHeight() + 3);
        component.setOriginOffsetX(3);
        component.addMouseListener(new MouseListener() {
            public void onMouseDown(MouseListener.MouseButtonEvent event) {
                int y = event.getY();
                int x = event.getX();

                if (y < 0) {
                    if (x > component.getWidth() - 22) {
                        if (component.isMinimizeable() && component.isCloseable()) {
                            if (y > -component.getOriginOffsetY() / 2) {
                                if (component.isMinimized()) {
                                    component.callPoof(FramePoof.class, new FramePoof.FramePoofInfo(FramePoof.Action.MAXIMIZE));
                                } else {
                                    component.callPoof(FramePoof.class, new FramePoof.FramePoofInfo(FramePoof.Action.MINIMIZE));
                                }
                            } else {
                                component.callPoof(FramePoof.class, new FramePoof.FramePoofInfo(FramePoof.Action.CLOSE));
                            }
                        } else if (component.isMinimized() && component.isMinimizeable()) {
                            component.callPoof(FramePoof.class, new FramePoof.FramePoofInfo(FramePoof.Action.MAXIMIZE));
                        } else if (component.isMinimizeable()) {
                            component.callPoof(FramePoof.class, new FramePoof.FramePoofInfo(FramePoof.Action.MINIMIZE));
                        } else if (component.isCloseable()) {
                            component.callPoof(FramePoof.class, new FramePoof.FramePoofInfo(FramePoof.Action.CLOSE));
                        }
                    }

                    if (x < 10 && x > 0 && component.isPinneable()) {
                        component.setPinned(!component.isPinned());
                    }
                }

            }

            public void onMouseRelease(MouseListener.MouseButtonEvent event) {}

            public void onMouseDrag(MouseListener.MouseButtonEvent event) {}

            public void onMouseMove(MouseListener.MouseMoveEvent event) {}

            public void onScroll(MouseListener.MouseScrollEvent event) {}
        });
        component.addUpdateListener(new UpdateListener() {
            public void updateSize(Component component, int oldWidth, int oldHeight) {
                if (component instanceof Frame) {
                    KamiGUI.dock((Frame) component);
                }

            }

            public void updateLocation(Component component, int oldX, int oldY) {}
        });
        component.addPoof(new Frame.FrameDragPoof() {
            public void execute(Frame component, Frame.FrameDragPoof.DragInfo info) {
                if (!Bind.isShiftDown() && !Bind.isAltDown() && !Bind.isCtrlDown()) {
                    int x = info.getX();
                    int y = info.getY();

                    KamiFrameUI.this.yLineComponent = null;
                    KamiFrameUI.this.xLineComponent = null;
                    component.setDocking(Docking.NONE);
                    KamiGUI rootGUI = KamiMod.getInstance().getGuiManager();
                    Iterator diff = rootGUI.getChildren().iterator();

                    while (diff.hasNext()) {
                        Component c = (Component) diff.next();

                        if (!c.equals(component)) {
                            int yDiff = Math.abs(y - c.getY());

                            if (yDiff < 4) {
                                y = c.getY();
                                KamiFrameUI.this.yLineComponent = component;
                            }

                            yDiff = Math.abs(y - (c.getY() + c.getHeight() + 3));
                            if (yDiff < 4) {
                                y = c.getY() + c.getHeight();
                                y += 3;
                                KamiFrameUI.this.yLineComponent = component;
                            }

                            int xDiff = Math.abs(x + component.getWidth() - (c.getX() + c.getWidth()));

                            if (xDiff < 4) {
                                x = c.getX() + c.getWidth();
                                x -= component.getWidth();
                                KamiFrameUI.this.xLineComponent = component;
                                KamiFrameUI.this.xLineOffset = component.getWidth();
                            }

                            xDiff = Math.abs(x - c.getX());
                            if (xDiff < 4) {
                                x = c.getX();
                                KamiFrameUI.this.xLineComponent = component;
                                KamiFrameUI.this.xLineOffset = 0;
                            }

                            xDiff = Math.abs(x - (c.getX() + c.getWidth() + 3));
                            if (xDiff < 4) {
                                x = c.getX() + c.getWidth() + 3;
                                KamiFrameUI.this.xLineComponent = component;
                                KamiFrameUI.this.xLineOffset = 0;
                            }
                        }
                    }

                    if (x < 5) {
                        x = 0;
                        ContainerHelper.setAlignment(component, AlignedComponent.Alignment.LEFT);
                        component.setDocking(Docking.LEFT);
                    }

                    int diff1 = (x + component.getWidth()) * DisplayGuiScreen.getScale() - Wrapper.getMinecraft().displayWidth;

                    if (-diff1 < 5) {
                        x = Wrapper.getMinecraft().displayWidth / DisplayGuiScreen.getScale() - component.getWidth();
                        ContainerHelper.setAlignment(component, AlignedComponent.Alignment.RIGHT);
                        component.setDocking(Docking.RIGHT);
                    }

                    if (y < 5) {
                        y = 0;
                        if (component.getDocking().equals(Docking.RIGHT)) {
                            component.setDocking(Docking.TOPRIGHT);
                        } else if (component.getDocking().equals(Docking.LEFT)) {
                            component.setDocking(Docking.TOPLEFT);
                        } else {
                            component.setDocking(Docking.TOP);
                        }
                    }

                    diff1 = (y + component.getHeight()) * DisplayGuiScreen.getScale() - Wrapper.getMinecraft().displayHeight;
                    if (-diff1 < 5) {
                        y = Wrapper.getMinecraft().displayHeight / DisplayGuiScreen.getScale() - component.getHeight();
                        if (component.getDocking().equals(Docking.RIGHT)) {
                            component.setDocking(Docking.BOTTOMRIGHT);
                        } else if (component.getDocking().equals(Docking.LEFT)) {
                            component.setDocking(Docking.BOTTOMLEFT);
                        } else {
                            component.setDocking(Docking.BOTTOM);
                        }
                    }

                    if (Math.abs((x + component.getWidth() / 2) * DisplayGuiScreen.getScale() * 2 - Wrapper.getMinecraft().displayWidth) < 5) {
                        KamiFrameUI.this.xLineComponent = null;
                        KamiFrameUI.this.centerXComponent = component;
                        KamiFrameUI.this.centerX = true;
                        x = Wrapper.getMinecraft().displayWidth / (DisplayGuiScreen.getScale() * 2) - component.getWidth() / 2;
                        if (component.getDocking().isTop()) {
                            component.setDocking(Docking.CENTERTOP);
                        } else if (component.getDocking().isBottom()) {
                            component.setDocking(Docking.CENTERBOTTOM);
                        } else {
                            component.setDocking(Docking.CENTERVERTICAL);
                        }

                        ContainerHelper.setAlignment(component, AlignedComponent.Alignment.CENTER);
                    } else {
                        KamiFrameUI.this.centerX = false;
                    }

                    if (Math.abs((y + component.getHeight() / 2) * DisplayGuiScreen.getScale() * 2 - Wrapper.getMinecraft().displayHeight) < 5) {
                        KamiFrameUI.this.yLineComponent = null;
                        KamiFrameUI.this.centerYComponent = component;
                        KamiFrameUI.this.centerY = true;
                        y = Wrapper.getMinecraft().displayHeight / (DisplayGuiScreen.getScale() * 2) - component.getHeight() / 2;
                        if (component.getDocking().isLeft()) {
                            component.setDocking(Docking.CENTERLEFT);
                        } else if (component.getDocking().isRight()) {
                            component.setDocking(Docking.CENTERRIGHT);
                        } else if (component.getDocking().isCenterHorizontal()) {
                            component.setDocking(Docking.CENTER);
                        } else {
                            component.setDocking(Docking.CENTERHOIZONTAL);
                        }
                    } else {
                        KamiFrameUI.this.centerY = false;
                    }

                    info.setX(x);
                    info.setY(y);
                }
            }
        });
    }
}
