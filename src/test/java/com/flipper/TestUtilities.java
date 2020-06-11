package com.flipper;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.flipper.helpers.TradePersister;

public class TestUtilities {
    public static void cleanTestResultFiles() {
        Path currentRelativePath = Paths.get("");
        String testFilePath = currentRelativePath.toAbsolutePath().toString()
                + "\\src\\test\\java\\com\\flipper\\test-result-files";
        // Delete any generated test-result-files
        File deleteBuys = new File(testFilePath + "\\" + TradePersister.BUYS_JSON_FILE);
        File deleteSells = new File(testFilePath + "\\" + TradePersister.SELLS_JSON_FILE);
        File deleteMargins = new File(testFilePath + "\\" + TradePersister.MARGINS_JSON_FILE);
        File deleteFlips = new File(testFilePath + "\\" + TradePersister.FLIPS_JSON_FILE);
        deleteBuys.delete();
        deleteSells.delete();
        deleteMargins.delete();
        deleteFlips.delete();
    }

}