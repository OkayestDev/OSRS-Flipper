package com.flipper.views.alchs;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.UUID;
import java.util.function.Consumer;
import java.awt.Color;
import java.awt.event.*;

import com.flipper.helpers.Numbers;
import com.flipper.helpers.Timestamps;
import com.flipper.helpers.UiUtilities;

import com.flipper.models.Alch;

import com.flipper.views.components.DeleteButton;
import com.flipper.views.components.ItemHeader;

import net.runelite.client.ui.ColorScheme;
import net.runelite.api.ItemComposition;
import net.runelite.client.game.ItemManager;

public class AlchPanel extends JPanel {
    private Alch alch;

    private static int LABEL_COUNT = 7;

    private JPanel container = new JPanel();
    private JPanel itemInfo = new JPanel(new BorderLayout());
    private JPanel leftInfoTextPanel = new JPanel(new GridLayout(LABEL_COUNT, 1));
    private JPanel rightValuesPanel = new JPanel(new GridLayout(LABEL_COUNT, 1));

    public AlchPanel(Alch alch, ItemManager itemManager, Consumer<UUID> removeAlchConsumer) {
        this.alch = alch;
        ItemComposition itemComp = itemManager.getItemComposition(alch.getItemId());

        DeleteButton deleteAlchButton = new DeleteButton((ActionEvent action) -> {
            String describedAlch = alch.describeAlch(itemComp.getName());
            int input = JOptionPane.showConfirmDialog(
                null, 
                "Delete alch of " + describedAlch + "?"
            );
            if (input == 0) {
                removeAlchConsumer.accept(alch.getId());
                setVisible(false);
            }
        });

        this.setLayout(new BorderLayout());
        container.setLayout(new BorderLayout());
        container.setBackground(ColorScheme.DARK_GRAY_COLOR);
        container.add(new ItemHeader(
            alch.getItemId(),
            0,
            itemComp.getName(),
            itemManager,
            false,
            deleteAlchButton
        ), BorderLayout.NORTH);
        constructItemInfo();
        this.setBorder(new EmptyBorder(0, 5, 3, 5));

        this.add(container, BorderLayout.NORTH);
    }

    public void constructItemInfo() {
        constructLeftLabels();
        constructRightLabels();
        itemInfo.setBackground(ColorScheme.DARK_GRAY_COLOR);
        itemInfo.add(leftInfoTextPanel, BorderLayout.WEST);
        itemInfo.add(rightValuesPanel, BorderLayout.EAST);
        itemInfo.setBorder(UiUtilities.ITEM_INFO_BORDER);
        container.add(itemInfo, BorderLayout.CENTER);
    }

    private JLabel newLeftLabel(String text) {
        JLabel newJLabel = new JLabel(text);
        newJLabel.setForeground(ColorScheme.GRAND_EXCHANGE_PRICE);
        newJLabel.setBorder(new EmptyBorder(0, 0, 2, 0));
        return newJLabel;
    }

    private void addLeftLabel(JLabel newLeftLabel) {
        leftInfoTextPanel.add(newLeftLabel);
    }

    private void constructLeftLabels() {
        leftInfoTextPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);

        JLabel quantityLabel = newLeftLabel("Quantity:");
        JLabel profitEachLabel = newLeftLabel("Profit Per:");
        JLabel totalProfitLabel = newLeftLabel("Total Profit:");
        JLabel buyPrice = newLeftLabel("Buy Price:");
        JLabel alchPrice = newLeftLabel("Alch Price:");
        JLabel natureRunePrice = newLeftLabel("Nature Rune:");
        JLabel alchLastUpdatedAt = newLeftLabel("Date:");

        addLeftLabel(quantityLabel);
        addLeftLabel(profitEachLabel);
        addLeftLabel(totalProfitLabel);
        addLeftLabel(buyPrice);
        addLeftLabel(alchPrice);
        addLeftLabel(natureRunePrice);
        addLeftLabel(alchLastUpdatedAt);

        leftInfoTextPanel.setBorder(new EmptyBorder(2, 5, 2, 10));
    }

    private JLabel newRightLabel(String value, Color fontColor) {
        JLabel newRightLabel = new JLabel(value);
        newRightLabel.setHorizontalAlignment(JLabel.RIGHT);
        newRightLabel.setForeground(fontColor);
        newRightLabel.setBorder(new EmptyBorder(0, 0, 2, 0));
        return newRightLabel;
    }

    private void addRightLabel(JLabel newRightLabel) {
        rightValuesPanel.add(newRightLabel);
    }

    private void constructRightLabels() {
        rightValuesPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);

        int totalProfit = alch.getTotalProfit();

        int quantity = alch.getQuantity();
        int profitEach = quantity != 0 ? alch.getAlchPrice() - alch.getBuyPrice() - alch.getNatureRunePrice() : 0;

        String quantityText = Integer.toString(alch.getQuantity());
        String totalProfitText = Integer.toString(totalProfit);
        String profitEachText = Integer.toString(profitEach);

        JLabel quantityLabel = newRightLabel(Numbers.numberWithCommas(quantityText), ColorScheme.GRAND_EXCHANGE_ALCH);

        Color profitEachColor = profitEach > 0 ? ColorScheme.GRAND_EXCHANGE_ALCH : ColorScheme.PROGRESS_ERROR_COLOR;
        JLabel profitEachLabel = newRightLabel(Numbers.numberWithCommas(profitEachText), profitEachColor);

        Color profitColor = alch.getTotalProfit() > 0 ? ColorScheme.GRAND_EXCHANGE_ALCH
                : ColorScheme.PROGRESS_ERROR_COLOR;
        JLabel totalProfitLabel = newRightLabel(Numbers.numberWithCommas(totalProfitText), profitColor);

        JLabel buyPrice = newRightLabel(Numbers.numberWithCommas(alch.getBuyPrice()),  ColorScheme.GRAND_EXCHANGE_ALCH);
        JLabel alchPrice = newRightLabel(Numbers.numberWithCommas(alch.getAlchPrice()),  ColorScheme.GRAND_EXCHANGE_ALCH);
        JLabel alchCreatedAt = newRightLabel(Timestamps.format(alch.getCreatedAt()),  ColorScheme.GRAND_EXCHANGE_ALCH);
        JLabel natureRunePrice = newRightLabel(String.valueOf(alch.natureRunePrice), ColorScheme.GRAND_EXCHANGE_ALCH);

        addRightLabel(quantityLabel);
        addRightLabel(profitEachLabel);
        addRightLabel(totalProfitLabel);
        addRightLabel(buyPrice);
        addRightLabel(alchPrice);
        addRightLabel(natureRunePrice);
        addRightLabel(alchCreatedAt);

        rightValuesPanel.setBorder(new EmptyBorder(2, 5, 2, 10));
    }
}
