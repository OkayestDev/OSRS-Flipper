package com.flipper.controllers;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.flipper.TestUtilities;
import com.flipper.helpers.Persistor;
import com.flipper.models.Flip;
import com.flipper.models.Transaction;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import net.runelite.api.ItemComposition;
import net.runelite.client.game.ItemManager;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class MarginsControllerTest {
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
        Persistor.setUp(testFilePath);
        when(itemManager.getItemComposition(mockItemId)).thenReturn(itemComposition);
        when(itemComposition.getName()).thenReturn("Test Name");
    }
  
    @After
    public void tearDown() {
        TestUtilities.cleanTestResultFiles();
    }

    @Test
    public void testConstructingMarginControllerLoadsMargins() throws IOException {
        MarginsController marginsController = new MarginsController(itemManager);
        List<Flip> margins = marginsController.getMargins();
        assertEquals(0, margins.size());
    }

    @Test
    public void testAddingMarginAndSaving() throws IOException {
        MarginsController marginsController = new MarginsController(itemManager);
        Transaction buy = new Transaction(1, 1, 1, 1, "test Name", true, true);
        Transaction sell = new Transaction(1, 1, 1, 1, "test Name", true, true);
        Flip margin = new Flip(buy, sell);
        marginsController.addMargin(margin);
        List<Flip> margins = marginsController.getMargins();
        assertEquals(1, margins.size());
        marginsController.saveMargins();
        List<Flip> loadedMargins = Persistor.loadMargins();
        assertEquals(1, loadedMargins.size());
    }

}