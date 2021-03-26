package com.flipper.views.margins;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;

import net.runelite.client.ui.ColorScheme;

public class MarginPage extends JPanel {
    private JPanel container;

    public MarginPage() {
        this.setLayout(new BorderLayout());
        this.setBackground(ColorScheme.DARK_GRAY_COLOR);
        this.setBorder(new EmptyBorder(5, 0, 0, 0));
    }

    public void addMarginPanel(MarginPanel marginPanel) {
        container.add(marginPanel, BorderLayout.CENTER);
    }

    public void build() {
        this.removeAll();

        container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
        container.setBackground(ColorScheme.DARK_GRAY_COLOR);

        JScrollPane scrollPane = new JScrollPane(container);
        scrollPane.setBackground(ColorScheme.LIGHT_GRAY_COLOR);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        this.add(scrollPane, BorderLayout.CENTER);
    }
}