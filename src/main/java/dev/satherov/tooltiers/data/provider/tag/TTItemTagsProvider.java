package dev.satherov.tooltiers.data.provider.tag;

import dev.satherov.tooltiers.ToolTiers;
import dev.satherov.tooltiers.common.VanillaToolTiers;
import dev.satherov.tooltiers.core.annotations.NothingNull;

import net.neoforged.neoforge.common.data.ExistingFileHelper;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;

import java.util.concurrent.CompletableFuture;

@NothingNull
public class TTItemTagsProvider extends ItemTagsProvider {
    
    public TTItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, CompletableFuture.completedFuture(TagLookup.empty()), ToolTiers.MOD_ID, existingFileHelper);
    }
    
    @Override
    protected void addTags(HolderLookup.Provider provider) {
        copy(VanillaToolTiers.INDESTRUCTIBLE.tag(), ItemTags.create(VanillaToolTiers.INDESTRUCTIBLE.tag().location()));
        copy(VanillaToolTiers.HAND.tag(), ItemTags.create(VanillaToolTiers.HAND.tag().location()));
        copy(VanillaToolTiers.WOOD.tag(), ItemTags.create(VanillaToolTiers.WOOD.tag().location()));
        copy(VanillaToolTiers.STONE.tag(), ItemTags.create(VanillaToolTiers.STONE.tag().location()));
        copy(VanillaToolTiers.IRON.tag(), ItemTags.create(VanillaToolTiers.IRON.tag().location()));
        copy(VanillaToolTiers.GOLD.tag(), ItemTags.create(VanillaToolTiers.GOLD.tag().location()));
        copy(VanillaToolTiers.DIAMOND.tag(), ItemTags.create(VanillaToolTiers.DIAMOND.tag().location()));
        copy(VanillaToolTiers.NETHERITE.tag(), ItemTags.create(VanillaToolTiers.NETHERITE.tag().location()));
    }
}
