package com.flipper.controller;

import com.flipper.mocks.MockGrandExchangeOffer;
import com.flipper.mocks.MockGrandExchangeOfferChanged;
import com.flipper.model.Transaction;
import net.runelite.api.GrandExchangeOfferState;
import org.junit.*;
import static org.junit.Assert.*;

public class GrandExchangeControllerTest {
    @Test
    public void testHandleOnGrandExchangeOfferChanged() {
//        MockGrandExchangeOfferChanged mockGrandExchangeOffer = new MockGrandExchangeOfferChanged(
//            1,
//            new MockGrandExchangeOffer(1, 1, 1, 1, 1, GrandExchangeOfferState.BOUGHT)
//        );
//        boolean result = GrandExchangeController.handleOnGrandExchangeOfferChanged(mockGrandExchangeOffer);
//        assertTrue(result);
    }

    @Test
    public void testHandleOnGrandExchangeOfferChangedReturnsNullIfNotComplete() {
        MockGrandExchangeOfferChanged mockGrandExchangeOffer = new MockGrandExchangeOfferChanged(
                1,
                new MockGrandExchangeOffer(1, 1, 1, 1, 1, GrandExchangeOfferState.SELLING)
        );
        Transaction result = GrandExchangeController.handleOnGrandExchangeOfferChanged(mockGrandExchangeOffer);
        assertNull(null, result);
    }

    @Test
    public void testHandleOnGrandExchangeOfferChangedReturnsBuyTransaction() {
        MockGrandExchangeOfferChanged mockGrandExchangeOffer = new MockGrandExchangeOfferChanged(
                1,
                new MockGrandExchangeOffer(1, 1, 1, 1, 1, GrandExchangeOfferState.BOUGHT)
        );
        Transaction result = GrandExchangeController.handleOnGrandExchangeOfferChanged(mockGrandExchangeOffer);
        assertNotNull(result);
        assertTrue(result.isBuy());
    }

    @Test
    public void testHandleOnGrandExchangeOfferChangedReturnsSellTransaction() {
        MockGrandExchangeOfferChanged mockGrandExchangeOffer = new MockGrandExchangeOfferChanged(
                1,
                new MockGrandExchangeOffer(1, 1, 1, 1, 1, GrandExchangeOfferState.SOLD)
        );
        Transaction result = GrandExchangeController.handleOnGrandExchangeOfferChanged(mockGrandExchangeOffer);
        assertNotNull(result);
        assertFalse(result.isBuy());
    }
}
