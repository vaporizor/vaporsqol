package com.github.vaporizor.vaporsqol;

import com.github.vaporizor.vaporsqol.config.VaporsQOLConfig;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class VaporsQOLMixinPlugin implements IMixinConfigPlugin {
    private static final String packageName = "com.github.vaporizor.vaporsqol.mixin";

    @Override
    public boolean shouldApplyMixin(String unused, String mixinClassName) {
        VaporsQOLConfig config = VaporsQOLConfig.get();
        boolean zoom = config.getZoomConfig().isEnabled();
        boolean bright = config.getBrightConfig().isEnabled();
        boolean fps = config.getIdleConfig().getIdleFpsConfig().isEnabled();
        boolean renderDist = config.getIdleConfig().getIdleRenderDistanceConfig().isEnabled();
        boolean audio = config.getIdleConfig().isMuteAudioEnabled();

        return (
               (zoom && Objects.equals(mixinClassName, packageName + ".AbstractClientPlayerMixin"))
            || (zoom && Objects.equals(mixinClassName, packageName + ".MouseHandlerMixin"))
            || (zoom && Objects.equals(mixinClassName, packageName + ".GameRendererMixin"))
            || (bright && Objects.equals(mixinClassName, packageName + ".LightTextureMixin"))
            || ((fps) && Objects.equals(mixinClassName, packageName + ".WindowMixin"))
            || (renderDist && Objects.equals(mixinClassName, packageName + ".OptionsMixin"))
            || (audio && Objects.equals(mixinClassName, packageName + ".SoundEngineMixin"))
        );
    }

    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
