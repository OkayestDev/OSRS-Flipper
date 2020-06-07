package com.flipper.view.buys;

import java.util.Iterator;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.flipper.FlipperPlugin;
import com.flipper.helpers.Log;
import com.flipper.model.Transaction;
import com.flipper.view.components.TransactionPanel;

import net.runelite.client.game.ItemManager;

public class BuysPanel extends JPanel {
    ItemManager itemManager;
    JLabel buyPriceVal = new JLabel();
    JLabel profitEachVal = new JLabel();
    JLabel profitTotalVal = new JLabel();
    JLabel limitLabel = new JLabel();
    JLabel roiLabel = new JLabel();

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
            Iterator<Transaction> buysIterator = buys.iterator();
            while (buysIterator.hasNext()) {
                Transaction buy = buysIterator.next();
                TransactionPanel buyTransactionPanel = new TransactionPanel(buy, itemManager);
                this.add(buyTransactionPanel);
            }
        });
    }
}