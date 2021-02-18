package com.flipper.views.buys;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;

import com.flipper.helpers.UiUtilities;
import com.flipper.models.Transaction;
import com.flipper.views.components.AmountProgressBar;
import com.flipper.views.components.DeleteButton;
import com.flipper.views.components.ItemHeader;

import net.runelite.api.ItemComposition;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.ColorScheme;

import java.awt.event.*;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Construct of two main components Item Header (item image and name) Item
 * Information (buy info)
 */
public class BuyPanel extends JPanel {
    public BuyPanel(Transaction buy, ItemManager itemManager, Consumer<UUID> removeBuyConsumer) {
        SwingUtilities.invokeLater(() -> {
            setLayout(new BorderLayout());
            setBackground(ColorScheme.DARK_GRAY_COLOR);

            DeleteButton deleteBuyButton = new DeleteButton((ActionEvent action) -> {
                String describedBuy = buy.describeTransaction();
                int input = JOptionPane.showConfirmDialog(null, "Delete Buy of " + describedBuy + "?");
                if (input == 0) {
                    removeBuyConsumer.accept(buy.getId());
                    setVisible(false);
                }
            });

            ItemComposition itemComp = itemManager.getItemComposition(buy.getItemId());
            JPanel container = new JPanel();
            container.setLayout(new BorderLayout());
            container.setBackground(ColorScheme.DARK_GRAY_COLOR);
            container.add(new ItemHeader(buy.getItemId(), buy.getPricePer(), itemComp.getName(), itemManager, true, deleteBuyButton), BorderLayout.NORTH);
            container.add(new AmountProgressBar(buy), BorderLayout.SOUTH);
            container.setBorder(UiUtilities.ITEM_INFO_BORDER);

            this.add(container);
            this.setBorder(new EmptyBorder(0, 0, 3, 0));
        });
    }
}