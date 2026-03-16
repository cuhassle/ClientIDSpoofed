package com.novinitygames.clientid.commands.subcommands;

import com.novinitygames.clientid.ClientID;
import com.novinitygames.clientid.commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ViewPacksSC extends SubCommand {
    @Override
    public String getName() {
        return "viewpacks";
    }

    @Override
    public String getDescription() {
        return "Allows you to view a player's active resource pack list.";
    }

    @Override
    public String getSyntax() {
        return "/clientid viewpacks <player>";
    }

    @Override
    public String getRequiredPermission() {
        return "clientid.viewpacks";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /clientid viewpacks <player>");
            return;
        }
        String playerName = args[1];
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            sender.sendMessage(ChatColor.RED + "Player not found.");
            return;
        }

        if (!ClientID.getInstance().enabledPacks.containsKey(player)) {
            sender.sendMessage(ChatColor.RED + "No pack list received for " + player.getName());
            return;
        }

        String s = ChatColor.GOLD + player.getName() + "'s pack list:\n" +
                ChatColor.GREEN + String.join(", ", ClientID.getInstance().enabledPacks.get(player));
        sender.sendMessage(s);
    }
}
