package dev.satherov.tooltiers.common;

public class VanillaToolTiers {
    
    public static final ToolTier MISSING = ToolTier.create("missing", 0x000000, Integer.MIN_VALUE);
    public static final ToolTier INDESTRUCTIBLE = ToolTier.create("indestructible", 0xFF7CFF, Integer.MAX_VALUE);
    public static final ToolTier HAND = ToolTier.create("hand", 0xFFFFFF, 0);
    public static final ToolTier WOOD = ToolTier.create("wood", 0x8B4513, 1);
    public static final ToolTier STONE = ToolTier.create("stone", 0x707070, 2);
    public static final ToolTier IRON = ToolTier.create("iron", 0xCCCCCC, 3);
    public static final ToolTier DIAMOND = ToolTier.create("diamond", 0x00FFFF, 4);
    public static final ToolTier NETHERITE = ToolTier.create("netherite", 0x39373A, 5);
}
