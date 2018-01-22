package com.crypto.processor;

import com.crypto.authentication.GoogleSheetsAuthentication;
import com.crypto.entity.ICODrop;
import com.crypto.entity.ICOEntry;
import com.crypto.enums.Authentication;
import com.crypto.reader.ICODropReader;
import com.crypto.reader.ICOSpreadsheetReader;
import com.crypto.writer.ICOSpreadsheetWriter;
import com.google.api.services.sheets.v4.Sheets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ICOSpreadsheetProcessor {

    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(ICOSpreadsheetProcessor.class);

    public ICOSpreadsheetProcessor() {}

    // TODO: Fix Basic authentication

    /**
     * Connect to Google Sheets API
     * Pull data from ICO spreadsheet
     * Retrieve data from ICO Drops page
     * Store data in personal Google Sheets
     */
    public void process() {
        try {
            // Connect to Google Sheets API
            Sheets service = GoogleSheetsAuthentication.getSheetsService(Authentication.OAUTH);

            // Pull data from Balina's ICO spreadsheet
            ICOSpreadsheetReader reader = new ICOSpreadsheetReader(service);
            List<ICOEntry> entries = reader.extractEntries();
            Map<String, Integer> columnIndexMap = reader.getColumnIndexMap();

            // Retrieve ICO Drops data for each ICO entry
            ICODropReader icoDropReader = new ICODropReader();
            List<ICODrop> icoDropList = new ArrayList<>();

            for (ICOEntry entry : entries) {
                ICODrop icoDetails = icoDropReader.extractDetails(entry);

                if (icoDetails != null) {
                    icoDropList.add(icoDetails);
                }
            }

            ICOSpreadsheetWriter writer = new ICOSpreadsheetWriter(service, columnIndexMap);
            writer.processResults(icoDropList);
        } catch (IOException ex) {
            logger.error("Error in processing ICO spreadsheet");
        }
    }
}
