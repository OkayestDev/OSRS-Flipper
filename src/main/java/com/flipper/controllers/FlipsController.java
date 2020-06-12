package com.flipper.controllers;

import java.io.IOException;
import java.util.List;
import java.util.ListIterator;

import com.flipper.helpers.GrandExchange;
import com.flipper.helpers.TradePersister;
import com.flipper.models.Flip;
import com.flipper.models.Transaction;
import com.flipper.views.flips.FlipsPanel;

import lombok.Getter;
import lombok.Setter;
import net.runelite.client.game.ItemManager;

public class FlipsController {
    @Getter
    @Setter
    private List<Flip> flips;
    private FlipsPanel flipsPanel;

    public FlipsController(ItemManager itemManager) throws IOException {
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
        this.flipsPanel.rebuildPanel(flips);
    }

    public void saveFlips() {
        TradePersister.saveFlips(flips);
    }

    private Flip updateFlip(Transaction sell, Transaction buy, Flip flip) {
        Flip updatedFlip = flip.updateFlip(sell, buy);
        this.flipsPanel.rebuildPanel(flips);
        return updatedFlip;
    }

    /**
     * Potentially creates a flip if the sell is complete and has a corresponding buy
     * @todo Bugfix: debug "Granite Gloves glitch": Bought 8, Started Sell, Sold 1 (created flip), Sold 7 more but didn't update flip
     * Seems to be bug with logging on and receiving updated sell
     * @param sell
     * @param buys
     */
    public Flip createFlip(Transaction sell, List<Transaction> buys) {
        ListIterator<Transaction> buysIterator = buys.listIterator(buys.size());
        // If sell has already been flipped, look for it's corresponding buy and update the flip
        if (sell.isFlipped()) {
            ListIterator<Flip> flipsIterator = flips.listIterator(flips.size());
            while (flipsIterator.hasPrevious()) {
                Flip flip = flipsIterator.previous();
                if (flip.getSell().id.equals(sell.id)) {
                    // Now find the corresponding buy
                    while (buysIterator.hasPrevious()) {
                        Transaction buy = buysIterator.previous();
                        if (buy.id.equals(flip.getBuy().id)) {
                            Flip updatedFlip = updateFlip(sell, buy, flip);
                            flipsIterator.set(updatedFlip);
                            if (updatedFlip.isMarginCheck()) {
                                flipsIterator.remove();
                            } else {
                                flipsIterator.set(updatedFlip);
                            }
                            return updatedFlip;
                        }
                    }
                }
            }
        } else {
            // Attempt to match sell to a buy
            while (buysIterator.hasPrevious()) {
                Transaction buy = buysIterator.previous();
                if (GrandExchange.checkIsSellAFlipOfBuy(sell, buy)) {
                    Flip flip = new Flip(buy, sell);
                    buy.setIsFlipped(true);
                    sell.setIsFlipped(true);
                    if (!flip.isMarginCheck()) {
                        this.addFlip(flip);
                    }
                    return flip;
                }
            }
        }

		return null;
    }
}