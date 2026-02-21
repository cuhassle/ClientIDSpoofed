package com.novinitygames.clientid.config;

import java.util.ArrayList;

public class ModConfig {
    public boolean requireMod = true;
    public ArrayList<String> keywordBans = new ArrayList<>() {{
        add("xray");
        add("x-ray");
        add("freecam");
    }};
    public ArrayList<String> blacklist = new ArrayList<>() {{
        add("wurst");
    }};
    public ArrayList<String> whitelist = new ArrayList<>() {{
        add("legacy-freecam");
    }};
    public ArrayList<String> playerBypass = new ArrayList<>() {{
        add("User1");
        add("User2");
    }};
    public boolean reversePlayerBypass = false;
    public boolean disablePieChart = false;
}
