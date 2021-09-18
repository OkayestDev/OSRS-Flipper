package com.flipper.views.transactions;

import com.flipper.views.components.SearchBar;
import java.awt.BorderLayout;
import java.util.function.Consumer;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import net.runelite.client.ui.ColorScheme;

public class TransactionPage extends JPanel {

    private JPanel container;

    public TransactionPage(Consumer<String> onSearchTextChanged) {
        this.setLayout(new BorderLayout());
        this.setBackground(ColorScheme.DARK_GRAY_COLOR);
        this.addSearchText(onSearchTextChanged);
        this.build();
    }

    private void addSearchText(Consumer<String> onSearchTextChanged) {
        SearchBar searchBar = new SearchBar(onSearchTextChanged);
        this.add(searchBar, BorderLayout.NORTH);
    }

    public void resetContainer() {
        container.removeAll();
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
