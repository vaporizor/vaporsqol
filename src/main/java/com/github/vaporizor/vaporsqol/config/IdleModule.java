package com.github.vaporizor.vaporsqol.config;

@SuppressWarnings("unused")
public class IdleModule {
    private boolean enabled;
    private int limit;

    private IdleModule() {}

    @Override
    public String toString() {
        return "IdleModule{" +
                "enabled=" + enabled +
                ", limit=" + limit +
                '}';
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getLimit() {
        return limit;
    }
}
