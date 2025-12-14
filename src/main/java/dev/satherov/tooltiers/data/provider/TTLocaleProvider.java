package dev.satherov.tooltiers.data.provider;

import dev.satherov.tooltiers.ToolTiers;
import dev.satherov.tooltiers.client.TTLanguage;

import net.neoforged.neoforge.common.data.LanguageProvider;

import net.minecraft.data.PackOutput;

public class TTLocaleProvider extends LanguageProvider {
    
    public TTLocaleProvider(PackOutput output) {
        super(output, ToolTiers.MOD_ID, "en_us");
    }
    
    @Override
    protected void addTranslations() {
        ToolTiers.getInstance().getConfig().translate(this::add);
        TTLanguage.translate(this::add);
    }
}
