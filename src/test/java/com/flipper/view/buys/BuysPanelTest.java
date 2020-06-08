package com.flipper.view.buys;

import javax.swing.JFrame;

import com.flipper.model.Transaction;

import net.runelite.api.ItemComposition;
import net.runelite.client.game.ItemManager;

import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class BuysPanelTest {
    static JFrame mainWindow;
    static ItemManager itemManager;
    static ItemComposition itemComposition;
    static String testItemName = "Coal";
    static Transaction buy;
    static List<Transaction> buys;

    public static void setUp() {
        itemManager = mock(ItemManager.class);
        itemComposition = mock(ItemComposition.class);
        when(itemManager.getItemComposition(453)).thenReturn(itemComposition);
        when(itemComposition.getName()).thenReturn(testItemName);
        when(itemManager.getImage(453)).thenReturn(null);
        mainWindow = new JFrame("Test Frame");
        buys = new ArrayList<Transaction>();
        buys.add(new Transaction(
            1,
            453,
            100,
            1000,
            testItemName,
            true,
            true
        ));
        buys.add(new Transaction(
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
        BuysPanel buysPanel = new BuysPanel(itemManager);
        buysPanel.rebuildPanel(buys);
        mainWindow.getContentPane().add(buysPanel);
        mainWindow.pack();
        mainWindow.setVisible(true);
    }
}