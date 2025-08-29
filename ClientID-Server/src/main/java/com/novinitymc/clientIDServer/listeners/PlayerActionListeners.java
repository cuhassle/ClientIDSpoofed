package com.novinitymc.clientIDServer.listeners;

import com.novinitymc.clientIDServer.ClientIDServer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;

public class PlayerActionListeners implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!ClientIDServer.getInstance().getConfig().getBoolean("requireMod", true)) return;
        if (!ClientIDServer.getInstance().confirmedPlayers.contains(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (!ClientIDServer.getInstance().getConfig().getBoolean("requireMod", true)) return;
        if (event.getDamager() instanceof Player player) {
            if (!ClientIDServer.getInstance().confirmedPlayers.contains(player)) {
                event.setCancelled(true);
            }
        } else if (event.getEntity() instanceof Player player) {
            if (!ClientIDServer.getInstance().confirmedPlayers.contains(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!ClientIDServer.getInstance().getConfig().getBoolean("requireMod", true)) return;
        if (event.getEntity() instanceof Player player) {
            if (!ClientIDServer.getInstance().confirmedPlayers.contains(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!ClientIDServer.getInstance().getConfig().getBoolean("requireMod", true)) return;
        if (!ClientIDServer.getInstance().confirmedPlayers.contains(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!ClientIDServer.getInstance().getConfig().getBoolean("requireMod", true)) return;
        if (!ClientIDServer.getInstance().confirmedPlayers.contains(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!ClientIDServer.getInstance().getConfig().getBoolean("requireMod", true)) return;
        if (!ClientIDServer.getInstance().confirmedPlayers.contains(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        if (!ClientIDServer.getInstance().getConfig().getBoolean("requireMod", true)) return;
        if (!ClientIDServer.getInstance().confirmedPlayers.contains(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent event) {
        if (!ClientIDServer.getInstance().getConfig().getBoolean("requireMod", true)) return;
        if (!ClientIDServer.getInstance().confirmedPlayers.contains(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}
