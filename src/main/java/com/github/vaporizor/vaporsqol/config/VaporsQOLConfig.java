package com.github.vaporizor.vaporsqol.config;

import net.fabricmc.loader.api.FabricLoader;

import com.github.vaporizor.vaporsqol.VaporsQOL;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

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
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static VaporsQOLConfig instance;

    private Zoom zoom;
    private FullBright fullbright;
    private Idle idle;


    private VaporsQOLConfig() {}

    public Zoom getZoomConfig() {
        return zoom;
    }

    public FullBright getBrightConfig() {
        return fullbright;
    }

    public Idle getIdleConfig() {
        return idle;
    }

    public static VaporsQOLConfig get() {
        if (instance != null) return instance;

        try {
            FileReader reader = new FileReader(CONFIG_FILE);
            instance = gson.fromJson(reader, VaporsQOLConfig.class);
        } catch (FileNotFoundException | JsonSyntaxException exception) {
            if (exception instanceof JsonSyntaxException)
                VaporsQOL.err("Found bad JSON syntax in existing configuration, reverting to default configuration.", exception);

            try (InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIG_FILE.getName())) {
                if (stream == null) throw new FileNotFoundException();

                String jsonString = new String(stream.readAllBytes(), Charset.defaultCharset());
                instance = gson.fromJson(jsonString, VaporsQOLConfig.class);
                instance.write();
            } catch (IOException ioException) {
                VaporsQOL.err("Could not load default configuration; mod initialization aborted.", ioException);
                // TODO: Abort mod load?
            }
        }

        if (instance.zoom.getMinFOVModifier() > instance.zoom.getMaxFOVModifier())
            VaporsQOL.err("Zoom feature may break! 'minFOVModifier' in configuration must be smaller than 'maxFOVModifier'");

        return instance;
    }

    protected void write() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            writer.write(gson.toJson(this));
        } catch (IOException exception) {
            VaporsQOL.err("Settings were not saved!", exception);
        }
    }

    @Override
    public String toString() {
        return "VaporsQOLConfig{\n" +
                "fullbright=" + fullbright +
                ",\nidle=" + idle +
                ",\nzoom=" + zoom +
                "\n}";
    }
}

