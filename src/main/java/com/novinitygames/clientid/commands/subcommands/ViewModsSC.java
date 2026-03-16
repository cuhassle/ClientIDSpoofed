package com.novinitygames.clientid.commands.subcommands;

import com.novinitygames.clientid.ClientID;
import com.novinitygames.clientid.commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ViewModsSC extends SubCommand {
    @Override
    public String getName() {
        return "viewmods";
    }

    @Override
    public String getDescription() {
        return "Allows you to view a player's mod list.";
    }

    @Override
    public String getSyntax() {
        return "/clientid viewmods <player>";
    }

    @Override
    public String getRequiredPermission() {
        return "clientid.viewmods";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /clientid viewmods <player>");
            return;
        }
        String playerName = args[1];
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            sender.sendMessage(ChatColor.RED + "Player not found.");
            return;
        }

        if (!ClientID.getInstance().installedMods.containsKey(player)) {
            sender.sendMessage(ChatColor.RED + "No mod list received for " + player.getName());
            return;
        }

        String s = ChatColor.GOLD + player.getName() + "'s mod list:\n" +
                ChatColor.GREEN + String.join(", ", ClientID.getInstance().installedMods.get(player));
        sender.sendMessage(s);
    }
}
