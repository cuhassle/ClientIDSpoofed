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
//        ClientIDServer.getInstance().reloadConfig();
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ClientID.PREFIX + "&aReloaded config!"));

//        if (ClientIDServer.getInstance().getConfig().getBoolean("requireMod", true)) {
//            for (Player p : Bukkit.getOnlinePlayers()) {
//                if (!ClientIDServer.getInstance().modConfirmation.get(p)) {
//                    ClientIDServer.getInstance().getLogger().warning("Player " + p.getName() + " did not have ClientID loaded.");
//                    p.kickPlayer(ChatColor.RED + "Failed ClientID check.\nThis happened because of one of the following reasons:\n\n- You don't have the ClientID mod installed\n- Your latency was too high to callback in a timely fashion\n\nIf this is believed to be an error, please contact the server owner.");
//                } else {
//                    ArrayList<String> bannedMods = CheckUtils.CheckIllicitMods(p);
//
//                    if (!bannedMods.isEmpty()) {
//                        StringBuilder kickMessage = new StringBuilder(ChatColor.RED + "Failed ClientID check.\nThe following mods must be removed to play:\n\n");
//                        for (String mod : bannedMods) {
//                            kickMessage.append(mod).append("\n");
//                        }
//                        kickMessage.append("\nIf this is believed to be an error, please contact the server owner.");
//                        p.kickPlayer(kickMessage.toString());
//                    }
//                }
//            }
//        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.kickPlayer(ChatColor.RED + "Config reload. Please rejoin.");
        }
    }
}
