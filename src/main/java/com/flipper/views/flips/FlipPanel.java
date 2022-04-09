package com.flipper.views.flips;

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
import com.flipper.models.Flip;
import com.flipper.views.components.DeleteButton;
import com.flipper.views.components.ItemHeader;

import net.runelite.client.ui.ColorScheme;
import net.runelite.api.ItemComposition;
import net.runelite.client.game.ItemManager;

public class FlipPanel extends JPanel {
    private Flip flip;

    private static int LABEL_COUNT = 6;

    private JPanel container = new JPanel();
    private JPanel itemInfo = new JPanel(new BorderLayout());
    private JPanel leftInfoTextPanel = new JPanel(new GridLayout(LABEL_COUNT, 1));
    private JPanel rightValuesPanel = new JPanel(new GridLayout(LABEL_COUNT, 1));

    public FlipPanel(
        Flip flip, 
        ItemManager itemManager, 
        Consumer<UUID> removeFlipConsumer, 
        boolean isPrompt
    ) {
        this.flip = flip;
        ItemComposition itemComp = itemManager.getItemComposition(flip.getItemId());

        DeleteButton deleteFlipButton = new DeleteButton((ActionEvent action) -> {
            String describedBuy = flip.describeFlip(itemComp.getName());
            int input = isPrompt
                ? JOptionPane.showConfirmDialog(null, "Delete flip of " + describedBuy + "?")
                : 0;
            if (input == 0) {
                removeFlipConsumer.accept(flip.getId());
                setVisible(false);
            }
        });

        this.setLayout(new BorderLayout());
        container.setLayout(new BorderLayout());
        container.setBackground(ColorScheme.DARK_GRAY_COLOR);
        container.add(new ItemHeader(flip.getItemId(), 0, itemComp.getName(), itemManager, false, deleteFlipButton), BorderLayout.NORTH);
        constructItemInfo();
        this.setBorder(new EmptyBorder(0, 5, 3, 5));

        this.add(container, BorderLayout.NORTH);
    }

    private void constructItemInfo() {
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

        JLabel amountFlippedLabel = newLeftLabel("Amount Flipped:");
        JLabel profitEachLabel = newLeftLabel("Profit Per:");
        JLabel totalProfitLabel = newLeftLabel("Total Profit:");
        JLabel buyPrice = newLeftLabel("Buy Price:");
        JLabel sellPrice = newLeftLabel("Sell Price:");
        JLabel tax = newLeftLabel("Tax:");
        JLabel flipCreatedAt = newLeftLabel("Date:");

        addLeftLabel(amountFlippedLabel);
        addLeftLabel(profitEachLabel);
        addLeftLabel(totalProfitLabel);
        addLeftLabel(buyPrice);
        addLeftLabel(sellPrice);
        addLeftLabel(tax);
        addLeftLabel(flipCreatedAt);

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

        int totalProfit = flip.getTotalProfit();
        int tax = flip.getTax();

        int quantity = this.flip.getQuantity();
        int profitEach = quantity != 0 ? totalProfit / quantity : 0;

        String amountFlippedText = Integer.toString(this.flip.getQuantity());
        String totalProfitText = Integer.toString(totalProfit);
        String taxEachText = Integer.toString(tax);
        String profitEachText = Integer.toString(profitEach);

        JLabel amountFlippedLabel = newRightLabel(Numbers.numberWithCommas(amountFlippedText), ColorScheme.GRAND_EXCHANGE_ALCH);

        Color profitEachColor = profitEach > 0 ? ColorScheme.GRAND_EXCHANGE_ALCH : ColorScheme.PROGRESS_ERROR_COLOR;
        JLabel profitEachLabel = newRightLabel(Numbers.numberWithCommas(profitEachText), profitEachColor);

        Color profitColor = flip.getTotalProfit() > 0 ? ColorScheme.GRAND_EXCHANGE_ALCH
                : ColorScheme.PROGRESS_ERROR_COLOR;
        JLabel totalProfitLabel = newRightLabel(Numbers.numberWithCommas(totalProfitText), profitColor);
        JLabel taxPerItem = newRightLabel(Numbers.numberWithCommas(taxEachText), ColorScheme.LIGHT_GRAY_COLOR);

        JLabel buyPrice = newRightLabel(Numbers.numberWithCommas(flip.getBuyPrice()),  ColorScheme.GRAND_EXCHANGE_ALCH);
        JLabel sellPrice = newRightLabel(Numbers.numberWithCommas(flip.getSellPrice()),  ColorScheme.GRAND_EXCHANGE_ALCH);
        JLabel flipCreatedAt = newRightLabel(Timestamps.format(flip.getCreatedAt()),  ColorScheme.GRAND_EXCHANGE_ALCH);

        addRightLabel(amountFlippedLabel);
        addRightLabel(profitEachLabel);
        addRightLabel(totalProfitLabel);
        addRightLabel(buyPrice);
        addRightLabel(sellPrice);
        addRightLabel(taxPerItem);
        addRightLabel(flipCreatedAt);

        rightValuesPanel.setBorder(new EmptyBorder(2, 5, 2, 10));
    }
}