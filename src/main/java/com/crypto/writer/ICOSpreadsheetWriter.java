package com.crypto.writer;

import com.crypto.entity.ICODrop;
import com.crypto.entity.ICOEntry;
import com.crypto.slack.SlackWebhook;
import com.crypto.utils.StringUtils;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.AddSheetRequest;
import com.google.api.services.sheets.v4.model.AutoResizeDimensionsRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetResponse;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesRequest;
import com.google.api.services.sheets.v4.model.CellData;
import com.google.api.services.sheets.v4.model.CellFormat;
import com.google.api.services.sheets.v4.model.DimensionProperties;
import com.google.api.services.sheets.v4.model.DimensionRange;
import com.google.api.services.sheets.v4.model.GridRange;
import com.google.api.services.sheets.v4.model.RepeatCellRequest;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.SheetProperties;
import com.google.api.services.sheets.v4.model.TextFormat;
import com.google.api.services.sheets.v4.model.UpdateDimensionPropertiesRequest;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ICOSpreadsheetWriter {

    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(ICOSpreadsheetWriter.class);

    /**
     * Columns for ICO Drop entries
     */
    private static Map<String, Boolean> icoDropColumnVisibility;

    /**
     * ID of ICO spreadsheet to write to
     */
    private final String PERSONAL_SPREADSHEET_ID = "1cJGBc2_9u_5Q-WNbe3eXSMYgEhurlINtyR7SDsAT78k";

    /**
     * Date format for use in Sheet titles
     */
    private final String DATE_FORMAT = "MM-dd";

    /**
     * Slack username to post as
     */
    private final String SLACK_USERNAME = "ico-spreadsheet-alert";

    /**
     * Service for accessing Google Sheets
     */
    private Sheets googleSheetsService;

    /**
     * Map of column names from original spreadsheet
     */
    private Map<String, Integer> columnIndexMap;

    /**
     * Hidden columns
     * TODO: Determine a way to better do this with the original column mapping
     */
    private static Set<String> hiddenColumns;

    static {
        icoDropColumnVisibility = new HashMap<>();
        icoDropColumnVisibility.put("URL", true);
        icoDropColumnVisibility.put("Hype Rate", true);
        icoDropColumnVisibility.put("Risk Rate", true);
        icoDropColumnVisibility.put("ROI Rate", true);
        icoDropColumnVisibility.put("Overall Score", true);
        icoDropColumnVisibility.put("Ticker", true);
        icoDropColumnVisibility.put("Token Type", true);
        icoDropColumnVisibility.put("ICO Token Price", true);
        icoDropColumnVisibility.put("Fundraising Goal", true);
        icoDropColumnVisibility.put("Sold On Pre-sale", true);
        icoDropColumnVisibility.put("Total Tokens", true);
        icoDropColumnVisibility.put("Available for Token Sale", true);
        icoDropColumnVisibility.put("Whitelist", true);
        icoDropColumnVisibility.put("Know Your Customer", true);
        icoDropColumnVisibility.put("Bonus for the First", true);
        icoDropColumnVisibility.put("Can't Participate", true);
        icoDropColumnVisibility.put("Min/Max Personal Cap", true);
        icoDropColumnVisibility.put("Token Issue", true);
        icoDropColumnVisibility.put("Accepts", true);

        hiddenColumns = new HashSet<>();
        hiddenColumns.add("Graded");
        hiddenColumns.add("IAN INVESTED");
        hiddenColumns.add("IAN'S OPINION");
        hiddenColumns.add("Ideal Investment (USD)");
        hiddenColumns.add("Ideal Investment (ETH)");
        hiddenColumns.add("Ideal % Portfolio");
    }

    public ICOSpreadsheetWriter(Sheets googleSheetsService, Map<String, Integer> columnIndexMap) {
        this.googleSheetsService = googleSheetsService;
        this.columnIndexMap = columnIndexMap;
    }

    /**
     * Post ICO information to personal Google Sheet
     * @param icoDropList
     */
    public void processResults(List<ICODrop> icoDropList) {
        Date today = new Date();
        String sheetTitle = StringUtils.EMPTY_STRING;
        sheetTitle = new SimpleDateFormat(this.DATE_FORMAT).format(today);

        // Add new sheet in spreadsheet
        Integer sheetId = addSheet(sheetTitle);

        // Pre-formatting updates
        formatHeaderRow(sheetId);

        // Setup header row and rename tab
        setupSheet(sheetTitle);

        // Post ICO details to sheet
        postResults(sheetTitle, icoDropList);

        // Resize columns
        String spreadsheetUrl = formatSheetAndFindUrl(sheetId);

        // Send slack alert
        sendSlackAlert(spreadsheetUrl, sheetId);
    }

    /**
     * Setup the Google Sheet by adding the header row and renaming the tab to today's date
     * @param sheetTitle
     */
    private void setupSheet(String sheetTitle) {
        String range = sheetTitle + "!A1";
        List<List<Object>> sheetData = new ArrayList<>();

        List<Object> rowData = new ArrayList<>();

        // ICO Entry columns
        for (String columnEntry : columnIndexMap.keySet()) {
            rowData.add(columnEntry);
        }

        // ICO Drop columns
        for (String columnEntry : icoDropColumnVisibility.keySet()) {
            rowData.add(columnEntry);
        }

        sheetData.add(rowData);

        ValueRange valueRange = new ValueRange();
        valueRange.setRange(range);
        valueRange.setValues(sheetData);

        List<ValueRange> oList = new ArrayList<>();
        oList.add(valueRange);

        BatchUpdateValuesRequest oRequest = new BatchUpdateValuesRequest();
        oRequest.setValueInputOption("RAW");
        oRequest.setData(oList);

        try {
            googleSheetsService.spreadsheets().values().batchUpdate(this.PERSONAL_SPREADSHEET_ID, oRequest).execute();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Send results from ICO Drop list to spreadsheet
     * @param sheetTitle
     * @param icoDropList
     */
    private void postResults(String sheetTitle, List<ICODrop> icoDropList) {
        String range = sheetTitle + "!A2";
        List<List<Object>> sheetData = new ArrayList<>();

        for (ICODrop icoDrop : icoDropList) {
            List<Object> rowData = new ArrayList<>();
            ICOEntry entry = icoDrop.getIcoEntry();

            // ICO Entry columns
            Field[] icoEntryFields = entry.getClass().getDeclaredFields();
            for (String columnName : columnIndexMap.keySet()) {
                List<Field> matchedFields =
                        Arrays.stream(icoEntryFields)
                                .filter(f ->
                                        StringUtils.areStringsEqualIgnoreCase(f.getName(), StringUtils.sanitizeAlphabeticalStringValue(columnName)))
                                .collect(Collectors.toList());

                if (matchedFields.size() != 1) {
                    logger.error("Missing field for {}", columnName);
                    rowData.add(StringUtils.EMPTY_STRING);
                    continue;
                }

                Field matchedField = matchedFields.get(0);
                try {
                    Object fieldValue = new PropertyDescriptor(matchedField.getName(), entry.getClass()).getReadMethod().invoke(entry);

                    if (fieldValue == null) {
                        rowData.add(StringUtils.EMPTY_STRING);
                    }
                    else {
                        rowData.add(fieldValue);
                    }

                } catch (IntrospectionException | IllegalAccessException | InvocationTargetException ex) {
                    logger.error("Illegal access for ICO Entry {}", entry.getIco());
                }
            }

            // ICO Drop columns
            Field[] icoDropFields = icoDrop.getClass().getDeclaredFields();
            for (String columnName : icoDropColumnVisibility.keySet()) {
                List<Field> matchedFields =
                        Arrays.stream(icoDropFields)
                                .filter(f ->
                                        StringUtils.areStringsEqualIgnoreCase(f.getName(), StringUtils.sanitizeAlphabeticalStringValue(columnName)))
                                .collect(Collectors.toList());

                if (matchedFields.size() != 1) {
                    logger.error("Missing field for {}", columnName);
                    rowData.add(StringUtils.EMPTY_STRING);
                    continue;
                }

                Field matchedField = matchedFields.get(0);
                try {
                    Object fieldValue = new PropertyDescriptor(matchedField.getName(), icoDrop.getClass()).getReadMethod().invoke(icoDrop);

                    if (fieldValue == null) {
                        rowData.add(StringUtils.EMPTY_STRING);
                    }
                    else {
                        rowData.add(fieldValue);
                    }
                } catch (IntrospectionException | IllegalAccessException | InvocationTargetException ex) {
                    logger.error("Illegal access for ICO Drop {}", icoDrop.getName());
                }
            }

            sheetData.add(rowData);
        }

        ValueRange valueRange = new ValueRange();
        valueRange.setRange(range);
        valueRange.setValues(sheetData);

        List<ValueRange> oList = new ArrayList<>();
        oList.add(valueRange);

        BatchUpdateValuesRequest oRequest = new BatchUpdateValuesRequest();
        oRequest.setValueInputOption("RAW");
        oRequest.setData(oList);

        try {
            googleSheetsService.spreadsheets().values().batchUpdate(this.PERSONAL_SPREADSHEET_ID, oRequest).execute();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        logger.info("ICO drop results posted to spreadsheet");
    }

    /**
     * Automatically resize columns and hide specified columns
     * @param sheetId
     * @return
     */
    private String formatSheetAndFindUrl(Integer sheetId) {
        List<Request> requests = new ArrayList<>();

        // Automatically resize columns
        requests.add(new Request()
                .setAutoResizeDimensions(new AutoResizeDimensionsRequest()
                        .setDimensions(new DimensionRange()
                                .setSheetId(sheetId)
                                .setStartIndex(0)
                                .setDimension("COLUMNS"))));

        // Hide specified columns
        Set<Integer> hiddenColumnIndices = new HashSet<>();
        for (Map.Entry<String, Integer> entry : this.columnIndexMap.entrySet()) {
            if (this.hiddenColumns.contains(entry.getKey())) {
                hiddenColumnIndices.add(entry.getValue());
            }
        }

        for (Integer index : hiddenColumnIndices) {
            requests.add(new Request()
                    .setUpdateDimensionProperties(new UpdateDimensionPropertiesRequest()
                            .setRange(new DimensionRange()
                                    .setSheetId(sheetId)
                                    .setDimension("COLUMNS")
                                    .setStartIndex(index)
                                    .setEndIndex(index+1))
                            .setProperties(new DimensionProperties()
                                    .setHiddenByUser(true))
                            .setFields("hiddenByUser")));
        }

        BatchUpdateSpreadsheetResponse response = applyBatchUpdateRequestsToSpreadsheet(requests);

        logger.info("Spreadsheet formatted and specified columns hidden");

        String spreadsheetUrl = response.getUpdatedSpreadsheet().getSpreadsheetUrl();
        return spreadsheetUrl;
    }

    /**
     * Bold the header row
     * @param sheetId
     */
    private void formatHeaderRow(Integer sheetId) {
        List<Request> requests = new ArrayList<>();

        // Bold the header row
        requests.add(new Request()
                .setRepeatCell(new RepeatCellRequest()
                        .setRange(new GridRange()
                                .setSheetId(sheetId)
                                .setEndRowIndex(1))
                        .setCell(new CellData()
                                .setUserEnteredFormat(new CellFormat()
                                        .setTextFormat(new TextFormat().setBold(true))))
                        .setFields("*")));

        applyBatchUpdateRequestsToSpreadsheet(requests);

        logger.info("Formatted header row");
    }

    /**
     * Adds a new sheet and returns the sheet id
     * @param sheetTitle
     */
    private Integer addSheet(String sheetTitle) {
        List<Request> requests = new ArrayList<>();

        requests.add(new Request()
                .setAddSheet(new AddSheetRequest()
                        .setProperties(new SheetProperties()
                                .setTitle(sheetTitle))));

        BatchUpdateSpreadsheetResponse response = applyBatchUpdateRequestsToSpreadsheet(requests);

        Integer sheetId = response.getReplies().get(0).getAddSheet().getProperties().getSheetId();

        logger.info("{} sheet added with id {}", sheetTitle, sheetId);
        return sheetId;
    }

    /**
     * Apply batch update requests to spreadsheet and return the update response
     * @param requests
     * @return
     */
    private BatchUpdateSpreadsheetResponse applyBatchUpdateRequestsToSpreadsheet(List<Request> requests) {
        BatchUpdateSpreadsheetRequest body = new BatchUpdateSpreadsheetRequest()
                .setRequests(requests)
                .setIncludeSpreadsheetInResponse(true);

        BatchUpdateSpreadsheetResponse response = null;

        try {
            response = googleSheetsService.spreadsheets().batchUpdate(this.PERSONAL_SPREADSHEET_ID, body).execute();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return response;
    }

    /**
     * Send slack alert of new spreadsheet with link
     * @param spreadsheetUrl
     */
    private void sendSlackAlert(String spreadsheetUrl, Integer sheetId) {
        SlackWebhook slack = new SlackWebhook(this.SLACK_USERNAME);

        String tabbedSpreadsheetUrl = spreadsheetUrl + "#gid=" + sheetId;

        String message = String.format("New ICO spreadsheet posted - %s\n%s", new Date().toString(), tabbedSpreadsheetUrl);
        slack.sendMessage(message);

        logger.info("Sent slack alert message");
        slack.shutdown();
    }
}
