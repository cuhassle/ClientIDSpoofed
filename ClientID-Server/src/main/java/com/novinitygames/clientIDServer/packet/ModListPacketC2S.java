package com.novinitygames.clientIDServer.packet;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.novinitygames.clientIDServer.ClientIDServer;
import com.novinitygames.clientIDServer.utils.CheckUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class ModListPacketC2S {
    public static void onModListPacket(String channel, Player player, byte[] message) {
        if (!channel.equalsIgnoreCase(ClientIDServer.NAMESPACE + ":modlist")) {
            return;
        }

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String s = in.readLine();

        String[] mods = s.split(",");
        ClientIDServer.getInstance().installedMods.put(player, Arrays.stream(mods).toList());

        ArrayList<String> bannedMods = CheckUtils.CheckIllicitMods(player);

        if (!bannedMods.isEmpty()) {
            StringBuilder kickMessage = new StringBuilder(ChatColor.RED + "Failed ClientID check.\nThe following mods must be removed to play:\n\n");
            for (String mod : bannedMods) {
                kickMessage.append(mod).append("\n");
            }
            kickMessage.append("\nIf this is believed to be an error, please contact the server owner.");
            player.kickPlayer(kickMessage.toString());
        }
    }
}
