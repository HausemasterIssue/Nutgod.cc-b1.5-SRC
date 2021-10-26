package me.zeroeightsix.kami.util;

import com.google.common.base.Converter;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import java.util.regex.Pattern;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;

public class Friends {

    public static final Friends INSTANCE = new Friends();
    public static Setting friends;

    public static void initFriends() {
        Friends.friends = Settings.custom("Friends", new ArrayList(), new Friends.FriendListConverter()).buildAndRegister("friends");
    }

    public static boolean isFriend(String name) {
        return ((ArrayList) Friends.friends.getValue()).stream().anyMatch(test<invokedynamic>(name));
    }

    private static boolean lambda$isFriend$0(String name, Friends.Friend friend) {
        return friend.username.equalsIgnoreCase(name);
    }

    public static class FriendListConverter extends Converter {

        protected JsonElement doForward(ArrayList list) {
            StringBuilder present = new StringBuilder();
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                Friends.Friend friend = (Friends.Friend) iterator.next();

                present.append(String.format("%s;%s$", new Object[] { friend.username, friend.uuid.toString()}));
            }

            return new JsonPrimitive(present.toString());
        }

        protected ArrayList doBackward(JsonElement jsonElement) {
            String v = jsonElement.getAsString();
            String[] pairs = v.split(Pattern.quote("$"));
            ArrayList friends = new ArrayList();
            String[] astring = pairs;
            int i = pairs.length;

            for (int j = 0; j < i; ++j) {
                String pair = astring[j];

                try {
                    String[] split = pair.split(";");
                    String username = split[0];
                    UUID uuid = UUID.fromString(split[1]);

                    friends.add(new Friends.Friend(this.getUsernameByUUID(uuid, username), uuid));
                } catch (Exception exception) {
                    ;
                }
            }

            return friends;
        }

        private String getUsernameByUUID(UUID uuid, String saved) {
            String src = getSource("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString());

            if (src != null && !src.isEmpty()) {
                try {
                    JsonElement e = (new JsonParser()).parse(src);

                    return e.getAsJsonObject().get("name").getAsString();
                } catch (Exception exception) {
                    exception.printStackTrace();
                    System.err.println(src);
                    return saved;
                }
            } else {
                return saved;
            }
        }

        private static String getSource(String link) {
            try {
                URL e = new URL(link);
                URLConnection con = e.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder buffer = new StringBuilder();

                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    buffer.append(inputLine);
                }

                in.close();
                return buffer.toString();
            } catch (Exception exception) {
                return null;
            }
        }
    }

    public static class Friend {

        String username;
        UUID uuid;

        public Friend(String username, UUID uuid) {
            this.username = username;
            this.uuid = uuid;
        }

        public String getUsername() {
            return this.username;
        }
    }
}
