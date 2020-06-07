package com.flipper.controller;

import java.io.IOException;
import java.util.List;

import com.flipper.model.Transaction;
import com.flipper.view.buys.BuysPanel;
import com.flipper.helpers.TradePersister;

import lombok.Getter;
import net.runelite.client.game.ItemManager;

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
    public BuysController(ItemManager itemManager) throws IOException {
        this.buysPanel = new BuysPanel(itemManager);
        this.loadBuys();
    }

    public void addBuy(Transaction buy) {
        this.buys.add(buy);
        this.buysPanel.rebuildPanel(buys);
    }

    public BuysPanel getPanel() {
        return this.buysPanel;
    }

    public void loadBuys() throws IOException {
        this.buys = TradePersister.loadBuys();
        this.buysPanel.rebuildPanel(this.buys);
    }

    public void saveBuys() {
        TradePersister.saveBuys(buys);
    }
}