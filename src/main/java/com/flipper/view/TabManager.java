package com.flipper.view;

import com.flipper.view.FlipperPanel;
import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import javax.inject.Inject;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import lombok.Getter;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.ComboBoxListRenderer;
import net.runelite.client.ui.components.materialtabs.MaterialTab;
import net.runelite.client.ui.components.materialtabs.MaterialTabGroup;

public class TabManager extends PluginPanel {
    @Getter
    private JComboBox<String> viewSelector = new JComboBox();

    /**
     * This manages the tab navigation bar at the top of the panel. Once a tab is
     * selected, the corresponding panel will be displayed below along with
     * indication of what tab is selected.
     *
     * @param flippingPanel FlippingPanel represents the main tool of the plugin.
     * @param statPanel     StatPanel represents useful performance statistics to
     *                      the user.
     */
    @Inject
    public TabManager(Consumer<String> viewChangerMethod, FlipperPanel flipperPanel) {
        super(false);

        setLayout(new BorderLayout());
        setBackground(ColorScheme.DARKER_GRAY_COLOR.darker());

        viewSelector.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        viewSelector.setFocusable(false);
        viewSelector.setForeground(ColorScheme.GRAND_EXCHANGE_PRICE);
        viewSelector.setRenderer(new ComboBoxListRenderer());
        viewSelector.setToolTipText("Select which of your account's trades list you want to view");
        viewSelector.addItemListener(event -> {
            if (event.getStateChange() == ItemEvent.SELECTED) {

                String selectedDisplayName = (String) event.getItem();

                if (selectedDisplayName == null) {
                    return;
                } else {
                    viewChangerMethod.accept(selectedDisplayName);
                }
            }
        });

        JPanel display = new JPanel();
        // contains the tab group and the view selector combo box.
        JPanel header = new JPanel(new BorderLayout());

        header.setBackground(ColorScheme.DARKER_GRAY_COLOR.darker());
        header.setBorder(new EmptyBorder(5, 0, 0, 0));
        header.add(viewSelector, BorderLayout.NORTH);

        MaterialTabGroup tabGroup = new MaterialTabGroup(display);
        MaterialTab flipperTab = new MaterialTab("Flipping", tabGroup, flipperPanel);
        // MaterialTab statTab = new MaterialTab("Statistics", tabGroup, statPanel);

        tabGroup.setBorder(new EmptyBorder(5, 0, 2, 0));
        tabGroup.addTab(flipperTab);
        // tabGroup.addTab(statTab);

        // Initialize with flipping tab open.
        tabGroup.select(flipperTab);

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
