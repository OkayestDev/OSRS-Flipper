package com.flipper.controllers;

import java.io.IOException;

import com.flipper.helpers.Persistor;

import net.runelite.client.game.ItemManager;

public class BuysController extends TransactionController {
    public BuysController(ItemManager itemManager) throws IOException {
        super("Buy", itemManager);
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