package com.flipper.controllers;

import com.flipper.FlipperConfig;
import com.flipper.helpers.Persistor;
import java.io.IOException;
import net.runelite.client.game.ItemManager;

public class SellsController extends TransactionController {

    public SellsController(ItemManager itemManager, FlipperConfig config) throws IOException {
        super("Sell", itemManager, config.isPromptDeleteSell());
    }

    @Override
    public void loadTransactions() throws IOException {
        this.transactions = Persistor.loadSells();
        this.filteredTransactions = this.transactions;
        this.buildView();
    }

    @Override
    public void saveTransactions() {
        Persistor.saveSells(this.transactions);
    }
}
