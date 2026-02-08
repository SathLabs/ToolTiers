package dev.satherov.tooltiers.core;

import dev.satherov.tooltiers.common.ToolTier;

import net.minecraft.resources.ResourceLocation;

import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DataHolder {
    
    private static final Map<ResourceLocation, ToolTier> DATA = new LinkedHashMap<>();
    private static volatile boolean FROZEN = false;
    
    private static List<ToolTier> ALL = List.of();
    private static List<ToolTier> TIERS = List.of();
    private static List<ToolTier> UNIQUE = List.of();
    
    public static void put(ToolTier tier) {
        if (FROZEN) throw new IllegalStateException("Tooltier data holder is already frozen");
        DATA.put(tier.identifier(), tier);
    }
    
    public static ToolTier get(ResourceLocation identifier) {
        return DATA.get(identifier);
    }
    
    public static void freeze() {
        if (FROZEN) return;
        
        List<ToolTier> all = new ArrayList<>(DATA.values());
        all.sort(Comparator.comparingInt(ToolTier::level).thenComparing(ToolTier::name));
        
        List<ToolTier> unique = new ArrayList<>();
        List<ToolTier> numeric = new ArrayList<>();
        
        for (ToolTier tier : all) {
            if (tier.level() == -1) unique.add(tier);
            else numeric.add(tier);
        }
        
        Collections.reverse(all);
        Collections.reverse(numeric);
        
        ALL = List.copyOf(all);
        TIERS = List.copyOf(numeric);
        UNIQUE = List.copyOf(unique);
        
        FROZEN = true;
    }
    
    /**
     * Returns a list of all tiers
     *
     * @return List of all tiers
     */
    public static @Unmodifiable List<ToolTier> all() {
        return ALL;
    }
    
    /**
     * Returns a list of all unique tiers
     *
     * @return List of all unique tiers
     */
    public static @Unmodifiable List<ToolTier> unique() {
        return UNIQUE;
    }
    
    /**
     * Returns a list of all tiers excluding unique tiers
     *
     * @return List of all tiers excluding unique tiers
     */
    public static @Unmodifiable List<ToolTier> tiers() {
        return TIERS;
    }
}
