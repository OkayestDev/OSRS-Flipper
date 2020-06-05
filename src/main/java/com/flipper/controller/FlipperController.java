package com.flipper.controller;

import com.flipper.model.Flip;
import com.flipper.model.Transaction;

import lombok.Getter;

import java.io.IOException;
import java.util.List;

public class FlipperController {
    @Getter
    private List<Flip> flips;
    @Getter
    private List<Transaction> buys;
    @Getter
    private List<Transaction> sells;
    private TradePersisterController tradePersisterController;

    /**
     * Instantiate tradePersistorController Load flips, sells, and buys
     */
    public FlipperController() throws IOException {
        tradePersisterController = new TradePersisterController();
        flips = tradePersisterController.loadFlips();
        buys = tradePersisterController.loadBuys();
        sells = tradePersisterController.loadSells();
    }

    public void addTransaction(Transaction newTransaction) {
        if (newTransaction.isBuy()) {
            buys.add(newTransaction);
        } else {
            sells.add(newTransaction);
            // see if we need to create a new flip here?
        }
    }

    public void saveAll() {
        tradePersisterController.saveTransactions(buys, sells, flips);
    }
}
