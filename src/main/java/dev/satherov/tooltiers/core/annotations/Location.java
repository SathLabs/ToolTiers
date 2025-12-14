package dev.satherov.tooltiers.core.annotations;

import org.intellij.lang.annotations.Pattern;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents a valid resource name <b>path</b>. Gives a warning at compile time if the given name is invalid.
 */
@Pattern("^[a-z0-9/._-]+$")
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Location { }