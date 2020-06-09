package com.flipper.views;

import net.runelite.api.ItemComposition;
import net.runelite.client.game.ItemManager;

import static org.mockito.Mockito.*;

import javax.swing.JFrame;
import javax.swing.JPanel;

/** 
 * Creates a new window for quickly viewing individual view components
 * Also mocks item manager and item composition
 */
public class ViewTestSetup {
    public static int mockItemId = 400;
    public static String testItemName = "Saradomin Godsword";
    public static JFrame mainWindow;
    public static ItemManager itemManager;

    public static void setUp() {
        itemManager = mock(ItemManager.class);
        ItemComposition itemComposition = mock(ItemComposition.class);
        when(itemManager.getItemComposition(453)).thenReturn(itemComposition);
        when(itemComposition.getName()).thenReturn(testItemName);
        when(itemManager.getImage(453)).thenReturn(null);
    }

    public static void launch(JPanel panel) {
        mainWindow = new JFrame("Test Frame");
        mainWindow.getContentPane().add(panel);
        mainWindow.pack();
        mainWindow.setVisible(true);
    }
}