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
import com.flipper.views.components.Pagination;
import com.flipper.helpers.GrandExchange;
import com.flipper.helpers.Persistor;
import com.flipper.helpers.UiUtilities;

import lombok.Getter;
import lombok.Setter;

import net.runelite.api.GrandExchangeOffer;
import net.runelite.client.game.ItemManager;

public class BuysController extends TransactionController {
    @Getter
    @Setter
    private List<Transaction> buys;
    private BuyPage buyPage;
    private ItemManager itemManager;
    private Pagination pagination;
    private Consumer<UUID> removeBuyConsumer;

    public BuysController(ItemManager itemManager) throws IOException {
        this.itemManager = itemManager;
        this.removeBuyConsumer = id -> this.removeBuy(id);
        this.buyPage = new BuyPage();
        Consumer<Object> renderItemCallback = (Object buy) -> {
            BuyPanel buyPanel = new BuyPanel((Transaction) buy, itemManager, this.removeBuyConsumer);
            this.buyPage.addBuyPanel(buyPanel);
        };
        Runnable buildViewCallback = () -> this.buildView();
        this.pagination = new Pagination(
            renderItemCallback,
            UiUtilities.ITEMS_PER_PAGE,
            buildViewCallback
        );
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

    public void upsertBuy(GrandExchangeOffer offer, int slot) {
        // Check to see if there is an incomplete buy transaction we can full fill
        ListIterator<Transaction> buysIterator = buys.listIterator(buys.size());
        while (buysIterator.hasPrevious()) {
            Transaction buy = buysIterator.previous();
            // Incomplete buy transaction found, update it
            if (GrandExchange.checkIsOfferPartOfTransaction(buy, offer, slot)) {
                buysIterator.set(buy.updateTransaction(offer));
                this.buildView();
                return;
            }
        }

        // It's a new buy, create one
        Transaction newBuy = GrandExchange.createTransactionFromOffer(offer, itemManager, slot);
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
            this.buyPage.add(this.pagination.getComponent(this.buys));
            this.pagination.renderList(this.buys);
        });
    }
}