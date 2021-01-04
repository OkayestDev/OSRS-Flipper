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
import com.flipper.helpers.Persistor;
import com.flipper.mocks.MockGrandExchangeOffer;
import com.flipper.models.Transaction;

public class SellsControllerTest {
    SellsController sellsController;
    ItemManager itemManager;
    ItemComposition itemComposition;
    MockGrandExchangeOffer offerToCreateNewTransaction;
    MockGrandExchangeOffer offerToUpdateTransaction;
    Transaction transactionToUpdate;
    Persistor tradePersister;
    int mockItemId = 400;

    @Before
    public void setUp() throws IOException {
        offerToCreateNewTransaction = new MockGrandExchangeOffer(100, mockItemId, 100, 10, 1000, GrandExchangeOfferState.BOUGHT);
        offerToUpdateTransaction = new MockGrandExchangeOffer(10, mockItemId, 10, 100, 1000, GrandExchangeOfferState.BOUGHT);
        transactionToUpdate = new Transaction(
            5,
            10,
            mockItemId,
            100,
            "Test Item",
            true,
            false
        );
        itemManager = mock(ItemManager.class);
        itemComposition = mock(ItemComposition.class);
        Path currentRelativePath = Paths.get("");
        String testFilePath = currentRelativePath.toAbsolutePath().toString()
                + "\\src\\test\\java\\com\\flipper\\test-result-files";
        Persistor.setUp(testFilePath);
        when(itemManager.getItemComposition(mockItemId)).thenReturn(itemComposition);
        when(itemComposition.getName()).thenReturn("Test Name");
        sellsController = new SellsController(itemManager);
    }

    @After
    public void tearDown() {
        TestUtilities.cleanTestResultFiles();
    }

    @Test
    public void testCreateSellCreatesANewSell() throws IOException {
        SellsController sellsController = new SellsController(itemManager);
        sellsController.setSells(new ArrayList<Transaction>());
        sellsController.createSell(offerToCreateNewTransaction);
        List<Transaction> sells = sellsController.getSells();
        assertEquals(1, sells.size());
        Transaction createdSell = sells.get(0);
        assertEquals(mockItemId, createdSell.getItemId());
    }

    @Test
    public void testCreateSellUpdatesAnExistingSell() throws IOException {
        SellsController sellsController = new SellsController(itemManager);
        // Add partially complete sell to see if we're updating transactions correctly
        List<Transaction> sells = new ArrayList<Transaction>();
        sells.add(transactionToUpdate);
        sellsController.setSells(sells);

        sellsController.createSell(offerToUpdateTransaction);
        // Ensure sells list is still only one (only updated transaction);
        List<Transaction> updatedSells = sellsController.getSells();
        assertEquals(1, updatedSells.size());

        Transaction updateSell = updatedSells.get(0);
        assertTrue(updateSell.isComplete());
    }
}