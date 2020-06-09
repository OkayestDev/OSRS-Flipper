package com.flipper.views.sells;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;

import com.flipper.models.Transaction;
import com.flipper.views.components.AmountProgressBar;
import com.flipper.views.components.ItemHeader;

import net.runelite.client.ui.ColorScheme;
import net.runelite.client.game.ItemManager;

/**
 * Construct of two main components
 * 	Item Header (item image and name)
 *  Item Information (sell info)
 */
public class SellPanel extends JPanel {
	private static final long serialVersionUID = -3722286075820009130L;

    public SellPanel(Transaction sell, ItemManager itemManager) {
        setLayout(new BorderLayout());
		setBackground(ColorScheme.DARK_GRAY_COLOR);
		this.add(new ItemHeader(sell, itemManager, true), BorderLayout.NORTH);
		this.add(new AmountProgressBar(sell), BorderLayout.SOUTH);
		this.setBorder(new EmptyBorder(0, 0, 5, 0));
    }
}