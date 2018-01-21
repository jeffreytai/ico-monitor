package com.crypto.utils;

import java.util.Map;

public class StringUtils {

    private static final String EMPTY_STRING = "";

    /**
     * Remove all non-alphabetical characters from the string,
     * and change the string to all lower-case characters.
     * @param value
     * @return
     */
    public static String sanitizeStringValue(String value) {
        return value.replaceAll("[^a-zA-Z]", "");
    }

    /**
     * Retrieve a value from the map based on the key, but if it doesn't exist, return an empty string.
     * @param key
     * @param map
     * @return
     */
    public static String extractValueFromMap(String key, Map<String, String> map) {
        return map.containsKey(key) ? map.get(key) : EMPTY_STRING;
    }
}
