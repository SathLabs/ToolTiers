package dev.satherov.tooltiers;

import lombok.Getter;

import dev.satherov.tooltiers.common.ToolTier;
import dev.satherov.tooltiers.common.VanillaToolTiers;
import dev.satherov.tooltiers.config.TTConfig;
import dev.satherov.tooltiers.core.DataRegistry;
import dev.satherov.tooltiers.config.ConfigLoader;

import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.fml.javafmlmod.FMLModContainer;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

@Mod(ToolTiers.MOD_ID)
@EventBusSubscriber(modid = ToolTiers.MOD_ID)
public class ToolTiers {
    
    public static final String MOD_ID = "tooltiers";
    private static @Getter ToolTiers instance;
    private final @Getter ConfigLoader config = new ConfigLoader();
    
    private static final DeferredRegister<DataComponentType<?>> REGISTER = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, ToolTiers.MOD_ID);
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ToolTier>> COMPONENT = REGISTER.register("tool_tier", () -> DataComponentType.<ToolTier>builder()
            .persistent(ToolTier.CODEC)
            .networkSynchronized(ToolTier.STREAM_CODEC)
            .build()
    );
    
    public ToolTiers(IEventBus bus, FMLModContainer container) {
        if (ToolTiers.instance != null) throw new IllegalStateException("ToolTiers is already initialized!");
        instance = this;
        this.config.discover(container);
        REGISTER.register(bus);
    }
    
    @SubscribeEvent
    private static void onConfigLoad(final ModConfigEvent.Loading event) {
        ToolTiers.getInstance().getConfig().update(event.getConfig().getSpec());
    }
    
    @SubscribeEvent
    private static void onConfigReload(final ModConfigEvent.Reloading event) {
        ToolTiers.getInstance().getConfig().update(event.getConfig().getSpec());
    }
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    private static void onHarvestCheck(final PlayerEvent.HarvestCheck event) {
        BlockState state = event.getTargetBlock();
        if (state.isAir()) return;
        
        Player player = event.getEntity();
        ItemStack stack = player.getMainHandItem();
        event.setCanHarvest(ToolTier.canBreak(stack, state) && event.canHarvest());
    }
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    private static void onBreakSpeed(final PlayerEvent.BreakSpeed event) {
        BlockState state = event.getState();
        if (state.isAir()) return;
        event.setNewSpeed(ToolTier.fromState(state).equals(VanillaToolTiers.MISSING) && TTConfig.Common.isForced() ? 0.0F : event.getNewSpeed());
    }
    
    @SubscribeEvent
    private static void onCommonSetup(final FMLLoadCompleteEvent event) {
        DataRegistry.put(VanillaToolTiers.MISSING);
        DataRegistry.put(VanillaToolTiers.INDESTRUCTIBLE);
        DataRegistry.put(VanillaToolTiers.HAND);
        DataRegistry.put(VanillaToolTiers.WOOD);
        DataRegistry.put(VanillaToolTiers.STONE);
        DataRegistry.put(VanillaToolTiers.IRON);
        DataRegistry.put(VanillaToolTiers.GOLD);
        DataRegistry.put(VanillaToolTiers.DIAMOND);
        DataRegistry.put(VanillaToolTiers.NETHERITE);
        DataRegistry.load();
    }
    
    public static ResourceLocation loc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
