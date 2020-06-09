package com.flipper.views.components;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Color;
import java.awt.Dimension;

import com.flipper.helpers.UiUtilities;
import com.flipper.models.Transaction;

import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.util.AsyncBufferedImage;

public class ItemHeader extends JPanel {
    public ItemHeader(Transaction transaction, ItemManager itemManager) {
		this.setLayout(new BorderLayout());
        int itemId = transaction.getItemId();
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
		JLabel itemName = new JLabel(transaction.getItemName(), SwingConstants.CENTER);
		itemName.setForeground(Color.WHITE);
		itemName.setFont(FontManager.getRunescapeBoldFont());
        itemName.setPreferredSize(new Dimension(0, 0));
        // JPanel titlePanel = new JPanel(new BorderLayout());
		setBackground(ColorScheme.DARKER_GRAY_COLOR.darker());
		add(itemClearPanel, BorderLayout.WEST);
		add(itemName, BorderLayout.CENTER);
		setBorder(new EmptyBorder(2, 1, 2, 1));
    }
}