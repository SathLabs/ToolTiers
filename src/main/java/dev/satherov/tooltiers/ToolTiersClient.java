package dev.satherov.tooltiers;

import dev.satherov.tooltiers.client.TTLanguage;
import dev.satherov.tooltiers.common.ToolTier;
import dev.satherov.tooltiers.common.VanillaToolTiers;
import dev.satherov.tooltiers.config.TTConfig;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

@Mod(value = ToolTiers.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = ToolTiers.MOD_ID, value = Dist.CLIENT)
public class ToolTiersClient {
    
    public ToolTiersClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    private static void onItemTooltip(final ItemTooltipEvent event) {
        if (!TTConfig.Client.isTooltip()) return;
        ItemStack stack = event.getItemStack();
        
        if (stack.getItem() instanceof BlockItem || stack.has(ToolTiers.COMPONENT)) {
            ToolTier tier = ToolTier.fromStack(stack);
            if (tier.equals(VanillaToolTiers.MISSING) && TTConfig.Client.isWarning()) ToolTiersClient.insert(event.getToolTip(), TTLanguage.TOOLTIP_MISSING_TIER.text(ChatFormatting.RED));
            else ToolTiersClient.insert(event.getToolTip(), tier.display());
        }
    }
    
    private static void insert(List<Component> tooltip, Component component) {
        if (tooltip.size() > 1) tooltip.add(1, component);
        else tooltip.add(component);
    }
}
