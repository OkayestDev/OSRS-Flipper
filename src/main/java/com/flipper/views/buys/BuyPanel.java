package com.flipper.views.buys;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;

import com.flipper.helpers.UiUtilities;
import com.flipper.models.Transaction;
import com.flipper.views.components.AmountProgressBar;
import com.flipper.views.components.ItemHeader;

import net.runelite.client.ui.ColorScheme;
import net.runelite.client.game.ItemManager;

/**
 * Construct of two main components
 * 	Item Header (item image and name)
 *  Item Information (buy info)
 */
public class BuyPanel extends JPanel {
    private static final long serialVersionUID = -6576417948629423220L;
    
    public BuyPanel(Transaction buy, ItemManager itemManager) {
        setLayout(new BorderLayout());
        setBackground(ColorScheme.DARK_GRAY_COLOR);

        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        container.setBackground(ColorScheme.DARK_GRAY_COLOR);
        container.add(new ItemHeader(buy, itemManager, true), BorderLayout.NORTH);
        container.add(new AmountProgressBar(buy), BorderLayout.SOUTH);
        container.setBorder(UiUtilities.ITEM_INFO_BORDER);
        
        this.add(container);
        this.setBorder(new EmptyBorder(0, 0, 3, 0));
    }
}