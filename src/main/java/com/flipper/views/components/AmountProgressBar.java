package com.flipper.views.components;

import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import java.util.Collections;
import java.util.List;

import com.flipper.models.Transaction;

import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.components.DimmableJPanel;

/**
 * Shows a progress par of amount bought/sold out of total quantity
 */
public class AmountProgressBar extends DimmableJPanel {
    private static final long serialVersionUID = 1910372592064348629L;

    private int maximumValue;
    private int value;
    private List<Integer> positions = Collections.emptyList();
    private final JLabel leftLabel = new JLabel();
    private final JLabel rightLabel = new JLabel();

    public AmountProgressBar(Transaction transaction) {
        setLayout(new BorderLayout());

        maximumValue = transaction.getTotalQuantity();
        value = transaction.getQuantity();

        setLabel(leftLabel, value);
        setLabel(rightLabel, maximumValue);

        setBackground(ColorScheme.DARKER_GRAY_COLOR);
        setForeground(Color.GREEN.darker().darker());

        setPreferredSize(new Dimension(100, 16));

        // Adds components to be automatically redrawn when paintComponents is called
        add(leftLabel, BorderLayout.WEST);
        add(rightLabel, BorderLayout.EAST);
    }

    private void setLabel(JLabel jLabel, int value) {
        String valueAsString = Integer.toString(value);
        jLabel.setText(valueAsString);
        jLabel.setForeground(Color.WHITE);
        jLabel.setFont(FontManager.getRunescapeBoldFont());
        jLabel.setBorder(new EmptyBorder(2, 5, 0, 2));
    }

    @Override
    public void paint(Graphics g) {
        int percentage = getPercentage();
        int topWidth = (int) (getSize().width * (percentage / 100f));

        super.paint(g);
        g.setColor(getForeground());
        g.fillRect(0, 0, topWidth, 16);

        for (final Integer position : positions) {
            final int xCord = getSize().width * position / maximumValue;
            if (xCord > topWidth) {
                g.fillRect(xCord, 0, 1, 16);
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