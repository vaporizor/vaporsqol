package com.github.vaporizor.vaporsqol.mixin;

import com.github.vaporizor.vaporsqol.VaporsQOLConfig;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;

@Mixin(Window.class)
class WindowMixin {
    @Inject(method = "getFramerateLimit", at = @At("HEAD"), cancellable = true)
    private void getModifiedFramerateLimit(CallbackInfoReturnable<Integer> cir) {
        if (!Minecraft.getInstance().isWindowActive()) {
            final VaporsQOLConfig.IdleConfig.IntModule FPS_CONFIG = VaporsQOLConfig.get().idleConfig().fps();
            if (FPS_CONFIG.enabled()) cir.setReturnValue(FPS_CONFIG.limit());
        }
    }
}