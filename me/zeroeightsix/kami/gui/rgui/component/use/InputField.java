package me.zeroeightsix.kami.gui.rgui.component.use;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import me.zeroeightsix.kami.gui.kami.DisplayGuiScreen;
import me.zeroeightsix.kami.gui.rgui.GUI;
import me.zeroeightsix.kami.gui.rgui.component.AbstractComponent;
import me.zeroeightsix.kami.gui.rgui.component.listen.KeyListener;
import me.zeroeightsix.kami.gui.rgui.component.listen.MouseListener;
import me.zeroeightsix.kami.gui.rgui.component.listen.RenderListener;
import me.zeroeightsix.kami.gui.rgui.poof.PoofInfo;
import me.zeroeightsix.kami.gui.rgui.poof.use.Poof;
import me.zeroeightsix.kami.gui.rgui.render.font.FontRenderer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class InputField extends AbstractComponent {

    char echoChar;
    InputField.InputState currentState;
    long startRail;
    float railT;
    boolean rail;
    int railChar;
    KeyListener inputListener;
    int railDelay;
    int railRepeat;
    long lastTypeMS;
    int undoT;
    ArrayList undoMap;
    ArrayList redoMap;
    int scrollX;
    boolean shift;
    FontRenderer fontRenderer;

    public FontRenderer getFontRenderer() {
        return this.fontRenderer == null ? this.getTheme().getFontRenderer() : this.fontRenderer;
    }

    public void setFontRenderer(FontRenderer fontRenderer) {
        this.fontRenderer = fontRenderer;
    }

    public InputField(String text) {
        this.echoChar = 0;
        this.currentState = new InputField.InputState("", 0, false, 0, 0);
        this.startRail = 0L;
        this.railT = 0.0F;
        this.rail = false;
        this.railChar = 0;
        this.railDelay = 500;
        this.railRepeat = 32;
        this.lastTypeMS = 0L;
        this.undoT = 0;
        this.undoMap = new ArrayList();
        this.redoMap = new ArrayList();
        this.scrollX = 0;
        this.shift = false;
        this.fontRenderer = null;
        this.currentState.text = text;
        this.addRenderListener(new RenderListener() {
            public void onPreRender() {}

            public void onPostRender() {
                if (!InputField.this.isFocussed()) {
                    InputField.this.currentState.selection = false;
                }

                int[] real = GUI.calculateRealPosition(InputField.this);
                int scale = DisplayGuiScreen.getScale();

                GL11.glScissor(real[0] * scale - InputField.this.getParent().getOriginOffsetX() - 1, Display.getHeight() - InputField.this.getHeight() * scale - real[1] * scale - 1, InputField.this.getWidth() * scale + InputField.this.getParent().getOriginOffsetX() + 1, InputField.this.getHeight() * scale + 1);
                GL11.glEnable(3089);
                GL11.glTranslatef((float) (-InputField.this.scrollX), 0.0F, 0.0F);
                FontRenderer fontRenderer = InputField.this.getFontRenderer();

                GL11.glLineWidth(1.0F);
                GL11.glColor3f(1.0F, 1.0F, 1.0F);
                boolean cursor = (int) ((System.currentTimeMillis() - InputField.this.lastTypeMS) / 500L) % 2 == 0 && InputField.this.isFocussed();
                int x = 0;
                int i = 0;
                boolean selection = false;

                if (InputField.this.getCursorRow() == 0 && cursor) {
                    GL11.glBegin(1);
                    GL11.glVertex2d(4.0D, 2.0D);
                    GL11.glVertex2d(4.0D, (double) (fontRenderer.getFontHeight() - 1));
                    GL11.glEnd();
                }

                char[] s = InputField.this.getDisplayText().toCharArray();
                int i = s.length;

                for (int j = 0; j < i; ++j) {
                    char c = s[j];
                    int w = fontRenderer.getStringWidth(c + "");

                    if (InputField.this.getCurrentState().isSelection() && i == InputField.this.getCurrentState().getSelectionStart()) {
                        selection = true;
                    }

                    if (selection) {
                        GL11.glColor4f(0.2F, 0.6F, 1.0F, 0.3F);
                        GL11.glBegin(7);
                        GL11.glVertex2d((double) (x + 2), 2.0D);
                        GL11.glVertex2d((double) (x + 2), (double) (fontRenderer.getFontHeight() - 2));
                        GL11.glVertex2d((double) (x + w + 2), (double) (fontRenderer.getFontHeight() - 2));
                        GL11.glVertex2d((double) (x + w + 2), 2.0D);
                        GL11.glEnd();
                    }

                    ++i;
                    x += w;
                    if (i == InputField.this.getCursorRow() && cursor && !InputField.this.getCurrentState().isSelection()) {
                        GL11.glBegin(1);
                        GL11.glVertex2d((double) (x + 2), 2.0D);
                        GL11.glVertex2d((double) (x + 2), (double) fontRenderer.getFontHeight());
                        GL11.glEnd();
                    }

                    if (InputField.this.getCurrentState().isSelection() && i == InputField.this.getCurrentState().getSelectionEnd()) {
                        selection = false;
                    }
                }

                String s = InputField.this.getDisplayText();

                if (s.isEmpty()) {
                    s = " ";
                }

                GL11.glEnable(3042);
                fontRenderer.drawString(0, -1, s);
                GL11.glDisable(3553);
                GL11.glBlendFunc(770, 771);
                GL11.glTranslatef((float) InputField.this.scrollX, 0.0F, 0.0F);
                GL11.glDisable(3089);
            }
        });
        this.addKeyListener(this.inputListener = new KeyListener() {
            public void onKeyDown(KeyListener.KeyEvent event) {
                InputField.this.lastTypeMS = System.currentTimeMillis();
                if (event.getKey() == 14) {
                    if (InputField.this.getText().length() > 0) {
                        InputField.this.pushUndo();
                        if (InputField.this.currentState.selection) {
                            InputField.this.currentState.cursorRow = InputField.this.currentState.selectionEnd;
                            InputField.this.scroll();
                            InputField.this.remove(InputField.this.currentState.selectionEnd - InputField.this.currentState.selectionStart);
                            InputField.this.currentState.selection = false;
                        } else {
                            InputField.this.remove(1);
                        }
                    }
                } else if (Keyboard.getEventCharacter() == 26) {
                    if (!InputField.this.undoMap.isEmpty()) {
                        InputField.this.redoMap.add(0, InputField.this.currentState.clone());
                        InputField.this.currentState = (InputField.InputState) InputField.this.undoMap.get(0);
                        InputField.this.undoMap.remove(0);
                    }
                } else if (Keyboard.getEventCharacter() == 25) {
                    if (!InputField.this.redoMap.isEmpty()) {
                        InputField.this.undoMap.add(0, InputField.this.currentState.clone());
                        InputField.this.currentState = (InputField.InputState) InputField.this.redoMap.get(0);
                        InputField.this.redoMap.remove(0);
                    }
                } else if (Keyboard.getEventCharacter() == 1) {
                    InputField.this.currentState.selection = true;
                    InputField.this.currentState.selectionStart = 0;
                    InputField.this.currentState.selectionEnd = InputField.this.currentState.getText().length();
                } else if (event.getKey() == 54) {
                    InputField.this.shift = true;
                } else if (event.getKey() == 1) {
                    InputField.this.currentState.selection = false;
                } else {
                    Clipboard clipboard;

                    if (Keyboard.getEventCharacter() == 22) {
                        clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

                        try {
                            InputField.this.type((String) clipboard.getData(DataFlavor.stringFlavor));
                        } catch (UnsupportedFlavorException unsupportedflavorexception) {
                            ;
                        } catch (IOException ioexception) {
                            ;
                        }
                    } else if (Keyboard.getEventCharacter() == 3) {
                        if (InputField.this.currentState.selection) {
                            clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                            StringSelection selection = new StringSelection(InputField.this.currentState.getText().substring(InputField.this.currentState.selectionStart, InputField.this.currentState.selectionEnd));

                            clipboard.setContents(selection, selection);
                        }
                    } else if (event.getKey() == 205) {
                        if (InputField.this.currentState.cursorRow < InputField.this.getText().length()) {
                            if (InputField.this.shift) {
                                if (!InputField.this.currentState.selection) {
                                    InputField.this.currentState.selectionStart = InputField.this.currentState.cursorRow;
                                    InputField.this.currentState.selectionEnd = InputField.this.currentState.cursorRow;
                                }

                                InputField.this.currentState.selection = true;
                                InputField.this.currentState.selectionEnd = Math.min(InputField.this.getText().length(), InputField.this.currentState.selectionEnd + 1);
                            } else if (InputField.this.currentState.selection) {
                                InputField.this.currentState.selection = false;
                                InputField.this.currentState.cursorRow = InputField.this.currentState.selectionEnd;
                                InputField.this.scroll();
                            } else {
                                InputField.this.currentState.cursorRow = Math.min(InputField.this.getText().length(), InputField.this.currentState.cursorRow + 1);
                                InputField.this.scroll();
                            }
                        }
                    } else if (event.getKey() == 203) {
                        if (InputField.this.currentState.cursorRow > 0) {
                            if (InputField.this.shift) {
                                if (!InputField.this.currentState.selection) {
                                    InputField.this.currentState.selectionStart = InputField.this.currentState.cursorRow;
                                    InputField.this.currentState.selectionEnd = InputField.this.currentState.cursorRow;
                                }

                                InputField.this.currentState.selection = true;
                                InputField.this.currentState.selectionStart = Math.max(0, InputField.this.currentState.selectionStart - 1);
                            } else if (InputField.this.currentState.selection) {
                                InputField.this.currentState.selection = false;
                                InputField.this.currentState.cursorRow = InputField.this.currentState.selectionStart;
                                InputField.this.scroll();
                            } else {
                                InputField.this.currentState.cursorRow = Math.max(0, InputField.this.currentState.cursorRow - 1);
                                InputField.this.scroll();
                            }
                        }
                    } else if (Keyboard.getEventCharacter() != 0) {
                        InputField.this.pushUndo();
                        if (InputField.this.currentState.selection) {
                            InputField.this.currentState.cursorRow = InputField.this.currentState.selectionEnd;
                            InputField.this.remove(InputField.this.currentState.selectionEnd - InputField.this.currentState.selectionStart);
                            InputField.this.currentState.selection = false;
                        }

                        InputField.this.type(Keyboard.getEventCharacter() + "");
                    }
                }

                if (event.getKey() != 42) {
                    InputField.this.startRail = System.currentTimeMillis();
                    InputField.this.railChar = event.getKey();
                }
            }

            public void onKeyUp(KeyListener.KeyEvent event) {
                InputField.this.rail = false;
                InputField.this.startRail = 0L;
                if (event.getKey() == 54) {
                    InputField.this.shift = false;
                }

            }
        });
        this.addMouseListener(new MouseListener() {
            public void onMouseDown(MouseListener.MouseButtonEvent event) {
                InputField.this.currentState.selection = false;
                int x = -InputField.this.scrollX;
                int i = 0;
                char[] achar = InputField.this.getText().toCharArray();
                int i = achar.length;

                for (int j = 0; j < i; ++j) {
                    char c = achar[j];

                    x += InputField.this.getFontRenderer().getStringWidth(c + "");
                    if (event.getX() < x) {
                        InputField.this.currentState.cursorRow = i;
                        InputField.this.scroll();
                        return;
                    }

                    ++i;
                }

                InputField.this.currentState.cursorRow = i;
                InputField.this.scroll();
            }

            public void onMouseRelease(MouseListener.MouseButtonEvent event) {}

            public void onMouseDrag(MouseListener.MouseButtonEvent event) {
                InputField.this.currentState.selection = true;
                InputField.this.currentState.selectionStart = InputField.this.currentState.cursorRow;
                int x = -InputField.this.scrollX;
                int i = 0;
                char[] buf = InputField.this.getText().toCharArray();
                int a = buf.length;

                for (int i = 0; i < a; ++i) {
                    char c = buf[i];

                    x += InputField.this.getFontRenderer().getStringWidth(c + "");
                    if (event.getX() < x) {
                        InputField.this.currentState.selectionEnd = i;
                        InputField.this.scroll();
                        break;
                    }

                    ++i;
                }

                InputField.this.currentState.selectionEnd = i;
                int j = InputField.this.currentState.cursorRow;

                InputField.this.currentState.cursorRow = i;
                InputField.this.scroll();
                InputField.this.currentState.cursorRow = j;
                if (InputField.this.currentState.selectionStart > InputField.this.currentState.selectionEnd) {
                    a = InputField.this.currentState.selectionStart;
                    InputField.this.currentState.selectionStart = InputField.this.currentState.selectionEnd;
                    InputField.this.currentState.selectionEnd = a;
                }

                if (InputField.this.currentState.selectionStart == InputField.this.currentState.selectionEnd) {
                    InputField.this.currentState.selection = false;
                }

            }

            public void onMouseMove(MouseListener.MouseMoveEvent event) {}

            public void onScroll(MouseListener.MouseScrollEvent event) {}
        });
        this.addRenderListener(new RenderListener() {
            public void onPreRender() {
                if (InputField.this.startRail != 0L) {
                    if (!InputField.this.rail) {
                        InputField.this.railT = (float) (System.currentTimeMillis() - InputField.this.startRail);
                        if (InputField.this.railT > (float) InputField.this.railDelay) {
                            InputField.this.rail = true;
                            InputField.this.startRail = System.currentTimeMillis();
                        }
                    } else {
                        InputField.this.railT = (float) (System.currentTimeMillis() - InputField.this.startRail);
                        if (InputField.this.railT > (float) InputField.this.railRepeat) {
                            InputField.this.inputListener.onKeyDown(new KeyListener.KeyEvent(InputField.this.railChar));
                            InputField.this.startRail = System.currentTimeMillis();
                        }
                    }

                }
            }

            public void onPostRender() {}
        });
    }

    public InputField() {
        this("");
    }

    public InputField(int width) {
        this("");
    }

    public InputField.InputState getCurrentState() {
        return this.currentState;
    }

    public void type(String text) {
        try {
            this.setText(this.getText().substring(0, this.currentState.getCursorRow()) + text + this.getText().substring(this.currentState.getCursorRow()));
            this.currentState.cursorRow += text.length();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        this.scroll();
    }

    public void remove(int back) {
        back = Math.min(back, this.currentState.getCursorRow());
        boolean a = this.setText(this.getText().substring(0, Math.max(this.currentState.getCursorRow() - back, 0)) + this.getText().substring(this.currentState.getCursorRow()));

        if (!a) {
            this.currentState.cursorRow -= back;
        }

        this.scroll();
    }

    private void scroll() {
        int aX = 0;
        int i = 0;
        String a = "";
        char[] diff = this.getText().toCharArray();
        int i = diff.length;

        for (int j = 0; j < i; ++j) {
            char c = diff[j];

            aX += this.getFontRenderer().getStringWidth(c + "");
            ++i;
            a = a + c;
            if (i >= this.currentState.cursorRow) {
                break;
            }
        }

        int k = aX - this.scrollX;

        if (k > this.getWidth()) {
            this.scrollX = aX - this.getWidth() + 8;
        } else if (k < 0) {
            this.scrollX = aX + 8;
        }

        if (this.currentState.cursorRow == 0) {
            this.scrollX = 0;
        }

    }

    public int getCursorRow() {
        return this.currentState.getCursorRow();
    }

    private void pushUndo() {
        ++this.undoT;
        if (this.undoT > 3) {
            this.undoT = 0;
            this.undoMap.add(0, this.currentState.clone());
        }

    }

    public String getText() {
        return this.currentState.getText();
    }

    public String getDisplayText() {
        return this.isEchoCharSet() ? this.getText().replaceAll(".", this.getEchoChar() + "") : this.getText();
    }

    public boolean setText(String text) {
        this.currentState.text = text;
        this.callPoof(InputField.InputFieldTextPoof.class, (PoofInfo) null);
        if (this.currentState.cursorRow > this.currentState.text.length()) {
            this.currentState.cursorRow = this.currentState.text.length();
            this.scroll();
            return true;
        } else {
            return false;
        }
    }

    public char getEchoChar() {
        return this.echoChar;
    }

    public InputField setEchoChar(char echoChar) {
        this.echoChar = echoChar;
        return this;
    }

    public boolean isEchoCharSet() {
        return this.echoChar != 0;
    }

    public abstract static class InputFieldTextPoof extends Poof {

    }

    public class InputState {

        String text;
        int cursorRow;
        boolean selection;
        int selectionStart;
        int selectionEnd;

        public InputState(String text, int cursorRow, boolean selection, int selectionStart, int selectionEnd) {
            this.text = text;
            this.cursorRow = cursorRow;
            this.selection = selection;
            this.selectionStart = selectionStart;
            this.selectionEnd = selectionEnd;
        }

        protected InputField.InputState clone() {
            return InputField.this.new InputState(this.getText(), this.getCursorRow(), this.isSelection(), this.getSelectionStart(), this.getSelectionEnd());
        }

        public String getText() {
            return this.text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getCursorRow() {
            return this.cursorRow;
        }

        public void setCursorRow(int cursorRow) {
            this.cursorRow = cursorRow;
            InputField.this.scroll();
        }

        public boolean isSelection() {
            return this.selection;
        }

        public void setSelection(boolean selection) {
            this.selection = selection;
        }

        public int getSelectionStart() {
            return this.selectionStart;
        }

        public void setSelectionStart(int selectionStart) {
            this.selectionStart = selectionStart;
        }

        public int getSelectionEnd() {
            return this.selectionEnd;
        }

        public void setSelectionEnd(int selectionEnd) {
            this.selectionEnd = selectionEnd;
        }
    }
}
