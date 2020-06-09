package com.flipper.views;

import java.awt.BorderLayout;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.flipper.views.buys.BuysPanel;
import com.flipper.views.flips.FlipsPanel;
import com.flipper.views.sells.SellsPanel;

import lombok.Getter;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.materialtabs.MaterialTab;
import net.runelite.client.ui.components.materialtabs.MaterialTabGroup;

public class TabManager extends PluginPanel {
    /**
     * This manages the tab navigation bar at the top of the panel. Once a tab is
     * selected, the corresponding panel will be displayed below along with
     * indication of what tab is selected.
     */
    public TabManager(BuysPanel buysPanel, SellsPanel sellsPanel, FlipsPanel flipsPanel) {
        JPanel display = new JPanel();
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(ColorScheme.DARKER_GRAY_COLOR.darker());
		header.setBorder(new EmptyBorder(5, 0, 0, 0));
		MaterialTabGroup tabGroup = new MaterialTabGroup(display);
        MaterialTab buysTab = new MaterialTab("Buys", tabGroup, buysPanel);
        MaterialTab sellsTab = new MaterialTab("Sells", tabGroup, sellsPanel);
        MaterialTab flipsTab = new MaterialTab("Flips", tabGroup, flipsPanel);
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
}
