package com.novinitygames.clientid.client.mixin;

import com.novinitygames.clientid.client.ClientIDClient;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class DebugHudMixin {
    @Inject(method = "onKey", at = @At("HEAD"), cancellable = true)
    private void onKey(long window, int key, int scancode, int action, int mods, CallbackInfo ci) {
        if (!ClientIDClient.pieChartDisabled) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.currentScreen != null) return;

        if (!client.options.debugEnabled) return;

        if (action == GLFW.GLFW_PRESS) {
            boolean shift = (mods & GLFW.GLFW_MOD_SHIFT) != 0;
            if (shift && key == GLFW.GLFW_KEY_F3) ci.cancel();
        }
    }
}
