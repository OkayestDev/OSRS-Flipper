package com.flipper.views.flips;

import java.util.List;
import java.util.ListIterator;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Font;

import com.flipper.helpers.UiUtilities;
import com.flipper.models.Flip;

import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;

public class FlipsPanel extends JPanel {
    private static final long serialVersionUID = -2680984300396244041L;
    
    private ItemManager itemManager;
    private int totalProfit = 0;

    public FlipsPanel(ItemManager itemManager) {
        this.itemManager = itemManager;
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setBackground(ColorScheme.DARK_GRAY_COLOR);
    }

    public void rebuildPanel(List<Flip> flips) {
        SwingUtilities.invokeLater(() -> {
            this.removeAll();
            this.totalProfit = 0;
            JPanel flipsPanel = constructFlips(flips);
            constructTotalProfit();
            this.add(flipsPanel, BorderLayout.WEST);
        });
    }

    private JPanel constructFlips(List<Flip> flips) {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
        container.setBackground(ColorScheme.DARK_GRAY_COLOR);

        JScrollPane scrollPane = new JScrollPane(container);
        scrollPane.setBackground(ColorScheme.LIGHT_GRAY_COLOR);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Reverse list to show newest first
        ListIterator<Flip> flipsIterator = flips.listIterator(flips.size());
        while(flipsIterator.hasPrevious()) {
            Flip flip = flipsIterator.previous();
            FlipPanel flipPanel = new FlipPanel(flip, itemManager);
            totalProfit += flip.getTotalProfit();
            container.add(flipPanel);
        }

        return container;
    }

    private void constructTotalProfit() {
        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        JPanel totalProfitContainer = new JPanel(new GridLayout(2, 1));

        JLabel totalProfitLabel = new JLabel("Total Profit");
        totalProfitLabel.setFont(new Font(
            FontManager.getRunescapeBoldFont().getName(), 
            FontManager.getRunescapeBoldFont().getStyle(), 
            16
        ));
        totalProfitLabel.setHorizontalAlignment(JLabel.CENTER);
        totalProfitLabel.setForeground(Color.WHITE);

        JLabel totalProfitValueLabel = new JLabel(Integer.toString(totalProfit));
        totalProfitValueLabel.setFont(new Font(
            FontManager.getRunescapeFont().getName(), 
            FontManager.getRunescapeFont().getStyle(), 
            24
        ));
        totalProfitValueLabel.setHorizontalAlignment(JLabel.CENTER);
        Color totalProfitColor = ColorScheme.GRAND_EXCHANGE_ALCH;
        totalProfitValueLabel.setForeground(totalProfitColor);

        totalProfitContainer.add(totalProfitLabel);
        totalProfitContainer.add(totalProfitValueLabel);
        totalProfitContainer.setBorder(UiUtilities.ITEM_INFO_BORDER);

        container.add(totalProfitContainer);
        container.setBorder(new EmptyBorder(0, 0, 3, 0));
        this.add(container, BorderLayout.WEST);
    }
}