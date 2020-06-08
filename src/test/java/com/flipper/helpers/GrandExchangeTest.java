package com.flipper.helpers;

import com.flipper.mocks.MockGrandExchangeOffer;
import com.flipper.models.Transaction;

import net.runelite.api.GrandExchangeOfferState;
import net.runelite.api.ItemComposition;
import net.runelite.client.game.ItemManager;

import static org.mockito.Mockito.*;

import org.junit.*;
import static org.junit.Assert.*;

public class GrandExchangeTest {
    ItemManager itemManager;
    ItemComposition itemComposition;
    String testItemName = "Test Item Name";

    @Before
    public void setUp() {
        itemManager = mock(ItemManager.class);
        itemComposition = mock(ItemComposition.class);
        when(itemManager.getItemComposition(1)).thenReturn(itemComposition);
        when(itemComposition.getName()).thenReturn(testItemName);
    }

    @Test
    public void testCreateTransactionFromOfferReturnsBuyTransaction() {
        MockGrandExchangeOffer mockGrandExchangeOffer = new MockGrandExchangeOffer(1, 1, 1, 1, 1, GrandExchangeOfferState.BOUGHT);
        Transaction result = GrandExchange.createTransactionFromOffer(mockGrandExchangeOffer, itemManager);
        assertNotNull(result);
        assertTrue(result.isBuy());
        assertEquals(result.getItemName(), testItemName);
    }

    @Test
    public void testCreateTransactionFromOfferReturnsSellTransaction() {
        MockGrandExchangeOffer mockGrandExchangeOffer = new MockGrandExchangeOffer(1, 1, 1, 1, 1, GrandExchangeOfferState.SOLD);
        Transaction result = GrandExchange.createTransactionFromOffer(mockGrandExchangeOffer, itemManager);
        assertNotNull(result);
        assertFalse(result.isBuy());
        assertEquals(result.getItemName(), testItemName);
    }

    @Test
    public void testCheckIsOfferPartOfTransactionReturnsTrue() {
        Transaction buy = new Transaction(
            1,
            1,
            453,
            1,
            testItemName,
            true,
            false
        );
        MockGrandExchangeOffer offer = new MockGrandExchangeOffer(1, 453, 1, 1, 1, GrandExchangeOfferState.BOUGHT);
        boolean result = GrandExchange.checkIsOfferPartOfTransaction(buy, offer);
        assertTrue(result);
    }
}
