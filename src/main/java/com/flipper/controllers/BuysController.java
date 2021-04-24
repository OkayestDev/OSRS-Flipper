package com.flipper.controllers;

import java.io.IOException;
import java.util.function.Consumer;

import com.flipper.helpers.Persistor;
import com.flipper.models.Transaction;

import net.runelite.client.game.ItemManager;

public class BuysController extends TransactionController {
    public BuysController(ItemManager itemManager, Consumer<Transaction> highAlchCallback) throws IOException {
        super("Buy", itemManager);
        this.extraComponent = new JButton();
    }

    @Override
    public void loadTransactions() throws IOException {
        this.transactions = Persistor.loadBuys();
        this.buildView();
    }

    @Override
    public void saveTransactions() {
        Persistor.saveBuys(this.transactions);
    }
}