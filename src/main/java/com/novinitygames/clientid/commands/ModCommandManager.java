package com.novinitygames.clientid.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.novinitygames.clientid.ClientID;
import com.novinitygames.clientid.config.ConfigManager;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class ModCommandManager {
    public void Init() {
        CommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("clientid").executes(context -> {
                FabricLoader.getInstance().getModContainer("clientid").ifPresent(modContainer -> {
                    java.lang.String version = modContainer.getMetadata().getVersion().getFriendlyString();
                    context.getSource().sendMessage(Text.literal("[ClientID] Version " + version).withColor(0x55FF55));
                });
                return 1;
            })
                    .then(CommandManager.literal("reload").executes(ModCommandManager::ReloadSubCommand).requires(CommandManager.requirePermissionLevel(CommandManager.OWNERS_CHECK)))
                    .then(CommandManager.literal("viewmods")
                            .then(CommandManager.argument("player", StringArgumentType.string()).executes(ModCommandManager::ViewModsSubCommand))
                        .requires(CommandManager.requirePermissionLevel(CommandManager.OWNERS_CHECK)))
                    .then(CommandManager.literal("viewpacks")
                            .then(CommandManager.argument("player", StringArgumentType.string()).executes(ModCommandManager::ViewPacksSubCommand))
                            .requires(CommandManager.requirePermissionLevel(CommandManager.OWNERS_CHECK))));
                });
    }

    private static int ReloadSubCommand(CommandContext<ServerCommandSource> ctx) {
        ConfigManager.load();
        for (ServerPlayerEntity player : ctx.getSource().getServer().getPlayerManager().getPlayerList()) {
            player.networkHandler.disconnect(Text.literal("Config reloaded. Please rejoin.").withColor(0xFF5555));
        }
        ClientID.LOGGER.info("Reloaded config!");

        return 1;
    }

    private static int ViewModsSubCommand(CommandContext<ServerCommandSource> ctx) {
        String plrName = StringArgumentType.getString(ctx, "player");
        ServerPlayerEntity target = ctx.getSource().getServer().getPlayerManager().getPlayer(plrName);

        if (target == null) {
            ctx.getSource().sendMessage(Text.literal("Player not found.").withColor(0xFF5555));
            return 0;
        }

        ctx.getSource().sendMessage(Text.literal(target.getName().getString() + "'s mod list:").withColor(0xFFAA00));
        ctx.getSource().sendMessage(Text.literal(String.join(", ", ClientID.modLists.get(target))).withColor(0x55FF55));

        return 1;
    }

    private static int ViewPacksSubCommand(CommandContext<ServerCommandSource> ctx) {
        String plrName = StringArgumentType.getString(ctx, "player");
        ServerPlayerEntity target = ctx.getSource().getServer().getPlayerManager().getPlayer(plrName);

        if (target == null) {
            ctx.getSource().sendMessage(Text.literal("Player not found.").withColor(0xFF5555));
            return 0;
        }

        ctx.getSource().sendMessage(Text.literal(target.getName().getString() + "'s pack list:").withColor(0xFFAA00));
        ctx.getSource().sendMessage(Text.literal(String.join(", ", ClientID.packLists.get(target))).withColor(0x55FF55));

        return 1;
    }
}
