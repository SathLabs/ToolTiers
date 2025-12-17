package dev.satherov.tooltiers.data;

import dev.satherov.tooltiers.ToolTiers;
import dev.satherov.tooltiers.data.provider.TTBlockTagsProvider;
import dev.satherov.tooltiers.data.provider.TTLocaleProvider;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;


@EventBusSubscriber(modid = ToolTiers.MOD_ID)
public class TTDataGenerator {
    
    @SubscribeEvent
    private static void onGatherData(GatherDataEvent event) {
        TTDataProvider provider = TTDataProvider.create(event);
        provider.add(event.includeClient(), (gen, out, helper, lookup) -> new TTLocaleProvider(out));
        provider.add(event.includeServer(), (gen, out, helper, lookup) -> new TTBlockTagsProvider(out, lookup, helper));
        provider.generate();
    }
}

