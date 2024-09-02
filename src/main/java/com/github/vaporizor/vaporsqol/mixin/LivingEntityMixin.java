package com.github.vaporizor.vaporsqol.mixin;

import com.github.vaporizor.vaporsqol.VaporsQOLConfig;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

import java.util.Map;

@Mixin(LivingEntity.class)
class LivingEntityMixin {
    @Unique
    private static final MobEffectInstance NIGHT_VISION_INSTANCE = new MobEffectInstance(MobEffects.NIGHT_VISION, -1);

    @Shadow @Final private Map<Holder<MobEffect>, MobEffectInstance> activeEffects;

    @Inject(method = "hasEffect", at = @At("HEAD"), cancellable = true)
    private void hasEffectOrFullBright(Holder<MobEffect> holder, CallbackInfoReturnable<Boolean> cir) {
        if (shouldOverride(this) && holder == MobEffects.NIGHT_VISION)
            cir.setReturnValue(true);
    }

    @Inject(method = "getEffect", at = @At("RETURN"), cancellable = true)
    private void getEffectOrFullBright(Holder<MobEffect> holder, CallbackInfoReturnable<MobEffectInstance> cir) {
        if (
            shouldOverride(this) &&
            holder == MobEffects.NIGHT_VISION &&
            !this.activeEffects.containsKey(MobEffects.NIGHT_VISION)
        )
            cir.setReturnValue(NIGHT_VISION_INSTANCE);
    }

    @Unique
    private boolean shouldOverride(Object instance) {
        return (
            instance instanceof LocalPlayer &&
            VaporsQOLConfig.get().brightConfig().toggled()
        );
    }
}
