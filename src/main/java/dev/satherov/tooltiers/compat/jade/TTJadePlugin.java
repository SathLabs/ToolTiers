package dev.satherov.tooltiers.compat.jade;

import dev.satherov.tooltiers.ToolTiers;
import dev.satherov.tooltiers.client.TTLanguage;
import dev.satherov.tooltiers.common.ToolTier;
import dev.satherov.tooltiers.common.VanillaToolTiers;
import dev.satherov.tooltiers.config.TTConfig;

import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;

import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import snownee.jade.api.config.IPluginConfig;

@WailaPlugin
public class TTJadePlugin implements IWailaPlugin {
    
    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(ToolTierProvider.INSTANCE, Block.class);
    }
    
    private static final class ToolTierProvider implements IBlockComponentProvider {
        
        public static final ResourceLocation TIER = ToolTiers.loc("tier");
        public static final ToolTierProvider INSTANCE = new ToolTierProvider();
        
        @Override
        public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
            BlockState state = accessor.getBlockState();
            if (state.getBlock() instanceof LiquidBlock) return;
            
            ToolTier tier = ToolTier.fromState(accessor.getBlockState());
            if (tier.equals(VanillaToolTiers.MISSING) && TTConfig.Client.isWarning()) tooltip.add(TTLanguage.TOOLTIP_MISSING_TIER.text(ChatFormatting.RED));
            else tooltip.add(tier.display());
        }
        
        @Override
        public ResourceLocation getUid() {
            return TIER;
        }
        
        @Override
        public int getDefaultPriority() {
            return -5000;
        }
    }
}
