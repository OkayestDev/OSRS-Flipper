package com.flipper.views.buys;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Dimension;

import com.flipper.helpers.UiUtilities;
import com.flipper.models.Transaction;

import net.runelite.client.ui.ColorScheme;
import net.runelite.client.game.ItemManager;
import net.runelite.client.util.AsyncBufferedImage;
import net.runelite.client.ui.FontManager;

/**
 * Construct of two main components
 * 	Item Header (item image and name)
 *  Item Information (buy info)
 */
public class BuyPanel extends JPanel {
	private static final Border ITEM_INFO_BORDER = new CompoundBorder(
		BorderFactory.createMatteBorder(0, 0, 0, 0, ColorScheme.DARK_GRAY_COLOR),
		BorderFactory.createLineBorder(ColorScheme.DARKER_GRAY_COLOR.darker(), 3));

    private Transaction buy;
    private ItemManager itemManager;
    private JLabel itemName;
	private JPanel titlePanel = new JPanel(new BorderLayout());
	private JPanel itemInfo = new JPanel(new BorderLayout());
	private JPanel leftInfoTextPanel = new JPanel(new GridLayout(7, 1));
    private JPanel rightValuesPanel = new JPanel(new GridLayout(7, 1));

    public BuyPanel(Transaction buy, ItemManager itemManager) {
		this.itemManager = itemManager;
		this.buy = buy;
		this.setSize(400, 400);
        setLayout(new BorderLayout());
		setBackground(ColorScheme.DARK_GRAY_COLOR);
		constructHeader();
		constructItemInfo();
		this.setBorder(new EmptyBorder(0, 0, 5, 0));
    }

	/**
	 * Constructs header
	 *  ItemImage / ItemName
	 */
    private void constructHeader() {
		int itemId = buy.getItemId();
		AsyncBufferedImage itemImage = itemManager.getImage(itemId);
		JLabel itemIcon = new JLabel();
		itemIcon.setAlignmentX(Component.LEFT_ALIGNMENT);
		itemIcon.setPreferredSize(UiUtilities.ICON_SIZE);
		if (itemImage != null) {
			itemImage.addTo(itemIcon);
		}
		JPanel itemClearPanel = new JPanel(new BorderLayout());
		itemClearPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR.darker());
		itemClearPanel.add(itemIcon, BorderLayout.WEST);
		itemName = new JLabel(buy.getItemName(), SwingConstants.CENTER);
		itemName.setForeground(Color.WHITE);
		itemName.setFont(FontManager.getRunescapeBoldFont());
		itemName.setPreferredSize(new Dimension(0, 0));
		titlePanel.setBackground(ColorScheme.DARKER_GRAY_COLOR.darker());
		titlePanel.add(itemClearPanel, BorderLayout.WEST);
		titlePanel.add(itemName, BorderLayout.CENTER);
		titlePanel.setBorder(new EmptyBorder(2, 1, 2, 1));
		add(titlePanel, BorderLayout.NORTH);
	}
	
	private void constructItemInfo() {
		constructLeftLabels();
		constructRightLabels();
		itemInfo.setBackground(ColorScheme.DARK_GRAY_COLOR);
		itemInfo.add(leftInfoTextPanel, BorderLayout.WEST);
		itemInfo.add(rightValuesPanel, BorderLayout.EAST);
		itemInfo.setBorder(ITEM_INFO_BORDER);
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

		JLabel quantityBoughtText = newLeftLabel("Amount Bought: ");
		JLabel buyPriceText = newLeftLabel("Price Per: ");
		JLabel totalAmountText = newLeftLabel("Total Cost: ");

		addLeftLabel(quantityBoughtText);
		addLeftLabel(buyPriceText);
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
		if (buy.isComplete()) {
			return Integer.toString(buy.getQuantity());
		} else {
			return Integer.toString(buy.getQuantity()) + "/" + Integer.toString(buy.getTotalQuantity());
		}
	}

	/**
	 * Constructs right side labels (buy transaction information)
	 *  Quantity Bought:
	 *  Price Per:
	 *  Total Amount:
	 */
    private void constructRightLabels() {
		rightValuesPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);

		String boughtOutOfQuantity = getBoughtOutOfQuantity();
		Color quantityBoughtColor = buy.isComplete() ? ColorScheme.GRAND_EXCHANGE_ALCH : ColorScheme.PROGRESS_ERROR_COLOR;
		JLabel quantityBoughtValueLabel = newRightLabel(boughtOutOfQuantity, quantityBoughtColor);
		JLabel priceEachValueLabel = newRightLabel(Integer.toString(buy.getPricePer()), ColorScheme.GRAND_EXCHANGE_ALCH);
		JLabel totalPriceValueLabel = newRightLabel(Integer.toString(buy.getTotalPrice()), ColorScheme.GRAND_EXCHANGE_ALCH);
		
		addRightLabel(quantityBoughtValueLabel);
		addRightLabel(priceEachValueLabel);
		addRightLabel(totalPriceValueLabel);

		rightValuesPanel.setBorder(new EmptyBorder(2, 5, 2, 10));
	}
}