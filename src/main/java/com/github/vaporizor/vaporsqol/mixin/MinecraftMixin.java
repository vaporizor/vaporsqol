package com.github.vaporizor.vaporsqol.mixin;

import com.github.vaporizor.vaporsqol.VQConfig;

import com.mojang.blaze3d.platform.FramerateLimitTracker;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundSource;

@Mixin(Minecraft.class)
abstract
class MinecraftMixin {
    @Shadow @Final private SoundManager soundManager;
    @Shadow @Final public Options options;

    @Inject(method = "setWindowActive", at = @At("TAIL"))
    private void AFKTweaks(boolean windowActive, CallbackInfo ci) {
        int normalFPS = options.framerateLimit().get();
        FramerateLimitTracker flt = Minecraft.getInstance().getFramerateLimitTracker();
        // null check is not unnecessary
        if (normalFPS > VQConfig.I.fpsLimit() && flt != null)
            flt.setFramerateLimit(windowActive ? normalFPS : VQConfig.I.fpsLimit());

        if (soundManager != null) {
            float normalVolume = options.getSoundSourceVolume(SoundSource.MASTER);
            if (normalVolume > VQConfig.I.volumeLimit()) {
                soundManager.updateSourceVolume(
                    SoundSource.MASTER,
                    windowActive ? normalVolume : VQConfig.I.volumeLimit()
                );
            }
        }
    }
}
