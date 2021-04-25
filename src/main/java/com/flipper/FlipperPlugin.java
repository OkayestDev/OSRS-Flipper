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
import com.flipper.controllers.AlchsController;
import com.flipper.controllers.BuysController;
import com.flipper.controllers.FlipsController;
import com.flipper.controllers.LoginController;
import com.flipper.controllers.MarginsController;
import com.flipper.controllers.SellsController;
import com.flipper.helpers.GrandExchange;
import com.flipper.helpers.Log;
import com.flipper.helpers.Persistor;
import com.flipper.helpers.UiUtilities;
import com.flipper.models.Alch;
import com.flipper.models.Flip;
import com.flipper.models.Transaction;
import com.flipper.responses.LoginResponse;
import com.flipper.views.TabManager;
import com.google.inject.Provides;

import java.io.IOException;
import java.util.UUID;
import java.util.function.Consumer;

import javax.inject.Inject;
import javax.swing.SwingUtilities;

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
    @Inject
	private FlipperConfig config;
    // Controllers
    private BuysController buysController;
    private SellsController sellsController;
    private FlipsController flipsController;
    private MarginsController marginsController;
    private LoginController loginController;
    private AlchsController alchsController;
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
            Log.info("Flipper failed to start");
        }
    }

    private void setUpNavigationButton() {
        navButton = NavigationButton
            .builder()
            .tooltip("Flipper")
            .icon(
                ImageUtil.loadImageResource(
                    getClass(),
                    UiUtilities.flipperNavIcon
                )
            )
            .priority(4)
            .panel(tabManager).build();
        clientToolbar.addNavigation(navButton);
    }

    private void alchFromBuy(Transaction buy) {
        // Create alch from transaction
        Alch alch = new Alch(buy, itemManager);
        this.alchsController.addAlch(alch);
    }

    private void flipFromMargin(Flip margin) {
        this.flipsController.addFlip(margin);
        this.marginsController.removeMargin(margin.getId());
    }

    private void changeToLoggedInView() {
        SwingUtilities.invokeLater(() -> {
            try {
                Runnable changeToLoggedOutViewRunnable = () -> this.changeToLoggedOutView();
                alchsController = new AlchsController(
                    itemManager,
                    config
                );
                Consumer<Transaction> highAlchCallback = (buy) -> alchFromBuy(buy);
                Consumer<Flip> convertToFlipConsumer = (margin) -> flipFromMargin(margin);
                flipsController = new FlipsController(
                    itemManager, 
                    config
                );
                buysController = new BuysController(
                    itemManager, 
                    highAlchCallback,
                    config
                );
                sellsController = new SellsController(
                    itemManager, 
                    config
                );
                marginsController = new MarginsController(
                    itemManager,
                    config,
                    convertToFlipConsumer
                );
                this.tabManager.renderLoggedInView(
                    buysController.getPage(),
                    sellsController.getPage(),
                    flipsController.getPage(),
                    alchsController.getPage(),
                    marginsController.getPage(),
                    changeToLoggedOutViewRunnable
                );
            } catch (IOException e) {
                Log.info("Flipper: Failed to load required files");
            }
        });
    }

    private void changeToLoggedOutView() {
        try {
            Runnable changeToLoggedInViewRunnable = () -> changeToLoggedInView();
            Persistor.deleteLoginResponse();
            SwingUtilities.invokeLater(() -> {
                loginController = new LoginController(changeToLoggedInViewRunnable);
                this.tabManager.renderLoggedOutView(loginController.getPanel());
            });
        } catch (IOException e) {
            Log.info("Flipper: Failed to load required files");
        }
    }

    private void saveAll() {
        try {
            buysController.saveTransactions();
            sellsController.saveTransactions();
            marginsController.saveMargins();
            Persistor.saveLoginResponse(Api.loginResponse);
        } catch (Exception error) {
            Log.info("Failed to save Flipper files");
        }
    }

    @Override
    protected void shutDown() throws Exception {
        clientToolbar.removeNavigation(navButton);
        this.saveAll();
    }

    /**
     * Ensure we save transactions when the client is being shutdown. Prevents main
     * thread from continuing until transactions have been saved
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
                buysController.upsertTransaction(offer, slot);
            } else {
                Transaction sell = sellsController.upsertTransaction(offer, slot);
                Flip flip = flipsController.upsertFlip(sell, buysController.getTransactions());
                boolean isOfferComplete = GrandExchange.checkIsComplete(offerState);
                if (flip != null && flip.isMarginCheck() && isOfferComplete) {
                    // Remove margin from buy and sell list
                    buysController.removeTransaction(flip.getBuyId());
                    sellsController.removeTransaction(sell.id);
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
