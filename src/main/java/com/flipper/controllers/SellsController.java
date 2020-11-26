package com.flipper.controllers;

import java.io.IOException;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

import com.flipper.helpers.GrandExchange;
import com.flipper.helpers.TradePersister;
import com.flipper.models.Transaction;
import com.flipper.views.sells.SellPage;

import lombok.Getter;
import lombok.Setter;
import net.runelite.api.GrandExchangeOffer;
import net.runelite.client.game.ItemManager;

public class SellsController {
    @Getter
    @Setter
    private List<Transaction> sells;
    private SellPage sellPage;
    private ItemManager itemManager;

    public SellsController(ItemManager itemManager) throws IOException {
        this.itemManager = itemManager;
        this.sellPage = new SellPage(itemManager);
        this.loadSells();
    }

    public void addSell(Transaction sell) {
        this.sells.add(sell);
        this.sellPage.rebuildPanel(sells);
    }

    public SellPage getPanel() {
        return this.sellPage;
    }

    public void loadSells() throws IOException {
        this.sells = TradePersister.loadSells();
        this.sellPage.rebuildPanel(sells);
    }

    public void saveSells() {
        TradePersister.saveSells(sells);
    }

    public Transaction createSell(GrandExchangeOffer offer) {
        // Check to see if there is an incomplete transaction we can fill
        ListIterator<Transaction> sellsIterator = sells.listIterator(sells.size());
        while (sellsIterator.hasPrevious()) {
            Transaction sell = sellsIterator.previous();
            // Incomplete sell transaction found, update it
            if (GrandExchange.checkIsOfferPartOfTransaction(sell, offer)) {
                Transaction updatedSell = sell.updateTransaction(offer);
                sellsIterator.set(updatedSell);
                this.sellPage.rebuildPanel(sells);
                return updatedSell;
            }
        }

        // It's a new sell, create one
        Transaction newSell = GrandExchange.createTransactionFromOffer(offer, itemManager);
        this.addSell(newSell);
        return newSell;
    }

    public void removeSell(UUID id) {
        ListIterator<Transaction> sellsIterator = sells.listIterator(sells.size());
        while (sellsIterator.hasPrevious()) {
            Transaction sell = sellsIterator.previous();
            if (sell.id.equals(id)) {
                sellsIterator.remove();
                this.sellPage.rebuildPanel(sells);
                return;
            }
        }
    }
}