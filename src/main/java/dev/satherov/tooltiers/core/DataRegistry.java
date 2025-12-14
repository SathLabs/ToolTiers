package dev.satherov.tooltiers.core;

import lombok.extern.slf4j.Slf4j;

import dev.satherov.tooltiers.ToolTiers;
import dev.satherov.tooltiers.common.ToolTier;
import dev.satherov.tooltiers.common.VanillaToolTiers;

import net.neoforged.fml.loading.FMLPaths;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Stream;

@Slf4j
public class DataRegistry {
    
    private static boolean loaded = false;
    private static final Map<ResourceLocation, ToolTier> DATA = new LinkedHashMap<>();
    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
    
    public static synchronized void load() {
        if (loaded) return;
        
        Path directory = FMLPaths.CONFIGDIR.get().resolve(ToolTiers.MOD_ID).resolve("tiers");
        try {
            Files.createDirectories(directory);
        } catch (IOException e) {
            log.warn("Failed to create directory for early data registry", e);
        }
        
        if (Files.isDirectory(directory)) {
            try(Stream<Path> stream = Files.walk(directory)) {
                stream.filter(p -> Files.isRegularFile(p) && p.getFileName().toString().endsWith(".json"))
                        .sorted()
                        .forEach(DataRegistry::parse);
            } catch (IOException e) {
                log.warn("Failed to load early data registry", e);
            }
        }
        
        loaded = true;
    }
    
    private static void parse(Path file) {
        try (Reader reader = Files.newBufferedReader(file)) {
            JsonObject root = DataRegistry.GSON.fromJson(reader, JsonObject.class);
            ToolTier tier = ToolTier.CODEC.parse(JsonOps.INSTANCE, root).getOrThrow();
            DataRegistry.DATA.put(tier.identifier(), tier);
        } catch (Exception e) {
            log.error("Failed to parse {} tier", file.getFileName(), e);
        }
    }
    
    public static void put(ToolTier tier) {
        DATA.put(tier.identifier(), tier);
    }
    
    public static Map<ResourceLocation, ToolTier> data() {
        if  (!loaded) load();
        return DATA;
    }
}
