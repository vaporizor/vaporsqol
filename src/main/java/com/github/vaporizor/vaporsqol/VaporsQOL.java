package com.github.vaporizor.vaporsqol;

import static com.github.vaporizor.vaporsqol.VaporsQOLConfig.ZoomConfig;
import static com.github.vaporizor.vaporsqol.VaporsQOLConfig.FullBrightConfig;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;

import com.mojang.blaze3d.platform.Monitor;
import com.mojang.blaze3d.platform.VideoMode;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import com.mojang.blaze3d.platform.InputConstants;

import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VaporsQOL implements ClientModInitializer {
    public static final String MOD_ID = "vapors-qol";
    private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static VaporsQOLConfig CONFIG;

    private static Holder.Reference<MobEffect> FULL_BRIGHT_EFFECT;
    private static MobEffectInstance FULL_BRIGHT_EFFECT_INSTANCE;
    static class FullBrightStatusEffect extends MobEffect {
        public FullBrightStatusEffect() {
            super(MobEffectCategory.BENEFICIAL,
                    0xFFFF00,
                    ColorParticleOption.create(
                            ParticleTypes.ENTITY_EFFECT,
                            FastColor.ARGB32.color(Mth.floor(38.25F), 0xFFFF00)
                    )
            );
        }
    }

    private static float zoom = 1;

    @Override
    public void onInitializeClient() {
        CONFIG = VaporsQOLConfig.get();
        if (!CONFIG.enabled()) return;

        final ZoomConfig ZOOM_CONFIG = CONFIG.zoomConfig();
        if (ZOOM_CONFIG.enabled()) setupZoom(ZOOM_CONFIG);

        final FullBrightConfig BRIGHT_CONFIG = CONFIG.brightConfig();
        if (BRIGHT_CONFIG.enabled()) setupFullBright(BRIGHT_CONFIG);

        if (CONFIG.borderless()) {
            ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
                Window window = client.getWindow();
                window.setWindowed(window.getWidth(), window.getHeight());
                GLFW.glfwSetWindowAttrib(window.getWindow(), GLFW.GLFW_DECORATED, GLFW.GLFW_FALSE);

                Monitor monitor = window.findBestMonitor();
                if (monitor == null) monitor = new Monitor(GLFW.glfwGetPrimaryMonitor());
                GLFW.glfwSetWindowPos(window.getWindow(), monitor.getX(), monitor.getY());

                VideoMode mode = monitor.getCurrentMode();
                GLFW.glfwSetWindowSize(window.getWindow(), mode.getWidth(), mode.getHeight());
            });
        }
    }

    private void setupZoom(VaporsQOLConfig.ZoomConfig config) {
        final KeyMapping ZOOM_BINDING = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.vapors-qol.zoom",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_V,
                "category.vapors-qol.main"
        ));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (ZOOM_BINDING.isDown() && !zooming()) zoomIn(config, client);
            else if (!ZOOM_BINDING.isDown() && zooming()) zoomOut(client);
        });
    }

    private void setupFullBright(VaporsQOLConfig.FullBrightConfig config) {
        final KeyMapping BRIGHT_BINDING = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.vapors-qol.bright",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_B,
                "category.vapors-qol.main"
        ));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            boolean done = false;
            while (BRIGHT_BINDING.consumeClick()) {
                if (done) continue;
                toggleFullBright(config, client);
                done = true;
            }
        });

        if (config.visualIndicator()) {
            registerFullBrightEffect();
            ClientEntityEvents.ENTITY_LOAD.register((entity, world) -> {
                if (entity instanceof LocalPlayer player) updateBrightEffect(player);
            });
        }
    }

    private void zoomIn(ZoomConfig config, Minecraft client) {
        zoom = config.maxFOVModifier();
        client.gameRenderer.setRenderHand(false);
    }

    private void zoomOut(Minecraft client) {
        zoom = 1;
        client.gameRenderer.setRenderHand(true);
    }

    public static boolean zooming() {
        return zoom != 1;
    }

    public static float getZoomLevel() {
        return zoom;
    }

    public static void stepZoom(float val, float min, float max) {
        if (!zooming() || val < min || val > max) return;

        final VaporsQOLConfig.ZoomConfig ZOOM_CONFIG = CONFIG.zoomConfig();
        zoom -= (((val - min) / (max - min)) * 2 - 1) * ZOOM_CONFIG.maxFOVStep();
        if (zoom <= ZOOM_CONFIG.minFOVModifier()) zoom = ZOOM_CONFIG.minFOVModifier();
        if (zoom >= ZOOM_CONFIG.maxFOVModifier()) zoom = ZOOM_CONFIG.maxFOVModifier();
    }

    private void toggleFullBright(FullBrightConfig config, Minecraft client) {
        config.setToggled(!config.toggled());
        if (config.visualIndicator() && client.player != null)
            updateBrightEffect(client.player);
    }

    private void registerFullBrightEffect() {
        FULL_BRIGHT_EFFECT = Registry.registerForHolder(
                BuiltInRegistries.MOB_EFFECT,
                ResourceLocation.fromNamespaceAndPath(MOD_ID, "fullbright"),
                new FullBrightStatusEffect()
        );
        FULL_BRIGHT_EFFECT_INSTANCE = new MobEffectInstance(FULL_BRIGHT_EFFECT, -1);
    }

    private void updateBrightEffect(LocalPlayer player) {
        final boolean BRIGHT_TOGGLED = CONFIG.enabled() && CONFIG.brightConfig().toggled();
        final boolean HAS_BRIGHT_EFFECT = player.hasEffect(FULL_BRIGHT_EFFECT);
        if (BRIGHT_TOGGLED && !HAS_BRIGHT_EFFECT)
            player.addEffect(FULL_BRIGHT_EFFECT_INSTANCE);
        else if (HAS_BRIGHT_EFFECT && !BRIGHT_TOGGLED)
            player.removeEffect(FULL_BRIGHT_EFFECT);
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
