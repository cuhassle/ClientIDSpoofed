package com.novinitygames.clientid.client.util;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;

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
        Minecraft client = Minecraft.getInstance();

        PackRepository rpm = client.getResourcePackRepository();
        Collection<Pack> enabled = rpm.getSelectedPacks();

        ArrayList<String> list = new ArrayList<>();

        for (Pack profile : enabled) {
            Component display = profile.getTitle();
            list.add(display.getString());
        }

        return list;
    }
}
