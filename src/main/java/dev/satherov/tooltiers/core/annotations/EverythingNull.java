package dev.satherov.tooltiers.core.annotations;

import org.jetbrains.annotations.Nullable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.annotation.ParametersAreNullableByDefault;
import javax.annotation.meta.TypeQualifierDefault;

@Nullable
@javax.annotation.Nullable
@ParametersAreNullableByDefault
@TypeQualifierDefault({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.CLASS)
public @interface EverythingNull {
}
