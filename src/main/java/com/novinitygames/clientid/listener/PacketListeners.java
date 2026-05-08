package com.novinitygames.clientid.listener;

import com.novinitygames.clientid.ClientID;
import com.novinitygames.clientid.client.records.*;
import com.novinitygames.clientid.config.ConfigManager;
import com.novinitygames.clientid.utils.CheckUtils;
import com.novinitygames.clientid.utils.Version;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Arrays;

@Environment(EnvType.SERVER)
public class PacketListeners {
    public static void Register() {
        ServerPlayNetworking.registerGlobalReceiver(ModCheckC2SPayload.ID, (payload, ctx) -> {
            ServerPlayerEntity player = ctx.player();
            ClientID.modConfirmed.put(player, true);
        });

        ServerPlayNetworking.registerGlobalReceiver(ModListC2SPayload.ID, (payload, ctx) -> {
            ServerPlayerEntity player = ctx.player();

            String s = payload.list();

            if (s == null || s.isEmpty()) return;

            String[] mods = s.split(",");

            if (mods.length < 5) return;
            boolean f = false;
            for (String mod : mods) {
                if (mod.equals("clientid")) {
                    f = true;
                    break;
                }
            }
            if (!f) return;

            ClientID.modLists.put(player, Arrays.stream(mods).toList());

            if (ClientID.isPlayerBypassed(player)) return;

            ArrayList<String> bannedMods = CheckUtils.CheckIllicitMods(player);

            if (!bannedMods.isEmpty()) {
                StringBuilder kickMessage = new StringBuilder("Failed ClientID check.\nThe following mods must be removed to play:\n\n");
                for (String mod : bannedMods) {
                    kickMessage.append(mod).append("\n");
                }
                kickMessage.append("\nIf this is believed to be an error, please contact the server owner.");
                player.networkHandler.disconnect(Text.literal(kickMessage.toString()).withColor(0xFF5555));
            }
        });

        ServerPlayNetworking.registerGlobalReceiver(PackListC2SPayload.ID, (payload, ctx) -> {
            ServerPlayerEntity player = ctx.player();

            String s = payload.list();

            String[] packs = s.split(",");
            ClientID.packLists.put(player, Arrays.stream(packs).toList());

            ArrayList<String> bannedPacks = new ArrayList<>();

            ArrayList<String> keywordBans = new ArrayList<>() {{
                for (String s : ConfigManager.CONFIG.keywordBans) {
                    add(s.toLowerCase());
                }
            }};
            ArrayList<String> blacklist = new ArrayList<>() {{
                for (String s : ConfigManager.CONFIG.blacklist) {
                    add(s.toLowerCase());
                }
            }};
            ArrayList<String> whitelist = new ArrayList<>() {{
                for (String s : ConfigManager.CONFIG.whitelist) {
                    add(s.toLowerCase());
                }
            }};

            ClientID.LOGGER.info(player.getName() + "'s Enabled Packs:");
            for (String pack : ClientID.packLists.get(player)) {
                String lowercase = pack.toLowerCase();
                boolean illicit = false;
                if (!whitelist.contains(lowercase)) {
                    if (blacklist.contains(lowercase)) {
                        illicit = true;
                    } else {
                        for (String st : keywordBans) {
                            if (lowercase.contains(st)) {
                                illicit = true;
                            }
                        }
                    }
                }
                if (illicit) {
                    bannedPacks.add(lowercase);
                    ClientID.LOGGER.error("- " + pack);
                } else {
                    ClientID.LOGGER.info("- " + pack);
                }
            }

            if (ClientID.isPlayerBypassed(player)) return;

            if (!bannedPacks.isEmpty()) {
                StringBuilder kickMessage = new StringBuilder("Failed ClientID check.\nOne of your resource packs was deemed to be malicious.\n\n");
                kickMessage.append("\nIf this is believed to be an error, please contact the server owner.");

                player.networkHandler.disconnect(Text.literal(kickMessage.toString()).withColor(0xFF5555));
            }
        });

        ServerPlayNetworking.registerGlobalReceiver(VersionC2SPayload.ID, (payload, ctx) -> {
            ServerPlayerEntity player = ctx.player();

            if (ClientID.isPlayerBypassed(player)) return;

            String s = payload.version();
            try {
                Version version = Version.fromString(s);
                if (version == null) {
                    throw new NullPointerException("Version failed to convert.");
                }

                if (ClientID.clientMinimumVersion.compareTo(version) > 0) {
                    player.networkHandler.disconnect(Text.literal("The minimum version of ClientID required to join this server is "
                            + ClientID.clientMinimumVersion.toString()).withColor(0xFF5555));
                } else {
                    ClientID.versionConfirmed.put(player, true);
                }
            } catch (Exception e) {
                player.networkHandler.disconnect(Text.literal("Failed to receive ClientID version.").withColor(0xFF5555));
            }
        });
    }
}
