package com.flipper.controllers;

import java.io.IOException;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

import com.flipper.helpers.GrandExchange;
import com.flipper.helpers.TradePersister;
import com.flipper.models.Flip;
import com.flipper.models.Transaction;
import com.flipper.views.flips.FlipPage;

import lombok.Getter;
import lombok.Setter;
import net.runelite.client.game.ItemManager;

public class FlipsController {
    @Getter
    @Setter
    private List<Flip> flips;
    private FlipPage flipPage;

    public FlipsController(ItemManager itemManager) throws IOException {
        this.flipPage = new FlipPage(itemManager);
        this.loadFlips();
    }

    public void addFlip(Flip flip) {
        this.flips.add(flip);
        this.flipPage.rebuildPanel(flips);
    }

    public boolean removeFlip(UUID flipId) {
        ListIterator<Flip> flipsIterator = this.flips.listIterator();

        while (flipsIterator.hasNext()) {
            Flip iterFlip = flipsIterator.next();

            if (iterFlip.getId().equals(flipId)) {
                flipsIterator.remove();
                this.flipPage.rebuildPanel(flips);
                return true;
            }
        }

        return false;
    }

    public FlipPage getPanel() {
        return this.flipPage;
    }

    private void loadFlips() throws IOException {
        this.flips = TradePersister.loadFlips();
        this.flipPage.rebuildPanel(flips);
    }

    public void saveFlips() {
        TradePersister.saveFlips(flips);
    }

    private Flip updateFlip(Transaction sell, Transaction buy, Flip flip) {
        Flip updatedFlip = flip.updateFlip(sell, buy);
        this.flipPage.rebuildPanel(flips);
        return updatedFlip;
    }

    /**
     * Potentially creates a flip if the sell is complete and has a corresponding
     * buy
     * 
     * @param sell
     * @param buys
     */
    public Flip createFlip(Transaction sell, List<Transaction> buys) {
        ListIterator<Transaction> buysIterator = buys.listIterator(buys.size());
        // If sell has already been flipped, look for it's corresponding buy and update
        // the flip
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