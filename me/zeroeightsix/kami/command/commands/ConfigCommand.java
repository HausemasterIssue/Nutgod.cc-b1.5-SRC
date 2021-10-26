package me.zeroeightsix.kami.command.commands;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import me.zeroeightsix.kami.KamiMod;
import me.zeroeightsix.kami.command.Command;
import me.zeroeightsix.kami.command.syntax.ChunkBuilder;
import me.zeroeightsix.kami.command.syntax.parsers.DependantParser;
import me.zeroeightsix.kami.command.syntax.parsers.EnumParser;
import me.zeroeightsix.kami.gui.kami.KamiGUI;

public class ConfigCommand extends Command {

    public ConfigCommand() {
        super("config", (new ChunkBuilder()).append("mode", true, new EnumParser(new String[] { "reload", "save", "path"})).append("path", true, new DependantParser(0, new DependantParser.Dependency(new String[][] { { "path", "path"}}, ""))).build());
    }

    public void call(String[] args) {
        if (args[0] == null) {
            Command.sendChatMessage("Missing argument &bmode&r: Choose from reload, save or path");
        } else {
            String s = args[0].toLowerCase();
            byte b0 = -1;

            switch (s.hashCode()) {
            case -934641255:
                if (s.equals("reload")) {
                    b0 = 0;
                }
                break;

            case 3433509:
                if (s.equals("path")) {
                    b0 = 2;
                }
                break;

            case 3522941:
                if (s.equals("save")) {
                    b0 = 1;
                }
            }

            switch (b0) {
            case 0:
                this.reload();
                break;

            case 1:
                try {
                    KamiMod.saveConfigurationUnsafe();
                    Command.sendChatMessage("Saved configuration!");
                } catch (IOException ioexception) {
                    ioexception.printStackTrace();
                    Command.sendChatMessage("Failed to save! " + ioexception.getMessage());
                }
                break;

            case 2:
                if (args[1] == null) {
                    Path newPath = Paths.get(KamiMod.getConfigName(), new String[0]);

                    Command.sendChatMessage("Path to configuration: &b" + newPath.toAbsolutePath().toString());
                } else {
                    String newPath1 = args[1];

                    if (!KamiMod.isFilenameValid(newPath1)) {
                        Command.sendChatMessage("&b" + newPath1 + "&r is not a valid path");
                    } else {
                        try {
                            BufferedWriter e = Files.newBufferedWriter(Paths.get("KAMILastConfig.txt", new String[0]), new OpenOption[0]);
                            Throwable throwable = null;

                            try {
                                e.write(newPath1);
                                this.reload();
                                Command.sendChatMessage("Configuration path set to &b" + newPath1 + "&r!");
                            } catch (Throwable throwable1) {
                                throwable = throwable1;
                                throw throwable1;
                            } finally {
                                if (e != null) {
                                    if (throwable != null) {
                                        try {
                                            e.close();
                                        } catch (Throwable throwable2) {
                                            throwable.addSuppressed(throwable2);
                                        }
                                    } else {
                                        e.close();
                                    }
                                }

                            }
                        } catch (IOException ioexception1) {
                            ioexception1.printStackTrace();
                            Command.sendChatMessage("Couldn\'t set path: " + ioexception1.getMessage());
                        }
                    }
                }
                break;

            default:
                Command.sendChatMessage("Incorrect mode, please choose from: reload, save or path");
            }

        }
    }

    private void reload() {
        KamiMod.getInstance().guiManager = new KamiGUI();
        KamiMod.getInstance().guiManager.initializeGUI();
        KamiMod.loadConfiguration();
        Command.sendChatMessage("Configuration reloaded!");
    }
}
