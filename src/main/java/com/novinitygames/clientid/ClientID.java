package com.novinitygames.clientid;

import com.novinitygames.clientid.records.*;
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
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClientID implements ModInitializer {
    public static final String MOD_ID = "clientid";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static HashMap<ServerPlayer, List<String>> modLists = new HashMap<>();
    public static HashMap<ServerPlayer, List<String>> packLists = new HashMap<>();
    public static HashMap<ServerPlayer, Boolean> versionConfirmed = new HashMap<>();
    public static HashMap<ServerPlayer, Boolean> modConfirmed = new HashMap<>();
    public static List<ServerPlayer> accepted = new ArrayList<>();

    public static final Version clientMinimumVersion = new Version(1, 1, 1);

    @Override
    public void onInitialize() {
        ConfigManager.load();
        SimpleServerScheduler.init();

        ModCommandManager commandManager = new ModCommandManager();
        commandManager.Init();

        PayloadTypeRegistry.serverboundPlay().register(ModCheckC2SPayload.ID, ModCheckC2SPayload.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(ModListC2SPayload.ID, ModListC2SPayload.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(PackListC2SPayload.ID, PackListC2SPayload.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(VersionC2SPayload.ID, VersionC2SPayload.CODEC);

        PayloadTypeRegistry.clientboundPlay().register(ChartsS2CPayload.ID, ChartsS2CPayload.CODEC);

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
            ConnectionListeners.Register();
            PacketListeners.Register();
            InteractionListeners.Register();

            UpdateChecker.checkForUpdates();
        }
    }
}
