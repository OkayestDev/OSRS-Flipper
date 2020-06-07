package com.flipper.view.components;

import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.Component;

import com.flipper.helpers.Log;
import com.flipper.helpers.UiUtilities;
import com.flipper.model.Transaction;

import net.runelite.client.ui.ColorScheme;
import net.runelite.client.game.ItemManager;
import net.runelite.client.util.AsyncBufferedImage;

public class TransactionPanel extends JPanel {
    private Transaction buy;
    private ItemManager itemManager;
    private JPanel container;

    public TransactionPanel(Transaction buy, ItemManager itemManager) {
        this.buy = buy;
        this.itemManager = itemManager;
        container = new JPanel();
        container.setBackground(ColorScheme.DARKER_GRAY_COLOR.darker());
        container.setSize(200, 200);
        this.add(container);
        addItemHeader();
    }

    private void addItemHeader() {
        int itemId = buy.getItemId();

		AsyncBufferedImage itemImage = itemManager.getImage(itemId);
		JLabel itemIcon = new JLabel();
		itemIcon.setAlignmentX(Component.LEFT_ALIGNMENT);
		itemIcon.setPreferredSize(UiUtilities.ICON_SIZE);
		if (itemImage != null) {
			itemImage.addTo(itemIcon);
        }
        container.add(itemIcon);

        // Why is this not working?
        String itemName = itemManager.getItemComposition(itemId).getName();
        JLabel itemNameLabel = new JLabel(itemName);
        container.add(itemNameLabel);

        // int itemPrice = itemManager.getItemPrice(itemId);
        // String itemPriceAsString = Integer.toString(itemPrice);
        // JLabel itemPriceLabel = new JLabel(itemPriceAsString);
        // container.add(itemPriceLabel);
    }
}