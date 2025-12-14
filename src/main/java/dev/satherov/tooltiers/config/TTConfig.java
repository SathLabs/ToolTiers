package dev.satherov.tooltiers.config;

import lombok.Getter;

import dev.satherov.tooltiers.config.annotation.Config;
import dev.satherov.tooltiers.config.annotation.ConfigHolder;
import dev.satherov.tooltiers.config.annotation.ConfigVal;

import net.neoforged.fml.config.ModConfig;

@ConfigHolder
public class TTConfig {
    
    @Config(ModConfig.Type.CLIENT)
    public static class Client {
        
        @ConfigVal(name = "Display Missing Warning", comment = """
               If set to false the warning about missing tiers will not be displayed
               """)
        @ConfigVal.Boolean
        private static @Getter boolean warning = true;
        
        @ConfigVal(name = "Tooltip Info", comment = """
               If set to true the Tier of a block and tool will be shown in their tooltip
               """)
        @ConfigVal.Boolean
        private static @Getter boolean tooltip = true;
    }
    
    @Config(ModConfig.Type.COMMON)
    public static class Common {
        
        @ConfigVal(name = "Force Tier", comment = """
               If set to true blocks will not be breakable if they do not have a tier assigned.
               If set to false blocks will be breakable by anything if they dont have a tier assigned.
               """)
        @ConfigVal.Boolean
        private static @Getter boolean forced = true;
        
        
        @ConfigVal(name = "Equal Tier", comment = """
               If set to true tools need to be above the tier of the block, or match exactly.
               If set to false tools need to be above or at the same tier of the block.
               """)
        @ConfigVal.Boolean
        private static @Getter boolean equal = true;
    }
}
