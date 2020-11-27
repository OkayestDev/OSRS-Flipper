package com.flipper.views.margins;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;

import net.runelite.client.ui.ColorScheme;

public class MarginPage extends JPanel {
    private static final long serialVersionUID = -2680984300396244041L;

    private JPanel container;

    public MarginPage() {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setBackground(ColorScheme.DARK_GRAY_COLOR);
    }

    public void addMarginPanel(MarginPanel marginPanel) {
        container.add(marginPanel);
    }

    public void build() {
        this.removeAll();

        container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
        container.setBackground(ColorScheme.DARK_GRAY_COLOR);

        JScrollPane scrollPane = new JScrollPane(container);
        scrollPane.setBackground(ColorScheme.LIGHT_GRAY_COLOR);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        this.add(container, BorderLayout.WEST);
    }
}