package me.zeroeightsix.kami.command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.zeroeightsix.kami.command.syntax.SyntaxChunk;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import me.zeroeightsix.kami.util.Wrapper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentBase;

public abstract class Command {

    protected String label;
    protected String syntax;
    protected String description;
    protected SyntaxChunk[] syntaxChunks;
    public static Setting commandPrefix = Settings.s("commandPrefix", ".");

    public Command(String label, SyntaxChunk[] syntaxChunks) {
        this.label = label;
        this.syntaxChunks = syntaxChunks;
        this.description = "Descriptionless";
    }

    public static void sendChatMessage(String message) {
        sendRawChatMessage("§8 [Nutgod.cc]" + message);
    }

    public static void sendStringChatMessage(String[] messages) {
        sendChatMessage("");
        String[] astring = messages;
        int i = messages.length;

        for (int j = 0; j < i; ++j) {
            String s = astring[j];

            sendRawChatMessage(s);
        }

    }

    public static void sendRawChatMessage(String message) {
        Wrapper.getPlayer().sendMessage(new Command.ChatMessage(message));
    }

    protected void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public static String getCommandPrefix() {
        return (String) Command.commandPrefix.getValue();
    }

    public String getLabel() {
        return this.label;
    }

    public abstract void call(String[] astring);

    public SyntaxChunk[] getSyntaxChunks() {
        return this.syntaxChunks;
    }

    protected SyntaxChunk getSyntaxChunk(String name) {
        SyntaxChunk[] asyntaxchunk = this.syntaxChunks;
        int i = asyntaxchunk.length;

        for (int j = 0; j < i; ++j) {
            SyntaxChunk c = asyntaxchunk[j];

            if (c.getType().equals(name)) {
                return c;
            }
        }

        return null;
    }

    public static char SECTIONSIGN() {
        return '§';
    }

    public static class ChatMessage extends TextComponentBase {

        String text;

        public ChatMessage(String text) {
            Pattern p = Pattern.compile("&[0123456789abcdefrlosmk]");
            Matcher m = p.matcher(text);
            StringBuffer sb = new StringBuffer();

            while (m.find()) {
                String replacement = "§" + m.group().substring(1);

                m.appendReplacement(sb, replacement);
            }

            m.appendTail(sb);
            this.text = sb.toString();
        }

        public String getUnformattedComponentText() {
            return this.text;
        }

        public ITextComponent createCopy() {
            return new Command.ChatMessage(this.text);
        }
    }
}
