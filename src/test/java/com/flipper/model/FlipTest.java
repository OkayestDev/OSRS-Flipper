package com.flipper.model;

import org.junit.Test;
import java.time.Instant;

import static org.junit.Assert.*;

public class FlipTest {
    @Test
    public void testGetTotalProfitOnlyTakesAmountSoldIntoAccount() {
        Transaction buy = new Transaction(5, 1, 20, 100, "Test Item", Instant.now(), true);
        Transaction sell = new Transaction(4, 1, 25, 100, "Test Item", Instant.now(), false);
        Flip flip = new Flip(buy, sell);

        // 4 * 20 = 80 (total buy price as we ignore the 1 not sold)
        // 4 * 25 = 100 (total sell price)
        // profit should be 20
        int totalProfit = flip.getTotalProfit();
        assertEquals(20, totalProfit);
        assertFalse(flip.isMarginCheck());
    }

    @Test
    public void testIsMarginCheckIsSetCorrectly() {
        Transaction buy = new Transaction(1, 1, 100, 100, "Test Item",Instant.now(), true);
        Transaction sell = new Transaction(1, 1, 90, 100, "Test Item", Instant.now(), false);
        Flip flip = new Flip(buy, sell);
        assertTrue(flip.isMarginCheck());
    }
}
