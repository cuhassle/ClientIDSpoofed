package com.novinitygames.clientIDServer;

import com.novinitygames.clientIDServer.commands.CommandManager;
import com.novinitygames.clientIDServer.listeners.PlayerActionListeners;
import com.novinitygames.clientIDServer.listeners.PlayerConnectionListeners;
import com.novinitygames.clientIDServer.packet.ClientVersionPacketC2S;
import com.novinitygames.clientIDServer.packet.ModCheckPacketC2S;
import com.novinitygames.clientIDServer.packet.ModListPacketC2S;
import com.novinitygames.clientIDServer.packet.PackListPacketC2S;
import com.novinitygames.clientIDServer.utils.ConfigVerification;
import com.novinitygames.clientIDServer.utils.UpdateChecker;
import com.novinitygames.clientIDServer.utils.Version;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class ClientIDServer extends JavaPlugin {
    public static ClientIDServer getInstance() {
        return instance;
    }

    private static ClientIDServer instance;

    public static final String NAMESPACE = "clientid";
    public static final String PREFIX = "&8[&c&lCLIENTID&8] &f";

    public HashMap<Player, Boolean> modConfirmation = new HashMap<>();
    public HashMap<Player, List<String>> installedMods = new HashMap<>();
    public HashMap<Player, List<String>> enabledPacks = new HashMap<>();
    public HashMap<Player, Boolean> versionConfirmed = new HashMap<>();
    public ArrayList<Player> confirmedPlayers = new ArrayList<>();

    public final Version clientMinimumVersion = new Version(1, 0, 1);

    @Override
    public void onEnable() {
        instance = this;

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        ConfigVerification.VerifyConfig();

        getServer().getPluginCommand("clientid").setExecutor(new CommandManager());
        getServer().getPluginCommand("clientid").setTabCompleter(new CommandManager());

        getServer().getPluginManager().registerEvents(new PlayerConnectionListeners(), this);
        getServer().getPluginManager().registerEvents(new PlayerActionListeners(), this);

        getServer().getMessenger().registerIncomingPluginChannel(this, NAMESPACE + ":modcheck", ModCheckPacketC2S::onModCheckReceived);
        getServer().getMessenger().registerIncomingPluginChannel(this, NAMESPACE + ":modlist", ModListPacketC2S::onModListPacket);
        getServer().getMessenger().registerIncomingPluginChannel(this, NAMESPACE + ":packlist", PackListPacketC2S::onPackListPacket);
        getServer().getMessenger().registerIncomingPluginChannel(this, NAMESPACE + ":clientversion", ClientVersionPacketC2S::onModListPacket);

        getServer().getMessenger().registerOutgoingPluginChannel(this, NAMESPACE + ":charts");

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.kickPlayer(ChatColor.RED + "Server reload, please rejoin.");
        }

        UpdateChecker.checkForUpdates();
    }

    @Override
    public void onDisable() {
        getServer().getMessenger().unregisterIncomingPluginChannel(this, NAMESPACE + ":modcheck");
        getServer().getMessenger().unregisterIncomingPluginChannel(this, NAMESPACE + ":modlist");
        getServer().getMessenger().unregisterIncomingPluginChannel(this, NAMESPACE + ":packlist");
    }
}
