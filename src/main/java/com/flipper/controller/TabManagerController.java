package com.flipper.controller;

import com.flipper.FlipperPlugin;
import com.flipper.view.buys.BuysPanel;
import com.flipper.view.sells.SellsPanel;
import com.flipper.view.flips.FlipsPanel;
import com.flipper.view.TabManager;
import java.util.concurrent.ScheduledExecutorService;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.ClientToolbar;

public class TabManagerController {
    private FlipperPlugin flipperPlugin;
    private TabManager tabManager;
    private NavigationButton navButton;
    private ScheduledExecutorService executor;
    private ClientToolbar clientToolbar;

    private BuysController buysController;

    public TabManagerController(
        FlipperPlugin flipperPlugin, 
        ScheduledExecutorService executor,
        ClientToolbar clientToolbar,
        BuysController buysController
    ) {
        this.buysController = buysController;
        this.flipperPlugin = flipperPlugin;
        this.executor = executor;
        this.clientToolbar = clientToolbar;              
        tabManager = new TabManager(this::changeView, this.buysController.getPanel());
        setUpNavigationButton();
    }

    private void setUpNavigationButton() {
        navButton = NavigationButton
                .builder()
                .tooltip("Flipper")
                .icon(ImageUtil.getResourceStreamFromClass(getClass(), "/flipper_nav_button.png")).priority(1)
                .panel(tabManager)
                .build();
        clientToolbar.addNavigation(navButton);
    }

    public void changeView(String selectedName) {

    }
}