package com.flipper.views.transactions;

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

public class TransactionPanel extends JPanel {
    public TransactionPanel(String name, Transaction transaction, ItemManager itemManager, Consumer<UUID> removeTransactionConsumer) {
        SwingUtilities.invokeLater(() -> {
            setLayout(new BorderLayout());
            setBackground(ColorScheme.DARK_GRAY_COLOR);

            DeleteButton deleteTransactionButton = new DeleteButton((ActionEvent action) -> {
                String describeTransaction = transaction.describeTransaction();
                int input = JOptionPane.showConfirmDialog(null, "Delete " + name + " of " + describeTransaction + "?");
                if (input == 0) {
                    removeTransactionConsumer.accept(transaction.getId());
                    setVisible(false);
                }
            });

            ItemComposition itemComp = itemManager.getItemComposition(transaction.getItemId());
            JPanel container = new JPanel();
            container.setLayout(new BorderLayout());
            container.setBackground(ColorScheme.DARK_GRAY_COLOR);
            container.add(new ItemHeader(transaction.getItemId(), transaction.getPricePer(), itemComp.getName(), itemManager, true, deleteTransactionButton), BorderLayout.NORTH);
            container.add(new AmountProgressBar(transaction), BorderLayout.SOUTH);
            container.setBorder(UiUtilities.ITEM_INFO_BORDER);

            this.add(container);
            this.setBorder(new EmptyBorder(0, 0, 3, 0));
        });
    }
}