package com.novinitygames.clientIDServer.utils;

import com.novinitygames.clientIDServer.ClientIDServer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.logging.Level;

public class CheckUtils {
    public static ArrayList<String> CheckIllicitMods(Player player) {
        ArrayList<String> bannedMods = new ArrayList<>();

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
        for (String mod : ClientIDServer.getInstance().installedMods.get(player)) {
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
                ClientIDServer.getInstance().getLogger().log(Level.SEVERE, "- " + mod);
            } else {
                ClientIDServer.getInstance().getLogger().info("- " + mod);
            }
        }

        return bannedMods;
    }
}
