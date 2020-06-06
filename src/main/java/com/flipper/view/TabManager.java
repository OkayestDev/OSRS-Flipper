package com.flipper.view;

import java.awt.BorderLayout;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import javax.inject.Inject;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.flipper.view.buys.BuysPanel;

import lombok.Getter;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.materialtabs.MaterialTab;
import net.runelite.client.ui.components.materialtabs.MaterialTabGroup;

public class TabManager extends PluginPanel {
    @Getter
    private JComboBox<String> viewSelector;

    /**
     * This manages the tab navigation bar at the top of the panel. Once a tab is
     * selected, the corresponding panel will be displayed below along with
     * indication of what tab is selected.
     */
    @Inject
    public TabManager(BuysPanel buysPanel) {
        JPanel display = new JPanel();
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(ColorScheme.DARKER_GRAY_COLOR.darker());
		header.setBorder(new EmptyBorder(5, 0, 0, 0));
		MaterialTabGroup tabGroup = new MaterialTabGroup(display);
        MaterialTab buysTab = new MaterialTab("Buys", tabGroup, buysPanel);
        MaterialTab sellsTab = new MaterialTab("Sells", tabGroup, new JPanel());
        MaterialTab flipsTab = new MaterialTab("Flips", tabGroup, new JPanel());
		tabGroup.setBorder(new EmptyBorder(5, 0, 2, 0));
        tabGroup.addTab(buysTab);
        tabGroup.addTab(sellsTab);
		tabGroup.addTab(flipsTab);
		// Initialize with buys tab open.
		tabGroup.select(buysTab);
		header.add(tabGroup, BorderLayout.CENTER);
		add(header, BorderLayout.NORTH);
        add(display, BorderLayout.CENTER);
    }

    public Set<String> getViewSelectorItems() {
        Set<String> items = new HashSet<>();
        for (int i = 0; i < viewSelector.getItemCount(); i++) {
            items.add(viewSelector.getItemAt(i));
        }
        return items;
    }
}
