package com.github.vaporizor.vaporsqol.config;

@SuppressWarnings("unused")
public class FullBright {
    private boolean enabled;
    private boolean toggled;
    private float gamma;

    private FullBright() {}

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isToggled() {
        return toggled;
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;
        VaporsQOLConfig.get().write();
    }

    public float getGamma() {
        return gamma;
    }

    @Override
    public String toString() {
        return "FullBright{" +
                "\nenabled=" + enabled +
                ",\ntoggled=" + toggled +
                ",\ngamma=" + gamma +
                "\n}";
    }
}
