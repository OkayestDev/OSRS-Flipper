package com.flipper.views;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import com.flipper.views.components.LoggedInUser;
import com.flipper.views.components.Tab;
import com.flipper.views.flips.FlipPage;
import com.flipper.views.login.LoginPage;
import com.flipper.views.margins.MarginPage;
import com.flipper.views.transactions.TransactionPage;

import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.materialtabs.MaterialTabGroup;

public class TabManager extends PluginPanel {
    public TabManager() {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    }

    public void renderLoggedInView(
        TransactionPage buyPage, 
        TransactionPage sellPage, 
        FlipPage flipPage, 
        MarginPage marginPage, 
        Runnable changeToLoggedOutView
    ) {
        SwingUtilities.invokeLater(() -> {
            this.removeAll();
            LoggedInUser loggedInUser = new LoggedInUser(changeToLoggedOutView);
            this.add(loggedInUser);
            JPanel display = new JPanel();
            JPanel header = new JPanel(new BorderLayout());
            header.setBackground(ColorScheme.DARKER_GRAY_COLOR.darker());
            header.setBorder(new EmptyBorder(5, 0, 0, 0));
            MaterialTabGroup tabGroup = new MaterialTabGroup(display);
            Tab buysTab = new Tab("Buys", tabGroup, buyPage);
            Tab sellsTab = new Tab("Sells", tabGroup, sellPage);
            Tab flipsTab = new Tab("Flips", tabGroup, flipPage);
            Tab marginsTab = new Tab("Margins", tabGroup, marginPage);
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
            this.repaint();
        });
    }

    public void renderLoggedOutView(LoginPage loginPage) {
        SwingUtilities.invokeLater(() -> {
            this.removeAll();
            add(loginPage);
            this.revalidate();
        });
    }
};