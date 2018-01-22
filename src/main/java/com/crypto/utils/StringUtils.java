package com.crypto.utils;

import java.util.Map;

public class StringUtils {

    /**
     * Alias for an empty string
     */
    public static final String EMPTY_STRING = "";

    /**
     * Remove all non-alphabetical characters from the string,
     * and change the string to all lower-case characters.
     * @param value
     * @return
     */
    public static String sanitizeAlphabeticalStringValue(String value) {
        return value.replaceAll("[^a-zA-Z]", "");
    }

    /**
     * Remove the string "(Code Name)" from a coin's name and any suffixed non-alphabetical characters
     * For example, "CANADA (Code Name) **" should become "CANADA"
     * @param value
     * @return
     */
    public static String sanitizeIcoCodeName(String value) {
        String valueWithoutCodeName = value.replaceAll("\\(Code Name\\)", "");

        int lastAlphabeticalCharIndex = valueWithoutCodeName.replaceAll("[^a-zA-Z]*$", "").length();
        return valueWithoutCodeName.substring(0, lastAlphabeticalCharIndex);
    }

    /**
     * Retrieve a value from the map based on the key, but if it doesn't exist, return an empty string.
     * @param key
     * @param map
     * @return
     */
    public static String extractValueFromMap(String key, Map<String, String> map) {
        return map.containsKey(key) ? map.get(key) : StringUtils.EMPTY_STRING;
    }

    /**
     * Returns true if the two strings are equal if casing is ignored
     * @param value1
     * @param value2
     * @return
     */
    public static boolean areStringsEqualIgnoreCase(String value1, String value2) {
        return value1.toLowerCase().equals(value2.toLowerCase());
    }
}
