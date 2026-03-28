package com.novinitygames.clientid.listeners;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.novinitygames.clientid.ClientID;
import com.novinitygames.clientid.utils.CheckUtils;
import com.novinitygames.clientid.utils.GeyserUtils;
import com.novinitygames.clientid.utils.UpdateChecker;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectionListeners implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        boolean isGeyser = false;
        if (ClientID.getInstance().getServer().getPluginManager().getPlugin("Geyser-Spigot") != null) {
            isGeyser = GeyserUtils.isGeyserPlayer(player);
        }
        if (isGeyser) {
            ClientID.getInstance().getLogger().info(event.getPlayer().getName() + " is a Bedrock player. Ignoring.");
        }
        if (CheckUtils.canPlayerBypass(player) || isGeyser) {
            ClientID.getInstance().confirmedPlayers.add(player);
            ClientID.getInstance().modConfirmation.put(player, true);
            return;
        }
        if (!ClientID.getInstance().modConfirmation.containsKey(player)) {
            ClientID.getInstance().modConfirmation.put(player, false);
            if (ClientID.getInstance().getConfig().getBoolean("requireMod", true)) {
                ClientID.getInstance().scheduler.global().runDelayed(() -> {
                    if (!ClientID.getInstance().modConfirmation.containsKey(player)
                            || !ClientID.getInstance().enabledPacks.containsKey(player)
                            || !ClientID.getInstance().installedMods.containsKey(player)
                            || !ClientID.getInstance().versionConfirmed.containsKey(player)) {
                        ClientID.getInstance().getLogger().warning("Player " + player.getName() + " failed to respond in a timely fashion.");
                        player.kickPlayer(ChatColor.RED + "Failed ClientID check.\nThis happened because of one of the following reasons:\n\n- You don't have the ClientID mod installed\n- Your latency was too high to callback in a timely fashion\n- Your version of ClientID is not supported by the server.\n\nIf this is believed to be an error, please contact the server owner.");
                    }
                }, 5*20L);
            }

            for (int i = 1; i < 100; i++) {
                ClientID.getInstance().scheduler.global().runDelayed(() -> {
                    if (ClientID.getInstance().modConfirmation.containsKey(player)
                            && ClientID.getInstance().enabledPacks.containsKey(player)
                            && ClientID.getInstance().installedMods.containsKey(player)
                            && ClientID.getInstance().versionConfirmed.containsKey(player)) {
                        if (!ClientID.getInstance().confirmedPlayers.contains(player)) {
                            ClientID.getInstance().confirmedPlayers.add(player);

                            ByteArrayDataOutput out = ByteStreams.newDataOutput();
                            out.writeBoolean(ClientID.getInstance().getConfig().getBoolean("disablePieChart", false));
                            player.sendPluginMessage(ClientID.getInstance(), ClientID.NAMESPACE + ":charts", out.toByteArray());
                        }
                    }
                }, (long) i);
            }
        }

        if (UpdateChecker.updateAvailable && player.hasPermission("clientid.updatecheck") && !UpdateChecker.playersNotified.contains(player)) {
            TextComponent component = new TextComponent(TextComponent.fromLegacy(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "[ClientID] A new update is available! Click here to get it."));
            component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://modrinth.com/plugin/client-id"));
            player.spigot().sendMessage(component);
            UpdateChecker.playersNotified.add(player);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        ClientID.getInstance().modConfirmation.remove(player);
        ClientID.getInstance().installedMods.remove(player);
        ClientID.getInstance().enabledPacks.remove(player);
        ClientID.getInstance().versionConfirmed.remove(player);
        ClientID.getInstance().confirmedPlayers.remove(player);
    }
}
