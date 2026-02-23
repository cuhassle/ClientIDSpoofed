package com.novinitygames.clientid.packet;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.novinitygames.clientid.ClientID;
import com.novinitygames.clientid.utils.ReadHelpers;
import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;

public class ModCheckPacketC2S {
    public static void onModCheckReceived(String channel, Player player, byte[] message) {
        if (!channel.equalsIgnoreCase(ClientID.NAMESPACE + ":modcheck")) {
            return;
        }

        ByteArrayDataInput in = ByteStreams.newDataInput(message);

        int length = ReadHelpers.readVarInt(in);
        byte[] bytes = new byte[length];
        in.readFully(bytes);
        String s = new String(bytes, StandardCharsets.UTF_8);

        ClientID.getInstance().getLogger().info("Received mod check from player " + player.getName() + ": " + s);
        ClientID.getInstance().modConfirmation.put(player, true);
    }
}
