package com.github.vaporizor.vaporsqol;

import com.github.vaporizor.vaporsqol.config.VaporsQOLConfig;
import com.github.vaporizor.vaporsqol.config.Zoom;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VaporsQOL implements ClientModInitializer {
	public static final String MOD_ID = "vapors-qol";
	private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private static final VaporsQOLConfig config = VaporsQOLConfig.get();
	private static float zoom = 1;

	@Override
	public void onInitializeClient() {
		if (config.getZoomConfig().isEnabled()) {
			KeyMapping zoomBinding = KeyBindingHelper.registerKeyBinding(new KeyMapping(
					"key.vapors-qol.zoom",
					InputConstants.Type.KEYSYM,
					GLFW.GLFW_KEY_V,
					"category.vapors-qol.main"
			));
			ClientTickEvents.END_CLIENT_TICK.register(client -> {
				if (zoomBinding.isDown() && !isZooming()) {
					zoom = config.getZoomConfig().getMaxFOVModifier();
					client.gameRenderer.setRenderHand(false);
				} else if (!zoomBinding.isDown() && isZooming()) {
					zoom = 1;
					client.gameRenderer.setRenderHand(true);
				}
			});
		}

		if (config.getBrightConfig().isEnabled()) {
			KeyMapping brightBinding = KeyBindingHelper.registerKeyBinding(new KeyMapping(
					"key.vapors-qol.bright",
					InputConstants.Type.KEYSYM,
					GLFW.GLFW_KEY_J,
					"category.vapors-qol.main"
			));
			ClientTickEvents.END_CLIENT_TICK.register(client -> {
				boolean done = false;
				while (brightBinding.consumeClick()) {
					if (done) continue;

					config.getBrightConfig().setToggled(!config.getBrightConfig().isToggled());
					done = true;
				}
			});
		}
	}

	public static boolean isZooming() {
		return zoom != 1;
	}

	public static float getZoom() {
		return zoom;
	}

	public static void stepZoom(float val, float min, float max) {
		if (!isZooming() || val < min || val > max) return;

		Zoom zoomConfig = config.getZoomConfig();
		zoom -= (((val - min) / (max - min)) * 2 - 1) * zoomConfig.getMaxFOVStep();
		if (zoom <= zoomConfig.getMinFOVModifier()) zoom = zoomConfig.getMinFOVModifier();
		if (zoom >= zoomConfig.getMaxFOVModifier()) zoom = zoomConfig.getMaxFOVModifier();
	}

	// For debugging
	@SuppressWarnings("unused")
	public static void log(String message) {
		LOGGER.info(message);
	}

	public static void err(String message) {
		LOGGER.error(message);
	}

	public static void err(String message, Throwable throwable) {
		LOGGER.error(message, throwable);
	}
}