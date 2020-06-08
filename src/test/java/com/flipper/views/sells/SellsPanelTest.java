package com.flipper.views.sells;

import javax.swing.JFrame;

import com.flipper.models.Transaction;
import com.flipper.views.sells.SellsPanel;

import net.runelite.api.ItemComposition;
import net.runelite.client.game.ItemManager;

import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class SellsPanelTest {
    static JFrame mainWindow;
    static ItemManager itemManager;
    static ItemComposition itemComposition;
    static String testItemName = "Coal";
    static Transaction sell;
    static List<Transaction> sells;

    public static void setUp() {
        itemManager = mock(ItemManager.class);
        itemComposition = mock(ItemComposition.class);
        when(itemManager.getItemComposition(453)).thenReturn(itemComposition);
        when(itemComposition.getName()).thenReturn(testItemName);
        when(itemManager.getImage(453)).thenReturn(null);
        mainWindow = new JFrame("Test Frame");
        sells = new ArrayList<Transaction>();
        sells.add(new Transaction(
            1,
            453,
            100,
            1000,
            testItemName,
            true,
            true
        ));
        sells.add(new Transaction(
            1,
            453,
            101,
            1001,
            testItemName,
            true,
            false
        ));
    }

    public static void main(String[] args) throws Exception {
        setUp();
        SellsPanel sellsPanel = new SellsPanel(itemManager);
        sellsPanel.rebuildPanel(sells);
        mainWindow.getContentPane().add(sellsPanel);
        mainWindow.pack();
        mainWindow.setVisible(true);
    }
}