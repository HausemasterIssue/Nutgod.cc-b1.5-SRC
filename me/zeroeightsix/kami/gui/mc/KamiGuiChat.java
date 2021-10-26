package me.zeroeightsix.kami.gui.mc;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import me.zeroeightsix.kami.KamiMod;
import me.zeroeightsix.kami.command.Command;
import me.zeroeightsix.kami.command.syntax.SyntaxChunk;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class KamiGuiChat extends GuiChat {

    private String startString;
    private String currentFillinLine;
    private int cursor;

    public KamiGuiChat(String startString, String historybuffer, int sentHistoryCursor) {
        super(startString);
        this.startString = startString;
        if (!startString.equals(Command.getCommandPrefix())) {
            this.calculateCommand(startString.substring(Command.getCommandPrefix().length()));
        }

        this.historyBuffer = historybuffer;
        this.cursor = sentHistoryCursor;
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        this.sentHistoryCursor = this.cursor;
        super.keyTyped(typedChar, keyCode);
        this.cursor = this.sentHistoryCursor;
        final String chatLine = this.inputField.getText();

        if (!chatLine.startsWith(Command.getCommandPrefix())) {
            GuiChat newGUI = new GuiChat(chatLine) {
                int cursor;

                {
                    this.cursor = KamiGuiChat.this.cursor;
                }

                protected void keyTyped(char typedChar, int keyCode) throws IOException {
                    this.sentHistoryCursor = this.cursor;
                    super.keyTyped(typedChar, keyCode);
                    this.cursor = this.sentHistoryCursor;
                }
            };

            newGUI.historyBuffer = this.historyBuffer;
            this.mc.displayGuiScreen(newGUI);
        } else if (chatLine.equals(Command.getCommandPrefix())) {
            this.currentFillinLine = "";
        } else {
            this.calculateCommand(chatLine.substring(Command.getCommandPrefix().length()));
        }
    }

    protected void calculateCommand(String line) {
        String[] args = line.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        HashMap options = new HashMap();

        if (args.length != 0) {
            Iterator map = KamiMod.getInstance().getCommandManager().getCommands().iterator();

            Command alphaCommand;

            while (map.hasNext()) {
                alphaCommand = (Command) map.next();
                if (alphaCommand.getLabel().startsWith(args[0]) && !line.endsWith(" ") || alphaCommand.getLabel().equals(args[0])) {
                    options.put(alphaCommand.getLabel(), alphaCommand);
                }
            }

            if (options.isEmpty()) {
                this.currentFillinLine = "";
            } else {
                TreeMap treemap = new TreeMap(options);

                alphaCommand = (Command) treemap.firstEntry().getValue();
                this.currentFillinLine = alphaCommand.getLabel().substring(args[0].length());
                if (alphaCommand.getSyntaxChunks() != null && alphaCommand.getSyntaxChunks().length != 0) {
                    if (!line.endsWith(" ")) {
                        this.currentFillinLine = this.currentFillinLine + " ";
                    }

                    SyntaxChunk[] chunks = alphaCommand.getSyntaxChunks();
                    boolean cutSpace = false;

                    for (int i = 0; i < chunks.length; ++i) {
                        if (i + 1 >= args.length - 1) {
                            SyntaxChunk c = chunks[i];
                            String result = c.getChunk(chunks, c, args, i + 1 == args.length - 1 ? args[i + 1] : null);

                            if (result != "" && (!result.startsWith("<") || !result.endsWith(">")) && (!result.startsWith("[") || !result.endsWith("]"))) {
                                cutSpace = true;
                            }

                            this.currentFillinLine = this.currentFillinLine + result + (result == "" ? "" : " ") + "";
                        }
                    }

                    if (cutSpace) {
                        this.currentFillinLine = this.currentFillinLine.substring(1);
                    }

                }
            }
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawRect(2, this.height - 14, this.width - 2, this.height - 2, Integer.MIN_VALUE);
        int x = this.inputField.fontRenderer.getStringWidth(this.inputField.getText() + "") + 4;
        int y = this.inputField.getEnableBackgroundDrawing() ? this.inputField.y + (this.inputField.height - 8) / 2 : this.inputField.y;

        this.inputField.fontRenderer.drawStringWithShadow(this.currentFillinLine, (float) x, (float) y, 6710886);
        this.inputField.drawTextBox();
        ITextComponent itextcomponent = this.mc.ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY());

        if (itextcomponent != null && itextcomponent.getStyle().getHoverEvent() != null) {
            this.handleComponentHover(itextcomponent, mouseX, mouseY);
        }

        boolean a = GL11.glIsEnabled(3042);
        boolean b = GL11.glIsEnabled(3553);

        GL11.glDisable(3042);
        GL11.glDisable(3553);
        GL11.glColor3f(0.8F, 0.1F, 0.0F);
        GL11.glBegin(1);
        GL11.glVertex2f((float) (this.inputField.x - 2), (float) (this.inputField.y - 2));
        GL11.glVertex2f((float) (this.inputField.x + this.inputField.getWidth() - 2), (float) (this.inputField.y - 2));
        GL11.glVertex2f((float) (this.inputField.x + this.inputField.getWidth() - 2), (float) (this.inputField.y - 2));
        GL11.glVertex2f((float) (this.inputField.x + this.inputField.getWidth() - 2), (float) (this.inputField.y + this.inputField.height - 2));
        GL11.glVertex2f((float) (this.inputField.x + this.inputField.getWidth() - 2), (float) (this.inputField.y + this.inputField.height - 2));
        GL11.glVertex2f((float) (this.inputField.x - 2), (float) (this.inputField.y + this.inputField.height - 2));
        GL11.glVertex2f((float) (this.inputField.x - 2), (float) (this.inputField.y + this.inputField.height - 2));
        GL11.glVertex2f((float) (this.inputField.x - 2), (float) (this.inputField.y - 2));
        GL11.glEnd();
        if (a) {
            GL11.glEnable(3042);
        }

        if (b) {
            GL11.glEnable(3553);
        }

    }
}
