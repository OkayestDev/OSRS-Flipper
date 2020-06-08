package com.flipper.controllers;

import java.io.IOException;
import java.util.List;
import java.util.ListIterator;

import com.flipper.helpers.GrandExchange;
import com.flipper.helpers.TradePersister;
import com.flipper.models.Flip;
import com.flipper.models.Transaction;
import com.flipper.views.flips.FlipsPanel;

import net.runelite.client.game.ItemManager;

public class FlipsController {
    private ItemManager itemManager;
    private List<Flip> flips;
    private FlipsPanel flipsPanel;

    public FlipsController(ItemManager itemManager) throws IOException {
        this.itemManager = itemManager;
        this.flipsPanel = new FlipsPanel(itemManager);
        this.loadFlips(); 
    }

    public void addFlip(Flip flip) {
        this.flips.add(flip);
        this.flipsPanel.rebuildPanel(flips);
    }

    public FlipsPanel getPanel() {
        return this.flipsPanel;
    }

    private void loadFlips() throws IOException {
        this.flips = TradePersister.loadFlips();
    }

    /**
     * Potentially creates a flip if the sell is complete and has a corresponding buy
     * @param sell
     * @param buys
     */
    public void createFlip(Transaction sell, List<Transaction> buys) {
        if (sell.isComplete()) {
            return;
        }

        ListIterator<Transaction> buysIterator = buys.listIterator(buys.size());
        while (buysIterator.hasPrevious()) {
            Transaction buy = buysIterator.previous();

            if (GrandExchange.checkIsSellAFlipOfBuy(sell, buy)) {
                Flip flip = new Flip(buy, sell);
                this.addFlip(flip);
            }
        }
    }
}