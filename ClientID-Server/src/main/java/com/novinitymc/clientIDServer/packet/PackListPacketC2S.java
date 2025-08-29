package com.novinitymc.clientIDServer.packet;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.novinitymc.clientIDServer.ClientIDServer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

public class PackListPacketC2S {
    public static void onPackListPacket(String channel, Player player, byte[] message) {
        if (!channel.equalsIgnoreCase(ClientIDServer.NAMESPACE + ":packlist")) {
            return;
        }

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String s = in.readLine();

        String[] packs = s.split(",");
        ClientIDServer.getInstance().enabledPacks.put(player, Arrays.stream(packs).toList());

        ArrayList<String> bannedPacks = new ArrayList<>();

        ArrayList<String> keywordBans = new ArrayList<>() {{
            for (String s : ClientIDServer.getInstance().getConfig().getStringList("keywordBans")) {
                add(s.toLowerCase());
            }
        }};
        ArrayList<String> blacklist = new ArrayList<>() {{
            for (String s : ClientIDServer.getInstance().getConfig().getStringList("blacklist")) {
                add(s.toLowerCase());
            }
        }};
        ArrayList<String> whitelist = new ArrayList<>() {{
            for (String s : ClientIDServer.getInstance().getConfig().getStringList("whitelist")) {
                add(s.toLowerCase());
            }
        }};

        ClientIDServer.getInstance().getLogger().info(player.getName() + "'s Enabled Packs:");
        for (String pack : ClientIDServer.getInstance().enabledPacks.get(player)) {
            String lowercase = pack.toLowerCase();
            boolean illicit = false;
            if (!whitelist.contains(lowercase)) {
                if (blacklist.contains(lowercase)) {
                    illicit = true;
                } else {
                    for (String st : keywordBans) {
                        if (lowercase.contains(st)) {
                            illicit = true;
                        }
                    }
                }
            }
            if (illicit) {
                bannedPacks.add(lowercase);
                ClientIDServer.getInstance().getLogger().log(Level.SEVERE, "- " + pack);
            } else {
                ClientIDServer.getInstance().getLogger().info("- " + pack);
            }
        }

        if (!bannedPacks.isEmpty()) {
            StringBuilder kickMessage = new StringBuilder(ChatColor.RED + "Failed ClientID check.\nOne of your resource packs was deemed to be malicious.\n\n");
            kickMessage.append("\nIf this is believed to be an error, please contact Novinity.");
            player.kickPlayer(kickMessage.toString());
        }
    }
}
