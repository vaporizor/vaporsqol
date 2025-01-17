package com.github.vaporizor.vaporsqol;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import net.fabricmc.loader.api.FabricLoader;

@SuppressWarnings("unused")
public enum VQConfig {
    I;

    private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve(VaporsQOL.MOD_ID + ".json").toFile();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES).create();
    private static Data data;

    private record Data (
        boolean enabled,
        boolean borderless,
        boolean playSoundsWithNoVolume,
        Zoom zoom,
        Fullbright fullbright,
        /*
         * JSON config naming convention: all-lower-case-with-dashes
         * Java code naming convention: camelCase; acronyms always in upper case
         */
        @SerializedName("afk")
        AFK AFK
    ) {}

    private record Zoom (
        boolean enabled,
        @SerializedName("min-fov-modifier")
        float minFOVModifier,
        @SerializedName("max-fov-modifier")
        float maxFOVModifier,
        @SerializedName("fov-step")
        float FOVStep
    ) {}

    private record Fullbright (
        boolean enabled,
        boolean indicator
    ) {}

    private record AFK(
        int fpsLimit,
        int renderLimit,
        float volumeLimit
    ) {
        public AFK {
            if (fpsLimit < 1) {
                fpsLimit = 1;
                VaporsQOL.err("AFK FPS limit cannot be configured to 0; clamping to 1.");
            }
            if (renderLimit < 1) {
                renderLimit = 1;
                VaporsQOL.err("AFK render distance limit cannot be configured to 0; clamping to 1.");
            }
        }
    }

    static {
        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            data = GSON.fromJson(reader, Data.class);
        } catch (FileNotFoundException | JsonSyntaxException exception) {
            if (exception instanceof JsonSyntaxException) {
                VaporsQOL.err("Found bad JSON syntax in existing configuration, using default configuration values for this session.\n", exception);
                I.readDefault();
            } else {
                VaporsQOL.warn("Could not read an existing configuration file at " + CONFIG_FILE.getAbsolutePath() + ", attempting to create it.");
                I.readDefault();
                I.write();
            }
        } catch (IOException ioException) {
            VaporsQOL.err("Closing configuration file reader failed.", ioException);
        }
    }

    private void readDefault() {
        try (InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIG_FILE.getName())) {
            if (stream == null) throw new FileNotFoundException();
            data = GSON.fromJson(new String(stream.readAllBytes(), Charset.defaultCharset()), Data.class);
        } catch (IOException ioException) {
            VaporsQOL.err("Could not read default configuration; disabling Vapor's QOL.", ioException);
            data = new Data(false, false, false, null, null, null);
        }
    }

    private void write() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            JsonWriter jsonWriter = GSON.newJsonWriter(writer);
            jsonWriter.setIndent("    ");
            GSON.toJson(data, Data.class, jsonWriter);
            VaporsQOL.log("Successfully saved configuration!");
        } catch (IOException exception) {
            VaporsQOL.err("Configuration was not saved!", exception);
        }
    }

    // Shortcut methods
    public boolean enabled() {
        return data.enabled();
    }

    public boolean borderless() {
        return data.borderless();
    }

    public boolean playSoundsWithNoVolume() {
        return data.playSoundsWithNoVolume();
    }

    public boolean zoom() {
        return data.zoom().enabled();
    }

    public float minFOVModifier() {
        return data.zoom().minFOVModifier();
    }

    public float maxFOVModifier() {
        return data.zoom().maxFOVModifier();
    }

    public float FOVStep() {
        return data.zoom().FOVStep();
    }

    public boolean fullbright() {
        return data.fullbright().enabled();
    }

    public boolean indicator() {
        return data.fullbright().indicator();
    }

    public int fpsLimit() {
        return data.AFK().fpsLimit();
    }

    public int renderLimit() {
        return data.AFK().renderLimit();
    }

    public float volumeLimit() {
        return data.AFK().volumeLimit();
    }
}
