package com.github.vaporizor.vaporsqol.mixin;

import com.github.vaporizor.vaporsqol.config.IdleModule;
import com.github.vaporizor.vaporsqol.config.VaporsQOLConfig;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Window.class)
public class WindowMixin {

    @Shadow @Final private long window;

    @Inject(method = "getFramerateLimit", at = @At("RETURN"), cancellable = true)
    private void getModifiedFramerateLimit(CallbackInfoReturnable<Integer> cir) {
        if (!Minecraft.getInstance().isWindowActive()) {
            IdleModule fpsConfig = VaporsQOLConfig.get().getIdleConfig().getIdleFpsConfig();
            if (fpsConfig.isEnabled()) cir.setReturnValue(fpsConfig.getLimit());
        }
    }
}
