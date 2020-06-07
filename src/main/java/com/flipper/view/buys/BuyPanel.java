package com.flipper.view.buys;

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
import com.flipper.model.Transaction;

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
    private Transaction buy;
    private ItemManager itemManager;
	private Color background;

	private static final Border ITEM_INFO_BORDER = new CompoundBorder(
		BorderFactory.createMatteBorder(0, 0, 0, 0, ColorScheme.DARK_GRAY_COLOR),
		BorderFactory.createLineBorder(ColorScheme.DARKER_GRAY_COLOR.darker(), 3));

	/* Panels */
	JPanel titlePanel = new JPanel(new BorderLayout());
	JPanel itemInfo = new JPanel(new BorderLayout());
	JPanel leftInfoTextPanel = new JPanel(new GridLayout(7, 1));
    JPanel rightValuesPanel = new JPanel(new GridLayout(7, 1));
    
    JLabel itemName;

    public BuyPanel(Transaction buy, ItemManager itemManager) {
		this.itemManager = itemManager;
		this.buy = buy;
        setLayout(new BorderLayout());
		setBackground(ColorScheme.DARKER_GRAY_COLOR);
		background = getBackground();
		constructHeader();
		constructItemInfo();
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
		itemClearPanel.setBackground(background.darker());
		itemClearPanel.add(itemIcon, BorderLayout.WEST);
		itemName = new JLabel(buy.getItemName(), SwingConstants.CENTER);
		itemName.setForeground(Color.WHITE);
		itemName.setFont(FontManager.getRunescapeBoldFont());
		itemName.setPreferredSize(new Dimension(0, 0)); // Make sure the item name fits
		// titlePanel.setComponentPopupMenu(UIUtilities.createGeTrackerLinksPopup(flippingItem));
		titlePanel.setBackground(background.darker());
		titlePanel.add(itemClearPanel, BorderLayout.WEST);
		titlePanel.add(itemName, BorderLayout.CENTER);
		titlePanel.setBorder(new EmptyBorder(2, 1, 2, 1));
		add(titlePanel, BorderLayout.NORTH);
	}
	
	private void constructItemInfo() {
		constructLeftLabels();
		constructRightLabels();
		itemInfo.setBackground(background);
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
		leftInfoTextPanel.setBackground(background);

		JLabel quantityBoughtText = newLeftLabel("Amount Bought: ");
		JLabel buyPriceText = newLeftLabel("Price Per: ");
		JLabel totalAmountText = newLeftLabel("Total Cost: ");

		addLeftLabel(quantityBoughtText);
		addLeftLabel(buyPriceText);
		addLeftLabel(totalAmountText);

		leftInfoTextPanel.setBorder(new EmptyBorder(2, 5, 2, 5));
	}
	
	private JLabel newRightLabel(int value) {
		String valueAsString = Integer.toString(value);
		JLabel newRightLabel = new JLabel(valueAsString);
		newRightLabel.setHorizontalAlignment(JLabel.RIGHT);
		newRightLabel.setForeground(ColorScheme.GRAND_EXCHANGE_ALCH);
		return newRightLabel;
	}

	private void addRightLabel(JLabel newRightLabel) {
		rightValuesPanel.add(newRightLabel);
		rightValuesPanel.add(new JLabel(" "));
	}

	/**
	 * Constructs right side labels (buy transaction information)
	 *  Quantity Bought:
	 *  Price Per:
	 *  Total Amount:
	 */
    private void constructRightLabels() {
		rightValuesPanel.setBackground(background);

		JLabel quantityBoughtValueLabel = newRightLabel(buy.getQuantity());
		JLabel priceEachValueLabel = newRightLabel(buy.getPrice());
		JLabel totalPriceValueLabel = newRightLabel(buy.getTotalPrice());
		
		addRightLabel(quantityBoughtValueLabel);
		addRightLabel(priceEachValueLabel);
		addRightLabel(totalPriceValueLabel);

		rightValuesPanel.setBorder(new EmptyBorder(2, 5, 2, 5));
	}
}