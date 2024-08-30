package com.github.vaporizor.vaporsqol.mixin;

import com.github.vaporizor.vaporsqol.config.VaporsQOLConfig;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.level.dimension.DimensionType;

@Mixin(LightTexture.class)
class LightTextureMixin {
    @Shadow @Final public static int FULL_BRIGHT;

    @Inject(method = "getBrightness", at = @At("RETURN"), cancellable = true)
    private static void getModifiedBrightness(DimensionType dimensionType, int i, CallbackInfoReturnable<Float> cir) {
        if (VaporsQOLConfig.get().getBrightConfig().isToggled())
            cir.setReturnValue((float) FULL_BRIGHT);
    }
}