package dev.satherov.tooltiers.core.mixin;

import dev.satherov.tooltiers.core.DataRegistry;

import net.minecraft.client.resources.language.ClientLanguage;
import net.minecraft.server.packs.resources.ResourceManager;

import com.llamalad7.mixinextras.sugar.Local;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;

@Mixin(ClientLanguage.class)
public class ClientLanguageMixin {
    
    /**
     * We inject translations here directly to avoid having untranslated tiers, 
     * but you can still overwrite the translations on a pack level if you chose to
     */
    @Inject(
            method = "loadFrom", 
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/google/common/collect/ImmutableMap;copyOf(Ljava/util/Map;)Lcom/google/common/collect/ImmutableMap;"
            )
    )
    private static void loadTranslations(ResourceManager resourceManager, List<String> filenames, boolean defaultRightToLeft, CallbackInfoReturnable<ClientLanguage> cir, @Local(name = "map") Map<String, String> map) {
        DataRegistry.data().values().forEach(data -> {
            if (!map.containsKey(data.key())) map.put(data.key(), data.translation());
        });
    }
}
