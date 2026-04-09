package com.novinitygames.clientid.mixin;

import com.novinitygames.clientid.ClientID;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public class PreventPickupMixin {
    @Inject(method = "playerTouch",
            at = @At("HEAD"),
            cancellable = true)
    private void onPlayerCollision(Player player, CallbackInfo ci) {
        if (player instanceof ServerPlayer p && !ClientID.accepted.contains(p)) {
            ci.cancel();
        }
    }
}
