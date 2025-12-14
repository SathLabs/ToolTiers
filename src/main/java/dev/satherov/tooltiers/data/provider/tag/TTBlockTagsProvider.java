package dev.satherov.tooltiers.data.provider.tag;

import dev.satherov.tooltiers.ToolTiers;
import dev.satherov.tooltiers.core.annotations.NothingNull;

import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

@NothingNull
public class TTBlockTagsProvider extends BlockTagsProvider {
    
    public TTBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, ToolTiers.MOD_ID, existingFileHelper);
    }
    
    @Override
    protected void addTags(HolderLookup.Provider provider) {
        
    }
}
