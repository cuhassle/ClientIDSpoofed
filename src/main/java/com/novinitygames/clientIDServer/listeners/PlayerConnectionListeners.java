package com.novinitygames.clientIDServer.listeners;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.novinitygames.clientIDServer.ClientIDServer;
import com.novinitygames.clientIDServer.utils.GeyserUtils;
import com.novinitygames.clientIDServer.utils.UpdateChecker;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.w3c.dom.Text;

import java.util.logging.Level;

public class PlayerConnectionListeners implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        boolean isGeyser = false;
        if (ClientIDServer.getInstance().getServer().getPluginManager().getPlugin("Geyser-Spigot") != null) {
            isGeyser = GeyserUtils.isGeyserPlayer(player);
        }
        if (isGeyser) {
            ClientIDServer.getInstance().getLogger().info(event.getPlayer().getName() + " is a Bedrock player. Ignoring.");
        }
        if (
                (ClientIDServer.getInstance().getConfig().getStringList("playerBypass").contains(player.getName()) && !ClientIDServer.getInstance().getConfig().getBoolean("reversePlayerBypass", false))
                || (!ClientIDServer.getInstance().getConfig().getStringList("playerBypass").contains(player.getName()) && ClientIDServer.getInstance().getConfig().getBoolean("reversePlayerBypass", false))
                        || isGeyser) {
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
                                || !ClientIDServer.getInstance().installedMods.containsKey(player)
                                || !ClientIDServer.getInstance().versionConfirmed.containsKey(player)) {
                            ClientIDServer.getInstance().getLogger().warning("Player " + player.getName() + " failed to respond in a timely fashion.");
                            player.kickPlayer(ChatColor.RED + "Failed ClientID check.\nThis happened because of one of the following reasons:\n\n- You don't have the ClientID mod installed\n- Your latency was too high to callback in a timely fashion\n- Your version of ClientID is not supported by the server.\n\nIf this is believed to be an error, please contact the server owner.");
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
                                && ClientIDServer.getInstance().installedMods.containsKey(player)
                                && ClientIDServer.getInstance().versionConfirmed.containsKey(player)) {
                            if (!ClientIDServer.getInstance().confirmedPlayers.contains(player)) {
                                ClientIDServer.getInstance().confirmedPlayers.add(player);

                                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                                out.writeBoolean(ClientIDServer.getInstance().getConfig().getBoolean("disablePieChart", false));
                                player.sendPluginMessage(ClientIDServer.getInstance(), ClientIDServer.NAMESPACE + ":charts", out.toByteArray());
                            }
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
        ClientIDServer.getInstance().modConfirmation.remove(player);
        ClientIDServer.getInstance().installedMods.remove(player);
        ClientIDServer.getInstance().enabledPacks.remove(player);
        ClientIDServer.getInstance().versionConfirmed.remove(player);
        ClientIDServer.getInstance().confirmedPlayers.remove(player);
    }
}
