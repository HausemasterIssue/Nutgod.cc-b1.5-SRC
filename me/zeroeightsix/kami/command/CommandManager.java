package me.zeroeightsix.kami.command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import me.zeroeightsix.kami.KamiMod;
import me.zeroeightsix.kami.command.commands.BindCommand;
import me.zeroeightsix.kami.util.ClassFinder;

public class CommandManager {

    private ArrayList commands = new ArrayList();

    public CommandManager() {
        Set classList = ClassFinder.findClasses(BindCommand.class.getPackage().getName(), Command.class);
        Iterator iterator = classList.iterator();

        while (iterator.hasNext()) {
            Class s = (Class) iterator.next();

            if (Command.class.isAssignableFrom(s)) {
                try {
                    Command e = (Command) s.getConstructor(new Class[0]).newInstance(new Object[0]);

                    this.commands.add(e);
                } catch (Exception exception) {
                    exception.printStackTrace();
                    System.err.println("Couldn\'t initiate command " + s.getSimpleName() + "! Err: " + exception.getClass().getSimpleName() + ", message: " + exception.getMessage());
                }
            }
        }

        KamiMod.log.info("Commands initialised");
    }

    public void callCommand(String command) {
        String[] parts = command.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        String label = parts[0].substring(1);
        String[] args = removeElement(parts, 0);

        for (int i = 0; i < args.length; ++i) {
            if (args[i] != null) {
                args[i] = strip(args[i], "\"");
            }
        }

        Iterator iterator = this.commands.iterator();

        Command c;

        do {
            if (!iterator.hasNext()) {
                Command.sendChatMessage("Unknown command. try \'commands\' for a list of commands.");
                return;
            }

            c = (Command) iterator.next();
        } while (!c.getLabel().equalsIgnoreCase(label));

        c.call(parts);
    }

    public static String[] removeElement(String[] input, int indexToDelete) {
        LinkedList result = new LinkedList();

        for (int i = 0; i < input.length; ++i) {
            if (i != indexToDelete) {
                result.add(input[i]);
            }
        }

        return (String[]) ((String[]) result.toArray(input));
    }

    private static String strip(String str, String key) {
        return str.startsWith(key) && str.endsWith(key) ? str.substring(key.length(), str.length() - key.length()) : str;
    }

    public Command getCommandByLabel(String commandLabel) {
        Iterator iterator = this.commands.iterator();

        Command c;

        do {
            if (!iterator.hasNext()) {
                return null;
            }

            c = (Command) iterator.next();
        } while (!c.getLabel().equals(commandLabel));

        return c;
    }

    public ArrayList getCommands() {
        return this.commands;
    }
}
