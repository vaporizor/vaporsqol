package com.github.vaporizor.vaporsqol.mixin;

import com.github.vaporizor.vaporsqol.VaporsQOL;
import net.minecraft.client.player.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public class AbstractClientPlayerMixin {

    @Inject(method = "getFieldOfViewModifier", at = @At("RETURN"), cancellable = true)
    private void getModifiedFieldOfViewModifier(CallbackInfoReturnable<Float> cir) {
        float fov = cir.getReturnValue();
        if (fov > 1.5) fov = 1.5F;
        cir.setReturnValue(fov * VaporsQOL.getZoom());
    }
}
