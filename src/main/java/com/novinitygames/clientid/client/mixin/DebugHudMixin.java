package com.novinitygames.clientid.client.mixin;

import com.novinitygames.clientid.client.ClientIDClient;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DebugScreenOverlay.class)
public class DebugHudMixin {
    @Inject(method = "toggleFpsCharts", at = @At("HEAD"), cancellable = true)
    private void onToggleRenderingAndTickCharts(CallbackInfo ci) {
        if (ClientIDClient.pieChartDisabled)
            ci.cancel();
    }

    @Inject(method = "toggleNetworkCharts", at = @At("HEAD"), cancellable = true)
    private void onToggleNetworkCharts(CallbackInfo ci) {
        if (ClientIDClient.pieChartDisabled)
            ci.cancel();
    }

    @Inject(method = "toggleProfilerChart", at = @At("HEAD"), cancellable = true)
    private void onToggleRenderingChart(CallbackInfo ci) {
        if (ClientIDClient.pieChartDisabled)
            ci.cancel();
    }
}
