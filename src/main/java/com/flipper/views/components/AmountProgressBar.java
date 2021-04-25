package com.flipper.views.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import java.util.Collections;
import java.util.List;

import com.flipper.models.Transaction;

import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.components.DimmableJPanel;

/**
 * Shows a progress par of amount bought/sold out of total quantity
 */
public class AmountProgressBar extends DimmableJPanel {
    private int maximumValue;
    private int value;
    private List<Integer> positions = Collections.emptyList();

    public AmountProgressBar(Transaction transaction) {
        setLayout(new BorderLayout());

        maximumValue = transaction.getTotalQuantity();
        value = transaction.getQuantity();

        setBackground(ColorScheme.DARKER_GRAY_COLOR);
        setForeground(Color.GREEN.darker().darker());

        setPreferredSize(new Dimension(100, 4));
    }

    @Override
    public void paint(Graphics g) {
        int percentage = getPercentage();
        int topWidth = (int) (getSize().width * (percentage / 100f));

        super.paint(g);
        g.setColor(getForeground());
        g.fillRect(0, 0, topWidth, 4);

        for (final Integer position : positions) {
            final int xCord = getSize().width * position / maximumValue;
            if (xCord > topWidth) {
                g.fillRect(xCord, 0, 1, 4);
            }
        }

        super.paintComponents(g);
    }

    private int getPercentage() {
        if (value == 0) {
            return 0;
        }
        return (value * 100) / maximumValue;
    }
}