package dev.satherov.tooltiers.compat.kubejs;

import dev.latvian.mods.kubejs.event.EventGroupRegistry;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;

public class ATMPlugin implements KubeJSPlugin {
    
    @Override
    public void registerEvents(EventGroupRegistry registry) {
        registry.register(TTEvents.GROUP);
    }
}
