package com.crypto.writer;

import com.crypto.entity.ICODrop;
import com.crypto.entity.ICOEntry;
import com.crypto.utils.StringUtils;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.AutoResizeDimensionsRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesRequest;
import com.google.api.services.sheets.v4.model.CellData;
import com.google.api.services.sheets.v4.model.CellFormat;
import com.google.api.services.sheets.v4.model.DimensionRange;
import com.google.api.services.sheets.v4.model.GridCoordinate;
import com.google.api.services.sheets.v4.model.GridRange;
import com.google.api.services.sheets.v4.model.RepeatCellRequest;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.SheetProperties;
import com.google.api.services.sheets.v4.model.TextFormat;
import com.google.api.services.sheets.v4.model.UpdateCellsRequest;
import com.google.api.services.sheets.v4.model.UpdateSheetPropertiesRequest;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
     * Service for accessing Google Sheets
     */
    private Sheets googleSheetsService;

    /**
     * Map of column names from original spreadsheet
     */
    private Map<String, Integer> columnIndexMap;

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
        // Pre-formatting updates
        formatHeaderRow();

        // Setup header row and rename tab
        setupSheet();

        // Post ICO details to sheet
        postResults(icoDropList);

        formatSheet();
    }

    /**
     * Setup the Google Sheet by adding the header row and renaming the tab to today's date
     * // TODO: Rename tab to today's date
     */
    private void setupSheet() {
        String range = "Sheet1!A1";
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

    private void postResults(List<ICODrop> icoDropList) {
        String range = "Sheet1!A2";
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
    }

    private void formatSheet() {
        List<Request> requests = new ArrayList<>();

        // Automatically resize columns
        requests.add(new Request()
                .setAutoResizeDimensions(new AutoResizeDimensionsRequest()
                        .setDimensions(new DimensionRange()
                                .setStartIndex(0)
                                .setDimension("COLUMNS"))));

        BatchUpdateSpreadsheetRequest body = new BatchUpdateSpreadsheetRequest().setRequests(requests);

        try {
            googleSheetsService.spreadsheets().batchUpdate(this.PERSONAL_SPREADSHEET_ID, body).execute();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void formatHeaderRow() {
        List<Request> requests = new ArrayList<>();

        // Bold the header row
        requests.add(new Request()
                .setRepeatCell(new RepeatCellRequest()
                        .setRange(new GridRange()
                                .setEndRowIndex(1))
                        .setCell(new CellData()
                                .setUserEnteredFormat(new CellFormat()
                                        .setTextFormat(new TextFormat().setBold(true))))
                        .setFields("*")));

        BatchUpdateSpreadsheetRequest body = new BatchUpdateSpreadsheetRequest().setRequests(requests);

        try {
            googleSheetsService.spreadsheets().batchUpdate(this.PERSONAL_SPREADSHEET_ID, body).execute();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
