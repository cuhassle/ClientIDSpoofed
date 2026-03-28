package com.novinitygames.clientid.utils;

import com.novinitygames.clientid.ClientID;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class CheckUtils {
    public static ArrayList<String> CheckIllicitMods(Player player) {
        ArrayList<String> bannedMods = new ArrayList<>();

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
        for (String mod : ClientID.getInstance().installedMods.get(player)) {
            String lowercase = mod.toLowerCase();
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
                bannedMods.add(lowercase);
                ClientID.getInstance().getLogger().log(Level.SEVERE, "- " + mod);
            } else {
                ClientID.getInstance().getLogger().info("- " + mod);
            }
        }

        return bannedMods;
    }

    public static boolean canPlayerBypass(Player player) {
        List<String> bypassedPlayers = ClientID.getInstance().getConfig().getStringList("playerBypass");
        boolean reverse = ClientID.getInstance().getConfig().getBoolean("reversePlayerBypass", false);
        for (String s : bypassedPlayers) {
            if (player.getName().equalsIgnoreCase(s)) {
                return !reverse;
            }
        }
        return reverse;
    }
}
