package dev.satherov.tooltiers.compat.kubejs;

import dev.satherov.tooltiers.common.ToolTier;
import dev.satherov.tooltiers.core.DataRegistry;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventTargetType;
import dev.latvian.mods.kubejs.event.TargetedEventHandler;

public class TTEvents {
    
    public static final EventGroup GROUP = EventGroup.of("ToolTierEvents");
    
    public static final TargetedEventHandler<String> register = GROUP.startup("toolTier", () -> ToolTierEvent.class).requiredTarget(EventTargetType.STRING);
    
    public static void dispatch() {
        for (ToolTier tier : DataRegistry.data().values()) {
            if (TTEvents.register.hasListeners(tier.name())) TTEvents.register.post(new ToolTierEvent(tier), tier.name());
        } 
    }
}
