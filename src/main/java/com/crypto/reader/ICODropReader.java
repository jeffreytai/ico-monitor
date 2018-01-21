package com.crypto.reader;

import com.crypto.entity.ICODrop;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ICODropReader {

    private static final Logger logger = LoggerFactory.getLogger(ICODropReader.class);

    /**
     * Base url for retrieving data
     */
    private final String BASE_URL = "https://icodrops.com/";

    private final String DASH_CHARACTER = "-";

    public ICODropReader() {}

    /**
     * Pull data to create an ICO Drops entity
     * @param icoName
     * @return
     */
    public ICODrop extractDetails(String icoName) {
        Document doc = retrieveJsoupDocument(icoName);

        if (doc != null) {
            ICODrop icoDetails = new ICODrop(icoName, doc);
            logger.info("Retrieved ICO drops details for {}", icoName);
        }

        return null;
    }

    // TODO: Determine how to handle coins with "Code name"

    /**
     * To handle coins that have spaces or camelcase in the name,
     * try different combinations to find valid URL
     * @param icoName
     * @return
     */
    public Document retrieveJsoupDocument(String icoName) {
        String requestUrl = "";
        Document doc = null;

        // Try the regular ico name itself
        try {
            try {
                requestUrl = BASE_URL + icoName + "/";
                doc = Jsoup.connect(requestUrl).get();
                return doc;
            } catch (HttpStatusException ex) {
                // Try a dash in between each character in the name
                for (int i=1; i<icoName.length(); i++) {
                    String modifiedIcoName = icoName.substring(0, i) + DASH_CHARACTER + icoName.substring(i, icoName.length());
                    requestUrl = BASE_URL + modifiedIcoName + "/";
                    try {
                        doc = Jsoup.connect(requestUrl).get();
                        return doc;
                    } catch (HttpStatusException hex) {
                        continue;
                    }
                }
            }
        } catch (IOException ex) {
            logger.error("Unable to retrieve ICO drops URL for {}", icoName);
        }

        return null;
    }
}
