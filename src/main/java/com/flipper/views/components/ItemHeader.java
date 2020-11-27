package com.flipper.views.components;

import javax.swing.JButton;
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

import java.awt.event.*;

/**
 * Item header: Item Icon / Item Name / Number bought/soul out of total
 */
public class ItemHeader extends JPanel {
    private static final long serialVersionUID = 6953863323192783140L;

    private ItemManager itemManager;
    private Transaction transaction;
    private JPanel topRightContainer;
    private JLabel costPerLabel;

    public ItemHeader(Transaction transaction, ItemManager itemManager, boolean isAddCostPer, JButton hoverButton) {
        this.transaction = transaction;
        this.itemManager = itemManager;
        this.setLayout(new BorderLayout());
        topRightContainer = new JPanel();
        topRightContainer.setBackground(ColorScheme.DARKER_GRAY_COLOR.darker());

        JPanel itemIconPanel = constructItemIcon();
        JLabel itemName = constructItemName();
        if (isAddCostPer) {
            this.costPerLabel = constructCostPerLabel();
            topRightContainer.add(costPerLabel);
        }

        hoverButton.setVisible(false);
        topRightContainer.add(hoverButton);

        add(topRightContainer, BorderLayout.EAST);
        setBackground(ColorScheme.DARKER_GRAY_COLOR.darker());
        add(itemIconPanel, BorderLayout.WEST);
        add(itemName, BorderLayout.CENTER);
        setBorder(new EmptyBorder(2, 1, 2, 5));

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent me) {
                hoverButton.setVisible(true);
                if (isAddCostPer) {
                    costPerLabel.setVisible(false);
                }
            }

            @Override
            public void mouseExited(MouseEvent me) {
                if (isAddCostPer) {
                    costPerLabel.setVisible(true);
                }
                hoverButton.setVisible(false);
            }
        });

        hoverButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent me) {
                hoverButton.setVisible(true);
                if (isAddCostPer) {
                    costPerLabel.setVisible(false);
                }
            }

            @Override
            public void mouseExited(MouseEvent me) {
                if (isAddCostPer) {
                    costPerLabel.setVisible(true);
                }
                hoverButton.setVisible(false);
            }
        });
    }

    private JPanel constructItemIcon() {
        int itemId = transaction.getItemId();
        AsyncBufferedImage itemImage = itemManager.getImage(itemId);
        JLabel itemIcon = new JLabel();
        itemIcon.setAlignmentX(Component.LEFT_ALIGNMENT);
        itemIcon.setPreferredSize(UiUtilities.ICON_SIZE);
        if (itemImage != null) {
            itemImage.addTo(itemIcon);
        }
        JPanel itemIconPanel = new JPanel(new BorderLayout());
        itemIconPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR.darker());
        itemIconPanel.add(itemIcon, BorderLayout.WEST);
        return itemIconPanel;
    }

    private JLabel constructItemName() {
        JLabel itemName = new JLabel(transaction.getItemName(), SwingConstants.CENTER);
        itemName.setForeground(Color.WHITE);
        itemName.setFont(FontManager.getRunescapeBoldFont());
        itemName.setPreferredSize(new Dimension(0, 0));
        return itemName;
    }

    private JLabel constructCostPerLabel() {
        String costPerString = Integer.toString(transaction.getPricePer());
        JLabel costPerLabel = new JLabel(costPerString + " gp");
        costPerLabel.setForeground(ColorScheme.GRAND_EXCHANGE_ALCH);
        return costPerLabel;
    }
}