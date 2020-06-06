package com.flipper.view.sells;

import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.flipper.FlipperPlugin;
import com.flipper.model.Transaction;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.IconTextField;
import net.runelite.client.ui.components.PluginErrorPanel;

import lombok.Setter;

public class SellsPanel extends JPanel {
    FlipperPlugin plugin;
    JLabel sellPriceVal = new JLabel();
    JLabel profitEachVal = new JLabel();
    JLabel profitTotalVal = new JLabel();
    JLabel limitLabel = new JLabel();
    JLabel roiLabel = new JLabel();

    public SellsPanel(final FlipperPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Updates view with new sells list
     * 
     * @param sells
     */
    public void updatePanel(List<Transaction> sells) {

    }
}