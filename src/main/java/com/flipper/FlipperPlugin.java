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

import com.flipper.controllers.BuysController;
import com.flipper.controllers.FlipsController;
import com.flipper.controllers.MarginsController;
import com.flipper.controllers.SellsController;
import com.flipper.controllers.TabManagerController;
import com.flipper.helpers.GrandExchange;
import com.flipper.helpers.TradePersister;
import com.flipper.models.Flip;
import com.flipper.models.Transaction;
import com.google.inject.Provides;
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

    @Override
    protected void startUp() throws Exception {
        TradePersister.setUp();
        buysController = new BuysController(itemManager);
        sellsController = new SellsController(itemManager);
        flipsController = new FlipsController(itemManager);
        marginsController = new MarginsController(itemManager);
        new TabManagerController(clientToolbar, buysController.getPanel(), sellsController.getPanel(),
                flipsController.getPanel(), marginsController.getPanel());
    }

    private void saveAll() {
        buysController.saveBuys();
        sellsController.saveSells();
        flipsController.saveFlips();
        marginsController.saveMargins();
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
    public void onClientShutdown(ClientShutdown clientShutdownEvent) {
        this.saveAll();
    }

    @Subscribe
    public void onGrandExchangeOfferChanged(GrandExchangeOfferChanged newOfferEvent) {
        GrandExchangeOffer offer = newOfferEvent.getOffer();
        // Ignore empty state event offers or offers that haven't bought/sold any
        if (offer.getState() != GrandExchangeOfferState.EMPTY && offer.getQuantitySold() != 0) {
            boolean isBuy = GrandExchange.checkIsBuy(offer.getState());
            if (isBuy) {
                buysController.createBuy(offer);
            } else {
                Transaction sell = sellsController.createSell(offer);
                Flip flip = flipsController.createFlip(sell, buysController.getBuys());
                if (flip != null && flip.isMarginCheck()) {
                    // Remove buy and sell from buy and sell lists since they're part of a margin
                    // check
                    buysController.removeBuy(flip.getBuy().id);
                    sellsController.removeSell(sell.id);
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
