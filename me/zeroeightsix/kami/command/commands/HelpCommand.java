package me.zeroeightsix.kami.command.commands;

import java.util.Arrays;
import me.zeroeightsix.kami.command.Command;
import me.zeroeightsix.kami.command.syntax.SyntaxChunk;
import me.zeroeightsix.kami.module.ModuleManager;

public class HelpCommand extends Command {

    private static final HelpCommand.Subject[] subjects = new HelpCommand.Subject[] { new HelpCommand.Subject(new String[] { "type", "int", "boolean", "double", "float"}, new String[] { "Every module has a value, and that value is always of a certain &btype.\n", "These types are displayed in kami as the ones java use. They mean the following:", "&bboolean&r: Enabled or not. Values &3true/false", "&bfloat&r: A number with a decimal point", "&bdouble&r: Like a float, but a more accurate decimal point", "&bint&r: A number with no decimal point"})};
    private static String subjectsList = "";

    public HelpCommand() {
        super("help", new SyntaxChunk[0]);
        this.setDescription("Delivers help on certain subjects. Use &b-help subjects&r for a list.");
    }

    public void call(String[] args) {
        if (args[0] == null) {
            Command.sendStringChatMessage(new String[] { "KAMI b9", "commands&7 to view all available commands", "bind <module> <key>&7 to bind mods", "&7Press &r" + ModuleManager.getModuleByName("ClickGUI").getBindName() + "&7 to open GUI", "prefix <prefix>&r to change the command prefix.", "help <subjects:[subject]> &r for more help."});
        } else {
            String subject = args[0];

            if (subject.equals("subjects")) {
                Command.sendChatMessage("Subjects: " + HelpCommand.subjectsList);
            } else {
                HelpCommand.Subject subject1 = (HelpCommand.Subject) Arrays.stream(HelpCommand.subjects).filter(test<invokedynamic>(subject)).findFirst().orElse((Object) null);

                if (subject1 == null) {
                    Command.sendChatMessage("No help found for &b" + args[0]);
                    return;
                }

                Command.sendStringChatMessage(subject1.info);
            }
        }

    }

    private static boolean lambda$call$0(String subject, HelpCommand.Subject subject2) {
        String[] astring = subject2.names;
        int i = astring.length;

        for (int j = 0; j < i; ++j) {
            String name = astring[j];

            if (name.equalsIgnoreCase(subject)) {
                return true;
            }
        }

        return false;
    }

    static {
        HelpCommand.Subject[] ahelpcommand_subject = HelpCommand.subjects;
        int i = ahelpcommand_subject.length;

        for (int j = 0; j < i; ++j) {
            HelpCommand.Subject subject = ahelpcommand_subject[j];

            HelpCommand.subjectsList = HelpCommand.subjectsList + subject.names[0] + ", ";
        }

        HelpCommand.subjectsList = HelpCommand.subjectsList.substring(0, HelpCommand.subjectsList.length() - 2);
    }

    private static class Subject {

        String[] names;
        String[] info;

        public Subject(String[] names, String[] info) {
            this.names = names;
            this.info = info;
        }
    }
}
