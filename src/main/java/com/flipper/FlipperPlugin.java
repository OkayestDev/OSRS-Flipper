package com.flipper;

import com.flipper.controller.GrandExchangeController;
import com.flipper.controller.Log;
import com.flipper.controller.FlipperController;
import com.flipper.model.Transaction;
import com.flipper.view.buys.BuysPanel;
import com.flipper.view.FlipperPanel;
import com.flipper.view.TabManager;
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
import net.runelite.client.util.ImageUtil;
import net.runelite.client.ui.NavigationButton;
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

	// VIEWS
	private BuysPanel buysPanel;
	private FlipperPanel flipperPanel;
	private TabManager tabManager;
	private NavigationButton navButton;

	// Controllers
	private FlipperController flipperController;

	@Override
	protected void startUp() throws Exception {
		Log.info("Flipper started!");

		setUpUI();

		clientThread.invokeLater(() -> {
			try {
				flipperController = new FlipperController();
				buysPanel.setBuys(flipperController.getBuys());
			} catch (IOException error) {
				Log.info("Flipper start up error " + error.toString());
			}
		});
	}

	private void setUpUI() {
		flipperPanel = new FlipperPanel(this, executor);
		navButton = NavigationButton.builder().tooltip("Flipper")
				.icon(ImageUtil.getResourceStreamFromClass(getClass(), "/flipper_nav_button.png")).priority(1)
				.panel(tabManager).build();

		buysPanel = new BuysPanel(this);
		clientToolbar.addNavigation(navButton);
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
	 * @todo implement
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
