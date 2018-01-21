package com.crypto.reader;

import com.crypto.entity.ICOEntry;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ICOSpreadsheetReader {

    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(ICOSpreadsheetReader.class);

    /**
     * ID of ICO spreadsheet to access
     */
    private final String ICO_SPREADSHEET_ID = "1qvCCS6lwEH9nOa8KwQGTVhtQ3VXPzed3rXUqksDQkT0";

    /**
     * Service for accessing Google Sheets
     */
    private Sheets googleSheetsService;

    public ICOSpreadsheetReader(Sheets googleSheetsService) {
        this.googleSheetsService = googleSheetsService;
    }

    /**
     * Extract data from the ICO spreadsheet
     */
    public List<ICOEntry> extractEntries() {
        List<ICOEntry> entries = new ArrayList<>();

        try {
            // Shorthand notation for retrieving all cells on a sheet
            List<String> ranges = new ArrayList<>();
            ranges.add("Overview!1:65536");

            // Extract each entry (row value) from the spreadsheet
            BatchGetValuesResponse response = googleSheetsService.spreadsheets().values().batchGet(this.ICO_SPREADSHEET_ID).setRanges(ranges).execute();
            if (response != null) {
                List<List<Object>> values =
                        response.getValueRanges()
                                .stream()
                                .flatMap(valueRange -> valueRange.getValues().stream())
                                .collect(Collectors.toList());

                // Find the maximum number of columns in any row
                Integer maxColumnCount =
                        values.stream()
                                .max(Comparator.comparingInt(List::size))
                                .get()
                                .size();
                logger.info("Max column count is {}", maxColumnCount);

                // All valid rows with data will have the maximum number of columns
                List<List<Object>> icoEntries =
                        values.stream()
                                .filter(row -> row.size() == maxColumnCount)
                                .collect(Collectors.toList());

                // Create a map of the column name to its index in case the spreadsheet order changes
                Map<String, Integer> columnIndexMap = new HashMap<>();
                boolean headerRow = true;
                for (List<Object> entry : icoEntries) {
                    if (headerRow) {
                        columnIndexMap = generateColumnIndexMap(entry);
                        headerRow = false;
                        logger.info("Created map of column name to index");
                    } else {
                        ICOEntry detailedIco = new ICOEntry(entry, columnIndexMap);
                        entries.add(detailedIco);
                    }

                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return entries;
    }

    /**
     * Create a mapping for each valid column index to name
     * Preserve the order with a LinkedHashMap
     * @param header
     * @return
     */
    private Map<String, Integer> generateColumnIndexMap(List<Object> header) {
        Map<String, Integer> map =
                IntStream.range(0, header.size())
                        .boxed()
                        .collect(Collectors.toMap(kv -> header.get(kv).toString(), kv -> kv,
                                (e1, e2) -> e1, LinkedHashMap::new));

        return map;
    }
}
