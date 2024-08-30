package com.github.vaporizor.vaporsqol.mixin;

import com.github.vaporizor.vaporsqol.config.IdleModule;
import com.github.vaporizor.vaporsqol.config.VaporsQOLConfig;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;

@Mixin(Window.class)
class WindowMixin {
    @Inject(method = "getFramerateLimit", at = @At("RETURN"), cancellable = true)
    private void getModifiedFramerateLimit(CallbackInfoReturnable<Integer> cir) {
        if (!Minecraft.getInstance().isWindowActive()) {
            IdleModule fpsConfig = VaporsQOLConfig.get().getIdleConfig().getIdleFpsConfig();
            if (fpsConfig.isEnabled()) cir.setReturnValue(fpsConfig.getLimit());
        }
    }
}