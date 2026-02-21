package com.novinitygames.clientid.client;

import com.novinitygames.clientid.client.records.*;
import com.novinitygames.clientid.client.util.ListerUtil;
import com.novinitygames.clientid.client.watcher.ResourcePackWatcher;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;

import java.util.List;

public class ClientIDClient implements ClientModInitializer {
    ResourcePackWatcher watcher;

    public static Boolean isConnectedToServer = false;
    public static Boolean pieChartDisabled = false;

    @Override
    public void onInitializeClient() {
        watcher = new ResourcePackWatcher();
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            isConnectedToServer = true;

            // Initial installation confirmation
            ModCheckC2SPayload payload = new ModCheckC2SPayload(MinecraftClient.getInstance().getGameProfile().id().toString());
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

            // Version
            FabricLoader.getInstance().getModContainer("clientid").ifPresent(modContainer -> {
                String version = modContainer.getMetadata().getVersion().getFriendlyString();
                VersionC2SPayload payload4 = new VersionC2SPayload(version);
                ClientPlayNetworking.send(payload4);
            });
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, sender) -> {
            isConnectedToServer = false;
            pieChartDisabled = false;
        });

        ClientPlayNetworking.registerGlobalReceiver(ChartsS2CPayload.ID, (payload, ctx) -> {
            pieChartDisabled = payload.val();
        });
    }
}
