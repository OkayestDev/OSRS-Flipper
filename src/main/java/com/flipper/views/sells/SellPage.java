package com.flipper.views.sells;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;

import net.runelite.client.ui.ColorScheme;

public class SellPage extends JPanel {
    private static final long serialVersionUID = 4764109045121749116L;

    private JPanel container;

    public SellPage() {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setBackground(ColorScheme.DARK_GRAY_COLOR);
    }

    public void build() {
        container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
        container.setBackground(ColorScheme.DARK_GRAY_COLOR);

        JScrollPane scrollPane = new JScrollPane(container);
        scrollPane.setBackground(ColorScheme.LIGHT_GRAY_COLOR);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        this.add(container, BorderLayout.WEST);
    }

    public void addSellPanel(SellPanel sellPanel) {
        container.add(sellPanel);
    }
}