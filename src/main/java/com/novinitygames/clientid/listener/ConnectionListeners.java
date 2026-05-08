package com.novinitygames.clientid.listener;

import com.novinitygames.clientid.ClientID;
import com.novinitygames.clientid.client.records.ChartsS2CPayload;
import com.novinitygames.clientid.config.ConfigManager;
import com.novinitygames.clientid.utils.SimpleServerScheduler;
import com.novinitygames.clientid.utils.UpdateChecker;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;

@Environment(EnvType.SERVER)
public class ConnectionListeners {
    public static void Register() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerEntity player = handler.getPlayer();
            if (ClientID.isPlayerBypassed(player)) {
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
                        player.networkHandler.disconnect(Text.literal("Failed ClientID check.\nThis happened because of one of the following reasons:\n\n- You don't have the ClientID mod installed\n- Your latenyc was too high to callback in a timely fashion\n- You are using an unsupported version of the mod\n\nIf this is believed to be an error, please contact the server owner.")
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
                Text linkText = Text.literal("[ClientID] A new update is available! Get it at https://modrinth.com/plugin/client-id")
                        .withColor(0x00FF00)
                        .styled(style -> style.withClickEvent(() -> ClickEvent.Action.OPEN_URL));
                player.sendMessage(linkText, false);
                UpdateChecker.playersNotified.add(player);
            }
        });

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            ServerPlayerEntity player = handler.getPlayer();

            ClientID.accepted.remove(player);
            ClientID.packLists.remove(player);
            ClientID.modConfirmed.remove(player);
            ClientID.modLists.remove(player);
            ClientID.versionConfirmed.remove(player);
        });
    }
}
