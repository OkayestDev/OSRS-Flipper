package com.flipper.view.buys;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.BorderLayout;

import com.flipper.model.Transaction;

import org.junit.Before;
import org.junit.Test;

import net.runelite.api.ItemComposition;
import net.runelite.client.game.ItemManager;

import static org.mockito.Mockito.*;

import java.time.Instant;

public class BuyPanelTest {
    static JFrame mainWindow;
    static ItemManager itemManager;
    static ItemComposition itemComposition;
    static String testItemName = "Coal";
    static Transaction buy;

    public static void setUp() {
        itemManager = mock(ItemManager.class);
        itemComposition = mock(ItemComposition.class);
        when(itemManager.getItemComposition(453)).thenReturn(itemComposition);
        when(itemComposition.getName()).thenReturn(testItemName);
        when(itemManager.getImage(453)).thenReturn(null);
        mainWindow = new JFrame("Test Frame");
        buy = new Transaction(
            1,
            453,
            100,
            1000,
            testItemName,
            Instant.now(),
            true
        );
    }

    public static void main(String[] args) throws Exception {
        setUp();
        BuyPanel buyPanel = new BuyPanel(buy, itemManager);
        mainWindow.getContentPane().add(buyPanel);
        mainWindow.pack();
        mainWindow.setVisible(true);
    }
}