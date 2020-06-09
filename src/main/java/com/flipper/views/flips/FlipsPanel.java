package com.flipper.views.flips;

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

public class FlipsPanel extends JPanel {
    private static final long serialVersionUID = -2680984300396244041L;
    
	private ItemManager itemManager;

    public FlipsPanel(ItemManager itemManager) {
        this.itemManager = itemManager;
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setBackground(ColorScheme.DARK_GRAY_COLOR);
    }

    public void rebuildPanel(List<Flip> flips) {
        SwingUtilities.invokeLater(() -> {
            this.removeAll();

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
                container.add(flipPanel);
            }

            this.add(container, BorderLayout.WEST);
        });
    }
}