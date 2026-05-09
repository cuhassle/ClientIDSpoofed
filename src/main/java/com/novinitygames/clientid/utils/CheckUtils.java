package com.novinitygames.clientid.utils;

import com.novinitygames.clientid.ClientID;
import com.novinitygames.clientid.config.ConfigManager;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;

public class CheckUtils {
    public static ArrayList<String> CheckIllicitMods(ServerPlayerEntity player) {
        ArrayList<String> bannedMods = new ArrayList<>();

        ArrayList<String> keywordBans = new ArrayList<>() {{
            for (String s : ConfigManager.CONFIG.keywordBans) {
                add(s.toLowerCase());
            }
        }};
        ArrayList<String> blacklist = new ArrayList<>() {{
            for (String s : ConfigManager.CONFIG.blacklist) {
                add(s.toLowerCase());
            }
        }};
        ArrayList<String> whitelist = new ArrayList<>() {{
            for (String s : ConfigManager.CONFIG.whitelist) {
                add(s.toLowerCase());
            }
        }};
        for (String mod : ClientID.modLists.get(player)) {
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
                ClientID.LOGGER.error("- " + mod);
            } else {
                ClientID.LOGGER.info("- " + mod);
            }
        }

        return bannedMods;
    }
}
