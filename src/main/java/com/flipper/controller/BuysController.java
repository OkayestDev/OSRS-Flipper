package com.flipper.controller;

import java.io.IOException;
import java.util.List;

import com.flipper.FlipperPlugin;
import com.flipper.model.Transaction;
import com.flipper.view.buys.BuysPanel;
import com.flipper.helpers.TradePersister;

import lombok.Getter;

public class BuysController {
    @Getter
    private List<Transaction> buys;
    private BuysPanel buysPanel;

    /**
     * Todo figure if reference to plugin is even required?
     * @param plugin
     * @param TradePersister
     * @throws IOException
     */
    public BuysController(FlipperPlugin plugin) throws IOException {
        this.buys = TradePersister.loadBuys();
        this.buysPanel = new BuysPanel(plugin);
    }

    public void addBuy(Transaction buy) {
        this.buys.add(buy);
        // Update panel here
        this.buysPanel.updatePanel(buys);
    }

    public BuysPanel getPanel() {
        return this.buysPanel;
    }

    public void saveBuys() {
        TradePersister.saveBuys(buys);
    }
}