package com.novinitygames.clientid.client.util;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Collection;

public class ListerUtil {
    public static ArrayList<String> getMods() {
        Collection<ModContainer> mods = FabricLoader.getInstance().getAllMods();
        ArrayList<String> list = new ArrayList<>();
        mods.forEach(mod -> {
            ModMetadata meta = mod.getMetadata();
            String id = meta.getId();
            list.add(id);
        });
        return list;
    }

    public static ArrayList<String> getEnabledPacks() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) return null;

        ResourcePackManager rpm = client.getResourcePackManager();
        Collection<ResourcePackProfile> enabled = rpm.getEnabledProfiles();

        ArrayList<String> list = new ArrayList<>();

        for (ResourcePackProfile profile : enabled) {
            Text display = profile.getDisplayName();
            String name = display == null ? "<unknown>" : display.getLiteralString();
            if (name == null) {
                continue;
            }
            list.add(name);
        }

        return list;
    }
}
