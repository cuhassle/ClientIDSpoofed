package com.novinitygames.clientIDServer.listeners;

import com.novinitygames.clientIDServer.ClientIDServer;
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
        if (ClientIDServer.getInstance().getConfig().getStringList("playerBypass").contains(player.getName())) {
            ClientIDServer.getInstance().confirmedPlayers.add(player);
            ClientIDServer.getInstance().modConfirmation.put(player, true);
            return;
        }
        if (!ClientIDServer.getInstance().modConfirmation.containsKey(player)) {
            ClientIDServer.getInstance().modConfirmation.put(player, false);
            if (ClientIDServer.getInstance().getConfig().getBoolean("requireMod", true)) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(ClientIDServer.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        if (!ClientIDServer.getInstance().modConfirmation.containsKey(player)
                                || !ClientIDServer.getInstance().enabledPacks.containsKey(player)
                                || !ClientIDServer.getInstance().installedMods.containsKey(player)) {
                            ClientIDServer.getInstance().getLogger().warning("Player " + player.getName() + " failed to respond in a timely fashion.");
                            player.kickPlayer(ChatColor.RED + "Failed ClientID check.\nThis happened because of one of the following reasons:\n\n- You don't have the ClientID mod installed\n- Your latency was too high to callback in a timely fashion\n\nIf this is believed to be an error, please contact the server owner.");
                        }
                    }
                }, 5*20L);
            }

            for (int i = 0; i < 100; i++) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(ClientIDServer.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        if (ClientIDServer.getInstance().modConfirmation.containsKey(player)
                                && ClientIDServer.getInstance().enabledPacks.containsKey(player)
                                && ClientIDServer.getInstance().installedMods.containsKey(player)) {
                            ClientIDServer.getInstance().confirmedPlayers.add(player);
                        }
                    }
                }, (long) i);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        ClientIDServer.getInstance().modConfirmation.remove(player);
        ClientIDServer.getInstance().installedMods.remove(player);
        ClientIDServer.getInstance().enabledPacks.remove(player);
    }
}
