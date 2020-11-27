package com.flipper.views.flips;

import javax.swing.JButton;
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

import com.flipper.helpers.UiUtilities;
import com.flipper.models.Flip;
import com.flipper.models.Transaction;
import com.flipper.views.components.DeleteButton;
import com.flipper.views.components.ItemHeader;

import net.runelite.client.ui.ColorScheme;
import net.runelite.client.game.ItemManager;

public class FlipPanel extends JPanel {
    private static final long serialVersionUID = 3879605322279273963L;

    private Flip flip;
    private Transaction buy;
    private Transaction sell;

    private JPanel itemInfo = new JPanel(new BorderLayout());
    private JPanel leftInfoTextPanel = new JPanel(new GridLayout(3, 1));
    private JPanel rightValuesPanel = new JPanel(new GridLayout(3, 1));

    public FlipPanel(Flip flip, ItemManager itemManager, Consumer<UUID> removeFlipConsumer) {
        this.flip = flip;
        this.sell = flip.getSell();
        this.buy = flip.getBuy();

        DeleteButton deleteFlipButton = new DeleteButton((ActionEvent action) -> {
            String describedBuy = flip.describeFlip();
            int input = JOptionPane.showConfirmDialog(null, "Delete flip of " + describedBuy + "?");
            if (input == 0) {
                removeFlipConsumer.accept(flip.getId());
                setVisible(false);
            }
        });

        setLayout(new BorderLayout());
        setBackground(ColorScheme.DARK_GRAY_COLOR);
        this.add(new ItemHeader(this.buy, itemManager, false, deleteFlipButton), BorderLayout.NORTH);
        constructItemInfo();
        this.setBorder(new EmptyBorder(0, 0, 5, 0));
    }

    private void constructItemInfo() {
        constructLeftLabels();
        constructRightLabels();
        itemInfo.setBackground(ColorScheme.DARK_GRAY_COLOR);
        itemInfo.add(leftInfoTextPanel, BorderLayout.WEST);
        itemInfo.add(rightValuesPanel, BorderLayout.EAST);
        itemInfo.setBorder(UiUtilities.ITEM_INFO_BORDER);
        add(itemInfo, BorderLayout.CENTER);
    }

    private JLabel newLeftLabel(String text) {
        JLabel newJLabel = new JLabel(text);
        newJLabel.setForeground(ColorScheme.GRAND_EXCHANGE_PRICE);
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

        addLeftLabel(amountFlippedLabel);
        addLeftLabel(profitEachLabel);
        addLeftLabel(totalProfitLabel);

        leftInfoTextPanel.setBorder(new EmptyBorder(2, 5, 2, 10));
    }

    private JLabel newRightLabel(String value, Color fontColor) {
        JLabel newRightLabel = new JLabel(value);
        newRightLabel.setHorizontalAlignment(JLabel.RIGHT);
        newRightLabel.setForeground(fontColor);
        return newRightLabel;
    }

    private void addRightLabel(JLabel newRightLabel) {
        rightValuesPanel.add(newRightLabel);
    }

    private void constructRightLabels() {
        rightValuesPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);

        int totalProfit = flip.getTotalProfit();
        int profitEach = totalProfit / sell.getQuantity();

        String amountFlippedText = Integer.toString(sell.getQuantity());
        String totalProfitText = Integer.toString(totalProfit);
        String profitEachText = Integer.toString(profitEach);

        JLabel amountFlippedLabel = newRightLabel(amountFlippedText, ColorScheme.GRAND_EXCHANGE_ALCH);

        Color profitEachColor = profitEach > 0 ? ColorScheme.GRAND_EXCHANGE_ALCH : ColorScheme.PROGRESS_ERROR_COLOR;
        JLabel profitEachLabel = newRightLabel(profitEachText, profitEachColor);

        Color profitColor = flip.getTotalProfit() > 0 ? ColorScheme.GRAND_EXCHANGE_ALCH
                : ColorScheme.PROGRESS_ERROR_COLOR;
        JLabel totalProfitLabel = newRightLabel(totalProfitText, profitColor);

        addRightLabel(amountFlippedLabel);
        addRightLabel(profitEachLabel);
        addRightLabel(totalProfitLabel);

        rightValuesPanel.setBorder(new EmptyBorder(2, 5, 2, 10));
    }
}