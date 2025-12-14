package dev.satherov.tooltiers.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import dev.satherov.tooltiers.ToolTiers;
import dev.satherov.tooltiers.config.annotation.Config;
import dev.satherov.tooltiers.config.annotation.ConfigVal;

import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.javafmlmod.FMLModContainer;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiConsumer;

@Slf4j
public class ConfigLoader {
    
    private final List<Cache> caches = new ArrayList<>();
    
    public static ConfigLoader create() {
        return new ConfigLoader();
    }
    
    public void discover(FMLModContainer container) {
        ConfigLoader.log.info("Discovering configs for container {}", container.getModId());
        
        Map<ModConfig.Type, List<Class<?>>> configs = new HashMap<>();
        for (Class<?> clazz : TTConfig.class.getDeclaredClasses()) {
            if (clazz.isAnnotationPresent(Config.class)) {
                Config config = clazz.getAnnotation(Config.class);
                ModConfig.Type type = config.value();
                configs.computeIfAbsent(type, k -> new ArrayList<>()).add(clazz);
                ConfigLoader.log.info("Found config of type {} for class {}", type, clazz);
            } else {
                ConfigLoader.log.warn("Class {} is not annotated with @Config", clazz.getName());
            }
        }
        
        configs.forEach((type, list) -> {
            list.forEach(clazz -> {
                Cache cache = new Cache();
                
                ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
                this.generate(clazz, cache, builder);
                ModConfigSpec spec = builder.build();
                
                cache.setSpec(spec);
                this.caches.add(cache);
                
                container.registerConfig(type, spec, ToolTiers.MOD_ID + "/" + ToolTiers.MOD_ID + "-" + type.extension() + ".toml");
                ConfigLoader.log.info("Registered config of type {} for class {} at {}", type, clazz, ToolTiers.MOD_ID + "/" + ToolTiers.MOD_ID + "-" + type.extension() + ".toml");
            });
        });
    }
    
    public void update(IConfigSpec spec) {
        this.caches.forEach(cache -> {
            if (cache.getSpec() == spec) {
                cache.getValues().forEach((field, value) -> {
                    try {
                        field.setAccessible(true);
                        field.set(null, value.get());
                    } catch (IllegalAccessException e) {
                        ConfigLoader.log.error("Failed to update config field {}", field.getName(), e);
                    }
                });
            }
        });
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected void generate(Class<?> parent, Cache cache, ModConfigSpec.Builder builder) {
        for (Class<?> clazz : parent.getDeclaredClasses()) {
            if (clazz.isAnnotationPresent(Config.Group.class)) {
                Config.Group group = clazz.getAnnotation(Config.Group.class);
                String name = group.value().isEmpty() ? clazz.getSimpleName() : group.value();
                builder.push(name);
                this.generate(clazz, cache, builder);
                builder.pop();
            }
        }
        
        for (Field field : parent.getDeclaredFields()) {
            if (!field.isAnnotationPresent(ConfigVal.class)) continue;
            ConfigVal val = field.getAnnotation(ConfigVal.class);
            String name = !val.name().isBlank() ? val.name() : ConfigLoader.pretty(field.getName());
            String[] comments = val.comment().contains("\\R") ? val.comment().split("\\R") : new String[]{ val.comment() };
            
            try {
                field.setAccessible(true);
                Object obj = field.get(null);
                ModConfigSpec.ConfigValue<?> spec;
                
                builder.comment(".");
                if (field.isAnnotationPresent(ConfigVal.String.class)) {
                    if (obj == null) throw new NullPointerException("Config Field " + field.getName() + " is null");
                    if (!(obj instanceof String value)) throw new IllegalArgumentException("Config Field " + field.getName() + " is not a String");
                    for (String comment : comments) builder.comment(" " + comment);
                    builder.comment(" Default: " + value);
                    spec = builder.define(name, value);
                    
                } else if (field.isAnnotationPresent(ConfigVal.Boolean.class)) {
                    if (obj == null) throw new NullPointerException("Config Field " + field.getName() + " is null");
                    if (!(obj instanceof Boolean value)) throw new IllegalArgumentException("Config Field " + field.getName() + " is not a Boolean");
                    for (String comment : comments) builder.comment(" " + comment);
                    builder.comment(" Default: " + value);
                    spec = builder.define(name, (boolean) value); //Thank you neo
                    
                } else if (field.isAnnotationPresent(ConfigVal.Integer.class)) {
                    ConfigVal.Integer entry = field.getAnnotation(ConfigVal.Integer.class);
                    if (obj == null) throw new NullPointerException("Config Field " + field.getName() + " is null");
                    if (!(obj instanceof Integer value)) throw new IllegalArgumentException("Config Field " + field.getName() + " is not an Integer");
                    for (String comment : comments) builder.comment(" " + comment);
                    spec = builder.defineInRange(name, value, entry.min(), entry.max());
                    
                } else if (field.isAnnotationPresent(ConfigVal.Long.class)) {
                    ConfigVal.Long entry = field.getAnnotation(ConfigVal.Long.class);
                    if (obj == null) throw new NullPointerException("Config Field " + field.getName() + " is null");
                    if (!(obj instanceof Long value)) throw new IllegalArgumentException("Config Field " + field.getName() + " is not a Long");
                    for (String comment : comments) builder.comment(" " + comment);
                    spec = builder.defineInRange(name, value, entry.min(), entry.max());
                    
                } else if (field.isAnnotationPresent(ConfigVal.Double.class)) {
                    ConfigVal.Double entry = field.getAnnotation(ConfigVal.Double.class);
                    if (obj == null) throw new NullPointerException("Config Field " + field.getName() + " is null");
                    if (!(obj instanceof Double value)) throw new IllegalArgumentException("Config Field " + field.getName() + " is not a Double");
                    for (String comment : comments) builder.comment(" " + comment);
                    spec = builder.defineInRange(name, value, entry.min(), entry.max());
                    
                } else if (field.isAnnotationPresent(ConfigVal.Enum.class)) {
                    ConfigVal.Enum entry = field.getAnnotation(ConfigVal.Enum.class);
                    if (obj == null) throw new NullPointerException("Config Field " + field.getName() + " is null");
                    if (!(obj instanceof ConfigEnum value)) throw new IllegalArgumentException("Config Field " + field.getName() + " is not a Config Enum");
                    for (String comment : comments) builder.comment(" " + comment);
                    builder.comment(" Default: " + obj);
                    Arrays.stream(entry.value().getEnumConstants()).forEach(e -> {
                        ConfigEnum cfg = (ConfigEnum) e;
                        builder.comment(" " + cfg.name() + " - " + cfg.comment());
                    });
                    spec = builder.defineEnum(name, (Enum) value);
                    
                } else throw new IllegalArgumentException("Field is annotated with @ConfigVal, but has no defined type");
                cache.values.put(field, spec);
                
            } catch (IllegalAccessException e) {
                ConfigLoader.log.error("Failed to access field {}", field.getName(), e);
            } catch (NullPointerException e) {
                ConfigLoader.log.error("Config Field value {} is null or not static", field.getName(), e);
            }
        }
    }
    
    @NoArgsConstructor
    protected static class Cache {
        public @Getter @Setter ModConfigSpec spec;
        public @Getter Map<Field, ModConfigSpec.ConfigValue<?>> values = new HashMap<>();
    }
    
    public void translate(BiConsumer<String, String> consumer) {
        this.caches.forEach(cache -> {
            cache.values.forEach((key, value) -> {
                String name = value.getPath().getLast();
                consumer.accept(ToolTiers.MOD_ID + ".configuration." + name, name);
            });
        });
    }
    
    private static String pretty(String string) {
        String result = string.toLowerCase(Locale.ROOT).trim();
        result = result.replaceAll("_", " ");
        result = result.replaceAll("\\.", " ");
        String[] words = result.split(" ");
        StringBuilder builder = new StringBuilder();
        for (String word : words) {
            builder.append(word.substring(0, 1).toUpperCase(Locale.ROOT)).append(word.substring(1)).append(" ");
        }
        return builder.toString().trim();
    }
}
