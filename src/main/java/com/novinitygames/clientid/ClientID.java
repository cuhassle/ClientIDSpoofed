package com.novinitygames.clientid;

import com.novinitygames.clientid.client.records.*;
import com.novinitygames.clientid.commands.ModCommandManager;
import com.novinitygames.clientid.config.ConfigManager;
import com.novinitygames.clientid.listener.ConnectionListeners;
import com.novinitygames.clientid.listener.InteractionListeners;
import com.novinitygames.clientid.listener.PacketListeners;
import com.novinitygames.clientid.utils.SimpleServerScheduler;
import com.novinitygames.clientid.utils.UpdateChecker;
import com.novinitygames.clientid.utils.Version;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.network.ServerPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClientID implements ModInitializer {
    public static final String MOD_ID = "clientid";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static HashMap<ServerPlayerEntity, List<String>> modLists = new HashMap<>();
    public static HashMap<ServerPlayerEntity, List<String>> packLists = new HashMap<>();
    public static HashMap<ServerPlayerEntity, Boolean> versionConfirmed = new HashMap<>();
    public static HashMap<ServerPlayerEntity, Boolean> modConfirmed = new HashMap<>();
    public static List<ServerPlayerEntity> accepted = new ArrayList<>();

    public static final Version clientMinimumVersion = new Version(1, 1, 1);

    @Override
    public void onInitialize() {
        ConfigManager.load();
        SimpleServerScheduler.init();

        ModCommandManager commandManager = new ModCommandManager();
        commandManager.Init();

        PayloadTypeRegistry.playC2S().register(ModCheckC2SPayload.ID, ModCheckC2SPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(ModListC2SPayload.ID, ModListC2SPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(PackListC2SPayload.ID, PackListC2SPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(VersionC2SPayload.ID, VersionC2SPayload.CODEC);

        PayloadTypeRegistry.playS2C().register(ChartsS2CPayload.ID, ChartsS2CPayload.CODEC);

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
            ConnectionListeners.Register();
            PacketListeners.Register();
            InteractionListeners.Register();

            UpdateChecker.checkForUpdates();
        }
    }

    public static boolean isPlayerBypassed(ServerPlayerEntity player) {
        return (ConfigManager.CONFIG.playerBypass.contains(player.getName().getLiteralString()) && !ConfigManager.CONFIG.reversePlayerBypass)
                || (!ConfigManager.CONFIG.playerBypass.contains(player.getName().getLiteralString()) && ConfigManager.CONFIG.reversePlayerBypass);
    }
}
