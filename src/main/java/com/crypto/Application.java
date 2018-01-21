package com.crypto;

import com.crypto.orm.HibernateUtils;
import com.crypto.processor.ICOSpreadsheetProcessor;

public class Application {

    public static void main(String[] args) {
        ICOSpreadsheetProcessor processor = new ICOSpreadsheetProcessor();
        processor.process();

        // Clean up existing database connections
        HibernateUtils.shutdown();
    }
}
