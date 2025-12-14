package dev.satherov.tooltiers.data;

import dev.satherov.tooltiers.ToolTiers;
import dev.satherov.tooltiers.core.annotations.NothingNull;

import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@NothingNull
public class TTDataProvider implements DataProvider {
    
    private final boolean server;
    private final DataGenerator generator;
    private final ExistingFileHelper fileHelper;
    private final PackOutput output;
    private final CompletableFuture<HolderLookup.Provider> lookup;
    private final RegistrySetBuilder builder;
    private final List<Provider> providers = new ArrayList<>();
    
    public static TTDataProvider create(GatherDataEvent event) {
        return new TTDataProvider(event);
    }
    
    private TTDataProvider(GatherDataEvent event) {
        this.generator = event.getGenerator();
        this.fileHelper = event.getExistingFileHelper();
        this.output = this.generator.getPackOutput();
        this.lookup = event.getLookupProvider();
        this.builder = new RegistrySetBuilder();
        this.server = event.includeServer();
    }
    
    public void add(boolean include, Provider provider) {
        if (include) this.providers.add(provider);
    }
    
    public <T> void add(ResourceKey<? extends Registry<T>> key, RegistrySetBuilder.RegistryBootstrap<T> bootstrap) {
        this.builder.add(key, bootstrap);
    }
    
    public void generate() {
        this.generator.addProvider(true, this);
        this.generator.addProvider(this.server, (Factory<DatapackBuiltinEntriesProvider>) output -> new DatapackBuiltinEntriesProvider(output, this.lookup, this.builder, Set.of(ToolTiers.MOD_ID)));
    }
    
    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        List<CompletableFuture<?>> list = new ArrayList<>();
        for (Provider provider : this.providers) {
            list.add(provider.create(this.generator, this.output, this.fileHelper, this.lookup).run(cachedOutput));
        }
        return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
    }
    
    @Override
    public String getName() {
        return "Stranded Core Data Provider";
    }
    
    @FunctionalInterface
    public interface Provider {
        DataProvider create(DataGenerator generator, PackOutput output, ExistingFileHelper fileHelper, CompletableFuture<HolderLookup.Provider> lookup);
    }
}
