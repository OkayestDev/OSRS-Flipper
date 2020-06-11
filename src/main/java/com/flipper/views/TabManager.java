package com.flipper.views;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.flipper.views.buys.BuysPanel;
import com.flipper.views.components.Tab;
import com.flipper.views.flips.FlipsPanel;
import com.flipper.views.margins.MarginsPanel;
import com.flipper.views.sells.SellsPanel;

import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.materialtabs.MaterialTabGroup;

public class TabManager extends PluginPanel {
    private static final long serialVersionUID = -947343100446545237L;

	/**
     * This manages the tab navigation bar at the top of the panel. Once a tab is
     * selected, the corresponding panel will be displayed below along with
     * indication of what tab is selected.
     */
    public TabManager(
        BuysPanel buysPanel, 
        SellsPanel sellsPanel, 
        FlipsPanel flipsPanel, 
        MarginsPanel marginsPanel
    ) {
        JPanel display = new JPanel();
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(ColorScheme.DARKER_GRAY_COLOR.darker());
		header.setBorder(new EmptyBorder(5, 0, 0, 0));
		MaterialTabGroup tabGroup = new MaterialTabGroup(display);
        Tab buysTab = new Tab("Buys", tabGroup, buysPanel);
        Tab sellsTab = new Tab("Sells", tabGroup, sellsPanel);
        Tab flipsTab = new Tab("Flips", tabGroup, flipsPanel);
        Tab marginsTab = new Tab("Margins", tabGroup, marginsPanel);
        tabGroup.setBorder(new EmptyBorder(5, 0, 2, 0));
        tabGroup.addTab(buysTab);
        tabGroup.addTab(sellsTab);
        tabGroup.addTab(flipsTab);
        tabGroup.addTab(marginsTab);
		// Initialize with buys tab open.
		tabGroup.select(buysTab);
		header.add(tabGroup, BorderLayout.CENTER);
		add(header, BorderLayout.WEST);
        add(display, BorderLayout.CENTER);
    }
}
;