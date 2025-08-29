package com.novinitymc.clientIDServer.commands.subcommands;

import com.novinitymc.clientIDServer.ClientIDServer;
import com.novinitymc.clientIDServer.commands.SubCommand;
import com.novinitymc.clientIDServer.utils.CheckUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.logging.Level;

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
        ClientIDServer.getInstance().reloadConfig();
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ClientIDServer.PREFIX + "&aReloaded config!"));

        if (ClientIDServer.getInstance().getConfig().getBoolean("requireMod", true)) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (!ClientIDServer.getInstance().modConfirmation.get(p)) {
                    ClientIDServer.getInstance().getLogger().warning("Player " + p.getName() + " did not have ClientID loaded.");
                    p.kickPlayer(ChatColor.RED + "Failed ClientID check.\nThis happened because of one of the following reasons:\n\n- You don't have the ClientID mod installed\n- Your latency was too high to callback in a timely fashion\n\nIf this is believed to be an error, please contact Novinity.");
                } else {
                    ArrayList<String> bannedMods = CheckUtils.CheckIllicitMods(p);

                    if (!bannedMods.isEmpty()) {
                        StringBuilder kickMessage = new StringBuilder(ChatColor.RED + "Failed ClientID check.\nThe following mods must be removed to play:\n\n");
                        for (String mod : bannedMods) {
                            kickMessage.append(mod).append("\n");
                        }
                        kickMessage.append("\nIf this is believed to be an error, please contact Novinity.");
                        p.kickPlayer(kickMessage.toString());
                    }
                }
            }
        }
    }
}
