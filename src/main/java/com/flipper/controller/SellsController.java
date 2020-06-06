package com.flipper.controller;

import java.io.IOException;
import java.util.List;

import com.flipper.FlipperPlugin;
import com.flipper.model.Transaction;
import com.flipper.view.sells.SellsPanel;

import lombok.Getter;

public class SellsController {
    @Getter
    private List<Transaction> sells;
    private SellsPanel sellsPanel;

    public SellsController(FlipperPlugin plugin) throws IOException {
        // this.sells = TradePersister.loadSells();
        this.sellsPanel = new SellsPanel(plugin);
    }

    public void addsell(Transaction sell) {
        this.sells.add(sell);
        // Update panel here
        this.sellsPanel.updatePanel(sells);
    }
}