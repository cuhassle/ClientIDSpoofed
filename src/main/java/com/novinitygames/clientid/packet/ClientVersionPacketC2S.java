package com.novinitygames.clientid.packet;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.novinitygames.clientid.ClientID;
import com.novinitygames.clientid.utils.CheckUtils;
import com.novinitygames.clientid.utils.ReadHelpers;
import com.novinitygames.clientid.utils.Version;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;

public class ClientVersionPacketC2S {
    public static void onModListPacket(String channel, Player player, byte[] message) {
        if (!channel.equalsIgnoreCase(ClientID.NAMESPACE + ":clientversion")) {
            return;
        }
        if (CheckUtils.canPlayerBypass(player)) return;

        ByteArrayDataInput in = ByteStreams.newDataInput(message);

        int length = ReadHelpers.readVarInt(in);
        byte[] bytes = new byte[length];
        in.readFully(bytes);
        String s = new String(bytes, StandardCharsets.UTF_8);

        try {
            Version version = Version.fromString(s);
            if (version == null) {
                throw new NullPointerException("Version failed to convert.");
            }

            if (ClientID.getInstance().clientMinimumVersion.compareTo(version) > 0) {
                player.kickPlayer(ChatColor.RED + "The minimum version of ClientID required to join this server is "
                        + ClientID.getInstance().clientMinimumVersion.toString());
            } else {
                ClientID.getInstance().versionConfirmed.put(player, true);
            }
        } catch (Exception e) {
            player.kickPlayer(ChatColor.translateAlternateColorCodes('&', "&aFailed to receive ClientID version."));
        }
    }
}
