package com.github.vaporizor.vaporsqol.mixin;

import static com.github.vaporizor.vaporsqol.VaporsQOLConfig.IdleConfig.IntModule;
import com.github.vaporizor.vaporsqol.VaporsQOLConfig;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.client.renderer.GameRenderer;

@Mixin(GameRenderer.class)
class GameRendererMixin {
	@Shadow @Final Minecraft minecraft;

	@ModifyConstant(method = "tickFov", constant = @Constant(floatValue = 0.1F))
	private float getMinFov(float originalMin) {
		if (VaporsQOLConfig.get().zoomConfig().enabled()) return 0.0001F;
		else return originalMin;
	}

	@ModifyExpressionValue(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Options;getEffectiveRenderDistance()I"))
	public int getModifiedRenderDistance(int originalDist) {
		final IntModule config = VaporsQOLConfig.get().idleConfig().renderDistance();
		if (config.enabled() && !minecraft.isWindowActive() && config.limit() < originalDist)
			return config.limit();
		else return originalDist;
	}
}
