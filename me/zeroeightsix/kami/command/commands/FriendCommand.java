package me.zeroeightsix.kami.command.commands;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.util.UUIDTypeAdapter;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.function.Predicate;
import me.zeroeightsix.kami.command.Command;
import me.zeroeightsix.kami.command.syntax.ChunkBuilder;
import me.zeroeightsix.kami.command.syntax.parsers.EnumParser;
import me.zeroeightsix.kami.util.Friends;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;

public class FriendCommand extends Command {

    public FriendCommand() {
        super("friend", (new ChunkBuilder()).append("mode", true, new EnumParser(new String[] { "add", "del"})).append("name").build());
    }

    public void call(String[] args) {
        Friends friends;

        if (args[0] != null) {
            if (args[1] == null) {
                Command.sendChatMessage(String.format(Friends.isFriend(args[0]) ? "Yes, %s is your friend." : "No, %s isn\'t a friend of yours.", new Object[] { args[0]}));
                Command.sendChatMessage(String.format(Friends.isFriend(args[0]) ? "Yes, %s is your friend." : "No, %s isn\'t a friend of yours.", new Object[] { args[0]}));
            } else if (!args[0].equalsIgnoreCase("add") && !args[0].equalsIgnoreCase("new")) {
                if (!args[0].equalsIgnoreCase("del") && !args[0].equalsIgnoreCase("remove") && !args[0].equalsIgnoreCase("delete")) {
                    Command.sendChatMessage("Please specify either &6add&r or &6remove");
                } else if (!Friends.isFriend(args[1])) {
                    Command.sendChatMessage("That player isn\'t your friend.");
                } else {
                    friends = Friends.INSTANCE;
                    Friends.Friend friend2 = (Friends.Friend) ((ArrayList) Friends.friends.getValue()).stream().filter((friend1) -> {
                        return friend1.getUsername().equalsIgnoreCase(args[1]);
                    }).findFirst().get();

                    friends = Friends.INSTANCE;
                    ((ArrayList) Friends.friends.getValue()).remove(friend2);
                    Command.sendChatMessage("&b" + friend2.getUsername() + "&r has been unfriended.");
                }
            } else if (Friends.isFriend(args[1])) {
                Command.sendChatMessage("That player is already your friend.");
            } else {
                (new Thread(() -> {
                    Friends.Friend f = this.getFriendByName(args[1]);

                    if (f == null) {
                        Command.sendChatMessage("Failed to find UUID of " + args[1]);
                    } else {
                        Friends friends = Friends.INSTANCE;

                        ((ArrayList) Friends.friends.getValue()).add(f);
                        Command.sendChatMessage("&b" + f.getUsername() + "&r has been friended.");
                    }
                })).start();
            }
        } else {
            friends = Friends.INSTANCE;
            if (((ArrayList) Friends.friends.getValue()).isEmpty()) {
                Command.sendChatMessage("You currently don\'t have any friends added. &bfriend add <name>&r to add one.");
            } else {
                String friend = "";

                friends = Friends.INSTANCE;

                Friends.Friend friend1;

                for (Iterator iterator = ((ArrayList) Friends.friends.getValue()).iterator(); iterator.hasNext(); friend = friend + friend1.getUsername() + ", ") {
                    friend1 = (Friends.Friend) iterator.next();
                }

                friend = friend.substring(0, friend.length() - 2);
                Command.sendChatMessage("Your friends: " + friend);
            }
        }
    }

    private Friends.Friend getFriendByName(String input) {
        ArrayList infoMap = new ArrayList(Minecraft.getMinecraft().getConnection().getPlayerInfoMap());
        NetworkPlayerInfo profile = (NetworkPlayerInfo) infoMap.stream().filter((networkPlayerInfo) -> {
            return networkPlayerInfo.getGameProfile().getName().equalsIgnoreCase(input);
        }).findFirst().orElse((Object) null);

        if (profile != null) {
            Friends.Friend f1 = new Friends.Friend(profile.getGameProfile().getName(), profile.getGameProfile().getId());

            return f1;
        } else {
            Command.sendChatMessage("Player isn\'t online. Looking up UUID..");
            String f = requestIDs("[\"" + input + "\"]");

            if (f != null && !f.isEmpty()) {
                JsonElement element = (new JsonParser()).parse(f);

                if (element.getAsJsonArray().size() == 0) {
                    Command.sendChatMessage("Couldn\'t find player ID. (1)");
                } else {
                    try {
                        String e = element.getAsJsonArray().get(0).getAsJsonObject().get("id").getAsString();
                        String username = element.getAsJsonArray().get(0).getAsJsonObject().get("name").getAsString();
                        Friends.Friend friend = new Friends.Friend(username, UUIDTypeAdapter.fromString(e));

                        return friend;
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        Command.sendChatMessage("Couldn\'t find player ID. (2)");
                    }
                }
            } else {
                Command.sendChatMessage("Couldn\'t find player ID. Are you connected to the internet? (0)");
            }

            return null;
        }
    }

    private static String requestIDs(String data) {
        try {
            String e = "https://api.mojang.com/profiles/minecraft";
            URL url = new URL(e);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setConnectTimeout(5000);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            OutputStream os = conn.getOutputStream();

            os.write(data.getBytes("UTF-8"));
            os.close();
            BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
            String res = convertStreamToString(in);

            in.close();
            conn.disconnect();
            return res;
        } catch (Exception exception) {
            return null;
        }
    }

    private static String convertStreamToString(InputStream is) {
        Scanner s = (new Scanner(is)).useDelimiter("\\A");
        String r = s.hasNext() ? s.next() : "/";

        return r;
    }
}
