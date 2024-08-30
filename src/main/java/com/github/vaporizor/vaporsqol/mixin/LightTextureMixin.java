package com.github.vaporizor.vaporsqol.mixin;

import com.github.vaporizor.vaporsqol.config.FullBright;
import com.github.vaporizor.vaporsqol.config.VaporsQOLConfig;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.renderer.LightTexture;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(LightTexture.class)
@Debug(export = true)
public class LightTextureMixin {

    @ModifyExpressionValue(method = "updateLightTexture", at = @At(value = "INVOKE", target = "Ljava/lang/Double;floatValue()F"), slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/client/Options;gamma()Lnet/minecraft/client/OptionInstance;")))
    private float getModifiedGamma(float originalGamma) {
        FullBright brightConfig = VaporsQOLConfig.get().getBrightConfig();
        if (brightConfig.isToggled()) return originalGamma * brightConfig.getGamma();
        else return originalGamma;
    }

}
