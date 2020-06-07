package com.flipper.view.buys;

import java.util.Iterator;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;

import java.awt.Dimension;

import com.flipper.model.Transaction;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.ColorScheme;

public class BuysPanel extends JPanel {
    ItemManager itemManager;
    JScrollPane buysScrollPane;

    public BuysPanel(ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    /**
     * rebuilds the view based on passed buys list
     * 
     * @param buys
     */
    public void rebuildPanel(List<Transaction> buys) {
        SwingUtilities.invokeLater(() -> {
            this.removeAll();

            // buysScrollPane = new JScrollPane();
            // buysScrollPane.setBackground(ColorScheme.DARK_GRAY_COLOR);
            // buysScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(5, 0));
            // buysScrollPane.getVerticalScrollBar().setBorder(new EmptyBorder(0, 0, 0, 0));

            Iterator<Transaction> buysIterator = buys.iterator();
            while (buysIterator.hasNext()) {
                Transaction buy = buysIterator.next();
                BuyPanel buyTransactionPanel = new BuyPanel(buy, itemManager);
                this.add(buyTransactionPanel);
            }

            // this.add(buysScrollPane);
        });
    }
}