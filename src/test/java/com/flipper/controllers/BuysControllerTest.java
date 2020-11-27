package com.flipper.controllers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import net.runelite.api.GrandExchangeOfferState;
import net.runelite.api.ItemComposition;
import net.runelite.client.game.ItemManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.flipper.TestUtilities;
import com.flipper.helpers.TradePersister;
import com.flipper.mocks.MockGrandExchangeOffer;
import com.flipper.models.Transaction;

public class BuysControllerTest {
    ItemManager itemManager;
    ItemComposition itemComposition;
    MockGrandExchangeOffer offerToCreateNewTransaction;
    MockGrandExchangeOffer offerToUpdateTransaction;
    Transaction transactionToUpdate;
    TradePersister tradePersister;
    int mockItemId = 400;

    @Before
    public void setUp() throws IOException {
        offerToCreateNewTransaction = new MockGrandExchangeOffer(100, mockItemId, 100, 10, 1000,
                GrandExchangeOfferState.BOUGHT);
        offerToUpdateTransaction = new MockGrandExchangeOffer(10, mockItemId, 10, 100, 1000,
                GrandExchangeOfferState.BOUGHT);
        transactionToUpdate = new Transaction(5, 10, mockItemId, 100, "Test Item", true, false);
        itemManager = mock(ItemManager.class);
        itemComposition = mock(ItemComposition.class);
        Path currentRelativePath = Paths.get("");
        String testFilePath = currentRelativePath.toAbsolutePath().toString()
                + "\\src\\test\\java\\com\\flipper\\test-result-files";
        TradePersister.setUp(testFilePath);
        when(itemManager.getItemComposition(mockItemId)).thenReturn(itemComposition);
        when(itemComposition.getName()).thenReturn("Test Name");
    }

    @After
    public void tearDown() {
        TestUtilities.cleanTestResultFiles();
    }

    @Test
    public void testCreateBuyCreatesANewBuy() throws Exception {
        BuysController buysController = new BuysController(itemManager);
        buysController.setBuys(new ArrayList<Transaction>());
        buysController.createBuy(offerToCreateNewTransaction);
        List<Transaction> buys = buysController.getBuys();
        assertEquals(1, buys.size());
        Transaction createdBuy = buys.get(0);
        assertEquals(mockItemId, createdBuy.getItemId());
    }

    @Test
    public void testCreateBuyUpdatesAnExistingBuy() throws Exception {
        BuysController buysController = new BuysController(itemManager);
        // Add partially complete buy to see if we're updating transactions correctly
        List<Transaction> buys = new ArrayList<Transaction>();
        buys.add(transactionToUpdate);
        buysController.setBuys(buys);

        buysController.createBuy(offerToUpdateTransaction);
        // Ensure buys list is still only one (only updated transaction);
        List<Transaction> updatedBuys = buysController.getBuys();
        assertEquals(1, updatedBuys.size());

        Transaction updateBuy = updatedBuys.get(0);
        assertTrue(updateBuy.isComplete());
    }
}