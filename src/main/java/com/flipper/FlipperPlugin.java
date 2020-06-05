package com.flipper;

import com.flipper.controller.GrandExchangeController;
import com.flipper.controller.Log;
import com.flipper.controller.TradePersisterController;
import com.flipper.controller.FlipperController;
import com.flipper.model.Transaction;

import com.google.inject.Provides;

import java.io.IOException;

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

@PluginDescriptor(name = "Flipper")
public class FlipperPlugin extends Plugin {
	@Inject
	private Client client;
	@Inject
	private FlipperConfig config;
	@Inject
	private ClientThread clientThread;

	private FlipperController flipperController;

	@Override
	protected void startUp() throws Exception {
		Log.info("Flipper started!");
		clientThread.invokeLater(() -> {
			try {
				flipperController = new FlipperController();
			} catch (IOException error) {
				Log.info("Flipper start up error " + error.toString());
			}
		});
	}

	@Override
	protected void shutDown() throws Exception {
		Log.info("Flipper stopped!");
		flipperController.saveAll();
	}

	/**
	 * Ensure we save transactions when the client is being shutdown. Prevents main
	 * thread from continue until transactions have been saved
	 *
	 * @param clientShutdownEvent even that we receive when the client is shutting
	 *                            down
	 */
	@Subscribe(priority = 101)
	public void onClientShutdown(ClientShutdown clientShutdownEvent) {
		// configManager.setConfiguration(CONFIG_GROUP, TIME_INTERVAL_CONFIG_KEY,
		// statPanel.getSelectedInterval());

		// timeUpdateFuture.cancel(true);

		// cacheUpdater.stop();

		// if (currentlyLoggedInAccount != null)
		// {
		// Log.info("Shutting down, saving trades!");
		// storeTrades(currentlyLoggedInAccount);
		// }
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
		Transaction transaction = GrandExchangeController.handleOnGrandExchangeOfferChanged(newOfferEvent);
		// Transaction created. Save to json
		if (transaction != null) {
			// boolean isSaved = tradePersisterController.saveTransactions();
			flipperController.addTransaction(transaction);
		}
	}

	@Provides
	FlipperConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(FlipperConfig.class);
	}
}
