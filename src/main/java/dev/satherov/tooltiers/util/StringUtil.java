package dev.satherov.tooltiers.util;

import java.util.Locale;

public final class StringUtil {
    
    public static String toLower(String string) {
        return string.toLowerCase(Locale.ROOT).trim();
    }
    
    public static String toUpper(String string) {
        return string.toUpperCase(Locale.ROOT).trim();
    }
    
    /**
     * Converts a string to a pretty version of itself
     * <p>
     * {@code "example_string.showing_off" -> "Example String Showing Off"}
     *
     * @param string Initial string
     *
     * @return Pretty string
     */
    public static String pretty(String string) {
        String result = StringUtil.toLower(string);
        result = result.replaceAll("_", " ");
        result = result.replaceAll("\\.", " ");
        String[] words = result.split(" ");
        StringBuilder builder = new StringBuilder();
        for (String word : words) {
            builder.append(StringUtil.toUpper(word.substring(0, 1))).append(word.substring(1)).append(" ");
        }
        return builder.toString().trim();
    }
}
