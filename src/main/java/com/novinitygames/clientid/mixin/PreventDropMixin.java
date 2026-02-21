package com.novinitygames.clientid.mixin;

import com.novinitygames.clientid.ClientID;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public class PreventDropMixin {
    @Inject(method = "dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;",
            at = @At("HEAD"),
            cancellable = true)
    private void dropItem(ItemStack itemStack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir) {
        ServerPlayerEntity self = (ServerPlayerEntity)(Object)this;
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
                self.getEntityWorld().spawnEntity(new ItemEntity(self.getEntityWorld(), self.getX(), self.getY(), self.getZ(), copy));

            cir.setReturnValue(null);
            cir.cancel();
        }
    }
}
