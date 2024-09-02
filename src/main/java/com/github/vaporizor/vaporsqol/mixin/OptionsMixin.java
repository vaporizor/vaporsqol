package com.github.vaporizor.vaporsqol.mixin;

import com.github.vaporizor.vaporsqol.VaporsQOLConfig;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;

@Mixin(Options.class)
class OptionsMixin {
    @Inject(method = "getEffectiveRenderDistance", at = @At("RETURN"), cancellable = true)
    private void getModifiedRenderDistance(CallbackInfoReturnable<Integer> cir) {
        final int LIMIT = VaporsQOLConfig.get().idleConfig().renderDistance().limit();
        if (!Minecraft.getInstance().isWindowActive() && LIMIT < cir.getReturnValue())
            cir.setReturnValue(LIMIT);
    }
}
