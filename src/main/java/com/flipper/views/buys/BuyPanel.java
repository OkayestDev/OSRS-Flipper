package com.flipper.views.buys;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Color;

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
		this.add(new ItemHeader(buy, itemManager, true), BorderLayout.NORTH);
		this.add(new AmountProgressBar(buy), BorderLayout.SOUTH);
		this.setBorder(new EmptyBorder(0, 0, 5, 0));
    }
}