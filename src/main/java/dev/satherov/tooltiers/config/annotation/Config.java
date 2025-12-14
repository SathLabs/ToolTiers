package dev.satherov.tooltiers.config.annotation;

import net.neoforged.fml.config.ModConfig;

import org.jetbrains.annotations.Nullable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Config {
    
    @Nullable ModConfig.Type value() default ModConfig.Type.COMMON;
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface Group {
        
        String value() default "";
    }
}
