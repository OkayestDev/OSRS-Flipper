/*
 * Copyright (c) 2020, Kyle Richardson <https://github.com/Sir-Kyle-Richardson>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.flipper;

import com.flipper.api.Api;
import com.flipper.controllers.BuysController;
import com.flipper.controllers.FlipsController;
import com.flipper.controllers.LoginController;
import com.flipper.controllers.MarginsController;
import com.flipper.controllers.SellsController;
import com.flipper.helpers.GrandExchange;
import com.flipper.helpers.Persistor;
import com.flipper.helpers.UiUtilities;
import com.flipper.models.Flip;
import com.flipper.models.Transaction;
import com.flipper.responses.LoginResponse;
import com.flipper.views.TabManager;
import com.google.inject.Provides;

import java.io.IOException;
import java.util.UUID;

import javax.inject.Inject;

import net.runelite.api.GrandExchangeOffer;
import net.runelite.api.GrandExchangeOfferState;
import net.runelite.api.events.GrandExchangeOfferChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ClientShutdown;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;


@PluginDescriptor(name = "Flipper")
public class FlipperPlugin extends Plugin {
    // Injects
    @Inject
    private ClientToolbar clientToolbar;
    @Inject
    private ItemManager itemManager;
    // Controllers
    private BuysController buysController;
    private SellsController sellsController;
    private FlipsController flipsController;
    private MarginsController marginsController;
    private LoginController loginController;
    // Views
    private NavigationButton navButton;
    private TabManager tabManager;

    @Override
    protected void startUp() throws Exception {
        try {
            Persistor.setUp();
            LoginResponse loginResponse = Persistor.loadLoginResponse();
            Boolean isLoggedIn = loginResponse != null;
            this.tabManager = new TabManager();
            this.setUpNavigationButton();

            if (isLoggedIn) {
                this.changeToLoggedInView();
            } else {
                this.changeToLoggedOutView();
            }
        } catch (Exception e) {
            System.out.println("Flipper failed to start");
        }
    }

    private void setUpNavigationButton() {
        navButton = NavigationButton
            .builder()
            .tooltip("Flipper")
            .icon(
                ImageUtil.getResourceStreamFromClass(
                    getClass(), 
                    UiUtilities.flipperNavIcon  
                )
            )
            .priority(4)
            .panel(tabManager).build();
        clientToolbar.addNavigation(navButton);
    }

    private void changeToLoggedInView() {
        try {
            Runnable changeToLoggedOutViewRunnable = () -> this.changeToLoggedOutView();
            flipsController = new FlipsController(itemManager);
            buysController = new BuysController(itemManager);
            sellsController = new SellsController(itemManager);
            marginsController = new MarginsController(itemManager);
            this.tabManager.renderLoggedInView(
                buysController.getPanel(),
                sellsController.getPanel(),
                flipsController.getPanel(),
                marginsController.getPanel(),
                changeToLoggedOutViewRunnable
            );
        } catch (IOException e) {
            System.out.println("Flipper: Failed to load required files");
        }
    }

    private void changeToLoggedOutView() {
        try {
            Runnable changeToLoggedInViewRunnable = () -> changeToLoggedInView();
            Persistor.deleteLoginResponse();
            loginController = new LoginController(changeToLoggedInViewRunnable);
            this.tabManager.renderLoggedOutView(loginController.getPanel());
        } catch (IOException e) {
            System.out.println("Flipper: Failed to load required files");
        }
    }

    private void saveAll() throws IOException {
        buysController.saveBuys();
        sellsController.saveSells();
        marginsController.saveMargins();
        Persistor.saveLoginResponse(Api.loginResponse);
    }

    @Override
    protected void shutDown() throws Exception {
        this.saveAll();
    }

    /**
     * Ensure we save transactions when the client is being shutdown. Prevents main
     * thread from continue until transactions have been saved
     *
     * @param clientShutdownEvent event that we receive when the client is shutting
     *                            down
     */
    @Subscribe
    public void onClientShutdown(ClientShutdown clientShutdownEvent) throws IOException {
        this.saveAll();
    }

    @Subscribe
    public void onGrandExchangeOfferChanged(GrandExchangeOfferChanged newOfferEvent) {
        int slot = newOfferEvent.getSlot();
        GrandExchangeOffer offer = newOfferEvent.getOffer();
        GrandExchangeOfferState offerState = offer.getState();
        int quantitySold = offer.getQuantitySold();
        // Ignore empty state event offers or offers that haven't bought/sold any
        if (offerState != GrandExchangeOfferState.EMPTY && quantitySold != 0) {
            boolean isBuy = GrandExchange.checkIsBuy(offerState);
            if (isBuy) {
                buysController.upsertBuy(offer, slot);
            } else {
                Transaction sell = sellsController.upsertSell(offer, slot);
                Flip flip = flipsController.upsertFlip(sell, buysController.getBuys());
                boolean isOfferComplete = GrandExchange.checkIsComplete(offerState);
                if (flip != null && flip.isMarginCheck() && isOfferComplete) {
                    // Remove margin from buy and sell list
                    buysController.removeBuy(flip.getBuyId());
                    sellsController.removeSell(sell.id);
                    flip.id = UUID.randomUUID();
                    flip.sellPrice = sell.getPricePer();
                    marginsController.addMargin(flip);
                }
            }
        }
    }

    @Provides
    FlipperConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(FlipperConfig.class);
    }
}
