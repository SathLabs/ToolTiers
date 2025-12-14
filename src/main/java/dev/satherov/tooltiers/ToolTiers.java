package dev.satherov.tooltiers;

import lombok.Getter;

import dev.satherov.tooltiers.config.ConfigLoader;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.javafmlmod.FMLModContainer;

@Mod(ToolTiers.MOD_ID)
public class ToolTiers {
    
    public static final String MOD_ID = "tooltiers";
    private static @Getter ToolTiers instance;
    private final @Getter ConfigLoader config = new ConfigLoader();

    public ToolTiers(IEventBus bus, FMLModContainer container) {
        if (ToolTiers.instance != null) throw new IllegalStateException("ToolTiers is already initialized!");
        instance = this;
        this.config.discover(container);
    }
    
    @SubscribeEvent
    private static void onConfigLoad(final ModConfigEvent.Loading event) {
        ToolTiers.getInstance().getConfig().update(event.getConfig().getSpec());
    }
    
    @SubscribeEvent
    private static void onConfigReload(final ModConfigEvent.Reloading event) {
        ToolTiers.getInstance().getConfig().update(event.getConfig().getSpec());
    }
}
