package com.novinitygames.clientidClient.client;

import com.novinitygames.clientidClient.client.records.ModCheckC2SPayload;
import com.novinitygames.clientidClient.client.records.ModListC2SPayload;
import com.novinitygames.clientidClient.client.records.PackListC2SPayload;
import com.novinitygames.clientidClient.client.util.ListerUtil;
import com.novinitygames.clientidClient.client.watcher.ResourcePackWatcher;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;

import java.util.List;

public class ClientidClientClient implements ClientModInitializer {
    ResourcePackWatcher watcher;

    public static Boolean isConnectedToServer = false;

    @Override
    public void onInitializeClient() {
        watcher = new ResourcePackWatcher();
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            isConnectedToServer = true;

            // Initial installation confirmation
            ModCheckC2SPayload payload = new ModCheckC2SPayload(MinecraftClient.getInstance().getGameProfile().getId().toString());
            ClientPlayNetworking.send(payload);

            // Mods
            List<String> installedModsList = ListerUtil.getMods();
            String installedMods = String.join(",", installedModsList);

            ModListC2SPayload payload2 = new ModListC2SPayload(installedMods);
            ClientPlayNetworking.send(payload2);

            // Resource packs
            List<String> enabledPacksList = ListerUtil.getEnabledPacks();
            String enabledPacks = String.join(",", enabledPacksList);

            PackListC2SPayload payload3 = new PackListC2SPayload(enabledPacks);
            ClientPlayNetworking.send(payload3);
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, sender) -> {
            isConnectedToServer = false;
        });
    }
}
