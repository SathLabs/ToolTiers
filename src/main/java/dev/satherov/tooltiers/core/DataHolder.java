package dev.satherov.tooltiers.core;

import lombok.extern.slf4j.Slf4j;

import dev.satherov.tooltiers.common.ToolTier;

import net.minecraft.resources.ResourceLocation;

import com.google.common.collect.ImmutableSet;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
public class DataHolder {
    
    private static final Map<ResourceLocation, ToolTier> DATA = new LinkedHashMap<>();
    
    public static void put(ToolTier tier) {
        DATA.put(tier.identifier(), tier);
    }
    
    public static Set<ToolTier> values() {
        return ImmutableSet.copyOf(DATA.values());
    }
}
