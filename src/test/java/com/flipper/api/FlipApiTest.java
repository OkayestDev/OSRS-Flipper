package com.flipper.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.flipper.models.Flip;
import com.flipper.responses.FlipResponse;

import org.junit.Before;
import org.junit.Test;

public class FlipApiTest {
    @Before
    public void setup() {
        // Sets jwt for Api.java
        UserApi.login("test@gmail.com", "test");
    }

    @Test
    public void testGetFlips() {
        FlipResponse flipResponse = FlipApi.getFlips();
        assertNotNull(flipResponse);
        assertNotNull(flipResponse.flips);
        assertNotNull(flipResponse.averageProfit);
        assertNotNull(flipResponse.maxProfit);
        assertNotNull(flipResponse.totalProfit);
    }

    @Test
    public void testCreateFlip() {
        Flip flip = new Flip();
        flip.itemId = 1;
        flip.quantity = 20;
        flip.buyPrice = 2;
        flip.sellPrice = 4;

        FlipResponse flipResponse = FlipApi.createFlip(flip);

        assertNotNull(flipResponse.flip.id);
    }

    @Test
    public void updateFlip() {
        // Create flip and then update it

        Flip flip = new Flip();
        flip.itemId = 1;
        flip.quantity = 20;
        flip.buyPrice = 2;
        flip.sellPrice = 0;

        FlipResponse flipResponse = FlipApi.createFlip(flip);

        Flip createdFlip = flipResponse.flip;

        createdFlip.sellPrice = 200;

        FlipResponse updateFlipResponse = FlipApi.updateFlip(createdFlip);

        assertEquals(200, updateFlipResponse.flip.sellPrice);
    }

    @Test
    public void deleteFlip() {
        Flip flip = new Flip();
        flip.itemId = 1;
        flip.quantity = 20;
        flip.buyPrice = 2;
        flip.sellPrice = 0;

        FlipResponse flipResponse = FlipApi.createFlip(flip);

        Flip createdFlip = flipResponse.flip;

        FlipResponse deletedFlipResponse = FlipApi.deleteFlip(createdFlip.id);

        assertEquals("Successfully deleted flip.", deletedFlipResponse.message);
    }
}
