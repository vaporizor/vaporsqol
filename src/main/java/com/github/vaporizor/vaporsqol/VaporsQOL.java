package com.github.vaporizor.vaporsqol;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;

import com.mojang.blaze3d.platform.Monitor;
import com.mojang.blaze3d.platform.VideoMode;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import com.mojang.blaze3d.platform.InputConstants;

import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VaporsQOL implements ClientModInitializer {
    public static final String MOD_ID = "vaporsqol";

    private static final String KEYBINDING_CATEGORY_NAMESPACE = String.format("category.%s.main", MOD_ID);
    private static final String KEYBINDING_ZOOM_NAMESPACE = String.format("key.%s.zoom", MOD_ID);
    private static final String KEYBINDING_FB_NAMESPACE =  String.format("key.%s.fullbright", MOD_ID);

    private static final ResourceLocation FB_EFFECT_NAMESPACE = ResourceLocation.fromNamespaceAndPath(MOD_ID, "fullbright");
    private static Holder.Reference<MobEffect> FB_EFFECT_ID;
    private static MobEffectInstance FB_EFFECT_INSTANCE;

    private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static float zoom = 1;
    private static boolean fullbright = false;

    @Override
    public void onInitializeClient() {
        if (VQConfig.I.enabled()) {
            if (VQConfig.I.zoom()) setupZoom();
            if (VQConfig.I.fullbright()) setupFullbright();
            if (VQConfig.I.borderless()) borderlessFullscreen();
        }
    }

    private void borderlessFullscreen() {
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            Window window = client.getWindow();
            long windowPointer = window.getWindow();
            window.setWindowed(window.getWidth(), window.getHeight());
            GLFW.glfwSetWindowAttrib(windowPointer, GLFW.GLFW_DECORATED, GLFW.GLFW_FALSE);

            Monitor monitor = window.findBestMonitor();
            if (monitor == null) monitor = new Monitor(GLFW.glfwGetPrimaryMonitor());
            GLFW.glfwSetWindowPos(windowPointer, monitor.getX(), monitor.getY());

            VideoMode mode = monitor.getCurrentMode();
            GLFW.glfwSetWindowSize(windowPointer, mode.getWidth(), mode.getHeight());
        });
    }

    private void setupZoom() {
        KeyMapping ZOOM_BINDING = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                KEYBINDING_ZOOM_NAMESPACE,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_V,
            KEYBINDING_CATEGORY_NAMESPACE
        ));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // TODO: prevent setRenderHand from clashing with other configs / mods
            if (ZOOM_BINDING.isDown() && !zooming()) {
                zoom = VQConfig.I.maxFOVModifier();
                client.gameRenderer.setRenderHand(false);
            } else if (!ZOOM_BINDING.isDown() && zooming()) {
                zoom = 1;
                client.gameRenderer.setRenderHand(true);
            }
        });
    }

    /*
     * The specifics of this method have been lost to time so sad.
     * I don't know what half of this does anymore but too scared to change it so...
     */
    public static void stepZoom(float val, float min, float max) {
        // Why on earth did I add a validity check here??
        if (zooming() && val >= min && val <= max) {
            // x2 - 1?? what??
            zoom -= (((val - min) / (max - min)) * 2 - 1) * VQConfig.I.FOVStep();
            if (zoom <= VQConfig.I.minFOVModifier()) zoom = VQConfig.I.minFOVModifier();
            if (zoom >= VQConfig.I.maxFOVModifier()) zoom = VQConfig.I.maxFOVModifier();
        }
    }

    public static boolean zooming() {
        return zoom != 1;
    }

    public static float zoomModifier() {
        return zoom;
    }

    private void setupFullbright() {
        final KeyMapping FB_BINDING = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            KEYBINDING_FB_NAMESPACE,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
            KEYBINDING_CATEGORY_NAMESPACE
        ));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (FB_BINDING.consumeClick()) toggleFullbright(client.player);
        });
        if (VQConfig.I.indicator()) registerFullbrightEffect();
    }

    private void registerFullbrightEffect() {
        FB_EFFECT_ID = Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, FB_EFFECT_NAMESPACE, new MobEffect(MobEffectCategory.BENEFICIAL, 50) {});
        FB_EFFECT_INSTANCE = new MobEffectInstance(FB_EFFECT_ID, -1);
    }

    private void toggleFullbright(LocalPlayer player) {
        fullbright = !fullbright;
        if (VQConfig.I.indicator() && player != null) {
            if (fullbright) player.addEffect(FB_EFFECT_INSTANCE);
            else player.removeEffect(FB_EFFECT_ID);
        }
    }

    public static boolean fullbright() {
        return fullbright;
    }

    public static void log(String message) {
        LOGGER.info(message);
    }
    public static void warn(String message) {
        LOGGER.warn(message);
    }
    public static void err(String message) {
        LOGGER.error(message);
    }
    public static void err(String message, Throwable throwable) {
        LOGGER.error(message, throwable);
    }
}
