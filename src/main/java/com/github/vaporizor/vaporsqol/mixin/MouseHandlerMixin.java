package com.github.vaporizor.vaporsqol.mixin;

import com.github.vaporizor.vaporsqol.VaporsQOL;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MouseHandler;

@Mixin(MouseHandler.class)
class MouseHandlerMixin {
    @Inject(method = "onScroll", at = @At("HEAD"), cancellable = true)
    private void onScroll(long l, double d, double e, CallbackInfo ci) {
        if (VaporsQOL.isZooming()) {
            VaporsQOL.stepZoom((float) e, -10, 10);
            ci.cancel();
        }
    }

    @ModifyConstant(method = "turnPlayer", constant = @Constant(doubleValue = 0.6000000238418579))
    private double getModifiedSensitivityMultiplier(double vanillaMultiplier) {
        return vanillaMultiplier * VaporsQOL.getZoom();
    }
}
