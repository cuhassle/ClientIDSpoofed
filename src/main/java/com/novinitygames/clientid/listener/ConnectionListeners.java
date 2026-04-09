package com.novinitygames.clientid.listener;

import com.novinitygames.clientid.ClientID;
import com.novinitygames.clientid.records.ChartsS2CPayload;
import com.novinitygames.clientid.config.ConfigManager;
import com.novinitygames.clientid.utils.SimpleServerScheduler;
import com.novinitygames.clientid.utils.UpdateChecker;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;

@Environment(EnvType.SERVER)
public class ConnectionListeners {
    public static void Register() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayer player = handler.getPlayer();
            if ((ConfigManager.CONFIG.playerBypass.contains(player.getName().getString()) && !ConfigManager.CONFIG.reversePlayerBypass)
                || (!ConfigManager.CONFIG.playerBypass.contains(player.getName().getString()) && ConfigManager.CONFIG.reversePlayerBypass)) {
                ClientID.accepted.add(player);
                return;
            }
            if (ConfigManager.CONFIG.requireMod) {
                SimpleServerScheduler.schedule(server, () -> {
                    if (ClientID.accepted.contains(player)) return;

                    if (ClientID.modLists.containsKey(player)
                            && ClientID.packLists.containsKey(player)
                            && ClientID.versionConfirmed.containsKey(player)
                            && ClientID.modConfirmed.containsKey(player)
                            && !ClientID.accepted.contains(player)) {
                        ClientID.accepted.add(player);
                    } else {
                        player.connection.disconnect(Component.literal("Failed ClientID check.\nThis happened because of one of the following reasons:\n\n- You don't have the ClientID mod installed\n- Your latenyc was too high to callback in a timely fashion\n- You are using an unsupported version of the mod\n\nIf this is believed to be an error, please contact the server owner.")
                                .withColor(0xFF5555));
                    }
                }, 5*20L);
            } else {
                ClientID.accepted.add(player);
            }
            for (int i = 0; i < (5*20); i++) {
                SimpleServerScheduler.schedule(server, () -> {
                    if (ClientID.modLists.containsKey(player)
                            && ClientID.packLists.containsKey(player)
                            && ClientID.versionConfirmed.containsKey(player)
                            && ClientID.modConfirmed.containsKey(player)
                            && !ClientID.accepted.contains(player)) {
                        ClientID.accepted.add(player);

                        // Send pie chart disable packet
                        ChartsS2CPayload pieChartPayload = new ChartsS2CPayload(ConfigManager.CONFIG.disablePieChart);
                        ServerPlayNetworking.send(player, pieChartPayload);
                    }
                }, (long)i);
            }
            if (UpdateChecker.updateAvailable && Permissions.check(player, "clientid.updatecheck") && !UpdateChecker.playersNotified.contains(player)) {
                Style style = Style.EMPTY.withClickEvent(() -> ClickEvent.Action.OPEN_URL);
                Component linkText = Component.literal("[ClientID] A new update is available! Get it at https://modrinth.com/plugin/client-id")
                        .withColor(0x00FF00)
                        .withStyle(style);
                player.sendSystemMessage(linkText, false);
                UpdateChecker.playersNotified.add(player);
            }
        });

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            ServerPlayer player = handler.getPlayer();

            ClientID.accepted.remove(player);
            ClientID.packLists.remove(player);
            ClientID.modConfirmed.remove(player);
            ClientID.modLists.remove(player);
            ClientID.versionConfirmed.remove(player);
        });
    }
}
