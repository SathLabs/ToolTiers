package dev.satherov.tooltiers.data.provider;

import dev.satherov.tooltiers.ToolTiers;
import dev.satherov.tooltiers.common.VanillaToolTiers;
import dev.satherov.tooltiers.core.annotations.NothingNull;

import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

@NothingNull
public class TTBlockTagsProvider extends BlockTagsProvider {
    
    public TTBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, ToolTiers.MOD_ID, existingFileHelper);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    protected void addTags(HolderLookup.Provider provider) {
        
        this.tag(VanillaToolTiers.INDESTRUCTIBLE.tag())
                .add(Blocks.BEDROCK)
                .add(Blocks.COMMAND_BLOCK)
                .add(Blocks.CHAIN_COMMAND_BLOCK)
                .add(Blocks.REPEATING_COMMAND_BLOCK)
                .add(Blocks.BARRIER)
                .add(Blocks.STRUCTURE_BLOCK)
                .add(Blocks.STRUCTURE_VOID)
                .add(Blocks.JIGSAW)
                .add(Blocks.DRAGON_EGG)
                .add(Blocks.END_GATEWAY)
                .add(Blocks.END_PORTAL)
                .add(Blocks.END_PORTAL_FRAME)
                .add(Blocks.NETHER_PORTAL)
                .add(Blocks.MOVING_PISTON)
                .add(Blocks.REINFORCED_DEEPSLATE)
                .add(Blocks.TRIAL_SPAWNER)
                .add(Blocks.VAULT)
        ;
        
        this.tag(VanillaToolTiers.HAND.tag())
                .add(Blocks.COBWEB);
        
        this.tag(VanillaToolTiers.WOOD.tag())
                .addTags(BlockTags.STONE_BUTTONS)
                .addTags(BlockTags.STONE_BRICKS)
                .addTags(BlockTags.STONE_PRESSURE_PLATES)
                .addTags(Tags.Blocks.COBBLESTONES)
                .addTags(BlockTags.BASE_STONE_OVERWORLD)
                .addTags(BlockTags.BASE_STONE_NETHER)
                .addTags(BlockTags.CORAL_BLOCKS)
                .addTags(Tags.Blocks.ORES_COAL)
                .addTags(Tags.Blocks.STORAGE_BLOCKS_COAL)
                .addTags(BlockTags.ANVIL)
                .addOptionalTag(Tags.Blocks.NEEDS_WOOD_TOOL)
        ;
        
        this.tag(VanillaToolTiers.STONE.tag())
                .addTag(BlockTags.INCORRECT_FOR_WOODEN_TOOL)
                .addOptionalTag(BlockTags.NEEDS_STONE_TOOL)
        ;
        
        this.tag(VanillaToolTiers.IRON.tag())
                .addTag(BlockTags.INCORRECT_FOR_STONE_TOOL)
                .addOptionalTag(BlockTags.NEEDS_IRON_TOOL)
        ;
        
        this.tag(VanillaToolTiers.DIAMOND.tag())
                .addTag(BlockTags.INCORRECT_FOR_IRON_TOOL)
                .addOptionalTag(BlockTags.NEEDS_DIAMOND_TOOL)
        ;
        
        this.tag(VanillaToolTiers.NETHERITE.tag())
                .addTag(BlockTags.INCORRECT_FOR_DIAMOND_TOOL)
                .addOptionalTag(Tags.Blocks.NEEDS_NETHERITE_TOOL)
        ;
    }
}
