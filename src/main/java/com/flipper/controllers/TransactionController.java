package com.flipper.controllers;

import java.io.IOException;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;
import java.util.function.Consumer;

import javax.swing.SwingUtilities;

import com.flipper.helpers.GrandExchange;
import com.flipper.helpers.UiUtilities;
import com.flipper.models.Transaction;
import com.flipper.views.transactions.TransactionPanel;
import com.flipper.views.transactions.TransactionPage;
import com.flipper.views.components.Pagination;

import lombok.Getter;
import lombok.Setter;
import net.runelite.api.GrandExchangeOffer;
import net.runelite.client.game.ItemManager;

public class TransactionController {
    @Getter
    @Setter
    protected List<Transaction> transactions;
    protected TransactionPage transactionPage;
    protected ItemManager itemManager;
    protected Pagination pagination;
    protected Consumer<UUID> removeTransactionConsumer;

    public TransactionController(String name, ItemManager itemManager) throws IOException {
        this.itemManager = itemManager;
        this.removeTransactionConsumer = id -> this.removeTransaction(id);
        this.transactionPage = new TransactionPage();
        Consumer<Object> renderItemCallback = (Object sell) -> {
            TransactionPanel transactionPanel = new TransactionPanel(
                name,
                (Transaction) sell,
                itemManager,
                this.removeTransactionConsumer
            );
            this.transactionPage.addTransactionPanel(transactionPanel);
        };
        Runnable buildViewCallback = () -> this.buildView();
        this.pagination = new Pagination(
            renderItemCallback,
            UiUtilities.ITEMS_PER_PAGE,
            buildViewCallback
        );
        this.loadTransactions();
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
        this.buildView();
    }

    public Transaction upsertTransaction(GrandExchangeOffer offer, int slot) {
        // Check to see if there is an incomplete transaction we can fill
        ListIterator<Transaction> transactionsIter = transactions.listIterator(transactions.size());
        while (transactionsIter.hasPrevious()) {
            Transaction transaction = transactionsIter.previous();
            // Incomplete sell transaction found, update it
            if (GrandExchange.checkIsOfferPartOfTransaction(transaction, offer, slot)) {
                Transaction updatedTransaction = transaction.updateTransaction(offer);
                transactionsIter.set(updatedTransaction);
                this.buildView();
                return updatedTransaction;
            }
        }

        // It's a new sell, create one
        Transaction newTransaction = GrandExchange.createTransactionFromOffer(offer, itemManager, slot);
        this.addTransaction(newTransaction);
        return newTransaction;
    }

    public void removeTransaction(UUID id) {
        ListIterator<Transaction> transactionIter = this.transactions.listIterator(this.transactions.size());
        while (transactionIter.hasPrevious()) {
            Transaction transaction = transactionIter.previous();
            if (transaction.id.equals(id)) {
                transactionIter.remove();
                this.buildView();
                return;
            }
        }
    }

    public void loadTransactions() throws IOException {};

    public void saveTransactions() {};

    public TransactionPage getPage() {
        return this.transactionPage;
    }

    public void buildView() {
        SwingUtilities.invokeLater(() -> {
            this.transactionPage.removeAll();
            this.transactionPage.build();
            this.transactionPage.add(this.pagination.getComponent(this.transactions));
            this.pagination.renderList(this.transactions);
        });
    }
}
