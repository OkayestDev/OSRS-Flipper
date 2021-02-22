package com.flipper.controllers;

import java.io.IOException;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;
import java.util.function.Consumer;

import javax.swing.SwingUtilities;

import com.flipper.helpers.GrandExchange;
import com.flipper.helpers.Persistor;
import com.flipper.helpers.UiUtilities;
import com.flipper.models.Transaction;
import com.flipper.views.sells.SellPage;
import com.flipper.views.sells.SellPanel;
import com.flipper.views.components.Pagination;

import lombok.Getter;
import lombok.Setter;
import net.runelite.api.GrandExchangeOffer;
import net.runelite.client.game.ItemManager;

public class SellsController extends TransactionController {
    @Getter
    @Setter
    private List<Transaction> sells;
    private SellPage sellPage;
    private ItemManager itemManager;
    private Pagination pagination;
    private Consumer<UUID> removeSellConsumer;

    public SellsController(ItemManager itemManager) throws IOException {
        this.itemManager = itemManager;
        this.removeSellConsumer = id -> this.removeSell(id);
        this.sellPage = new SellPage();
        Consumer<Object> renderItemCallback = (Object sell) -> {
            SellPanel sellPanel = new SellPanel((Transaction) sell, itemManager, this.removeSellConsumer);
            this.sellPage.addSellPanel(sellPanel);
        };
        Runnable buildViewCallback = () -> this.buildView();
        this.pagination = new Pagination(
            renderItemCallback,
            UiUtilities.ITEMS_PER_PAGE,
            buildViewCallback
        );
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

    public Transaction upsertSell(GrandExchangeOffer offer, int slot) {
        // Check to see if there is an incomplete transaction we can fill
        ListIterator<Transaction> sellsIterator = sells.listIterator(sells.size());
        while (sellsIterator.hasPrevious()) {
            Transaction sell = sellsIterator.previous();
            // Incomplete sell transaction found, update it
            if (GrandExchange.checkIsOfferPartOfTransaction(sell, offer, slot)) {
                Transaction updatedSell = sell.updateTransaction(offer);
                sellsIterator.set(updatedSell);
                this.buildView();
                return updatedSell;
            }
        }

        // It's a new sell, create one
        Transaction newSell = GrandExchange.createTransactionFromOffer(offer, itemManager, slot);
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
            this.sellPage.add(this.pagination.getComponent(this.sells));
            this.pagination.renderList(this.sells);
        });
    }
}