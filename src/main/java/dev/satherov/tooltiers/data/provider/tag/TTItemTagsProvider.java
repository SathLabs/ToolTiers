package dev.satherov.tooltiers.data.provider.tag;

import dev.satherov.tooltiers.core.annotations.NothingNull;
import dev.satherov.tooltiers.ToolTiers;

import net.neoforged.neoforge.common.data.ExistingFileHelper;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;

import java.util.concurrent.CompletableFuture;

@NothingNull
public class TTItemTagsProvider extends ItemTagsProvider {
    
    public TTItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, CompletableFuture.completedFuture(TagLookup.empty()), ToolTiers.MOD_ID, existingFileHelper);
    }
    
    @Override
    protected void addTags(HolderLookup.Provider provider) {
        
    }
}
