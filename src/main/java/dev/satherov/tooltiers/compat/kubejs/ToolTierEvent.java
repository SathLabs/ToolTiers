package dev.satherov.tooltiers.compat.kubejs;

import dev.satherov.tooltiers.common.ToolTier;
import dev.satherov.tooltiers.core.TierManager;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

import dev.latvian.mods.kubejs.event.KubeEvent;
import dev.latvian.mods.kubejs.typings.Info;

import java.util.Collection;

@Info("""
        Allows you to register tool tiers to specific items
        """)
public record ToolTierEvent(ToolTier tier) implements KubeEvent {
    
    @Info("""
            Registers an item to a tool tier
            """)
    public void add(Object... filters) {
        this.resolve(tier,  filters);
    }
    
    private void resolve(ToolTier tier, Object... filters) {
        for (Object filter : filters) {
            if (filter instanceof Collection<?> collection) {
                for (Object item : collection) {
                    this.resolve(tier, item);
                }
            } else {
                String string = String.valueOf(filter).trim();
                if (string.isEmpty()) return;
                TierManager.TOOLS.put(BuiltInRegistries.ITEM.get(ResourceLocation.parse(string)), tier);
            }
        }
    }
}
