package com.crypto;

import com.crypto.reader.GoogleSheetReader;
import com.google.api.services.sheets.v4.Sheets;

import java.io.IOException;

public class Application {

    public static void main(String[] args) {
        try {
            Sheets service = GoogleSheetReader.getSheetsService();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
