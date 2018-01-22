package com.crypto.reader;

import com.crypto.entity.ICODrop;
import com.crypto.entity.ICOEntry;
import com.crypto.orm.entity.ICOInformation;
import com.crypto.orm.repository.ICOInformationRepository;
import com.crypto.utils.DbUtils;
import com.crypto.utils.StringUtils;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;

public class ICODropReader {

    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(ICODropReader.class);

    /**
     * Base url for retrieving data
     */
    private final String BASE_URL = "https://icodrops.com/";

    /**
     * Dash character used for different combinations of coin URL
     */
    private final String DASH_CHARACTER = "-";

    public ICODropReader() {}

    /**
     * Pull data to create an ICO Drops entity
     * @param entry
     * @return
     */
    public ICODrop extractDetails(ICOEntry entry) {
        String icoName = entry.getIco();
        Document doc = retrieveJsoupDocument(icoName);

        if (doc != null) {
            logger.info("Retrieving ICO drops details for {}", icoName);
            ICODrop icoDetails = new ICODrop(doc, entry);

            return icoDetails;
        }

        return null;
    }

    // TODO: Handle coins that need "the" in front of the name
    /**
     * To handle coins that have spaces or camelcase in the name,
     * try different combinations to find valid URL
     * @param icoName
     * @return
     */
    private Document retrieveJsoupDocument(String icoName) {
        String requestUrl = StringUtils.EMPTY_STRING;
        String sanitizedIcoName = StringUtils.EMPTY_STRING;
        Document doc = null;
        boolean isNewEntry = true;

        // First attempt to find by the name, then the code name, then add it
        sanitizedIcoName = StringUtils.sanitizeAlphabeticalStringValue(icoName);
        ICOInformation nameInformation = ICOInformationRepository.findByName(sanitizedIcoName);

        String potentialIcoCodeName = StringUtils.sanitizeIcoCodeName(icoName);
        ICOInformation codeNameInformation = ICOInformationRepository.findByCodeName(potentialIcoCodeName);
        if (nameInformation != null) {
            sanitizedIcoName = nameInformation.getName();
            requestUrl = nameInformation.getUrl();
            isNewEntry = false;
        }
        else if (codeNameInformation != null) {
            sanitizedIcoName = codeNameInformation.getName();
            requestUrl = codeNameInformation.getUrl();
            isNewEntry = false;
        }
        else {
            sanitizedIcoName = StringUtils.sanitizeAlphabeticalStringValue(potentialIcoCodeName);
            requestUrl = this.BASE_URL + sanitizedIcoName + "/";
            isNewEntry = true;
        }

        // Try the base ico name itself
        try {
            try {
                doc = Jsoup.connect(requestUrl).get();
            } catch (HttpStatusException ex) {
                if (isNewEntry) {
                    // Try a dash in between each character in the name
                    for (int i=1; i<sanitizedIcoName.length(); i++) {
                        String modifiedIcoName = sanitizedIcoName.substring(0, i) + this.DASH_CHARACTER + sanitizedIcoName.substring(i, sanitizedIcoName.length());
                        requestUrl = this.BASE_URL + modifiedIcoName + "/";
                        try {
                            doc = Jsoup.connect(requestUrl).get();
                            break;
                        } catch (HttpStatusException hex) {
                            continue;
                        }
                    }
                }
            }
        } catch (IOException ex) {
            logger.error("Unable to retrieve ICO drops URL for {}", icoName);
        } finally {
            // If the ICO detail is new, save it to the database
            if (isNewEntry) {
                ICOInformation newEntry = new ICOInformation(
                        sanitizedIcoName,
                        icoName.contains("(Code Name)") ? potentialIcoCodeName : StringUtils.EMPTY_STRING,
                        doc != null ? requestUrl : StringUtils.EMPTY_STRING,
                        new Date()
                );
                DbUtils.saveEntity(newEntry);
            }

            if (doc == null) {
                logger.error("Unable to retrieve ICO drops URL for {}", icoName);
            }
            return doc;
        }
    }
}
