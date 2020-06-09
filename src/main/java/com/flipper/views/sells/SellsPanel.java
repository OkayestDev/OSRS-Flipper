package com.flipper.views.sells;

import java.util.List;
import java.util.ListIterator;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;

import com.flipper.models.Transaction;

import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.ColorScheme;

public class SellsPanel extends JPanel {
    private static final long serialVersionUID = -5441336522969800315L;
    
	private ItemManager itemManager;

    public SellsPanel(ItemManager itemManager) {
        this.itemManager = itemManager;
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setBackground(ColorScheme.DARK_GRAY_COLOR);
    }

    /**
     * rebuilds the views based on passed sells list
     * 
     * @param sells
     */
    public void rebuildPanel(List<Transaction> sells) {
        SwingUtilities.invokeLater(() -> {
            this.removeAll();
            
            JPanel container = new JPanel();
            container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
            container.setBackground(ColorScheme.DARK_GRAY_COLOR);

            JScrollPane scrollPane = new JScrollPane(container);
            scrollPane.setBackground(ColorScheme.LIGHT_GRAY_COLOR);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

            // Reverse list to show newest first
            ListIterator<Transaction> sellsIterator = sells.listIterator(sells.size());
            while(sellsIterator.hasPrevious()) {
                Transaction sell = sellsIterator.previous();
                SellPanel sellTransactionPanel = new SellPanel(sell, itemManager);
                container.add(sellTransactionPanel);
            }

            this.add(container, BorderLayout.WEST);
        });
    }
}