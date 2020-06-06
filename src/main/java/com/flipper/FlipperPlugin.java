package com.flipper;

import com.flipper.helpers.GrandExchange;
import com.flipper.helpers.Log;
import com.flipper.helpers.TradePersister;
import com.flipper.controller.TabManagerController;
import com.flipper.controller.BuysController;
import com.flipper.controller.FlipsController;
import com.flipper.controller.SellsController;
import com.flipper.model.Transaction;
import com.google.inject.Provides;
import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Inject;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GrandExchangeOfferChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ClientShutdown;
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
	// Controllers
	private SellsController sellsController;
	private BuysController buysController;
	private FlipsController flipsController;
	private TabManagerController tabManagerController;

	@Override
	protected void startUp() throws Exception {
		Log.info("Flipper started!");

		clientThread.invokeLater(() -> {
			try {
				TradePersister.setUp();
				buysController = new BuysController(this);
				tabManagerController = new TabManagerController(
					this,
					executor,
					clientToolbar,
					buysController
				);
			} catch (IOException error) {
				Log.info("Flipper start up error " + error.toString());
			}
		});
	}

	@Override
	protected void shutDown() throws Exception {
		Log.info("Flipper stopped!");
		// flipperController.saveAll();
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
		// flipperController.saveAll();
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged) {
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN) {
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Flipper says " + config.greeting(), null);
		}
	}

	@Subscribe
	public void onGrandExchangeOfferChanged(GrandExchangeOfferChanged newOfferEvent) {
		Log.info("Grand Exchange Event: " + newOfferEvent.toString());
		Transaction transaction = GrandExchange.handleOnGrandExchangeOfferChanged(newOfferEvent);
		if (transaction != null) {
			if (transaction.isBuy()) {
				buysController.addBuy(transaction);
			} else {
				// Add sell
			}
		}
	}

	@Provides
	FlipperConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(FlipperConfig.class);
	}
}
