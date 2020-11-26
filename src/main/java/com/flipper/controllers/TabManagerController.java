package com.flipper.controllers;

import net.runelite.client.util.ImageUtil;
import net.runelite.client.ui.NavigationButton;

import com.flipper.helpers.UiUtilities;
import com.flipper.views.TabManager;
import com.flipper.views.buys.BuyPage;
import com.flipper.views.flips.FlipPage;
import com.flipper.views.margins.MarginPage;
import com.flipper.views.sells.SellPage;

import net.runelite.client.ui.ClientToolbar;

public class TabManagerController {
    private TabManager tabManager;
    private NavigationButton navButton;
    private ClientToolbar clientToolbar;

    public TabManagerController(ClientToolbar clientToolbar, BuyPage buyPage, SellPage sellPage, FlipPage flipPage,
            MarginPage marginPage) {
        this.clientToolbar = clientToolbar;
        tabManager = new TabManager(buyPage, sellPage, flipPage, marginPage);
        setUpNavigationButton();
    }

    private void setUpNavigationButton() {
        navButton = NavigationButton.builder().tooltip("Flipper")
                .icon(ImageUtil.getResourceStreamFromClass(getClass(), UiUtilities.flipperNavIcon)).priority(4)
                .panel(tabManager).build();
        clientToolbar.addNavigation(navButton);
    }
}