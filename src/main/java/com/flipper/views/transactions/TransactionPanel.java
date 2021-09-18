package com.flipper.views.transactions;

import com.flipper.helpers.Numbers;
import com.flipper.helpers.Timestamps;
import com.flipper.helpers.UiUtilities;
import com.flipper.models.Transaction;
import com.flipper.views.components.AmountProgressBar;
import com.flipper.views.components.DeleteButton;
import com.flipper.views.components.ItemHeader;
import com.google.common.base.Supplier;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.*;
import java.util.UUID;
import java.util.function.Consumer;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import net.runelite.api.ItemComposition;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.ColorScheme;

public class TransactionPanel extends JPanel {

    JPanel container;

    private final int LABEL_COUNT = 3;

    private JPanel leftInfoTextPanel = new JPanel(new GridLayout(LABEL_COUNT, 1));
    private JPanel rightValuesPanel = new JPanel(new GridLayout(LABEL_COUNT, 1));

    private Transaction transaction;
    private Supplier<JButton> renderExtraComponentSupplier;
    private Consumer<Transaction> extraComponentPressedConsumer;

    public TransactionPanel(
        String name,
        Transaction transaction,
        ItemManager itemManager,
        Consumer<UUID> removeTransactionConsumer,
        boolean isPrompt,
        Supplier<JButton> renderExtraComponentSupplier,
        Consumer<Transaction> extraComponentPressedConsumer
    ) {
        this.renderExtraComponentSupplier = renderExtraComponentSupplier;
        this.extraComponentPressedConsumer = extraComponentPressedConsumer;
        init(name, transaction, itemManager, removeTransactionConsumer, isPrompt);
    }

    public TransactionPanel(
        String name,
        Transaction transaction,
        ItemManager itemManager,
        Consumer<UUID> removeTransactionConsumer,
        boolean isPrompt
    ) {
        init(name, transaction, itemManager, removeTransactionConsumer, isPrompt);
    }

    private void init(String name, Transaction transaction, ItemManager itemManager, Consumer<UUID> removeTransactionConsumer, boolean isPrompt) {
        SwingUtilities.invokeLater(
            () -> {
                this.transaction = transaction;
                setLayout(new BorderLayout());
                setBackground(ColorScheme.DARK_GRAY_COLOR);

                DeleteButton deleteTransactionButton = new DeleteButton(
                    (ActionEvent action) -> {
                        String describeTransaction = transaction.describeTransaction();
                        int input = isPrompt ? JOptionPane.showConfirmDialog(null, "Delete " + name + " of " + describeTransaction + "?") : 0;
                        if (input == 0) {
                            removeTransactionConsumer.accept(transaction.getId());
                            setVisible(false);
                        }
                    }
                );

                ItemComposition itemComp = itemManager.getItemComposition(transaction.getItemId());
                container = new JPanel();
                container.setLayout(new BorderLayout());
                container.setBackground(ColorScheme.DARK_GRAY_COLOR);
                container.add(
                    new ItemHeader(
                        transaction.getItemId(),
                        transaction.getPricePer(),
                        itemComp.getName(),
                        itemManager,
                        false,
                        deleteTransactionButton
                    ),
                    BorderLayout.NORTH
                );

                leftInfoTextPanel.setBorder(new EmptyBorder(2, 5, 2, 10));
                rightValuesPanel.setBorder(new EmptyBorder(2, 5, 2, 10));

                constructItemInfo();

                if (transaction.getQuantity() != transaction.getTotalQuantity()) {
                    container.add(new AmountProgressBar(transaction), BorderLayout.SOUTH);
                } else {
                    JButton extraComponent = renderExtraComponentSupplier.get();
                    if (extraComponent != null && !transaction.isAlched()) {
                        extraComponent.addActionListener(
                            (ActionEvent event) -> {
                                this.extraComponentPressedConsumer.accept(transaction);
                                extraComponent.setVisible(false);
                            }
                        );
                        container.add(extraComponent, BorderLayout.SOUTH);
                    }
                }

                container.setBorder(UiUtilities.ITEM_INFO_BORDER);
                this.add(container, BorderLayout.NORTH);
                this.setBorder(new EmptyBorder(0, 5, 3, 5));
            }
        );
    }

    private JLabel newLeftLabel(String text) {
        JLabel newJLabel = new JLabel(text);
        newJLabel.setForeground(ColorScheme.GRAND_EXCHANGE_PRICE);
        newJLabel.setBorder(new EmptyBorder(0, 0, 2, 0));
        return newJLabel;
    }

    private JLabel newRightLabel(String value, Color fontColor) {
        JLabel newRightLabel = new JLabel(value);
        newRightLabel.setHorizontalAlignment(JLabel.RIGHT);
        newRightLabel.setForeground(fontColor);
        newRightLabel.setBorder(new EmptyBorder(0, 0, 2, 0));
        return newRightLabel;
    }

    private void addLeftLabel(JLabel newLeftLabel) {
        leftInfoTextPanel.add(newLeftLabel);
    }

    private void addRightLabel(JLabel newRightLabel) {
        rightValuesPanel.add(newRightLabel);
    }

    private void constructItemInfo() {
        JPanel itemInfoContainer = new JPanel();
        itemInfoContainer.setLayout(new BorderLayout());

        String pricePerString = Numbers.numberWithCommas(transaction.getPricePer());
        String quantityString = Numbers.numberWithCommas(transaction.getQuantity());
        String totalQuantityString = Numbers.numberWithCommas(transaction.getTotalQuantity());

        JLabel pricePerLabel = newLeftLabel("Price Per:");
        JLabel pricePerValue = newRightLabel(pricePerString, ColorScheme.GRAND_EXCHANGE_ALCH);
        addLeftLabel(pricePerLabel);
        addRightLabel(pricePerValue);

        String quantityValueText = transaction.isFilled() ? quantityString : String.valueOf(quantityString + "/" + totalQuantityString);

        JLabel quantityLabel = newLeftLabel("Quantity:");
        JLabel quantityValue = newRightLabel(quantityValueText, ColorScheme.GRAND_EXCHANGE_ALCH);
        addLeftLabel(quantityLabel);
        addRightLabel(quantityValue);

        JLabel dateLabel = newLeftLabel("Date:");
        JLabel dateValue = newRightLabel(Timestamps.format(transaction.getCreatedTime()), ColorScheme.GRAND_EXCHANGE_ALCH);
        addLeftLabel(dateLabel);
        addRightLabel(dateValue);

        itemInfoContainer.add(leftInfoTextPanel, BorderLayout.WEST);
        itemInfoContainer.add(rightValuesPanel, BorderLayout.EAST);

        container.add(itemInfoContainer, BorderLayout.CENTER);
    }
}
