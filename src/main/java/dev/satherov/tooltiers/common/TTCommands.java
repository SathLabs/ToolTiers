package dev.satherov.tooltiers.common;

import dev.satherov.tooltiers.ToolTiers;
import dev.satherov.tooltiers.core.DataHolder;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.block.state.BlockState;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@EventBusSubscriber(modid = ToolTiers.MOD_ID)
public class TTCommands {
    
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    
    @SubscribeEvent
    @SuppressWarnings("DuplicatedCode")
    public static void onRegisterCommands(final RegisterCommandsEvent event) {
        final CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        
        dispatcher.register(
                Commands.literal(ToolTiers.MOD_ID)
                        .then(Commands.literal("dump")
                                .then(Commands.literal("tiers")
                                        .then(Commands.argument("tier", StringArgumentType.word())
                                                .suggests(TIER_SUGGESTER)
                                                .executes(ctx -> {
                                                    CompletableFuture.runAsync(() -> dumpToolTiers(ctx.getSource(), DataHolder.get(ToolTiers.loc(StringArgumentType.getString(ctx, "tier")))));
                                                    return 1;
                                                })
                                        )
                                        .executes(ctx -> {
                                            CompletableFuture.runAsync(() -> dumpToolTiers(ctx.getSource(), null));
                                            return 1;
                                        })
                                ).then(Commands.literal("vanilla")
                                        .then(Commands.literal("missing-only")
                                                .executes(ctx -> {
                                                    CompletableFuture.runAsync(() -> dumpVanillaTier(ctx.getSource(), true));
                                                    return 1;
                                                })
                                        )
                                        .executes(ctx -> {
                                            CompletableFuture.runAsync(() -> dumpVanillaTier(ctx.getSource(), false));
                                            return 1;
                                        })
                                )
                        )
        );
    }
    
    private static void dumpToolTiers(CommandSourceStack source, @Nullable ToolTier required) {
        source.sendSystemMessage(Component.literal("Dumping Tier entries..."));
        
        ConcurrentHashMap<ResourceLocation, ToolTier> blocks = new ConcurrentHashMap<>();
        BuiltInRegistries.BLOCK.iterator().forEachRemaining(block -> {
            ToolTier tier = ToolTier.fromState(block.defaultBlockState());
            if (required != null && !tier.equals(required)) return;
            blocks.put(BuiltInRegistries.BLOCK.getKey(block), tier);
        });
        
        if (TTCommands.dumpTiers(source, blocks, "blocks")) return;
        
        ConcurrentHashMap<ResourceLocation, ToolTier> items = new ConcurrentHashMap<>();
        BuiltInRegistries.ITEM.iterator().forEachRemaining(item -> {
            if (!item.components().has(ToolTiers.COMPONENT.get())) return;
            ToolTier tier = ToolTier.fromStack(item.getDefaultInstance());
            if (required != null && !tier.equals(required)) return;
            items.put(BuiltInRegistries.ITEM.getKey(item), tier);
        });
        
        if (TTCommands.dumpTiers(source, items, "items")) return;
        
        source.sendSuccess(() -> Component.literal(String.format("All entries successfully written to %s", TTCommands.directory(source))).withStyle(ChatFormatting.GREEN), true);
    }
    
    private static boolean dumpTiers(CommandSourceStack source, Map<ResourceLocation, ToolTier> map, String name) {
        final Comparator<ResourceLocation> comparator = Comparator.comparing((ResourceLocation loc) -> map.get(loc)).thenComparing(ResourceLocation::toString);
        final Map<ResourceLocation, String> json = new TreeMap<>(comparator);
        map.forEach((block, tier) -> json.put(block, tier.name()));
        return TTCommands.dump(source, name, json);
    }
    
    private static void dumpVanillaTier(CommandSourceStack source, boolean missing_only) {
        source.sendSystemMessage(Component.literal("Dumping Vanilla Tier entries..."));
        
        List<Item> tools = new ArrayList<>();
        BuiltInRegistries.ITEM.iterator().forEachRemaining(item -> {
            if (item.components().has(DataComponents.TOOL)) tools.add(item);
        });
        
        TreeMap<ResourceLocation, List<String>> tags = new TreeMap<>();
        
        BuiltInRegistries.BLOCK.iterator().forEachRemaining(block -> {
            
            BlockState state = block.defaultBlockState();
            if (missing_only && !ToolTier.fromState(state).equals(VanillaToolTiers.MISSING)) return;
            
            String loc = BuiltInRegistries.BLOCK.getKey(block).toString();
            if (!state.requiresCorrectToolForDrops()) {
                tags.computeIfAbsent(ResourceLocation.fromNamespaceAndPath("_none_needed_", "_none_needed_"), k -> new ArrayList<>()).add(loc);
                return;
            }
            
            boolean added = false;
            
            for (Item item : tools) {
                ResourceLocation key = BuiltInRegistries.ITEM.getKey(item);
                
                if (item instanceof TieredItem tieredItem) {
                    ResourceLocation tag = tieredItem.getTier().getIncorrectBlocksForDrops().location();
                    key = ResourceLocation.fromNamespaceAndPath(tag.getNamespace(), tag.getPath()
                            .replaceFirst("(?i)^(incorrect_)?(for_)?", "")
                            .replaceFirst("(?i)_?tool$", "")
                    );
                }
                
                Tool tool = item.components().get(DataComponents.TOOL);
                if (Objects.requireNonNull(tool).isCorrectForDrops(state)) {
                    tags.computeIfAbsent(key, k -> new ArrayList<>()).add(loc);
                    added = true;
                }
            }
            
            if (!added) {
                tags.computeIfAbsent(ResourceLocation.fromNamespaceAndPath("_non_assigned_", "_non_assigned_"), k -> new ArrayList<>()).add(loc);
            }
        });
        
        for (List<String> list : tags.values()) if (list.size() > 1) Collections.sort(list);
        
        if (TTCommands.dump(source, "vanilla", tags)) return;
        
        source.sendSuccess(() -> Component.literal(String.format("All vanilla tier entries successfully written to %s", TTCommands.directory(source))).withStyle(ChatFormatting.GREEN), true);
    }
    
    private static boolean dump(CommandSourceStack source, String file, Object content) {
        final @Nullable Path directory = TTCommands.directory(source);
        if (directory == null) return true;
        
        final Path path = directory.resolve(file + ".json");
        
        try {
            Files.writeString(path, GSON.toJson(content));
            source.sendSuccess(() -> Component.literal("Written " + file + " entries").withStyle(ChatFormatting.GRAY), true);
            return false;
        } catch (IOException e) {
            source.sendFailure(Component.literal(String.format("Failed to write entries at %s", path)).withStyle(ChatFormatting.RED));
        }
        
        return true;
    }
    
    private static Path directory(CommandSourceStack source) {
        final Path directory = FMLPaths.GAMEDIR.get().resolve("local").resolve(ToolTiers.MOD_ID);
        
        try {
            Files.createDirectories(directory);
        } catch (IOException e) {
            source.sendFailure(Component.literal(String.format("Failed to create dump directory at %s", directory)).withStyle(ChatFormatting.RED));
            return null;
        }
        
        if (!Files.isDirectory(directory)) {
            source.sendFailure(Component.literal(String.format("Failed to open dump directory at %s", directory)).withStyle(ChatFormatting.RED));
            return null;
        }
        
        return directory;
    }
    
    private static final SuggestionProvider<CommandSourceStack> TIER_SUGGESTER = (ctx, builder) -> {
        List<String> tiers = DataHolder.all().stream().map(ToolTier::name).toList();
        tiers.forEach(builder::suggest);
        return builder.buildFuture();
    };
}
