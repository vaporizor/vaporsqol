package com.github.vaporizor.vaporsqol.mixin;

import com.github.vaporizor.vaporsqol.VQConfig;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.client.sounds.SoundEngine;

@Mixin(SoundEngine.class)
class SoundEngineMixin {
    @ModifyConstant(method = "method_19750", constant = @Constant(floatValue = 0.0F))
    private static float playSoundsWithNoVolume(float originalMin) {
        if (VQConfig.I.playSoundsWithNoVolume()) return -1;
        else return originalMin;
    }
}
