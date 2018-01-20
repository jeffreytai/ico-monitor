package com.crypto.authentication;

import com.crypto.enums.Authentication;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class GoogleSheetsAuthentication {

    private static final Logger logger = LoggerFactory.getLogger(GoogleSheetsAuthentication.class);

    /**
     * Name of application
     */
    private static final String APPLICATION_NAME = "ico-monitor";

    /**
     * Global instance of JSON factory
     */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /**
     * Global instance of the scopes required for this application
     */
    private static final List<String> SCOPES = Arrays.asList(SheetsScopes.SPREADSHEETS_READONLY);

    /**
     * Directory to store user credentials for application
     */
    private static final File DATA_STORE_DIR = new File(
            System.getProperty("user.dir"), ".credentials/ico-monitor");

    /**
     * Global instance of the FileDataStoreFactory
     */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /**
     * Global instance of the HTTP transport
     */
    private static HttpTransport HTTP_TRANSPORT;


    /************************
     * Constants
     ************************/

    /**
     * Required access type for API
     */
    private static final String ACCESS_TYPE = "offline";

    /**
     * File name that contains OAuth 2.0 credentials
     */
    private static final String PROPERTIES_FILE = "client_secret.json";

    /**
     * Name of file for basic authentication
     */
    private static final String CREDENTIALS_FILE = "google.properties";

    /**
     * Client ID for basic authentication
     */
    private static String CLIENT_ID = "";

    /**
     * Client secret for basic authentication
     */
    private static String CLIENT_SECRET = "";

    /**
     * Google sheets service
     */
    private static Sheets service;

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    public GoogleSheetsAuthentication() {
        try {
            Properties props = new Properties();
            props.load(new InputStreamReader(GoogleSheetsAuthentication.class.getClassLoader().getResourceAsStream(CREDENTIALS_FILE)));

            CLIENT_ID = props.getProperty("client-id");
            CLIENT_SECRET = props.getProperty("client-secret");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public static Sheets getSheetsService(Authentication authenticationType) throws IOException {
        if (authenticationType == Authentication.OAUTH) {
            Credential credential = authorize();
            service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME)
                    .build();

            logger.info("Connected to Google Sheets service through OAuth authentication");
        }
        else if (authenticationType == Authentication.BASIC) {
            BasicAuthentication credentials = new BasicAuthentication(CLIENT_ID, CLIENT_SECRET);
            service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credentials)
                    .setApplicationName(APPLICATION_NAME)
                    .build();

            logger.info("Connected to Google Sheets service through basic authentication");
        }
        return service;
    }

    private static Credential authorize() throws IOException {
        // Load client secrets
        InputStream stream = GoogleSheetsAuthentication.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(stream));

        // Build flow and trigger user authorization request
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow
                .Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(DATA_STORE_FACTORY)
                .setAccessType(ACCESS_TYPE)
                .build();

        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("user");
        logger.info("Credentials saved to {}", DATA_STORE_DIR.getAbsolutePath());

        return credential;
    }
}
