package com.flipper.view.buys;

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

public class BuysPanel extends JPanel {
    FlipperPlugin plugin;
    @Setter
    List<Transaction> buys;

    JLabel buyPriceVal = new JLabel();
    JLabel sellPriceVal = new JLabel();
    JLabel profitEachVal = new JLabel();
    JLabel profitTotalVal = new JLabel();
    JLabel limitLabel = new JLabel();
    JLabel roiLabel = new JLabel();

    public BuysPanel(final FlipperPlugin plugin) {
        this.plugin = plugin;
    }
}