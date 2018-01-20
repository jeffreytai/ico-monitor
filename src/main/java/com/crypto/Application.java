package com.crypto;

import com.crypto.authentication.GoogleSheetsAuthentication;
import com.crypto.enums.Authentication;
import com.crypto.reader.ICOSpreadsheetReader;
import com.google.api.services.sheets.v4.Sheets;

import java.io.IOException;

public class Application {

    public static void main(String[] args) {
        try {
            Sheets service = GoogleSheetsAuthentication.getSheetsService(Authentication.OAUTH);
            ICOSpreadsheetReader reader = new ICOSpreadsheetReader(service);
            reader.processSpreadsheet();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
