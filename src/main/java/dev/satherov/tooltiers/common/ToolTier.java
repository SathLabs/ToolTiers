package dev.satherov.tooltiers.common;

import lombok.Getter;
import lombok.experimental.Accessors;

import dev.satherov.tooltiers.ToolTiers;
import dev.satherov.tooltiers.client.TTLanguage;
import dev.satherov.tooltiers.config.TTConfig;
import dev.satherov.tooltiers.core.DataHolder;
import dev.satherov.tooltiers.util.StringUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Accessors(fluent = true)
public class ToolTier implements Comparable<ToolTier> {
    
    public static final Codec<ToolTier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(ToolTier::name),
            Codec.INT.fieldOf("color").forGetter(ToolTier::color),
            Codec.INT.optionalFieldOf("level", -1).forGetter(ToolTier::level)
    ).apply(instance, ToolTier::new));
    
    public static final StreamCodec<RegistryFriendlyByteBuf, ToolTier> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, ToolTier::name,
            ByteBufCodecs.VAR_INT, ToolTier::color,
            ByteBufCodecs.VAR_INT, ToolTier::level,
            ToolTier::new
    );
    
    private final @Getter ResourceLocation identifier;
    private final @Getter String name;
    private final @Getter int color;
    private final @Getter int level;
    
    private final @Getter TagKey<Block> tag;
    
    private final @Getter String key;
    private final @Getter String translation;
    private final @Getter MutableComponent display;
    
    public static ToolTier create(String name, int color, int level) {
        return new ToolTier(name, color, level);
    }
    
    private ToolTier(String name, int color, int level) {
        this.identifier = ToolTiers.loc(name);
        this.name = name;
        this.color = color;
        this.level = level;
        
        this.tag = TagKey.create(Registries.BLOCK, ToolTiers.loc(String.format("requires/%s", name)));
        
        this.key = Util.makeDescriptionId("tier", identifier);
        this.translation = StringUtil.pretty(name);
        
        MutableComponent component = Component.translatable(this.key);
        component.setStyle(Style.EMPTY.withColor(color));
        if (this.level > 0 && this.level < Integer.MAX_VALUE) component.append(Component.literal(String.format(" (%d)", level)));
        if (this.level == Integer.MAX_VALUE) component.append(Component.literal(" (∞)"));
        this.display = TTLanguage.TOOLTIP_TIER.text(ChatFormatting.GRAY, component);
    }
    
    public static ToolTier fromStack(ItemStack stack) {
        if (stack.isEmpty()) return VanillaToolTiers.HAND;
        if (stack.getItem() instanceof BlockItem block) return ToolTier.fromState(block.getBlock().defaultBlockState());
        @Nullable ToolTier tier = stack.get(ToolTiers.COMPONENT);
        return tier != null ? tier : VanillaToolTiers.MISSING;
    }
    
    public static ToolTier fromState(BlockState state) {
        if (state.is(VanillaToolTiers.INDESTRUCTIBLE.tag())) return VanillaToolTiers.INDESTRUCTIBLE;
        if (state.isAir() || !state.requiresCorrectToolForDrops()) return VanillaToolTiers.HAND;
        
        for (ToolTier unique : DataHolder.unique()) {
            if (state.is(unique.tag())) return unique;
        }
        
        for (ToolTier tier : DataHolder.tiers()) {
            if (state.is(tier.tag())) return tier;
        }
        
        if (TTConfig.Common.isLenient() && (
                state.is(BlockTags.MINEABLE_WITH_PICKAXE)
                        || state.is(BlockTags.MINEABLE_WITH_AXE)
                        || state.is(BlockTags.MINEABLE_WITH_SHOVEL)
                        || state.is(BlockTags.MINEABLE_WITH_HOE)
                        || state.is(BlockTags.SWORD_EFFICIENT)
        )) {
            return VanillaToolTiers.WOOD;
        }
        
        return VanillaToolTiers.MISSING;
    }
    
    public static boolean canBreak(ItemStack stack, BlockState state) {
        ToolTier block = ToolTier.fromState(state);
        if (block.equals(VanillaToolTiers.HAND)) return true;
        if (block.equals(VanillaToolTiers.INDESTRUCTIBLE)) return false;
        if (block.equals(VanillaToolTiers.MISSING)) return !TTConfig.Common.isForced();
        
        ToolTier tool = ToolTier.fromStack(stack);
        if (tool.level() == -1) return tool.equals(block);
        
        if (TTConfig.Common.isEqual()) return tool.level() > block.level() || tool.equals(block);
        else return tool.level() >= block.level();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ToolTier other)) return false;
        if (other == this) return true;
        return this.identifier.equals(other.identifier);
    }
    
    @Override
    public int hashCode() {
        return 31 * this.identifier.hashCode();
    }
    
    @Override
    public int compareTo(@NotNull ToolTier o) {
        return Integer.compare(this.level, o.level);
    }
}
