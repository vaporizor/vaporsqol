package com.github.vaporizor.vaporsqol.mixin;

import com.github.vaporizor.vaporsqol.VQConfig;

import com.mojang.blaze3d.platform.FramerateLimitTracker;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundSource;

@Mixin(Minecraft.class)
abstract
class MinecraftMixin {
    @Shadow @Final private SoundManager soundManager;
    @Shadow @Final public Options options;
    @Shadow @Final private Window window;

    @Inject(method = "setWindowActive", at = @At("TAIL"))
    private void AFKTweaks(boolean windowActive, CallbackInfo ci) {
        int normalFPS = options.framerateLimit().get();
        FramerateLimitTracker flt = Minecraft.getInstance().getFramerateLimitTracker();
        if (normalFPS > VQConfig.I.fps() && flt != null)
            flt.setFramerateLimit(windowActive ? normalFPS : VQConfig.I.fps());

        if (soundManager != null) {
            float normalVolume = options.getSoundSourceVolume(SoundSource.MASTER);
            if (normalVolume > VQConfig.I.volume()) {
                soundManager.updateSourceVolume(
                    SoundSource.MASTER,
                    windowActive ? normalVolume : VQConfig.I.volume()
                );
            }
        }
    }
}
