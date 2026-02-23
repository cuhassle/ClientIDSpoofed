package com.novinitygames.clientid.utils;

import org.bukkit.entity.Player;
import org.geysermc.geyser.api.GeyserApi;
import org.geysermc.geyser.api.connection.GeyserConnection;

public class GeyserUtils {
    public static boolean isGeyserPlayer(Player player) {
        GeyserConnection geyserConnection = GeyserApi.api().connectionByUuid(player.getUniqueId());
        if (geyserConnection != null) {
            return true;
        }
        return false;
    }
}
