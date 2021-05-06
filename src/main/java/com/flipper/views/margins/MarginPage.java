package com.flipper.views.margins;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import com.flipper.views.components.SearchBar;

import java.awt.BorderLayout;
import java.util.function.Consumer;

import net.runelite.client.ui.ColorScheme;

public class MarginPage extends JPanel {
    private JPanel container;

    public MarginPage(Consumer<String> onSearchTextChangedCallback) {
        this.setLayout(new BorderLayout());
        this.setBackground(ColorScheme.DARK_GRAY_COLOR);
        this.addSearchBar(onSearchTextChangedCallback);
        this.build();
    }

    private void addSearchBar(Consumer<String> onSearchTextChangedCallback) {
        SearchBar searchBar = new SearchBar(onSearchTextChangedCallback);
        this.add(searchBar, BorderLayout.NORTH);
    }

    public void resetContainer() {
        this.container.removeAll();
    }

    public void addMarginPanel(MarginPanel marginPanel) {
        container.add(marginPanel, BorderLayout.CENTER);
        this.revalidate();
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
}