package com.novinitygames.clientid;

import com.cjcrafter.foliascheduler.FoliaCompatibility;
import com.cjcrafter.foliascheduler.ServerImplementation;
import com.novinitygames.clientid.commands.CommandManager;
import com.novinitygames.clientid.listeners.PlayerActionListeners;
import com.novinitygames.clientid.listeners.PlayerConnectionListeners;
import com.novinitygames.clientid.packet.ClientVersionPacketC2S;
import com.novinitygames.clientid.packet.ModCheckPacketC2S;
import com.novinitygames.clientid.packet.ModListPacketC2S;
import com.novinitygames.clientid.packet.PackListPacketC2S;
import com.novinitygames.clientid.utils.ConfigVerification;
import com.novinitygames.clientid.utils.UpdateChecker;
import com.novinitygames.clientid.utils.Version;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class ClientID extends JavaPlugin {
    public static ClientID getInstance() {
        return instance;
    }

    private static ClientID instance;

    public static final String NAMESPACE = "clientid";
    public static final String PREFIX = "&8[&c&lCLIENTID&8] &f";

    public HashMap<Player, Boolean> modConfirmation = new HashMap<>();
    public HashMap<Player, List<String>> installedMods = new HashMap<>();
    public HashMap<Player, List<String>> enabledPacks = new HashMap<>();
    public HashMap<Player, Boolean> versionConfirmed = new HashMap<>();
    public ArrayList<Player> confirmedPlayers = new ArrayList<>();

    public final Version clientMinimumVersion = new Version(1, 1, 0);

    public ServerImplementation scheduler;

    @Override
    public void onEnable() {
        instance = this;
        scheduler = new FoliaCompatibility(this).getServerImplementation();

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

        int pluginId = 29723;
        Metrics metrics = new Metrics(this, pluginId);

        UpdateChecker.checkForUpdates();
    }

    @Override
    public void onDisable() {
        getServer().getMessenger().unregisterIncomingPluginChannel(this, NAMESPACE + ":modcheck");
        getServer().getMessenger().unregisterIncomingPluginChannel(this, NAMESPACE + ":modlist");
        getServer().getMessenger().unregisterIncomingPluginChannel(this, NAMESPACE + ":packlist");
    }
}
