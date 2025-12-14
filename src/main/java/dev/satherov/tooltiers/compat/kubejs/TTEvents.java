package dev.satherov.tooltiers.compat.kubejs;

import dev.satherov.tooltiers.common.ToolTier;
import dev.satherov.tooltiers.core.DataHolder;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;
import dev.latvian.mods.kubejs.event.EventTargetType;
import dev.latvian.mods.kubejs.event.TargetedEventHandler;
import dev.latvian.mods.kubejs.script.ConsoleJS;

public class TTEvents {
    
    public static final EventGroup GROUP = EventGroup.of("ToolTierEvents");
    
    public static final TargetedEventHandler<String> tools = GROUP.startup("tools", () -> ItemTiersEvent.class).requiredTarget(EventTargetType.STRING);
    public static final EventHandler register = GROUP.startup("register", () -> TierRegisterEvent.class);
    
    
    public static void dispatchItems() {
        for (ToolTier tier : DataHolder.values()) {
            if (TTEvents.tools.hasListeners(tier.name())) {
                TTEvents.tools.post(new ItemTiersEvent(tier), tier.name());
                ConsoleJS.STARTUP.log("Dispatching register for tool tier '" + tier.name() + "'");
            } else ConsoleJS.STARTUP.debug("Tool tier '" + tier.name() + "' has no listeners");
        } 
    }
    
    public static void dispatchRegister() {
        if (TTEvents.register.hasListeners()) TTEvents.register.post(new TierRegisterEvent());
    }
}
