package com.github.vaporizor.vaporsqol.config;

@SuppressWarnings("unused")
public class Zoom {
    private boolean enabled;
    private float minFOVModifier;
    private float maxFOVModifier;
    private float maxFOVStep;

    private Zoom() {}

    public boolean isEnabled() {
        return enabled;
    }

    public float getMinFOVModifier() {
        return minFOVModifier;
    }

    public float getMaxFOVModifier() {
        return maxFOVModifier;
    }

    public float getMaxFOVStep() {
        return maxFOVStep;
    }

    @Override
    public String toString() {
        return "Zoom{" +
                "\nenabled=" + enabled +
                ",\nminFOVModifier=" + minFOVModifier +
                ",\nmaxFOVModifier=" + maxFOVModifier +
                ",\nmaxFOVStep=" + maxFOVStep +
                "\n}";
    }
}
