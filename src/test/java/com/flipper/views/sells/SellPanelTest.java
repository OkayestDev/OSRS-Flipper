package com.flipper.views.sells;

import javax.swing.JFrame;

import com.flipper.models.Transaction;
import com.flipper.views.sells.SellPanel;

import net.runelite.api.ItemComposition;
import net.runelite.client.game.ItemManager;

import static org.mockito.Mockito.*;

import java.time.Instant;

public class SellPanelTest {
    static JFrame mainWindow;
    static ItemManager itemManager;
    static ItemComposition itemComposition;
    static String testItemName = "Coal";
    static Transaction sell;

    public static void setUp() {
        itemManager = mock(ItemManager.class);
        itemComposition = mock(ItemComposition.class);
        when(itemManager.getItemComposition(453)).thenReturn(itemComposition);
        when(itemComposition.getName()).thenReturn(testItemName);
        when(itemManager.getImage(453)).thenReturn(null);
        mainWindow = new JFrame("Test Frame");
        sell = new Transaction(
            1,
            1000,
            453,
            100,
            testItemName,
            true,
            true
        );
    }

    public static void main(String[] args) throws Exception {
        setUp();
        SellPanel sellPanel = new SellPanel(sell, itemManager);
        mainWindow.getContentPane().add(sellPanel);
        mainWindow.pack();
        mainWindow.setVisible(true);
    }
}