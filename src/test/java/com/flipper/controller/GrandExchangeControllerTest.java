package com.flipper.controller;

import com.flipper.mocks.MockGrandExchangeOffer;
import com.flipper.mocks.MockGrandExchangeOfferChanged;
import net.runelite.api.GrandExchangeOfferState;
import org.junit.*;
import static org.junit.Assert.*;

public class GrandExchangeControllerTest {
    @Test
    public void testHandleOnGrandExchangeOfferChanged() {
        MockGrandExchangeOfferChanged mockGrandExchangeOffer = new MockGrandExchangeOfferChanged(
            1,
            new MockGrandExchangeOffer(1, 1, 1, 1, 1, GrandExchangeOfferState.BOUGHT)
        );
        boolean result = GrandExchangeController.handleOnGrandExchangeOfferChanged(mockGrandExchangeOffer);
        assertTrue(result);
    }
}
