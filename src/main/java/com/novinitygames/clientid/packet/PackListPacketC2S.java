package com.novinitygames.clientid.packet;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.novinitygames.clientid.ClientID;
import com.novinitygames.clientid.utils.CheckUtils;
import com.novinitygames.clientid.utils.ReadHelpers;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

public class PackListPacketC2S {
    public static void onPackListPacket(String channel, Player player, byte[] message) {
        if (!channel.equalsIgnoreCase(ClientID.NAMESPACE + ":packlist")) {
            return;
        }

        ByteArrayDataInput in = ByteStreams.newDataInput(message);

        int length = ReadHelpers.readVarInt(in);
        byte[] bytes = new byte[length];
        in.readFully(bytes);
        String s = new String(bytes, StandardCharsets.UTF_8);

        String[] packs = s.split(",");
        ClientID.getInstance().enabledPacks.put(player, Arrays.stream(packs).toList());

        ArrayList<String> bannedPacks = new ArrayList<>();

        ArrayList<String> keywordBans = new ArrayList<>() {{
            for (String s : ClientID.getInstance().getConfig().getStringList("keywordBans")) {
                add(s.toLowerCase());
            }
        }};
        ArrayList<String> blacklist = new ArrayList<>() {{
            for (String s : ClientID.getInstance().getConfig().getStringList("blacklist")) {
                add(s.toLowerCase());
            }
        }};
        ArrayList<String> whitelist = new ArrayList<>() {{
            for (String s : ClientID.getInstance().getConfig().getStringList("whitelist")) {
                add(s.toLowerCase());
            }
        }};

        ClientID.getInstance().getLogger().info(player.getName() + "'s Enabled Packs:");
        for (String pack : ClientID.getInstance().enabledPacks.get(player)) {
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
                ClientID.getInstance().getLogger().log(Level.SEVERE, "- " + pack);
            } else {
                ClientID.getInstance().getLogger().info("- " + pack);
            }
        }
        if (CheckUtils.canPlayerBypass(player)) return;

        if (!bannedPacks.isEmpty()) {
            StringBuilder kickMessage = new StringBuilder(ChatColor.RED + "Failed ClientID check.\nOne of your resource packs was deemed to be malicious.\n\n");
            kickMessage.append("\nIf this is believed to be an error, please contact the server owner.");
            player.kickPlayer(kickMessage.toString());
        }
    }
}
