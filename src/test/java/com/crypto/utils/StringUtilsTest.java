package com.crypto.utils;

import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StringUtilsTest {

    /**
     * Remove all non-alphabetical characters from the string,
     * and change the string to all lower-case characters.
     */
    @Test
    public void SanitizeAlphabeticalStringValueTest() {
        String nucleusVisionName = "Nucleus Vision **";
        String sanitizedNuclearVisionName = StringUtils.sanitizeAlphabeticalStringValue(nucleusVisionName);
        assertEquals(sanitizedNuclearVisionName, "NucleusVision");

        String paretoNetworkName = "Pareto Network **";
        String sanitizedParetoNetworkName = StringUtils.sanitizeAlphabeticalStringValue(paretoNetworkName);
        assertEquals(sanitizedParetoNetworkName, "ParetoNetwork");

        String mobiusName = "Mobius";
        String sanitizedMobiusName = StringUtils.sanitizeAlphabeticalStringValue(mobiusName);
        assertEquals(sanitizedMobiusName, mobiusName);
    }

    /**
     * Remove the string "(Code Name)" from a coin's name and any suffixed non-alphabetical characters
     * For example, "CANADA (Code Name) **" should become "CANADA"
     */
    @Test
    public void SanitizeIcoCodeNameTest() {
        String canadaCodeName = "CANADA (Code Name) **";
        String sanitizedCanadaCodeName = StringUtils.sanitizeIcoCodeName(canadaCodeName);
        assertEquals(sanitizedCanadaCodeName, "CANADA");

        String twitterCodeName = "Twitter (Code Name)";
        String sanitizedTwitterCodeName = StringUtils.sanitizeIcoCodeName(twitterCodeName);
        assertEquals(sanitizedTwitterCodeName, "Twitter");

        String homeRunCodeName = "Home Run (Code Name)";
        String sanitizedHomeRunCodeName = StringUtils.sanitizeIcoCodeName(homeRunCodeName);
        assertEquals(sanitizedHomeRunCodeName, "Home Run");

        String pundixName = "Pundix";
        String sanitizedPundixName = StringUtils.sanitizeIcoCodeName(pundixName);
        assertEquals(sanitizedPundixName, pundixName);
    }

    /**
     * Retrieve a value from the map based on the key, but if it doesn't exist, return an empty string.
     */
    @Test
    public void ExtractValueFromMapTest() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("Ticker", "GOT");
        map.put("Token type", "ERC20");
        map.put("Total Tokens", "100,000,000");
        map.put("Available for Token Sale", "50%");
        map.put("Whitelist", "Yes (period isn't set, JOIN ");
        map.put("Know Your Customer (KYC)", "Yes");
        map.put("Can't participate", "USA, Singapore, China, Canada");
        map.put("Accepts", "ETH, BTC");

        String ticker = StringUtils.extractValueFromMap("Ticker", map);
        assertEquals(ticker, "GOT");

        String totalTokens = StringUtils.extractValueFromMap("Total Tokens", map);
        assertEquals(totalTokens, "100,000,000");

        String cantParticipate = StringUtils.extractValueFromMap("Can't participate", map);
        assertEquals(cantParticipate, "USA, Singapore, China, Canada");

        String nonexistentTest = StringUtils.extractValueFromMap("Testing", map);
        assertEquals(nonexistentTest, StringUtils.EMPTY_STRING);
    }

    /**
     * Returns true if the two strings are equal if casing is ignored
     */
    @Test
    public void AreStringsEqualIgnoreCaseTest() {
        String zenProtocolUpper = "Zen Protocol";
        String zenProtocolLower = "zen protocol";
        assertTrue(StringUtils.areStringsEqualIgnoreCase(zenProtocolLower, zenProtocolUpper));

        String crypteriumUpper = "CRYPTERIUM";
        String crypterumLower = "crypterium";
        assertTrue(StringUtils.areStringsEqualIgnoreCase(crypteriumUpper, crypterumLower));

        String kairosUpper = "KaIrOs";
        String kairosLower = "kairos";
        assertTrue(StringUtils.areStringsEqualIgnoreCase(kairosLower, kairosUpper));

        String bluzelleUpper = "bluzelle";
        String bluzelleLower = "bluzelle";
        assertTrue(StringUtils.areStringsEqualIgnoreCase(bluzelleLower, bluzelleUpper));
    }
}
