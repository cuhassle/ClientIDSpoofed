package com.novinitygames.clientidClient;

import com.novinitygames.clientidClient.client.records.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class ClientidClient implements ModInitializer {
    public static final String MOD_ID = "clientid";

    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playC2S().register(ModCheckC2SPayload.ID, ModCheckC2SPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(ModListC2SPayload.ID, ModListC2SPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(PackListC2SPayload.ID, PackListC2SPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(VersionC2SPayload.ID, VersionC2SPayload.CODEC);

        PayloadTypeRegistry.playS2C().register(ChartsS2CPayload.ID, ChartsS2CPayload.CODEC);
    }
}
