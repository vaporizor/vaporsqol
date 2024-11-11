package com.github.vaporizor.vaporsqol;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class VQMixinPlugin implements IMixinConfigPlugin {
    private static String MIXIN_PACKAGE_NAME;

    @Override
    public boolean shouldApplyMixin(String unused, String mixinClassName) {
        final boolean ZOOM = VQConfig.I.zoom();
        final boolean FB = VQConfig.I.fullbright();
        final boolean FPS = VQConfig.I.fps() > 0;
        final boolean RENDER_DIST = VQConfig.I.render() > 0;
        final boolean AUDIO = VQConfig.I.volume() >= 0;
        final boolean NO_VOLUME_SOUND = VQConfig.I.playSoundsWithNoVolume();

        return (
            VQConfig.I.enabled() && (
               (ZOOM && Objects.equals(mixinClassName, MIXIN_PACKAGE_NAME + ".AbstractClientPlayerMixin")) ||
               (ZOOM && Objects.equals(mixinClassName, MIXIN_PACKAGE_NAME + ".MouseHandlerMixin")) ||
               ((ZOOM || RENDER_DIST) && Objects.equals(mixinClassName, MIXIN_PACKAGE_NAME + ".GameRendererMixin")) ||
               (FB && Objects.equals(mixinClassName, MIXIN_PACKAGE_NAME + ".LivingEntityMixin")) ||
               ((FPS || AUDIO) && Objects.equals(mixinClassName, MIXIN_PACKAGE_NAME + ".MinecraftMixin")) ||
               (NO_VOLUME_SOUND && Objects.equals(mixinClassName, MIXIN_PACKAGE_NAME + ".SoundManagerMixin")) ||
               (NO_VOLUME_SOUND && Objects.equals(mixinClassName, MIXIN_PACKAGE_NAME + ".SoundEngineMixin"))
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
