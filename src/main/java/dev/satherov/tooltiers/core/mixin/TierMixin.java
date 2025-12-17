package dev.satherov.tooltiers.core.mixin;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.block.Block;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(Tier.class)
public interface TierMixin {
    
    @Shadow
    float getSpeed();
    
    /**
     * @author Satherov
     * @reason The {@link Tier#getIncorrectBlocksForDrops()} collection is worthless for us
     */
    @Overwrite
    default Tool createToolProperties(TagKey<Block> type) {
        return new Tool(List.of(Tool.Rule.minesAndDrops(type, this.getSpeed())), 1.0F, 1);
    }
}
