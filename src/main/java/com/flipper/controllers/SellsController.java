package com.flipper.controllers;

import java.io.IOException;

import com.flipper.helpers.Persistor;
import net.runelite.client.game.ItemManager;

public class SellsController extends TransactionController {
    public SellsController(ItemManager itemManager) throws IOException {
        super("Sell", itemManager);
    }

    @Override
    public void loadTransactions() throws IOException {
        this.transactions = Persistor.loadSells();
        this.buildView();
    }

    @Override
    public void saveTransactions() {
        Persistor.saveSells(this.transactions);
    }
}