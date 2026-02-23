package com.novinitygames.clientid.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.novinitygames.clientid.ClientID;
import com.novinitygames.clientid.Properties;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UpdateChecker {
    private static final String PROJECT_ID = "client-id";
    private static final String CURRENT_VERSION = Properties.getProperty("version");
    private static final String API_URL = "https://api.modrinth.com/v2";

    public static boolean updateAvailable = false;
    public static List<Player> playersNotified = new ArrayList<>();

    public static void checkForUpdates() {
        ClientID.getInstance().scheduler.async().runNow (() -> {
            try {
                URL url = new URL(API_URL + "/project/" + PROJECT_ID + "/version");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("User-Agent", "ClientID/" + CURRENT_VERSION);

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JsonArray versions = new Gson().fromJson(response.toString(), JsonArray.class);

                if (!versions.isEmpty()) {
                    String latestVersionNumber = "";
                    for (int i = 0; i < versions.size(); i++) {
                        JsonObject v = versions.get(i).getAsJsonObject();
                        for (JsonElement element : v.get("loaders").getAsJsonArray()) {
                            if (element.getAsString().equalsIgnoreCase("spigot")) {
                                latestVersionNumber = v.get("version_number").getAsString();
                                break;
                            }
                        }
                        if (!latestVersionNumber.isEmpty()) break;
                    }

                    if (!latestVersionNumber.isEmpty() && !latestVersionNumber.equals(CURRENT_VERSION)) {
                        Version newVersion = Version.fromString(latestVersionNumber);
                        Version currentVersion = Version.fromString(CURRENT_VERSION);
                        if (newVersion == null || currentVersion == null) return;

                        if (newVersion.compareTo(currentVersion) > 0) {
                            updateAvailable = true;
                            TextComponent component = new TextComponent(TextComponent.fromLegacy(ChatColor.GREEN + "[ClientID] A new update is available! Get it at https://modrinth.com/plugin/client-id"));
                            Bukkit.getConsoleSender().spigot().sendMessage(component);
                        } else {
                            ClientID.getInstance().getLogger().info("No updates available.");
                        }
                    }
                }
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
