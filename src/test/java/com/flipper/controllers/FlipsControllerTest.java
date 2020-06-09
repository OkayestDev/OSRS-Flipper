package com.flipper.controllers;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.flipper.helpers.TradePersister;
import com.flipper.models.Flip;
import com.flipper.models.Transaction;

import org.junit.Before;
import org.junit.Test;

import net.runelite.api.ItemComposition;
import net.runelite.client.game.ItemManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class FlipsControllerTest {
    ItemManager itemManager;
    ItemComposition itemComposition;
    int mockItemId = 400;

    @Before
    public void setUp() throws IOException {
        itemManager = mock(ItemManager.class);
        itemComposition = mock(ItemComposition.class);
        Path currentRelativePath = Paths.get("");
        String testFilePath = currentRelativePath.toAbsolutePath().toString()
                + "\\src\\test\\java\\com\\flipper\\test-result-files";
        TradePersister.setUp(testFilePath);
        when(itemManager.getItemComposition(mockItemId)).thenReturn(itemComposition);
        when(itemComposition.getName()).thenReturn("Test Name");
    }

    @Test
    public void testCreateFlipCreatesANewFlip() throws IOException {
        FlipsController flipsController = new FlipsController(itemManager);
        flipsController.setFlips(new ArrayList<Flip>());
        Transaction sell = new Transaction(10, 10, mockItemId, 100, "Test Item", false, true);
        List<Transaction> buys = new ArrayList<Transaction>();
        Transaction buy = new Transaction(10, 10, mockItemId, 100, "Test Item", true, true);
        buys.add(buy);
        // Expect a flip to be generated
        flipsController.createFlip(sell, buys);
        assertTrue(buy.isFlipped());
        assertTrue(sell.isFlipped());
        Flip flips = flipsController.getFlips().get(0);
        assertEquals(sell.id, flips.getSell().id);
        assertEquals(buy.id, flips.getBuy().id);
    }

    @Test
    public void testCreateFlipUpdatesACurrentFlipIfPresent() throws IOException {
        FlipsController flipsController = new FlipsController(itemManager);
        flipsController.setFlips(new ArrayList<Flip>());
        Transaction buy = new Transaction(10, 10, mockItemId, 100, "Test Item", true, true);
        List<Transaction> buys = new ArrayList<Transaction>();
        buys.add(buy);
        Transaction sell = new Transaction(10, 10, mockItemId, 100, "Test Item", true, true);
        sell.setIsFlipped(true);
        Flip flip = new Flip(buy, sell);
        flipsController.addFlip(flip);
        // Expect the flip to be updated
        flipsController.createFlip(sell, buys);
        List<Flip> flips = flipsController.getFlips();
        assertEquals(1, flips.size());
    }
}