package com.crypto.processor;

import com.crypto.authentication.GoogleSheetsAuthentication;
import com.crypto.entity.ICODrop;
import com.crypto.entity.ICOEntry;
import com.crypto.enums.Authentication;
import com.crypto.reader.ICODropReader;
import com.crypto.reader.ICOSpreadsheetReader;
import com.google.api.services.sheets.v4.Sheets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ICOSpreadsheetProcessor {

    private static final Logger logger = LoggerFactory.getLogger(ICOSpreadsheetProcessor.class);

    public ICOSpreadsheetProcessor() {}

    // TODO: Fix Basic authentication
    public void process() {
        try {
            // Connect to Google Sheets API
            Sheets service = GoogleSheetsAuthentication.getSheetsService(Authentication.OAUTH);

            // Pull data from Balina's ICO spreadsheet
            ICOSpreadsheetReader reader = new ICOSpreadsheetReader(service);
            List<ICOEntry> entries = reader.extractEntries();

            // Retrieve ICO Drops data for each ICO entry
            ICODropReader icoDropReader = new ICODropReader();
            List<ICODrop> icoDropList = new ArrayList<>();

            for (ICOEntry entry : entries) {
                ICODrop icoDetails = icoDropReader.extractDetails(entry.getIco());

                if (icoDetails != null) {
                    icoDropList.add(icoDetails);
                }
            }
        } catch (IOException ex) {
            logger.error("Error in processing ICO spreadsheet");
        }
    }
}
