package com.flipper.helpers;

import com.flipper.TestUtilities;
import com.flipper.models.Flip;
import com.flipper.models.Transaction;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import java.nio.file.Paths;
import java.nio.file.Path;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TradePersisterTest {
    // private List<Transaction> buys;
    // private List<Transaction> sells;
    // private List<Flip> flips;
    // private String testFilePath;
    // private Transaction buy;
    // private Transaction sell;
    // private Flip flip;

    // @Before
    // public void setUp() throws IOException {
    //     buys = new ArrayList<>();
    //     sells = new ArrayList<>();
    //     flips = new ArrayList<>();
    //     Path currentRelativePath = Paths.get("");
    //     testFilePath = currentRelativePath.toAbsolutePath().toString()
    //             + "\\src\\test\\java\\com\\flipper\\test-result-files";
    //     Persistor.setUp(testFilePath);

    //     buy = new Transaction(1, 1, 1, 1, "Coal", true, true);
    //     sell = new Transaction(2, 2, 2, 1, "Coal", false, true);
    //     flip = new Flip(buy, sell);

    //     buys.add(buy);
    //     sells.add(sell);
    //     flips.add(flip);
    // }

    // @After
    // public void tearDown() throws IOException {
    //     TestUtilities.cleanTestResultFiles();
    // }
    
    // @Test
    // public void testLoadFlipsReturnsListOfFlips() throws IOException {
    //     // Create test file to load
    //     Persistor.saveJson(flips, Persistor.FLIPS_JSON_FILE);
    //     List<Flip> returnedFlips = Persistor.loadFlips();
    //     assertEquals(returnedFlips.get(0).getBuy().getItemId(), flip.getBuy().getItemId());
    // }

    // @Test
    // public void testLoadBuysReturnsListOfBuys() throws IOException {
    //     // Create test file to load
    //     Persistor.saveJson(buys, Persistor.BUYS_JSON_FILE);
    //     List<Transaction> returnedBuys = Persistor.loadBuys();
    //     assertEquals(returnedBuys.get(0).getItemId(), buy.getItemId());
    // }

    // @Test
    // public void testLoadSellsReturnsListOfSells() throws IOException {
    //     // Create test file to load
    //     Persistor.saveJson(sells, Persistor.SELLS_JSON_FILE);
    //     List<Transaction> returnedSells = Persistor.loadSells();
    //     assertEquals(returnedSells.get(0).getItemId(), sell.getItemId());
    // }

    // @Test
    // public void testSetUpGeneratesRequiredFiles() throws IOException {
    //     Persistor.setUp(testFilePath);
    //     // Ensure all three files exist
    //     File buysFile = new File(this.testFilePath + "\\" + Persistor.BUYS_JSON_FILE);
    //     File sellsFile = new File(this.testFilePath + "\\" + Persistor.SELLS_JSON_FILE);
    //     File flipsFile = new File(this.testFilePath + "\\" + Persistor.FLIPS_JSON_FILE);
    //     assertTrue(buysFile.exists());
    //     assertTrue(sellsFile.exists());
    //     assertTrue(flipsFile.exists());
    // }

    // @Test
    // public void testSetUpControllerDoesNotOverridePreviousFiles() throws IOException {
    //     Persistor.saveJson(flips, Persistor.FLIPS_JSON_FILE);
    //     Persistor.saveJson(sells, Persistor.SELLS_JSON_FILE);
    //     Persistor.saveJson(buys, Persistor.BUYS_JSON_FILE);

    //     Persistor.setUp(testFilePath);

    //     // Ensure files are still populated (not overwritten)
    //     List<Flip> returnedFlips = Persistor.loadFlips();
    //     assertEquals(returnedFlips.get(0).getBuy().getItemId(), flip.getBuy().getItemId());
    //     List<Transaction> returnedBuys = Persistor.loadBuys();
    //     assertEquals(returnedBuys.get(0).getItemId(), buy.getItemId());
    //     List<Transaction> returnedSells = Persistor.loadSells();
    //     assertEquals(returnedSells.get(0).getItemId(), sell.getItemId());
    // }
}
