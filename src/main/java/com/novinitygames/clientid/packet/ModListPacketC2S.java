package com.novinitygames.clientid.packet;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.novinitygames.clientid.ClientID;
import com.novinitygames.clientid.utils.CheckUtils;
import com.novinitygames.clientid.utils.ReadHelpers;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class ModListPacketC2S {
    public static void onModListPacket(String channel, Player player, byte[] message) {
        if (!channel.equalsIgnoreCase(ClientID.NAMESPACE + ":modlist")) {
            return;
        }

        ByteArrayDataInput in = ByteStreams.newDataInput(message);

        int length = ReadHelpers.readVarInt(in);
        byte[] bytes = new byte[length];
        in.readFully(bytes);
        String s = new String(bytes, StandardCharsets.UTF_8);

        if (s == null || s.isEmpty()) return;

        String[] mods = s.split(",");

        if (mods.length < 5) return;
        boolean f = false;
        for (String mod : mods) {
            if (mod.equals("clientid")) {
                f = true;
                break;
            }
        }
        if (!f) return;

        ClientID.getInstance().installedMods.put(player, Arrays.stream(mods).toList());

        ArrayList<String> bannedMods = CheckUtils.CheckIllicitMods(player);
        if (CheckUtils.canPlayerBypass(player)) return;

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
