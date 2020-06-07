package com.flipper.helpers;

import com.flipper.mocks.MockGrandExchangeOffer;
import com.flipper.mocks.MockGrandExchangeOfferChanged;
import com.flipper.model.Transaction;

import net.runelite.api.GrandExchangeOfferState;
import net.runelite.api.ItemComposition;
import net.runelite.client.game.ItemManager;

import static org.mockito.Mockito.*;

import org.junit.*;
import static org.junit.Assert.*;

public class GrandExchangeTest {
    ItemManager itemManager;
    ItemComposition itemComposition;

    @Before
    public void setUp() {
        itemManager = mock(ItemManager.class);
        itemComposition = mock(ItemComposition.class);
        when(itemManager.getItemComposition(1)).thenReturn(itemComposition);
        when(itemComposition.getName()).thenReturn("Test Item Name");
    }

    @Test
    public void testHandleOnGrandExchangeOfferChangedReturnsNullIfNotComplete() {
        MockGrandExchangeOfferChanged mockGrandExchangeOffer = new MockGrandExchangeOfferChanged(
                1,
                new MockGrandExchangeOffer(1, 1, 1, 1, 1, GrandExchangeOfferState.SELLING)
        );
        Transaction result = GrandExchange.handleOnGrandExchangeOfferChanged(mockGrandExchangeOffer, itemManager);
        assertNull(null, result);
    }

    @Test
    public void testHandleOnGrandExchangeOfferChangedReturnsBuyTransaction() {
        MockGrandExchangeOfferChanged mockGrandExchangeOffer = new MockGrandExchangeOfferChanged(
                1,
                new MockGrandExchangeOffer(1, 1, 1, 1, 1, GrandExchangeOfferState.BOUGHT)
        );
        Transaction result = GrandExchange.handleOnGrandExchangeOfferChanged(mockGrandExchangeOffer, itemManager);
        assertNotNull(result);
        assertTrue(result.isBuy());
    }

    @Test
    public void testHandleOnGrandExchangeOfferChangedReturnsSellTransaction() {
        MockGrandExchangeOfferChanged mockGrandExchangeOffer = new MockGrandExchangeOfferChanged(
                1,
                new MockGrandExchangeOffer(1, 1, 1, 1, 1, GrandExchangeOfferState.SOLD)
        );
        Transaction result = GrandExchange.handleOnGrandExchangeOfferChanged(mockGrandExchangeOffer, itemManager);
        assertNotNull(result);
        assertFalse(result.isBuy());
    }
}
