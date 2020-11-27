package com.flipper.views.buys;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;

import com.flipper.helpers.UiUtilities;
import com.flipper.models.Transaction;
import com.flipper.views.components.AmountProgressBar;
import com.flipper.views.components.ItemHeader;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.game.ItemManager;
import java.awt.Color;

import java.awt.event.*;
import java.util.UUID;
import java.util.function.Consumer;
import java.awt.Dimension;

/**
 * Construct of two main components Item Header (item image and name) Item
 * Information (buy info)
 */
public class BuyPanel extends JPanel {
    private static final long serialVersionUID = -6576417948629423220L;

    public BuyPanel(Transaction buy, ItemManager itemManager, Consumer<UUID> removeBuyConsumer) {
        setLayout(new BorderLayout());
        setBackground(ColorScheme.DARK_GRAY_COLOR);

        JButton deleteBuyButton = new JButton("X");
        deleteBuyButton.addActionListener((ActionEvent action) -> {
            String describedBuy = buy.describeTransaction();
            int input = JOptionPane.showConfirmDialog(null, "Delete Buy of " + describedBuy + "?");
            if (input == 0) {
                removeBuyConsumer.accept(buy.getId());
                setVisible(false);
            }
        });

        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        container.setBackground(ColorScheme.DARK_GRAY_COLOR);
        container.add(new ItemHeader(buy, itemManager, true, deleteBuyButton), BorderLayout.NORTH);
        container.add(new AmountProgressBar(buy), BorderLayout.SOUTH);
        container.setBorder(UiUtilities.ITEM_INFO_BORDER);

        this.add(container);
        this.setBorder(new EmptyBorder(0, 0, 3, 0));
    }
}