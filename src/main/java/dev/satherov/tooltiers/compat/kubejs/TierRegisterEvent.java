package dev.satherov.tooltiers.compat.kubejs;

import dev.satherov.tooltiers.common.ToolTier;
import dev.satherov.tooltiers.core.DataHolder;

import com.google.common.base.Preconditions;

import dev.latvian.mods.kubejs.event.KubeEvent;
import dev.latvian.mods.kubejs.script.ConsoleJS;
import dev.latvian.mods.kubejs.typings.Info;

@Info("""
        Allows you to create new tool tiers
        """)
public class TierRegisterEvent implements KubeEvent {
    
    @Info("""
            Creates a new tier with a given level. The tier will be, by default,
            able to break blocks of the same tier or all with a lower level
            """)
    public void create(String name, int color, int level) {
        Preconditions.checkArgument(level > 0, "Level must be a positive integer");
        ToolTier tier = ToolTier.create(name, color, level);
        DataHolder.put(tier);
        ConsoleJS.STARTUP.log("Created new tool tier with name '" + name + "'");
    }
    
    @Info("""
            Creates a new unique tier. These kinds of tiers are only able to break
            the exact blocks registered to it.
            """)
    public void create(String name, int color) {
        ToolTier tier = ToolTier.create(name, color, -1);
        DataHolder.put(tier);
        ConsoleJS.STARTUP.log("Created new unique tool tier with name '" + name + "'");
    }
}
