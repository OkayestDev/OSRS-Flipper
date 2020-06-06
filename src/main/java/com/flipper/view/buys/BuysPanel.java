package com.flipper.view.buys;

import java.util.Iterator;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.flipper.FlipperPlugin;
import com.flipper.helpers.Log;
import com.flipper.model.Transaction;

public class BuysPanel extends JPanel {
    FlipperPlugin plugin;
    JLabel buyPriceVal = new JLabel();
    JLabel profitEachVal = new JLabel();
    JLabel profitTotalVal = new JLabel();
    JLabel limitLabel = new JLabel();
    JLabel roiLabel = new JLabel();

    public BuysPanel(final FlipperPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * rebuilds the view based on passed buys list
     * 
     * @param buys
     */
    public void rebuildPanel(List<Transaction> buys) {
        Log.info("Rebuilding Buys Panel with: " + buys.toString());
        this.removeAll();
        Iterator<Transaction> buysIterator = buys.iterator();
        while (buysIterator.hasNext()) {
            Transaction buy = buysIterator.next();
            JLabel itemIdLabel = new JLabel();
            itemIdLabel.setText(Integer.toString(buy.getItemId()));
            this.add(itemIdLabel);
        }
    }
}