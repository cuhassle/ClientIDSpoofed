package com.novinitygames.clientid.client.mixin;

import com.novinitygames.clientid.client.ClientIDClient;
import net.minecraft.client.gui.hud.DebugHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DebugHud.class)
public class DebugHudMixin {
    // Cancels the method that toggles the rendering/tick charts (Shift+F3 combos)
    @Inject(method = "toggleRenderingAndTickCharts", at = @At("HEAD"), cancellable = true)
    private void onToggleRenderingAndTickCharts(CallbackInfo ci) {
        if (ClientIDClient.pieChartDisabled)
            ci.cancel();
    }

    // Cancels packet-size / ping chart toggling (the other debug-chart group)
    @Inject(method = "togglePacketSizeAndPingCharts", at = @At("HEAD"), cancellable = true)
    private void onTogglePacketSizeAndPingCharts(CallbackInfo ci) {
        if (ClientIDClient.pieChartDisabled)
            ci.cancel();
    }

    // prevent small rendering chart
    @Inject(method = "toggleRenderingChart", at = @At("HEAD"), cancellable = true)
    private void onToggleRenderingChart(CallbackInfo ci) {
        if (ClientIDClient.pieChartDisabled)
            ci.cancel();
    }
}
