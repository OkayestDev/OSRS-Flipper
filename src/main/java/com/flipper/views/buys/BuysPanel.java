package com.flipper.views.buys;

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

public class BuysPanel extends JPanel {
    private static final long serialVersionUID = 4764109045121749116L;
    
    private ItemManager itemManager;

    public BuysPanel(ItemManager itemManager) {
        this.itemManager = itemManager;
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setBackground(ColorScheme.DARK_GRAY_COLOR);
    }

    public void rebuildPanel(List<Transaction> buys) {
        SwingUtilities.invokeLater(() -> {
            this.removeAll();
            
            JPanel container = new JPanel();
            container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
            container.setBackground(ColorScheme.DARK_GRAY_COLOR);

            JScrollPane scrollPane = new JScrollPane(container);
            scrollPane.setBackground(ColorScheme.LIGHT_GRAY_COLOR);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

            // Reverse list to show newest first
            ListIterator<Transaction> buysIterator = buys.listIterator(buys.size());
            while(buysIterator.hasPrevious()) {
                Transaction buy = buysIterator.previous();
                BuyPanel buyTransactionPanel = new BuyPanel(buy, itemManager);
                container.add(buyTransactionPanel);
            }

            this.add(container, BorderLayout.WEST);
        });
    }
}