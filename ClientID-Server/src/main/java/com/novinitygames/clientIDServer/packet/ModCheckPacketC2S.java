package com.novinitygames.clientIDServer.packet;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.novinitygames.clientIDServer.ClientIDServer;
import org.bukkit.entity.Player;

public class ModCheckPacketC2S {
    public static void onModCheckReceived(String channel, Player player, byte[] message) {
        if (!channel.equalsIgnoreCase(ClientIDServer.NAMESPACE + ":modcheck")) {
            return;
        }

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String s = in.readLine();
        ClientIDServer.getInstance().getLogger().info("Received mod check from player " + player.getName() + ": " + s);
        ClientIDServer.getInstance().modConfirmation.put(player, true);
    }
}
