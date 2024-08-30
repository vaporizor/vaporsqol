package com.github.vaporizor.vaporsqol.config;

@SuppressWarnings("unused")
public class Idle {
    private IdleModule fps;
    private IdleModule renderDistance;
    private boolean muteAudio;

    private Idle() {}

    public IdleModule getIdleFpsConfig() {
        return fps;
    }

    public IdleModule getIdleRenderDistanceConfig() {
        return renderDistance;
    }

    public boolean isMuteAudioEnabled() {
        return muteAudio;
    }

    public boolean anyEnabled() {
        return fps.isEnabled() || muteAudio || renderDistance.isEnabled();
    }

    @Override
    public String toString() {
        return "Idle{" +
                "\nfps=" + fps +
                ",\nrenderDistance=" + renderDistance +
                ",\naudio=" + muteAudio +
                "\n}";
    }
}
