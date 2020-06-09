package com.flipper.views.flips;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import com.flipper.models.Flip;
import com.flipper.models.Transaction;
import com.flipper.views.components.ItemHeader;

import net.runelite.client.ui.ColorScheme;
import net.runelite.client.game.ItemManager;

public class FlipPanel extends JPanel {
	private static final long serialVersionUID = 3879605322279273963L;

	private Flip flip;
    private Transaction buy;
    private Transaction sell;
    private ItemManager itemManager;
    private JLabel itemName;
	
	private JPanel itemInfo = new JPanel(new BorderLayout());
	private JPanel leftInfoTextPanel = new JPanel(new GridLayout(7, 1));
    private JPanel rightValuesPanel = new JPanel(new GridLayout(7, 1));

    public FlipPanel(Flip flip, ItemManager itemManager) {
		this.itemManager = itemManager;
        this.flip = flip;
        this.sell = flip.getSell();
        this.buy = flip.getBuy();
        setLayout(new BorderLayout());
		setBackground(ColorScheme.DARK_GRAY_COLOR);
		this.add(new ItemHeader(this.buy, itemManager, false), BorderLayout.NORTH);
		// constructItemInfo();
		this.setBorder(new EmptyBorder(0, 0, 5, 0));
    }
	
	// private void constructItemInfo() {
	// 	constructLeftLabels();
	// 	constructRightLabels();
	// 	itemInfo.setBackground(ColorScheme.DARK_GRAY_COLOR);
	// 	itemInfo.add(leftInfoTextPanel, BorderLayout.WEST);
	// 	itemInfo.add(rightValuesPanel, BorderLayout.EAST);
	// 	itemInfo.setBorder(ITEM_INFO_BORDER);
	// 	add(itemInfo, BorderLayout.CENTER);
	// }

	// private JLabel newLeftLabel(String text) {
	// 	JLabel newJLabel = new JLabel(text);
	// 	newJLabel.setForeground(ColorScheme.GRAND_EXCHANGE_PRICE);
	// 	return newJLabel;
	// }

	// private void addLeftLabel(JLabel newLeftLabel) {
	// 	leftInfoTextPanel.add(newLeftLabel);
	// 	leftInfoTextPanel.add(new JLabel(" "));
	// }

	// /**
	//  * Constructs left side labels
	//  *  Quantity Bought:  @todo maybe put this in header to the right of item name?
	//  * 	Price Per: 
	//  *  Total Amount: 
	//  */
    // private void constructLeftLabels() {
	// 	leftInfoTextPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);

	// 	JLabel quantityBoughtText = newLeftLabel("Amount Bought: ");
	// 	JLabel buyPriceText = newLeftLabel("Price Per: ");
	// 	JLabel totalAmountText = newLeftLabel("Total Cost: ");

	// 	addLeftLabel(quantityBoughtText);
	// 	addLeftLabel(buyPriceText);
	// 	addLeftLabel(totalAmountText);

	// 	leftInfoTextPanel.setBorder(new EmptyBorder(2, 5, 2, 10));
	// }
	
	// private JLabel newRightLabel(String value, Color fontColor) {
	// 	JLabel newRightLabel = new JLabel(value);
	// 	newRightLabel.setHorizontalAlignment(JLabel.RIGHT);
	// 	newRightLabel.setForeground(fontColor);
	// 	return newRightLabel;
	// }

	// private void addRightLabel(JLabel newRightLabel) {
	// 	rightValuesPanel.add(newRightLabel);
	// 	rightValuesPanel.add(new JLabel(" "));
	// }

	// /**
	//  * returns fraction x/x if not all are bought,
	//  * otherwise return the whole number
	//  */
	// private String getBoughtOutOfQuantity() {
	// 	if (buy.isComplete()) {
	// 		return Integer.toString(buy.getQuantity());
	// 	} else {
	// 		return Integer.toString(buy.getQuantity()) + "/" + Integer.toString(buy.getTotalQuantity());
	// 	}
	// }

	// /**
	//  * Constructs right side labels (buy transaction information)
	//  *  Quantity Bought:
	//  *  Price Per:
	//  *  Total Amount:
	//  */
    // private void constructRightLabels() {
	// 	rightValuesPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);

	// 	String boughtOutOfQuantity = getBoughtOutOfQuantity();
	// 	Color quantityBoughtColor = buy.isComplete() ? ColorScheme.GRAND_EXCHANGE_ALCH : ColorScheme.PROGRESS_ERROR_COLOR;
	// 	JLabel quantityBoughtValueLabel = newRightLabel(boughtOutOfQuantity, quantityBoughtColor);
	// 	JLabel priceEachValueLabel = newRightLabel(Integer.toString(buy.getPricePer()), ColorScheme.GRAND_EXCHANGE_ALCH);
	// 	JLabel totalPriceValueLabel = newRightLabel(Integer.toString(buy.getTotalPrice()), ColorScheme.GRAND_EXCHANGE_ALCH);
		
	// 	addRightLabel(quantityBoughtValueLabel);
	// 	addRightLabel(priceEachValueLabel);
	// 	addRightLabel(totalPriceValueLabel);

	// 	rightValuesPanel.setBorder(new EmptyBorder(2, 5, 2, 10));
	// }
}