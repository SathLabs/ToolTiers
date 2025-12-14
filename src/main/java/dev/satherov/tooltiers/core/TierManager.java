package dev.satherov.tooltiers.core;

import dev.satherov.tooltiers.ToolTiers;
import dev.satherov.tooltiers.common.ToolTier;
import dev.satherov.tooltiers.common.VanillaToolTiers;
import dev.satherov.tooltiers.compat.kubejs.TTEvents;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.concurrent.ConcurrentHashMap;

@EventBusSubscriber(modid = ToolTiers.MOD_ID)
public class TierManager {
    
    public static final ConcurrentHashMap<Item, ToolTier> TOOLS = new ConcurrentHashMap<>();
    
    @SubscribeEvent
    private static void onModifyDefaultComponents(final ModifyDefaultComponentsEvent event) {
        TOOLS.put(Items.WOODEN_HOE, VanillaToolTiers.WOOD);
        TOOLS.put(Items.WOODEN_SHOVEL, VanillaToolTiers.WOOD);
        TOOLS.put(Items.WOODEN_AXE, VanillaToolTiers.WOOD);
        TOOLS.put(Items.WOODEN_PICKAXE, VanillaToolTiers.WOOD);
        TOOLS.put(Items.WOODEN_SWORD, VanillaToolTiers.WOOD);
        
        TOOLS.put(Items.STONE_HOE, VanillaToolTiers.STONE);
        TOOLS.put(Items.STONE_SHOVEL, VanillaToolTiers.STONE);
        TOOLS.put(Items.STONE_AXE, VanillaToolTiers.STONE);
        TOOLS.put(Items.STONE_PICKAXE, VanillaToolTiers.STONE);
        TOOLS.put(Items.STONE_SWORD, VanillaToolTiers.STONE);
        
        TOOLS.put(Items.IRON_HOE, VanillaToolTiers.IRON);
        TOOLS.put(Items.IRON_SHOVEL, VanillaToolTiers.IRON);
        TOOLS.put(Items.IRON_AXE, VanillaToolTiers.IRON);
        TOOLS.put(Items.IRON_PICKAXE, VanillaToolTiers.IRON);
        TOOLS.put(Items.IRON_SWORD, VanillaToolTiers.IRON);
        
        TOOLS.put(Items.GOLDEN_HOE, VanillaToolTiers.GOLD);
        TOOLS.put(Items.GOLDEN_SHOVEL, VanillaToolTiers.GOLD);
        TOOLS.put(Items.GOLDEN_AXE, VanillaToolTiers.GOLD);
        TOOLS.put(Items.GOLDEN_PICKAXE, VanillaToolTiers.GOLD);
        TOOLS.put(Items.GOLDEN_SWORD, VanillaToolTiers.GOLD);
        
        TOOLS.put(Items.DIAMOND_HOE, VanillaToolTiers.DIAMOND);
        TOOLS.put(Items.DIAMOND_SHOVEL, VanillaToolTiers.DIAMOND);
        TOOLS.put(Items.DIAMOND_AXE, VanillaToolTiers.DIAMOND);
        TOOLS.put(Items.DIAMOND_PICKAXE, VanillaToolTiers.DIAMOND);
        TOOLS.put(Items.DIAMOND_SWORD, VanillaToolTiers.DIAMOND);
        
        TOOLS.put(Items.NETHERITE_HOE, VanillaToolTiers.NETHERITE);
        TOOLS.put(Items.NETHERITE_SHOVEL, VanillaToolTiers.NETHERITE);
        TOOLS.put(Items.NETHERITE_AXE, VanillaToolTiers.NETHERITE);
        TOOLS.put(Items.NETHERITE_PICKAXE, VanillaToolTiers.NETHERITE);
        TOOLS.put(Items.NETHERITE_SWORD, VanillaToolTiers.NETHERITE);
        
        if (ModList.get().isLoaded("kubejs")) TTEvents.dispatch();
        
        event.getAllItems().forEach(item -> { 
            ToolTier tier = TOOLS.getOrDefault(item, VanillaToolTiers.MISSING);
            if (tier.equals(VanillaToolTiers.MISSING) && !item.components().has(DataComponents.TOOL)) return ;
            event.modify(item, builder -> builder.set(ToolTiers.COMPONENT.get(), tier));
        });
        
        TOOLS.clear();
    }
}
