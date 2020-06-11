package com.flipper.views.margins;

import java.util.List;
import java.util.ListIterator;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import java.awt.BorderLayout;

import com.flipper.models.Flip;

import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.ColorScheme;

public class MarginsPanel extends JPanel {
    private static final long serialVersionUID = -2680984300396244041L;
    
    private ItemManager itemManager;
    private int totalProfit;

    public MarginsPanel(ItemManager itemManager) {
        this.itemManager = itemManager;
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setBackground(ColorScheme.DARK_GRAY_COLOR);
    }

    public void rebuildPanel(List<Flip> margins) {
        SwingUtilities.invokeLater(() -> {
            this.removeAll();

            JPanel container = new JPanel();
            container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
            container.setBackground(ColorScheme.DARK_GRAY_COLOR);

            JScrollPane scrollPane = new JScrollPane(container);
            scrollPane.setBackground(ColorScheme.LIGHT_GRAY_COLOR);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

            // Reverse list to show newest first
            ListIterator<Flip> marginsIterator = margins.listIterator(margins.size());
            while(marginsIterator.hasPrevious()) {
                Flip margin = marginsIterator.previous();
                MarginPanel flipPanel = new MarginPanel(margin, itemManager);
                totalProfit += margin.getTotalProfit();
                container.add(flipPanel);
            }

            this.add(container, BorderLayout.WEST);
        });
    }
}