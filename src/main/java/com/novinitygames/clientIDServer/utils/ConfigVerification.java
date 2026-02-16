package com.novinitygames.clientIDServer.utils;

import com.novinitygames.clientIDServer.ClientIDServer;

public class ConfigVerification {
    public static void VerifyConfig() {
        ClientIDServer plugin = ClientIDServer.getInstance();
        if (plugin.getConfig().get("requireMod") == null) plugin.getConfig().set("requireMod", true);
        if (plugin.getConfig().get("keywordBans") == null) plugin.getConfig().set("keywordBans", new String[] {});
        if (plugin.getConfig().get("blacklist") == null) plugin.getConfig().set("blacklist", new String[] {});
        if (plugin.getConfig().get("whitelist") == null) plugin.getConfig().set("whitelist", new String[] {});
        if (plugin.getConfig().get("playerBypass") == null) plugin.getConfig().set("playerBypass", new String[] {});
        if (plugin.getConfig().get("reversePlayerBypass") == null) plugin.getConfig().set("reversePlayerBypass", false);
        if (plugin.getConfig().get("disablePieChart") == null) plugin.getConfig().set("disablePieChart", false);
        plugin.saveConfig();
        plugin.reloadConfig();
    }
}
