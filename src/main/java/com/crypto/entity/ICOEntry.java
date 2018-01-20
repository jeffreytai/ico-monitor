package com.crypto.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class ICOEntry {

    /*************************
     * Private variables
     *************************/

    /**
     * If the ICO has already been scored/graded
     */
    private String graded;

    /**
     * Status of the Pre-Sale
     */
    @SerializedName("pre-sale")
    private String presale;

    /**
     * Details on Ian's investment
     */
    @SerializedName("ian invested")
    private String ianInvested;

    /**
     * Opinion in the following categories:
     * Hall of Fame, All-Star, Starter, Substitute, Didn't make the team, Wait for the rebound
     */
    @SerializedName("ian's opinion")
    private String opinion;

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
    private Double rating;

    /**
     * Largest bonus given for coin as a percentage
     */
    @SerializedName("largest bonus")
    private Double largestBonus;

    /**
     * Ian's preferred investment amount in USD
     */
    @SerializedName("ideal investment (usd)")
    private Double idealInvestmentInUsd;

    /**
     * Ian's preferred investment in ETH
     */
    @SerializedName("ideal investment (eth)")
    private Double idealInvestmentInEth;

    /**
     * Ian's preferred portfolio composition for coin
     */
    @SerializedName("ideal % portfolio")
    private Double idealPortfolioPercentage;

    /**
     * If US investors are allowed
     */
    @SerializedName("us investors")
    private String usInvestorsAllowed;

    /**
     * Pre-sale start date
     */
    @SerializedName("pre-sale date")
    private String presaleDate;

    /**
     * ICO start date
     */
    @SerializedName("ico start date")
    private String icoStartDate;

    /**
     * ICO end date
     */
    @SerializedName("ico end date")
    private String icoEndDate;

    /**
     * Number of days until ICO starts
     */
    @SerializedName("ico starts in")
    private String icoStartsIn;

    /**
     * Number of days until ICO ends
     */
    @SerializedName("ico ends in")
    private String icoEndsIn;

    /**
     * Type, utility, or purpose of coin
     */
    private String type;

    /**
     * Score/rating of core team
     */
    @SerializedName("all-star team")
    private Integer allStarTeamRating;

    /**
     * Score/rating of advisors
     */
    @SerializedName("all-star advisors")
    private Integer allStarAdvisorsRating;

    /**
     * Score/rating of base idea
     */
    @SerializedName("idea")
    private Integer ideaRating;


    /*************************
     * Constructors
     *************************/

    public ICOEntry() {}

    public ICOEntry(String graded, String presale, String ianInvested, String opinion,
                    Integer ranking, String ico, Double rating, Double largestBonus,
                    Double idealInvestmentInUsd, Double idealInvestmentInEth, Double idealPortfolioPercentage, String usInvestorsAllowed,
                    String presaleDate, String icoStartDate, String icoEndDate, String icoStartsIn, String icoEndsIn,
                    String type, Integer allStarTeamRating, Integer allStarAdvisorsRating, Integer ideaRating) {
        this.graded = graded;
        this.presale = presale;
        this.ianInvested = ianInvested;
        this.opinion = opinion;
        this.ranking = ranking;
        this.ico = ico;
        this.rating = rating;
        this.largestBonus = largestBonus;
        this.idealInvestmentInUsd = idealInvestmentInUsd;
        this.idealInvestmentInEth = idealInvestmentInEth;
        this.idealPortfolioPercentage = idealPortfolioPercentage;
        this.usInvestorsAllowed = usInvestorsAllowed;
        this.presaleDate = presaleDate;
        this.icoStartDate = icoStartDate;
        this.icoEndDate = icoEndDate;
        this.icoStartsIn = icoStartsIn;
        this.icoEndsIn = icoEndsIn;
        this.type = type;
        this.allStarTeamRating = allStarTeamRating;
        this.allStarAdvisorsRating = allStarAdvisorsRating;
        this.ideaRating = ideaRating;
    }

    public ICOEntry(List<Object> rowEntry, Map<String, Integer> columnIndexMap) {
//        this.graded = graded;
//        this.presale = presale;
//        this.ianInvested = ianInvested;
//        this.opinion = opinion;
//        this.ranking = ranking;
//        this.ico = ico;
//        this.rating = rating;
//        this.largestBonus = largestBonus;
//        this.idealInvestmentInUsd = idealInvestmentInUsd;
//        this.idealInvestmentInEth = idealInvestmentInEth;
//        this.idealPortfolioPercentage = idealPortfolioPercentage;
//        this.usInvestorsAllowed = usInvestorsAllowed;
//        this.presaleDate = presaleDate;
//        this.icoStartDate = icoStartDate;
//        this.icoEndDate = icoEndDate;
//        this.icoStartsIn = icoStartsIn;
//        this.icoEndsIn = icoEndsIn;
//        this.type = type;
//        this.allStarTeamRating = allStarTeamRating;
//        this.allStarAdvisorsRating = allStarAdvisorsRating;
//        this.ideaRating = ideaRating;
    }
}
