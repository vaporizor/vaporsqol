package com.github.vaporizor.vaporsqol.mixin;

import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(GameRenderer.class)
class GameRendererMixin {
	@ModifyConstant(method = "tickFov", constant = @Constant(floatValue = 0.1F))
	private float getMinFov(float unused) {
		return 0.0001F;
	}
}
