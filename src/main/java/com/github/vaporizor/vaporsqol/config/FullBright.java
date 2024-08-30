package com.github.vaporizor.vaporsqol.config;

@SuppressWarnings("unused")
public class FullBright {
    private boolean enabled;
    private boolean toggled;

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

    @Override
    public String toString() {
        return "FullBright{" +
                "\nenabled=" + enabled +
                ",\ntoggled=" + toggled +
                "\n}";
    }
}
