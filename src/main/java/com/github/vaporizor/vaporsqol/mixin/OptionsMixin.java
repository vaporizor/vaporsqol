package com.github.vaporizor.vaporsqol.mixin;

import com.github.vaporizor.vaporsqol.config.VaporsQOLConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Options.class)
public class OptionsMixin {

    @Inject(method = "getEffectiveRenderDistance", at = @At("RETURN"), cancellable = true)
    private void getModifiedRenderDistance(CallbackInfoReturnable<Integer> cir) {
        if (!Minecraft.getInstance().isWindowActive())
            cir.setReturnValue(VaporsQOLConfig.get().getIdleConfig().getIdleRenderDistanceConfig().getLimit());
    }
}
