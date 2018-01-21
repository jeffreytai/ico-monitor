package com.crypto.writer;

import com.crypto.entity.ICODrop;
import com.crypto.entity.ICOEntry;
import com.google.api.services.sheets.v4.Sheets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class ICOSpreadsheetWriter {

    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(ICOSpreadsheetWriter.class);

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

    public ICOSpreadsheetWriter(Sheets googleSheetsService, Map<String, Integer> columnIndexMap) {
        this.googleSheetsService = googleSheetsService;
        this.columnIndexMap = columnIndexMap;
    }

    public void processResults(List<ICOEntry> icoEntries, List<ICODrop> icoDropList) {

    }
}
