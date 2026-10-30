package lbarto12.stacks.config;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import lbarto12.stacks.MassiveStacks;
import lbarto12.stacks.StackConfig;
import net.fabricmc.loader.api.FabricLoader;

public final class ConfigFile {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private ConfigFile() {
    }

    public static MassiveStacksConfig load() {
        Path path = path();
        if (!Files.exists(path)) {
            MassiveStacksConfig defaults = new MassiveStacksConfig();
            save(defaults);
            return defaults;
        }

        try (Reader reader = Files.newBufferedReader(path)) {
            MassiveStacksConfig config = GSON.fromJson(reader, MassiveStacksConfig.class);
            return config != null ? config : new MassiveStacksConfig();
        } catch (IOException | JsonParseException e) {
            MassiveStacks.LOGGER.error("Failed to read config {}, using defaults", path, e);
            return new MassiveStacksConfig();
        }
    }

    public static void save(MassiveStacksConfig config) {
        Path path = path();
        try (Writer writer = Files.newBufferedWriter(path)) {
            GSON.toJson(config, writer);
        } catch (IOException e) {
            MassiveStacks.LOGGER.error("Failed to write config {}", path, e);
        }
    }

    private static Path path() {
        return FabricLoader.getInstance().getConfigDir().resolve(StackConfig.CONFIG_FILE_NAME);
    }
}
