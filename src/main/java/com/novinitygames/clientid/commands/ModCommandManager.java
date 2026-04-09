package com.novinitygames.clientid.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.novinitygames.clientid.ClientID;
import com.novinitygames.clientid.config.ConfigManager;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class ModCommandManager {
    public void Init() {
        CommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess, environment) -> {
            dispatcher.register(Commands.literal("clientid").executes(context -> {
                FabricLoader.getInstance().getModContainer("clientid").ifPresent(modContainer -> {
                    java.lang.String version = modContainer.getMetadata().getVersion().getFriendlyString();
                    context.getSource().sendSystemMessage(Component.literal("[ClientID] Version " + version).withColor(0x55FF55));
                });
                return 1;
            })
                    .then(Commands.literal("reload").executes(ModCommandManager::ReloadSubCommand).requires(Commands.hasPermission(Commands.LEVEL_OWNERS)))
                    .then(Commands.literal("viewmods")
                            .then(Commands.argument("player", StringArgumentType.string()).executes(ModCommandManager::ViewModsSubCommand))
                        .requires(Commands.hasPermission(Commands.LEVEL_OWNERS)))
                    .then(Commands.literal("viewpacks")
                            .then(Commands.argument("player", StringArgumentType.string()).executes(ModCommandManager::ViewPacksSubCommand))
                            .requires(Commands.hasPermission(Commands.LEVEL_OWNERS))));
                });
    }

    private static int ReloadSubCommand(CommandContext<CommandSourceStack> ctx) {
        ConfigManager.load();
        for (ServerPlayer player : ctx.getSource().getServer().getPlayerList().getPlayers()) {
            player.connection.disconnect(Component.literal("Config reloaded. Please rejoin.").withColor(0xFF5555));
        }
        ClientID.LOGGER.info("Reloaded config!");

        return 1;
    }

    private static int ViewModsSubCommand(CommandContext<CommandSourceStack> ctx) {
        String plrName = StringArgumentType.getString(ctx, "player");
        ServerPlayer target = ctx.getSource().getServer().getPlayerList().getPlayer(plrName);

        if (target == null) {
            ctx.getSource().sendSystemMessage(Component.literal("Player not found.").withColor(0xFF5555));
            return 0;
        }

        ctx.getSource().sendSystemMessage(Component.literal(target.getName().getString() + "'s mod list:").withColor(0xFFAA00));
        ctx.getSource().sendSystemMessage(Component.literal(String.join(", ", ClientID.modLists.get(target))).withColor(0x55FF55));

        return 1;
    }

    private static int ViewPacksSubCommand(CommandContext<CommandSourceStack> ctx) {
        String plrName = StringArgumentType.getString(ctx, "player");
        ServerPlayer target = ctx.getSource().getServer().getPlayerList().getPlayer(plrName);

        if (target == null) {
            ctx.getSource().sendSystemMessage(Component.literal("Player not found.").withColor(0xFF5555));
            return 0;
        }

        ctx.getSource().sendSystemMessage(Component.literal(target.getName().getString() + "'s pack list:").withColor(0xFFAA00));
        ctx.getSource().sendSystemMessage(Component.literal(String.join(", ", ClientID.packLists.get(target))).withColor(0x55FF55));

        return 1;
    }
}
