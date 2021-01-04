package com.flipper.controllers;

import java.io.IOException;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;
import java.util.function.Consumer;

import javax.swing.SwingUtilities;

import com.flipper.helpers.GrandExchange;
import com.flipper.helpers.Persistor;
import com.flipper.models.Transaction;
import com.flipper.views.sells.SellPage;
import com.flipper.views.sells.SellPanel;

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

    private Consumer<UUID> removeSellConsumer;

    public SellsController(ItemManager itemManager) throws IOException {
        this.itemManager = itemManager;
        this.removeSellConsumer = id -> this.removeSell(id);
        this.sellPage = new SellPage();
        this.loadSells();
    }

    public void addSell(Transaction sell) {
        this.sells.add(sell);
        this.buildView();
    }

    public SellPage getPanel() {
        return this.sellPage;
    }

    public void loadSells() throws IOException {
        this.sells = Persistor.loadSells();
        this.buildView();
    }

    public void saveSells() {
        Persistor.saveSells(sells);
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
                this.buildView();
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
                this.buildView();
                return;
            }
        }
    }

    public void buildView() {
        SwingUtilities.invokeLater(() -> {
            this.sellPage.removeAll();
            this.sellPage.build();

            ListIterator<Transaction> sellsIterator = sells.listIterator(sells.size());

            while (sellsIterator.hasPrevious()) {
                Transaction sell = sellsIterator.previous();
                SellPanel sellPanel = new SellPanel(sell, itemManager, this.removeSellConsumer);
                this.sellPage.addSellPanel(sellPanel);
            }
        });
    }
}