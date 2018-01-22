package com.crypto.entity;

import com.crypto.utils.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedHashMap;
import java.util.Map;

public class ICODrop {

    /**
     * Name of ICO
     */
    private String name;

    /**
     * URL to ICO Drops page
     */
    private String url;

    /*************************
     * ICO Drop rating
     *************************/

    /**
     * Predicted hype rate
     */
    private String hypeRate;

    /**
     * Predicted risk rate
     */
    private String riskRate;

    /**
     * Predicted return on investment rate
     */
    private String roiRate;

    /**
     * Overall score (e.g. Very high interest)
     */
    private String overallScore;

    /*************************
     * Token Sale information
     *************************/

    /**
     * Coin ticket
     */
    private String ticker;

    /**
     * Type of token
     */
    private String tokenType;

    /**
     * ICO price in USD
     */
    private String icoTokenPrice;

    /**
     * ICO fundraising goal in USD
     */
    private String fundraisingGoal;

    /**
     * Details on token amount sold for pre-sale
     */
    private String soldOnPresale;

    /**
     * Total number of tokens minted
     */
    private String totalTokens;

    /**
     * Amount of tokens available for token sale as a percentage
     */
    private String availableForTokenSale;

    /**
     * Details on whitelist availability and dates
     */
    private String whitelist;

    /**
     * KYC requirement details
     */
    private String knowYourCustomer;

    /**
     * Optional bonus details
     */
    private String bonusForTheFirst;

    /**
     * Countries banned from participation
     */
    private String cantParticipate;

    /**
     * Individual max/min contribution
     */
    private String minMaxPersonalCap;

    /**
     * Details on token issuance after the sale
     */
    private String tokenIssue;

    /**
     * Coins accepted for ICO (e.g. BTC, ETH)
     */
    private String accepts;

    /**
     * ICO entry entity from Balina's spreadsheet
     */
    private ICOEntry icoEntry;


    public ICODrop() {}

    public ICODrop(Document doc, ICOEntry icoEntry) {
        String url = doc.location();

        String coinName = doc.select(".ico-desk .ico-main-info h3").text();
        String hypeRate = doc.select(".rating-field .rating-items .rating-item:nth-child(1) p.rate").text();
        String riskRate = doc.select(".rating-field .rating-items .rating-item:nth-child(2) p.rate").text();
        String roiRate = doc.select(".rating-field .rating-items .rating-item:nth-child(3) p.rate").text();
        String overallScore = doc.select(".rating-result .rating-box p.ico-rate").text();

        // Create a map for each row value
        Map<String, String> tokenSaleInformation = new LinkedHashMap<>();
        Elements tokenSaleDetails = doc.select(".white-desk.ico-desk .row.list li");
        for (Element detail : tokenSaleDetails) {
            String detailText = detail.text();
            String delimiter = ": ";

            if (detailText.contains(delimiter)) {
                String key = detailText.substring(0, detailText.indexOf(delimiter));
                String value = detailText.substring(detailText.indexOf(delimiter) + delimiter.length(), detailText.length());

                tokenSaleInformation.put(key, value);
            }
        }

        String ticker = StringUtils.extractValueFromMap("Ticker", tokenSaleInformation);
        String tokenType = StringUtils.extractValueFromMap("Token type", tokenSaleInformation);
        String icoTokenPrice = StringUtils.extractValueFromMap("ICO Token Price", tokenSaleInformation);
        String fundraisingGoal = StringUtils.extractValueFromMap("Fundraising Goal", tokenSaleInformation);
        String soldOnPresale = StringUtils.extractValueFromMap("Sold on pre-sale", tokenSaleInformation);
        String totalTokens = StringUtils.extractValueFromMap("Total Tokens", tokenSaleInformation);
        String availableForTokenSale = StringUtils.extractValueFromMap("Available for Token Sale", tokenSaleInformation);
        String whitelist = StringUtils.extractValueFromMap("Whitelist", tokenSaleInformation);
        String knowYourCustomer = StringUtils.extractValueFromMap("Know Your Customer (KYC)", tokenSaleInformation);
        String bonusForTheFirst = StringUtils.extractValueFromMap("Bonus for the First", tokenSaleInformation);
        String cantParticipate = StringUtils.extractValueFromMap("Сan\'t participate", tokenSaleInformation);
        String minMaxPersonalCap = StringUtils.extractValueFromMap("Min/Max Personal Cap", tokenSaleInformation);
        String tokenIssue = StringUtils.extractValueFromMap("Token Issue", tokenSaleInformation);
        String accepts = StringUtils.extractValueFromMap("Accepts", tokenSaleInformation);

        this.name = coinName;
        this.url = url;
        this.hypeRate = hypeRate;
        this.riskRate = riskRate;
        this.roiRate = roiRate;
        this.overallScore = overallScore;
        this.ticker = ticker;
        this.tokenType = tokenType;
        this.icoTokenPrice = icoTokenPrice;
        this.fundraisingGoal = fundraisingGoal;
        this.soldOnPresale = soldOnPresale;
        this.totalTokens = totalTokens;
        this.availableForTokenSale = availableForTokenSale;
        this.whitelist = whitelist;
        this.knowYourCustomer = knowYourCustomer;
        this.bonusForTheFirst = bonusForTheFirst;
        this.cantParticipate = cantParticipate;
        this.minMaxPersonalCap = minMaxPersonalCap;
        this.tokenIssue = tokenIssue;
        this.accepts = accepts;

        this.icoEntry = icoEntry;
    }

    /**************************
     * Getters and setters
     **************************/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHypeRate() {
        return hypeRate;
    }

    public void setHypeRate(String hypeRate) {
        this.hypeRate = hypeRate;
    }

    public String getRiskRate() {
        return riskRate;
    }

    public void setRiskRate(String riskRate) {
        this.riskRate = riskRate;
    }

    public String getRoiRate() {
        return roiRate;
    }

    public void setRoiRate(String roiRate) {
        this.roiRate = roiRate;
    }

    public String getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(String overallScore) {
        this.overallScore = overallScore;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getIcoTokenPrice() {
        return icoTokenPrice;
    }

    public void setIcoTokenPrice(String icoTokenPrice) {
        this.icoTokenPrice = icoTokenPrice;
    }

    public String getFundraisingGoal() {
        return fundraisingGoal;
    }

    public void setFundraisingGoal(String fundraisingGoal) {
        this.fundraisingGoal = fundraisingGoal;
    }

    public String getSoldOnPresale() {
        return soldOnPresale;
    }

    public void setSoldOnPresale(String soldOnPresale) {
        this.soldOnPresale = soldOnPresale;
    }

    public String getTotalTokens() {
        return totalTokens;
    }

    public void setTotalTokens(String totalTokens) {
        this.totalTokens = totalTokens;
    }

    public String getAvailableForTokenSale() {
        return availableForTokenSale;
    }

    public void setAvailableForTokenSale(String availableForTokenSale) {
        this.availableForTokenSale = availableForTokenSale;
    }

    public String getWhitelist() {
        return whitelist;
    }

    public void setWhitelist(String whitelist) {
        this.whitelist = whitelist;
    }

    public String getKnowYourCustomer() {
        return knowYourCustomer;
    }

    public void setKnowYourCustomer(String knowYourCustomer) {
        this.knowYourCustomer = knowYourCustomer;
    }

    public String getBonusForTheFirst() {
        return bonusForTheFirst;
    }

    public void setBonusForTheFirst(String bonusForTheFirst) {
        this.bonusForTheFirst = bonusForTheFirst;
    }

    public String getCantParticipate() {
        return cantParticipate;
    }

    public void setCantParticipate(String cantParticipate) {
        this.cantParticipate = cantParticipate;
    }

    public String getMinMaxPersonalCap() {
        return minMaxPersonalCap;
    }

    public void setMinMaxPersonalCap(String minMaxPersonalCap) {
        this.minMaxPersonalCap = minMaxPersonalCap;
    }

    public String getTokenIssue() {
        return tokenIssue;
    }

    public void setTokenIssue(String tokenIssue) {
        this.tokenIssue = tokenIssue;
    }

    public String getAccepts() {
        return accepts;
    }

    public void setAccepts(String accepts) {
        this.accepts = accepts;
    }

    public ICOEntry getIcoEntry() {
        return icoEntry;
    }

    public void setIcoEntry(ICOEntry icoEntry) {
        this.icoEntry = icoEntry;
    }
}
