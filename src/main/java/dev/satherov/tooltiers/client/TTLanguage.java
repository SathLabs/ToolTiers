package dev.satherov.tooltiers.client;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import dev.satherov.tooltiers.ToolTiers;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum TTLanguage {
    TOOLTIP_TIER("tooltip", "tier", "Tier: %s"),
    TOOLTIP_MISSING_TIER("tooltip", "missing_tier", "No tier assigned. Report this to the pack author"),
    TIER_PLUGIN(String.format("config.jade.plugin_%s.%s", ToolTiers.MOD_ID, "tier"), "Tool Tier");
    
    private final String key;
    private final String translation;
    
    TTLanguage(String type, String key, String translation) {
        this(Util.makeDescriptionId(type, ToolTiers.loc(key)), translation);
    }
    
    public MutableComponent text() {
        return Component.translatable(this.key());
    }
    
    public MutableComponent text(Object... args) {
        List<ChatFormatting> formatting = new ArrayList<>();
        List<Object> filtered = new ArrayList<>();
        
        for (Object arg : args) {
            if (arg == null) continue;
            if (arg instanceof ChatFormatting format) {
                formatting.add(format);
            } else {
                filtered.add(arg);
            }
        }
        
        MutableComponent component = Component.translatable(this.key());
        
        if (!filtered.isEmpty()) {
            component = Component.translatable(this.key(), filtered.toArray());
        }
        
        if (!formatting.isEmpty()) {
            component = component.withStyle(formatting.toArray(new ChatFormatting[0]));
        }
        
        return component;
    }
    
    public static void translate(BiConsumer<String, String> consumer) {
        for (TTLanguage lang : TTLanguage.values()) {
            consumer.accept(lang.key(), lang.translation());
        }
    }
}
