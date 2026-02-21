package com.novinitygames.clientid.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.novinitygames.clientid.ClientID;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.SERVER)
public class UpdateChecker {
    private static final String PROJECT_ID = "client-id";
    private static String CURRENT_VERSION;
    private static final String API_URL = "https://api.modrinth.com/v2";

    public static boolean updateAvailable = false;
    public static List<ServerPlayerEntity> playersNotified = new ArrayList<>();

    public static void checkForUpdates() {
        try {
            FabricLoader.getInstance().getModContainer("clientid").ifPresent(modContainer -> {
                CURRENT_VERSION = modContainer.getMetadata().getVersion().getFriendlyString();
            });
            if (CURRENT_VERSION == null) {
                return;
            }

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
                        if (element.getAsString().equalsIgnoreCase("fabric")) {
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
                        Text linkText = Text.literal("[ClientID] A new update is available! Get it at https://modrinth.com/plugin/client-id")
                                .withColor(0x00FF00)
                                .styled(style -> style.withClickEvent(() -> ClickEvent.Action.OPEN_URL));
                        ClientID.LOGGER.info(linkText.toString());
                    } else {
                        ClientID.LOGGER.info("No updates available.");
                    }
                }
            }
        } catch (Exception e) {
            ClientID.LOGGER.warn("Failed to check for updates!", e);
        }
    }
}
