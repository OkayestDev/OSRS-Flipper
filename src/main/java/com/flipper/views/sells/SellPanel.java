package com.flipper.views.sells;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Color;

import com.flipper.helpers.UiUtilities;
import com.flipper.models.Transaction;
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

	private Transaction sell;
	private JPanel itemInfo = new JPanel(new BorderLayout());
	private JPanel leftInfoTextPanel = new JPanel(new GridLayout(7, 1));
    private JPanel rightValuesPanel = new JPanel(new GridLayout(7, 1));

    public SellPanel(Transaction sell, ItemManager itemManager) {
		this.sell = sell;
        setLayout(new BorderLayout());
		setBackground(ColorScheme.DARK_GRAY_COLOR);
		this.add(new ItemHeader(sell, itemManager, true), BorderLayout.NORTH);
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
		leftInfoTextPanel.add(new JLabel(" "));
	}

	/**
	 * Constructs left side labels
	 *  Quantity Bought:  @todo maybe put this in header to the right of item name?
	 * 	Price Per: 
	 *  Total Amount: 
	 */
    private void constructLeftLabels() {
		leftInfoTextPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);

		JLabel quantityBoughtText = newLeftLabel("Amount Sold: ");
		JLabel sellPriceText = newLeftLabel("Price Per: ");
		JLabel totalAmountText = newLeftLabel("Total Amount: ");

		addLeftLabel(quantityBoughtText);
		addLeftLabel(sellPriceText);
		addLeftLabel(totalAmountText);

		leftInfoTextPanel.setBorder(new EmptyBorder(2, 5, 2, 10));
	}
	
	private JLabel newRightLabel(String value, Color fontColor) {
		JLabel newRightLabel = new JLabel(value);
		newRightLabel.setHorizontalAlignment(JLabel.RIGHT);
		newRightLabel.setForeground(fontColor);
		return newRightLabel;
	}

	private void addRightLabel(JLabel newRightLabel) {
		rightValuesPanel.add(newRightLabel);
		rightValuesPanel.add(new JLabel(" "));
	}

	/**
	 * returns fraction x/x if not all are bought,
	 * otherwise return the whole number
	 */
	private String getBoughtOutOfQuantity() {
		if (sell.isComplete()) {
			return Integer.toString(sell.getQuantity());
		} else {
			return Integer.toString(sell.getQuantity()) + "/" + Integer.toString(sell.getTotalQuantity());
		}
	}

	/**
	 * Constructs right side labels (sell transaction information)
	 *  Quantity Bought:
	 *  Price Per:
	 *  Total Amount:
	 */
    private void constructRightLabels() {
		rightValuesPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);

		String boughtOutOfQuantity = getBoughtOutOfQuantity();
		Color quantityBoughtColor = sell.isComplete() ? ColorScheme.GRAND_EXCHANGE_ALCH : ColorScheme.PROGRESS_ERROR_COLOR;
		JLabel quantityBoughtValueLabel = newRightLabel(boughtOutOfQuantity, quantityBoughtColor);
		JLabel priceEachValueLabel = newRightLabel(Integer.toString(sell.getPricePer()), ColorScheme.GRAND_EXCHANGE_ALCH);
		JLabel totalPriceValueLabel = newRightLabel(Integer.toString(sell.getTotalPrice()), ColorScheme.GRAND_EXCHANGE_ALCH);
		
		addRightLabel(quantityBoughtValueLabel);
		addRightLabel(priceEachValueLabel);
		addRightLabel(totalPriceValueLabel);

		rightValuesPanel.setBorder(new EmptyBorder(2, 5, 2, 10));
	}
}