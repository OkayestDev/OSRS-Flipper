package com.flipper.views.sells;

import javax.swing.JFrame;

import com.flipper.models.Transaction;
import com.flipper.views.ViewTestSetup;

import net.runelite.api.ItemComposition;
import net.runelite.client.game.ItemManager;

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
        ViewTestSetup.setUp();
        SellsPanel sellsPanel = new SellsPanel(ViewTestSetup.itemManager);
        sellsPanel.rebuildPanel(sells);
        ViewTestSetup.launch(sellsPanel);
    }
}