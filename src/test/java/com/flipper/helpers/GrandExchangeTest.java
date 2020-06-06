package com.flipper.helpers;

import com.flipper.mocks.MockGrandExchangeOffer;
import com.flipper.mocks.MockGrandExchangeOfferChanged;
import com.flipper.model.Transaction;
import net.runelite.api.GrandExchangeOfferState;
import com.flipper.helpers.GrandExchange;
import org.junit.*;
import static org.junit.Assert.*;

public class GrandExchangeTest {
    @Test
    public void testHandleOnGrandExchangeOfferChangedReturnsNullIfNotComplete() {
        MockGrandExchangeOfferChanged mockGrandExchangeOffer = new MockGrandExchangeOfferChanged(
                1,
                new MockGrandExchangeOffer(1, 1, 1, 1, 1, GrandExchangeOfferState.SELLING)
        );
        Transaction result = GrandExchange.handleOnGrandExchangeOfferChanged(mockGrandExchangeOffer);
        assertNull(null, result);
    }

    @Test
    public void testHandleOnGrandExchangeOfferChangedReturnsBuyTransaction() {
        MockGrandExchangeOfferChanged mockGrandExchangeOffer = new MockGrandExchangeOfferChanged(
                1,
                new MockGrandExchangeOffer(1, 1, 1, 1, 1, GrandExchangeOfferState.BOUGHT)
        );
        Transaction result = GrandExchange.handleOnGrandExchangeOfferChanged(mockGrandExchangeOffer);
        assertNotNull(result);
        assertTrue(result.isBuy());
    }

    @Test
    public void testHandleOnGrandExchangeOfferChangedReturnsSellTransaction() {
        MockGrandExchangeOfferChanged mockGrandExchangeOffer = new MockGrandExchangeOfferChanged(
                1,
                new MockGrandExchangeOffer(1, 1, 1, 1, 1, GrandExchangeOfferState.SOLD)
        );
        Transaction result = GrandExchange.handleOnGrandExchangeOfferChanged(mockGrandExchangeOffer);
        assertNotNull(result);
        assertFalse(result.isBuy());
    }
}
