package com.flipper;

import com.flipper.helpers.GrandExchange;
import com.flipper.helpers.Log;
import com.flipper.helpers.TradePersister;
import com.flipper.controller.TabManagerController;
import com.flipper.controller.BuysController;
import com.flipper.controller.SellsController;
import com.google.inject.Provides;
import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GrandExchangeOffer;
import net.runelite.api.GrandExchangeOfferState;
import net.runelite.api.events.GrandExchangeOfferChanged;
import net.runelite.client.callback.ClientThread;
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
    private Client client;
    @Inject
    private FlipperConfig config;
    @Inject
    private ScheduledExecutorService executor;
    @Inject
    private ClientThread clientThread;
    @Inject
    private ClientToolbar clientToolbar;
    @Inject
    private ItemManager itemManager;
    // Controllers
    private BuysController buysController;
    private SellsController sellsController;
    private TabManagerController tabManagerController;

    /** @todo figure how to run panel updates on dispatch thread */
    @Override
    protected void startUp() throws Exception {
        TradePersister.setUp();
        buysController = new BuysController(itemManager);
        sellsController = new SellsController(itemManager);
        tabManagerController = new TabManagerController(
            clientToolbar,
            buysController.getPanel(),
            sellsController.getPanel()
        );
    }

    private void saveAll() {
        buysController.saveBuys();
        sellsController.saveSells();
    }

    @Override
    protected void shutDown() throws Exception {
        Log.info("Flipper stopped!");
        this.saveAll();
    }

    /**
     * Ensure we save transactions when the client is being shutdown. Prevents main
     * thread from continue until transactions have been saved
     *
     * @param clientShutdownEvent even that we receive when the client is shutting
     *                            down
     * @todo implement
     */
    @Subscribe(priority = 101)
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
                sellsController.createSell(offer);
            }
        }
    }

    @Provides
    FlipperConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(FlipperConfig.class);
    }
}
