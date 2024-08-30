package com.github.vaporizor.vaporsqol.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.sounds.SoundSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SoundEngine.class)
public class SoundEngineMixin {

    @Inject(method = "getVolume", at = @At("RETURN"), cancellable = true)
    private void getModifiedVolume(SoundSource soundSource, CallbackInfoReturnable<Float> cir) {
        if (!Minecraft.getInstance().isWindowActive()) cir.setReturnValue(0F);
    }

}
