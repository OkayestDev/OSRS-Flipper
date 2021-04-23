package com.flipper.views.transactions;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;

import net.runelite.client.ui.ColorScheme;

public class TransactionPage extends JPanel {
    private JPanel container;

    public TransactionPage() {
        this.setLayout(new BorderLayout());
        this.setBackground(ColorScheme.DARK_GRAY_COLOR);
    }

    public void build() {
        JPanel scrollContainer = new JPanel();
        scrollContainer.setLayout(new BorderLayout());
        container = new JPanel();
        container.setBorder(new EmptyBorder(5, 0, 0, 0));
        container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
        container.setBackground(ColorScheme.DARK_GRAY_COLOR);

        JScrollPane scrollPane = new JScrollPane(scrollContainer);
        scrollPane.setBackground(ColorScheme.LIGHT_GRAY_COLOR);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        scrollContainer.add(container, BorderLayout.PAGE_START);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    public void addTransactionPanel(TransactionPanel transactionPanel) {
        container.add(transactionPanel, BorderLayout.CENTER);
        this.revalidate();
    }
}