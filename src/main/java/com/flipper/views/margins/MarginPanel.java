package com.flipper.views.margins;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import com.flipper.helpers.UiUtilities;
import com.flipper.models.Flip;
import com.flipper.models.Transaction;
import com.flipper.views.components.ItemHeader;

import net.runelite.client.ui.ColorScheme;
import net.runelite.client.game.ItemManager;

public class MarginPanel extends JPanel {
    private static final long serialVersionUID = 3879605322279273963L;

    private Flip margin;
    private Transaction buy;
    private Transaction sell;
    
    private JPanel itemInfo = new JPanel(new BorderLayout());
    private JPanel leftInfoTextPanel = new JPanel(new GridLayout(3, 1));
    private JPanel rightValuesPanel = new JPanel(new GridLayout(3, 1));

    public MarginPanel(Flip flip, ItemManager itemManager) {
        this.margin = flip;
        this.sell = margin.getSell();
        this.buy = margin.getBuy();
        setLayout(new BorderLayout());
        setBackground(ColorScheme.DARK_GRAY_COLOR);
        this.add(new ItemHeader(this.buy, itemManager, false), BorderLayout.NORTH);
        constructItemInfo();
        this.setBorder(new EmptyBorder(0, 0, 5, 0));
    }
    
    private void constructItemInfo() {
        constructLeftLabels();
        constructRightLabels();
        itemInfo.setBackground(ColorScheme.DARK_GRAY_COLOR);
        itemInfo.add(leftInfoTextPanel, BorderLayout.WEST);
        itemInfo.add(rightValuesPanel, BorderLayout.EAST);
        itemInfo.setBorder(UiUtilities.ITEM_INFO_BORDER);
        add(itemInfo, BorderLayout.CENTER);
    }

    private JLabel newLeftLabel(String text) {
        JLabel newJLabel = new JLabel(text);
        newJLabel.setForeground(ColorScheme.GRAND_EXCHANGE_PRICE);
        return newJLabel;
    }

    private void addLeftLabel(JLabel newLeftLabel) {
        leftInfoTextPanel.add(newLeftLabel);
    }

    private void constructLeftLabels() {
        leftInfoTextPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);

        JLabel buyAtLabel = newLeftLabel("Buy At:");
        JLabel sellAtLabel = newLeftLabel("Sell At:");
        JLabel potentialProfitLabel = newLeftLabel("Potential Profit Per:");

        addLeftLabel(buyAtLabel);
        addLeftLabel(sellAtLabel);
        addLeftLabel(potentialProfitLabel);

        leftInfoTextPanel.setBorder(new EmptyBorder(2, 5, 2, 10));
    }
    
    private JLabel newRightLabel(String value) {
        JLabel newRightLabel = new JLabel(value);
        newRightLabel.setHorizontalAlignment(JLabel.RIGHT);
        newRightLabel.setForeground(ColorScheme.GRAND_EXCHANGE_ALCH);
        return newRightLabel;
    }

    private void addRightLabel(JLabel newRightLabel) {
        rightValuesPanel.add(newRightLabel);
    }

    private void constructRightLabels() {
        rightValuesPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);

        int potentialProfitPer = buy.getPricePer() - sell.getPricePer();

        String buyAtText = Integer.toString(sell.getPricePer());
        String sellAtText = Integer.toString(buy.getPricePer());
        String potentialProfitEachText = Integer.toString(potentialProfitPer);

        JLabel buyAtLabel = newRightLabel(buyAtText);
        JLabel sellAtLabel = newRightLabel(sellAtText);
        JLabel potentialProfitEachLabel = newRightLabel(potentialProfitEachText);
        
        addRightLabel(buyAtLabel);
        addRightLabel(sellAtLabel);
        addRightLabel(potentialProfitEachLabel);

        rightValuesPanel.setBorder(new EmptyBorder(2, 5, 2, 10));
    }
}