package com.github.vaporizor.vaporsqol;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class VaporsQOLMixinPlugin implements IMixinConfigPlugin {
    private static String MIXIN_PACKAGE_NAME;

    @Override
    public boolean shouldApplyMixin(String unused, String mixinClassName) {
        final VaporsQOLConfig CONFIG = VaporsQOLConfig.get();
        final boolean ZOOM = CONFIG.zoomConfig().enabled();
        final boolean BRIGHT = CONFIG.brightConfig().enabled();
        final boolean FPS = CONFIG.idleConfig().fps().enabled();
        final boolean RENDER_DIST = CONFIG.idleConfig().renderDistance().enabled();
        final boolean AUDIO = CONFIG.idleConfig().audio().enabled();

        return (
            CONFIG.enabled() && (
               (ZOOM && Objects.equals(mixinClassName, MIXIN_PACKAGE_NAME + ".AbstractClientPlayerMixin")) ||
               (ZOOM && Objects.equals(mixinClassName, MIXIN_PACKAGE_NAME + ".MouseHandlerMixin")) ||
               (ZOOM && Objects.equals(mixinClassName, MIXIN_PACKAGE_NAME + ".GameRendererMixin")) ||
               (BRIGHT && Objects.equals(mixinClassName, MIXIN_PACKAGE_NAME + ".LivingEntityMixin")) ||
               (FPS && Objects.equals(mixinClassName, MIXIN_PACKAGE_NAME + ".WindowMixin")) ||
               (RENDER_DIST && Objects.equals(mixinClassName, MIXIN_PACKAGE_NAME + ".OptionsMixin")) ||
               (AUDIO && Objects.equals(mixinClassName, MIXIN_PACKAGE_NAME + ".SoundEngineMixin"))
            )
        );
    }

    @Override
    public void onLoad(String mixinPackage) {
        MIXIN_PACKAGE_NAME = mixinPackage;
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}
