package com.novinitygames.clientid.client.watcher;

import com.novinitygames.clientid.ClientID;
import com.novinitygames.clientid.client.ClientIDClient;
import com.novinitygames.clientid.client.records.PackListC2SPayload;
import com.novinitygames.clientid.client.util.ListerUtil;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class ResourcePackWatcher implements SimpleSynchronousResourceReloadListener {
    private final Identifier id = Identifier.of(ClientID.MOD_ID, "resource_pack_watcher");
    private List<String> lastEnabled = new ArrayList<>();

    public ResourcePackWatcher() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(this);
        updateLastEnabled();
    }

    @Override
    public Identifier getFabricId() {
        return id;
    }

    @Override
    public void reload(ResourceManager manager) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) return;

        ArrayList<String> current = new ArrayList<>();
        for (ResourcePackProfile profile : client.getResourcePackManager().getEnabledProfiles()) {
            current.add(profile.getDisplayName().toString());
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
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) return;
        ArrayList<String> list = new ArrayList<>();
        for (ResourcePackProfile profile : client.getResourcePackManager().getEnabledProfiles()) {
            list.add(profile.getDisplayName().toString());
        }
        lastEnabled = list;
    }
}
