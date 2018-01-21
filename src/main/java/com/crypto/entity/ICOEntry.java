package com.crypto.entity;

import com.crypto.utils.StringUtils;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The name of each field should match what is on the spreadsheet, excluding any symbols.
 */
public class ICOEntry {

    private static final Logger logger = LoggerFactory.getLogger(ICOEntry.class);

    /*************************
     * Fields
     *************************/

    /**
     * If the ICO has already been scored/graded
     */
    private String graded;

    /**
     * Status of the Pre-Sale
     */
    private String presale;

    /**
     * Details on Ian's investment
     */
    private String ianInvested;

    /**
     * Opinion in the following categories:
     * Hall of Fame, All-Star, Starter, Substitute, Didn't make the team, Wait for the rebound
     */
    private String iansOpinion;

    /**
     * ICO ranking
     */
    private Integer ranking;

    /**
     * Coin name
     */
    private String ico;

    /**
     * Coin rating as a percentage
     */
    private String rating;

    /**
     * Largest bonus given for coin as a percentage
     */
    private String largestBonus;

    /**
     * Ian's preferred investment amount in USD
     */
    private String idealInvestmentUsd;

    /**
     * Ian's preferred investment in ETH
     */
    private String idealInvestmentEth;

    /**
     * Ian's preferred portfolio composition for coin
     */
    private String idealPortfolio;

    /**
     * If US investors are allowed
     */
    private String usInvestors;

    /**
     * Pre-sale start date
     */
    private String presaleDate;

    /**
     * ICO start date
     */
    private String icoStartDate;

    /**
     * ICO end date
     */
    private String icoEndDate;

    /**
     * Number of days until ICO starts
     */
    private String icoStartsIn;

    /**
     * Number of days until ICO ends
     */
    private String icoEndsIn;

    /**
     * Type, utility, or purpose of coin
     */
    private String type;

    /**
     * Score/rating of core team
     */
    private Integer allStarTeam;

    /**
     * Score/rating of advisors
     */
    private Integer allStarAdvisors;

    /**
     * Score/rating of base idea
     */
    private Integer idea;


    /*************************
     * Constructors
     *************************/

    public ICOEntry() {}

    public ICOEntry(String graded, String presale, String ianInvested, String iansOpinion,
                    Integer ranking, String ico, String rating, String largestBonus,
                    String idealInvestmentUsd, String idealInvestmentEth, String idealPortfolio,
                    String usInvestors, String presaleDate, String icoStartDate, String icoEndDate,
                    String icoStartsIn, String icoEndsIn, String type,
                    Integer allStarTeam, Integer allStarAdvisors, Integer idea) {
        this.graded = graded;
        this.presale = presale;
        this.ianInvested = ianInvested;
        this.iansOpinion = iansOpinion;
        this.ranking = ranking;
        this.ico = ico;
        this.rating = rating;
        this.largestBonus = largestBonus;
        this.idealInvestmentUsd = idealInvestmentUsd;
        this.idealInvestmentEth = idealInvestmentEth;
        this.idealPortfolio = idealPortfolio;
        this.usInvestors = usInvestors;
        this.presaleDate = presaleDate;
        this.icoStartDate = icoStartDate;
        this.icoEndDate = icoEndDate;
        this.icoStartsIn = icoStartsIn;
        this.icoEndsIn = icoEndsIn;
        this.type = type;
        this.allStarTeam = allStarTeam;
        this.allStarAdvisors = allStarAdvisors;
        this.idea = idea;
    }

    /**
     * Retrieve values dynamically in case the order of the columns change
     * Use Java reflection to get the list of names
     * Exclude static fields to ignore variables like Logger
     * @param rowEntry
     * @param columnIndexMap
     */
    public ICOEntry(List<Object> rowEntry, Map<String, Integer> columnIndexMap) {
        Integer nameIndex = columnIndexMap.get("ICO").intValue();
        logger.info("{} - creating entity", rowEntry.get(nameIndex).toString());

        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            if ((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) {
                continue;
            }

            String fieldName = field.getName();

            List<Map.Entry<String, Integer>> matchedFields =
                    columnIndexMap.entrySet()
                            .stream()
                            .filter(c -> StringUtils.sanitizeStringValue(c.getKey())
                                    .toLowerCase()
                                    .equals(fieldName.toLowerCase()))
                            .collect(Collectors.toList());

            if (matchedFields.size() != 1) {
                logger.error("No valid header found for {}", fieldName);
            }

            Map.Entry<String, Integer> matchedField = matchedFields.get(0);

            try {
                Integer indexOfValue = matchedField.getValue();
                Class<?> dynamicClassType = Class.forName(field.getType().getName());

                // Parse string for Integer equivalent, account for nullable fields
                if (field.getType().isAssignableFrom(Integer.class)) {
                    String stringValue = rowEntry.get(indexOfValue).toString();
                    Integer value = Strings.isNullOrEmpty(stringValue)
                        ? null
                        : Integer.parseInt(stringValue);

                    field.set(this, value);
                }
                // Parse string for Double equivalent, account for nullable fields
                else if (field.getType().isAssignableFrom(Double.class)) {
                    String doubleValue = rowEntry.get(indexOfValue).toString();
                    Double value = Strings.isNullOrEmpty(doubleValue)
                            ? null
                            : Double.parseDouble(doubleValue);

                    field.set(this, value);
                }
                else {
                    field.set(this, dynamicClassType.cast(rowEntry.get(indexOfValue)));
                }
            } catch (IllegalAccessException ex) {
                logger.error("Unable to access {} modifier", fieldName);
            } catch (ClassNotFoundException ex) {
                logger.error("Unable to infer class type for {}", fieldName);
            }
        }
    }



}
