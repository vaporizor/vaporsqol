package com.github.vaporizor.vaporsqol.mixin;

import static com.github.vaporizor.vaporsqol.VaporsQOLConfig.IdleConfig;
import static com.github.vaporizor.vaporsqol.VaporsQOLConfig.IdleConfig.IntModule;
import static com.github.vaporizor.vaporsqol.VaporsQOLConfig.IdleConfig.FloatModule;
import com.github.vaporizor.vaporsqol.VaporsQOLConfig;

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
    private void idleOptimizations(boolean bl, CallbackInfo ci) {
        final IdleConfig IDLE_CONFIG = VaporsQOLConfig.get().idleConfig();

        final FloatModule AUDIO_CONFIG = IDLE_CONFIG.audio();
        if (AUDIO_CONFIG.enabled() && soundManager != null) {
            final float normalVolume = options.getSoundSourceVolume(SoundSource.MASTER);
            if (normalVolume > AUDIO_CONFIG.limit())
                soundManager.updateSourceVolume(
                        SoundSource.MASTER,
                        bl ? normalVolume : AUDIO_CONFIG.limit()
                );
        }


        final IntModule FPS_CONFIG = IDLE_CONFIG.fps();
        if (FPS_CONFIG.enabled()) {
            final int normalFPS = options.framerateLimit().get();
            if (normalFPS > FPS_CONFIG.limit())
                window.setFramerateLimit(bl ? normalFPS : FPS_CONFIG.limit());
        }

    }
}
