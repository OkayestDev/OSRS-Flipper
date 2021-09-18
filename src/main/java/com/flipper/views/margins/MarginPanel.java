package com.flipper.views.margins;

import com.flipper.helpers.Numbers;
import com.flipper.helpers.Timestamps;
import com.flipper.helpers.UiUtilities;
import com.flipper.models.Flip;
import com.flipper.views.components.DeleteButton;
import com.flipper.views.components.ItemHeader;
import java.awt.BorderLayout;
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

public class MarginPanel extends JPanel {

    private Flip margin;

    private final int LABEL_COUNT = 4;

    private JPanel container = new JPanel();
    private JPanel itemInfo = new JPanel(new BorderLayout());
    private JPanel leftInfoTextPanel = new JPanel(new GridLayout(LABEL_COUNT, 1));
    private JPanel rightValuesPanel = new JPanel(new GridLayout(LABEL_COUNT, 1));

    public MarginPanel(
        Flip margin,
        ItemManager itemManager,
        Consumer<UUID> removeMarginConsumer,
        boolean isPrompt,
        Consumer<Flip> convertToFlipConsumer
    ) {
        SwingUtilities.invokeLater(
            () -> {
                this.margin = margin;

                DeleteButton deleteMarginButton = new DeleteButton(
                    (ActionEvent action) -> {
                        int input = isPrompt ? JOptionPane.showConfirmDialog(null, "Delete margin check?") : 0;
                        if (input == 0) {
                            removeMarginConsumer.accept(margin.getId());
                            setVisible(false);
                        }
                    }
                );
                ItemComposition itemComp = itemManager.getItemComposition(margin.getItemId());

                this.setLayout(new BorderLayout());
                this.setBorder(new EmptyBorder(0, 5, 3, 5));
                container.setLayout(new BorderLayout());
                setBackground(ColorScheme.DARK_GRAY_COLOR);
                container.add(new ItemHeader(margin.getItemId(), 0, itemComp.getName(), itemManager, false, deleteMarginButton), BorderLayout.NORTH);
                constructItemInfo();
                JButton convertToFlipButton = new JButton("Convert To Flip");
                convertToFlipButton.addActionListener(
                    (ActionEvent event) -> {
                        convertToFlipConsumer.accept(margin);
                    }
                );
                container.add(convertToFlipButton, BorderLayout.SOUTH);
                container.setBorder(UiUtilities.ITEM_INFO_BORDER);

                this.add(container, BorderLayout.NORTH);
            }
        );
    }

    private void constructItemInfo() {
        constructLeftLabels();
        constructRightLabels();
        itemInfo.setBackground(ColorScheme.DARK_GRAY_COLOR);
        itemInfo.add(leftInfoTextPanel, BorderLayout.WEST);
        itemInfo.add(rightValuesPanel, BorderLayout.EAST);
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

        JLabel buyAtLabel = newLeftLabel("Buy At:");
        JLabel sellAtLabel = newLeftLabel("Sell At:");
        JLabel potentialProfitLabel = newLeftLabel("Profit Per:");
        JLabel dateLabel = newLeftLabel("Date:");

        addLeftLabel(buyAtLabel);
        addLeftLabel(sellAtLabel);
        addLeftLabel(potentialProfitLabel);
        addLeftLabel(dateLabel);

        leftInfoTextPanel.setBorder(new EmptyBorder(2, 5, 2, 10));
    }

    private JLabel newRightLabel(String value) {
        JLabel newRightLabel = new JLabel(value);
        newRightLabel.setHorizontalAlignment(JLabel.RIGHT);
        newRightLabel.setForeground(ColorScheme.GRAND_EXCHANGE_ALCH);
        newRightLabel.setBorder(new EmptyBorder(0, 0, 2, 0));
        return newRightLabel;
    }

    private void addRightLabel(JLabel newRightLabel) {
        rightValuesPanel.add(newRightLabel);
    }

    private void constructRightLabels() {
        rightValuesPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);

        int potentialProfitPer = this.margin.buyPrice - this.margin.sellPrice;

        String buyAtText = Integer.toString(this.margin.sellPrice);
        String sellAtText = Integer.toString(this.margin.buyPrice);
        String potentialProfitEachText = Integer.toString(potentialProfitPer);

        JLabel buyAtLabel = newRightLabel(Numbers.numberWithCommas(buyAtText));
        JLabel sellAtLabel = newRightLabel(Numbers.numberWithCommas(sellAtText));
        JLabel potentialProfitEachLabel = newRightLabel(Numbers.numberWithCommas(potentialProfitEachText));
        String formattedTimestamp = margin.getCreatedAt() != null ? Timestamps.format(margin.getCreatedAt()) : "--";
        JLabel dateLabel = newRightLabel(formattedTimestamp);

        addRightLabel(buyAtLabel);
        addRightLabel(sellAtLabel);
        addRightLabel(potentialProfitEachLabel);
        addRightLabel(dateLabel);

        rightValuesPanel.setBorder(new EmptyBorder(2, 5, 2, 10));
    }
}
