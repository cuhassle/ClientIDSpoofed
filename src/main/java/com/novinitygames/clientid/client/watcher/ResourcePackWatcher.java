package com.novinitygames.clientid.client.watcher;

import com.novinitygames.clientid.ClientID;
import com.novinitygames.clientid.client.ClientIDClient;
import com.novinitygames.clientid.records.PackListC2SPayload;
import com.novinitygames.clientid.client.util.ListerUtil;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.resources.ResourceManager;

import java.util.ArrayList;
import java.util.List;

public class ResourcePackWatcher implements SimpleSynchronousResourceReloadListener {
    private final Identifier id = Identifier.fromNamespaceAndPath(ClientID.MOD_ID, "resource_pack_watcher");
    private List<String> lastEnabled = new ArrayList<>();

    public ResourcePackWatcher() {
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(this);
        updateLastEnabled();
    }

    @Override
    public Identifier getFabricId() {
        return id;
    }

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        ArrayList<String> current = new ArrayList<>();
        for (PackResources profile : manager.listPacks().toList()) {
            current.add(profile.packId());
        }

        List<String> added = new ArrayList<>(current);
        added.removeAll(lastEnabled);

        List<String> removed = new ArrayList<>(lastEnabled);
        removed.removeAll(current);

        if (!added.isEmpty() || !removed.isEmpty()) {
            if (ClientIDClient.isConnectedToServer) {
                String enabledPacks = String.join(",", ListerUtil.getEnabledPacks());
                PackListC2SPayload payload = new PackListC2SPayload(enabledPacks);
                ClientPlayNetworking.send(payload);
            }
        }

        lastEnabled = current;
    }

    private void updateLastEnabled() {
        Minecraft client = Minecraft.getInstance();

        ArrayList<String> list = new ArrayList<>();
        for (Pack profile : client.getResourcePackRepository().getSelectedPacks()) {
            if (profile.getTitle().getString().startsWith("Fabric Mod \"")) continue;
            list.add(profile.getTitle().getString());
        }
        lastEnabled = list;
    }
}
