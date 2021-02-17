package com.flipper.views.sells;

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

import net.runelite.client.ui.ColorScheme;
import net.runelite.client.game.ItemManager;

import java.awt.event.*;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Construct of two main components Item Header (item image and name) Item
 * Information (sell info)
 */
public class SellPanel extends JPanel {
    public SellPanel(Transaction sell, ItemManager itemManager, Consumer<UUID> removeSellConsumer) {
        SwingUtilities.invokeLater(() -> {
            setLayout(new BorderLayout());
            setBackground(ColorScheme.DARK_GRAY_COLOR);

            DeleteButton deleteSellButton = new DeleteButton((ActionEvent action) -> {
                String describedSell = sell.describeTransaction();
                int input = JOptionPane.showConfirmDialog(null, "Delete Sell of " + describedSell + "?");
                if (input == 0) {
                    removeSellConsumer.accept(sell.getId());
                    setVisible(false);
                }
            });

            JPanel container = new JPanel();
            container.setLayout(new BorderLayout());
            container.setBackground(ColorScheme.DARK_GRAY_COLOR);
            container.add(new ItemHeader(sell, itemManager, true, deleteSellButton), BorderLayout.NORTH);
            container.add(new AmountProgressBar(sell), BorderLayout.SOUTH);
            container.setBorder(UiUtilities.ITEM_INFO_BORDER);

            this.add(container);
            this.setBorder(new EmptyBorder(0, 0, 3, 0));
        });
    }
}