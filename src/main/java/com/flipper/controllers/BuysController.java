package com.flipper.controllers;

import java.io.IOException;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

import com.flipper.models.Transaction;
import com.flipper.views.buys.BuyPage;
import com.flipper.helpers.GrandExchange;
import com.flipper.helpers.TradePersister;

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

    public BuysController(ItemManager itemManager) throws IOException {
        this.itemManager = itemManager;
        this.buyPage = new BuyPage(itemManager);
        this.loadBuys();
    }

    public void addBuy(Transaction buy) {
        this.buys.add(buy);
        this.buyPage.rebuildPanel(buys);
    }

    public BuyPage getPanel() {
        return this.buyPage;
    }

    public void loadBuys() throws IOException {
        this.buys = TradePersister.loadBuys();
        this.buyPage.rebuildPanel(this.buys);
    }

    public void saveBuys() {
        TradePersister.saveBuys(buys);
    }

    public void createBuy(GrandExchangeOffer offer) {
        // Check to see if there is an incomplete buy transaction we can full fill
        ListIterator<Transaction> buysIterator = buys.listIterator(buys.size());
        while (buysIterator.hasPrevious()) {
            Transaction buy = buysIterator.previous();
            // Incomplete buy transaction found, update it
            if (GrandExchange.checkIsOfferPartOfTransaction(buy, offer)) {
                buysIterator.set(buy.updateTransaction(offer));
                this.buyPage.rebuildPanel(buys);
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
                this.buyPage.rebuildPanel(buys);
                return;
            }
        }
    }
}