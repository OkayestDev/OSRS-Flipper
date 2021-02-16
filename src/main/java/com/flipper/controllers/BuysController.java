package com.flipper.controllers;

import java.io.IOException;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;
import java.util.function.Consumer;

import javax.swing.SwingUtilities;

import com.flipper.models.Transaction;
import com.flipper.views.buys.BuyPage;
import com.flipper.views.buys.BuyPanel;
import com.flipper.helpers.GrandExchange;
import com.flipper.helpers.Persistor;

import lombok.Getter;
import lombok.Setter;
import net.runelite.api.GrandExchangeOffer;
import net.runelite.client.game.ItemManager;

/**
 * @todo weird edge cases where user logs in with complete GE trnasaction.
 *       Duplicates buy (i.e. 6800 cannonballs duplicated)
 */
public class BuysController {
    @Getter
    @Setter
    private List<Transaction> buys;
    private BuyPage buyPage;
    private ItemManager itemManager;
    private int page = 0;
    private final static int ITEMS_PER_PAGE = 15;

    private Consumer<UUID> removeBuyConsumer;

    public BuysController(ItemManager itemManager) throws IOException {
        this.itemManager = itemManager;
        this.removeBuyConsumer = id -> this.removeBuy(id);
        this.buyPage = new BuyPage();
        this.loadBuys();
    }

    public void addBuy(Transaction buy) {
        this.buys.add(buy);
        this.buildView();
    }

    public BuyPage getPanel() {
        return this.buyPage;
    }

    public void loadBuys() throws IOException {
        this.buys = Persistor.loadBuys();
        this.buildView();
    }

    public void saveBuys() {
        Persistor.saveBuys(buys);
    }

    public void createBuy(GrandExchangeOffer offer) {
        // Check to see if there is an incomplete buy transaction we can full fill
        ListIterator<Transaction> buysIterator = buys.listIterator(buys.size());
        while (buysIterator.hasPrevious()) {
            Transaction buy = buysIterator.previous();
            // Incomplete buy transaction found, update it
            if (GrandExchange.checkIsOfferPartOfTransaction(buy, offer)) {
                buysIterator.set(buy.updateTransaction(offer));
                this.buildView();
                return;
            }
        }

        // It's a new buy, create one
        Transaction newBuy = GrandExchange.createTransactionFromOffer(offer, itemManager);
        this.addBuy(newBuy);
    }

    public void removeBuy(UUID id) {
        ListIterator<Transaction> buysIterator = buys.listIterator(buys.size());
        while (buysIterator.hasPrevious()) {
            Transaction buy = buysIterator.previous();
            if (buy.id.equals(id)) {
                buysIterator.remove();
                this.buildView();
                return;
            }
        }
    }

    public void buildView() {
        SwingUtilities.invokeLater(() -> {
            this.buyPage.removeAll();
            this.buyPage.build();
            ListIterator<Transaction> buysIterator = buys.listIterator(buys.size());
            int currentRenderCount = 0;

            while (buysIterator.hasPrevious() && currentRenderCount < ITEMS_PER_PAGE) {
                Transaction buy = buysIterator.previous();
                BuyPanel buyPanel = new BuyPanel(buy, itemManager, this.removeBuyConsumer);
                this.buyPage.addBuyPanel(buyPanel);
                currentRenderCount++;
            }
        });
    }
}