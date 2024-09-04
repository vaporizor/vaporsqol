package com.github.vaporizor.vaporsqol;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonWriter;

import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.client.Options;

import static java.lang.Math.signum;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

@SuppressWarnings("unused")
public class VaporsQOLConfig {
    private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve(VaporsQOL.MOD_ID + ".json").toFile();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static VaporsQOLConfig INSTANCE;

    private boolean enabled;
    private boolean borderless;
    private boolean playNoVolumeSounds;
    private ZoomConfig zoom;
    private FullBrightConfig fullbright;
    private IdleConfig idle;


    public record ZoomConfig (
        boolean enabled,
        float minFOVModifier,
        float maxFOVModifier,
        float maxFOVStep
    ) {}

    // Not a record because FullBrightConfig.toggled needs to be changed when the player toggles fullbright on / off.
    public static class FullBrightConfig {
        private boolean enabled;
        private boolean toggled;
        private boolean visualIndicator;

        private FullBrightConfig() {}

        public boolean enabled() {
            return enabled;
        }
        public boolean toggled() {
            return toggled;
        }
        public boolean visualIndicator() {
            return visualIndicator;
        }

        public void setToggled(boolean toggled) {
            final VaporsQOLConfig CONFIG = VaporsQOLConfig.get();
            if (!CONFIG.enabled()) return;

            this.toggled = toggled;
            CONFIG.write();
        }

        @Override
        public String toString() {
            return "FullBright{" +
                    "\nenabled=" + enabled +
                    ",\ntoggled=" + toggled +
                    ",\nvisualIndicator=" + visualIndicator +
                    "\n}";
        }
    }

    public record IdleConfig (
        IntModule fps,
        IntModule renderDistance,
        FloatModule audio
    ) {
        /* Needs to be force disabled if `limit` is a nonsensical value such as a negative number, hence not a record.
         * See get() static method below
         *
         * Generics were avoided here to make use of Gson's automatic type validation, otherwise we'd have to manually
         * validate that `limit` is an integer / floating point value.
         */
        public static class IntModule {
            int limit;
            boolean enabled;
            
            public boolean enabled() {
                return enabled;
            }
            public int limit() {
                return limit;
            }
        }
        public static class FloatModule {
            float limit;
            boolean enabled;
            
            public boolean enabled() {
                return enabled;
            }
            public float limit() {
                return limit;
            }
        }
    }

    private VaporsQOLConfig() {}

    public boolean enabled() {
        return enabled;
    }

    public boolean borderless() {
        return borderless;
    }

    public boolean playNoVolumeSounds() {
        return playNoVolumeSounds;
    }

    public ZoomConfig zoomConfig() {
        return zoom;
    }

    public FullBrightConfig brightConfig() {
        return fullbright;
    }

    public IdleConfig idleConfig() {
        return idle;
    }

    public static VaporsQOLConfig get() {
        if (INSTANCE != null) return INSTANCE;

        try {
            INSTANCE = GSON.fromJson(new FileReader(CONFIG_FILE), VaporsQOLConfig.class);
        } catch (FileNotFoundException | JsonSyntaxException exception) {
            if (exception instanceof JsonSyntaxException)
                VaporsQOL.err("Found bad JSON syntax in existing configuration, reverting to default configuration.", exception);

            try (InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIG_FILE.getName())) {
                if (stream == null) throw new FileNotFoundException();

                INSTANCE = GSON.fromJson(
                    new String(stream.readAllBytes(), Charset.defaultCharset()),
                    VaporsQOLConfig.class
                );
                INSTANCE.write();
            } catch (IOException ioException) {
                VaporsQOL.err("Could not load default configuration; disabling Vapor's QOL.", ioException);
                INSTANCE = new VaporsQOLConfig();
                INSTANCE.enabled = false;
                return INSTANCE;
            }
        }

        if (INSTANCE.enabled) {
            if (INSTANCE.zoom.minFOVModifier >= INSTANCE.zoom.maxFOVModifier)
                VaporsQOL.err("Zoom adjustment feature may not work properly! 'minFOVModifier' in configuration must be smaller than 'maxFOVModifier'");
                // No need to do anything else because the feature will fail without crashing the rest of the mod

            final IdleConfig IDLE_CONFIG = INSTANCE.idle;
            if (
                IDLE_CONFIG.fps.enabled &&
                notWithin(IDLE_CONFIG.fps.limit, 1, Integer.MAX_VALUE)
            ) {
                VaporsQOL.err("FPS limit must not be below 1 or above the integer limit; disabling framerate limiting.");
                IDLE_CONFIG.fps.enabled = false;
                INSTANCE.write();
            }
            if (
                IDLE_CONFIG.renderDistance.enabled &&
                notWithin(IDLE_CONFIG.renderDistance.limit, 1, Options.RENDER_DISTANCE_EXTREME)
            ) {
                VaporsQOL.err("Render distance limit must not be below 1 or above the maximum render distance; disabling render distance limiting.");
                IDLE_CONFIG.renderDistance.enabled = false;
                INSTANCE.write();
            }
            if (
                IDLE_CONFIG.audio.enabled &&
                notWithin(IDLE_CONFIG.audio.limit, 0F, 1F)
            ) {
                VaporsQOL.err("Audio limit must be a number between 0 and 1; disabling audio limiting.");
                IDLE_CONFIG.audio.enabled = false;
                INSTANCE.write();
            }
        }

        return INSTANCE;
    }

    private void write() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            final JsonWriter jsonWriter = GSON.newJsonWriter(writer);
            jsonWriter.setIndent("    ");
            GSON.toJson(this, VaporsQOLConfig.class, jsonWriter);
        } catch (IOException exception) {
            VaporsQOL.err("Settings were not saved!", exception);
        }
    }

    private static <N extends Number & Comparable<N>> boolean notWithin(N val, N min, N max) {
        return signum(val.compareTo(min)) == -1 || signum(val.compareTo(max)) == 1;
    }

    @Override
    public String toString() {
        return "VaporsQOLConfig{\n" +
                "enabled=" + enabled +
                "\nfullbright=" + fullbright +
                ",\nidle=" + idle +
                ",\nzoom=" + zoom +
                "\n}";
    }
}

