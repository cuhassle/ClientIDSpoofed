package com.novinitygames.clientid.commands.subcommands;

import com.novinitygames.clientid.ClientID;
import com.novinitygames.clientid.commands.SubCommand;
import com.novinitygames.clientid.utils.ConfigVerification;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadSC extends SubCommand {
    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "Reload the config";
    }

    @Override
    public String getSyntax() {
        return "/clientid reload";
    }

    @Override
    public String getRequiredPermission() {
        return "clientid.reload";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        ConfigVerification.VerifyConfig();
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ClientID.PREFIX + "&aReloaded config!"));

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.kickPlayer(ChatColor.RED + "Config reload. Please rejoin.");
        }
    }
}
