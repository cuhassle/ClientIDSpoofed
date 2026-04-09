package com.novinitygames.clientid.mixin;

import com.novinitygames.clientid.ClientID;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public class PreventDropMixin {
    @Inject(method = "drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;",
            at = @At("HEAD"),
            cancellable = true)
    private void dropItem(ItemStack itemStack, boolean randomly, boolean thrownFromHand, CallbackInfoReturnable<ItemEntity> cir) {
        ServerPlayer self = (ServerPlayer)(Object)this;
        if (!ClientID.accepted.contains(self)) {
            ItemStack copy = itemStack.copy();

            boolean added = false;
            // Attempt to add to inventory the normal way
            try {
                self.getInventory().getClass().getMethod("offerOrDrop", ItemStack.class)
                        .invoke(self.getInventory(), copy);
                added = true;
            } catch (ReflectiveOperationException ignored) {}

            // If that doesn't work, try to forcefully insert it into the inventory somewhere
            if (!added) {
                try {
                    Object ret = self.getInventory().getClass().getMethod("insertStack", ItemStack.class)
                            .invoke(self.getInventory(), copy);
                    if (ret instanceof Boolean && (Boolean)ret) added = true;
                } catch (ReflectiveOperationException ignored) {}
            }

            // If neither work, just drop it to the floor
            if (!added)
                self.level().addFreshEntity(new ItemEntity(self.level(), self.getX(), self.getY(), self.getZ(), copy));

            cir.setReturnValue(null);
            cir.cancel();
        }
    }
}
