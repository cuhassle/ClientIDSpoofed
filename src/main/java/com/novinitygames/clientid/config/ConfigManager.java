package com.novinitygames.clientid.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.novinitygames.clientid.ClientID;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("clientid.json");

    public static ModConfig CONFIG;

    public static void load() {
        try {
            if (Files.exists(CONFIG_PATH)) {
                CONFIG = GSON.fromJson(Files.readString(CONFIG_PATH), ModConfig.class);
            }  else {
                CONFIG = new ModConfig();
                save();
            }
        } catch (IOException e) {
            ClientID.LOGGER.error("Failed to load config!", e);
        }
    }

    public static void save() {
        try {
            Files.writeString(CONFIG_PATH, GSON.toJson(CONFIG));
        } catch (IOException e) {
            ClientID.LOGGER.error("Failed to save config!", e);
        }
    }
}
