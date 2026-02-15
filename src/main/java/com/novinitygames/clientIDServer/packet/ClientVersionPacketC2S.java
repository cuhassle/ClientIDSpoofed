package com.novinitygames.clientIDServer.packet;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.novinitygames.clientIDServer.ClientIDServer;
import com.novinitygames.clientIDServer.utils.CheckUtils;
import com.novinitygames.clientIDServer.utils.ReadHelpers;
import com.novinitygames.clientIDServer.utils.UpdateChecker;
import com.novinitygames.clientIDServer.utils.Version;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class ClientVersionPacketC2S {
    public static void onModListPacket(String channel, Player player, byte[] message) {
        if (!channel.equalsIgnoreCase(ClientIDServer.NAMESPACE + ":clientversion")) {
            return;
        }

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

            if (ClientIDServer.getInstance().clientMinimumVersion.compareTo(version) > 0) {
                player.kickPlayer(ChatColor.RED + "The minimum version of ClientID required to join this server is "
                        + ClientIDServer.getInstance().clientMinimumVersion.toString());
            } else {
                ClientIDServer.getInstance().versionConfirmed.put(player, true);
            }
        } catch (Exception e) {
            player.kickPlayer(ChatColor.translateAlternateColorCodes('&', "&aFailed to receive ClientID version."));
        }
    }
}
