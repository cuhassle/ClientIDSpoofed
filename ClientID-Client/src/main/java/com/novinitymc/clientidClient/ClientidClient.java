package com.novinitymc.clientidClient;

import com.novinitymc.clientidClient.client.records.ModCheckC2SPayload;
import com.novinitymc.clientidClient.client.records.ModListC2SPayload;
import com.novinitymc.clientidClient.client.records.PackListC2SPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class ClientidClient implements ModInitializer {
    public static final String MOD_ID = "clientid";

    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playC2S().register(ModCheckC2SPayload.ID, ModCheckC2SPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(ModListC2SPayload.ID, ModListC2SPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(PackListC2SPayload.ID, PackListC2SPayload.CODEC);
    }
}
