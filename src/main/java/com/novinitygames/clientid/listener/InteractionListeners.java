package com.novinitygames.clientid.listener;

import com.novinitygames.clientid.ClientID;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Vec3d;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Environment(EnvType.SERVER)
public class InteractionListeners {
    private static final Map<ServerPlayerEntity, FrozenData> frozenPositions = new ConcurrentHashMap<>();
    private static record FrozenData(Vec3d pos, float yaw, float pitch) {}

    public static void Register() {
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (!ClientID.accepted.contains(player)) return ActionResult.FAIL;
            return ActionResult.PASS;
        });
        AttackBlockCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (!ClientID.accepted.contains(player)) return ActionResult.FAIL;
            return ActionResult.PASS;
        });
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (!ClientID.accepted.contains(player)) return ActionResult.FAIL;
            return ActionResult.PASS;
        });
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (!ClientID.accepted.contains(player)) return ActionResult.FAIL;
            return ActionResult.PASS;
        });
        UseItemCallback.EVENT.register((player, world, hand) -> {
            if (!ClientID.accepted.contains(player)) return ActionResult.FAIL;
            return ActionResult.PASS;
        });
        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> ClientID.accepted.contains(player));

        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            if (entity instanceof ServerPlayerEntity p)
                return ClientID.accepted.contains(p);
            return true;
        });

        ServerTickEvents.END_SERVER_TICK.register((server) -> {
            for (ServerPlayerEntity p : server.getPlayerManager().getPlayerList()) {
                if (ClientID.accepted.contains(p)) continue;

                FrozenData data = frozenPositions.get(p);
                if (data == null) {
                    frozenPositions.put(p, new FrozenData(p.getEntityPos(), p.getYaw(), p.getPitch()));
                    continue;
                }

                p.setSprinting(false);
                p.setVelocity(Vec3d.ZERO);

                p.setYaw(data.yaw());
                p.setPitch(data.pitch());
                p.setHeadYaw(data.yaw());
                p.setBodyYaw(data.yaw());

                p.networkHandler.requestTeleport(data.pos().x, data.pos().y, data.pos().z, data.yaw(), data.pitch());
            }
        });
    }
}
