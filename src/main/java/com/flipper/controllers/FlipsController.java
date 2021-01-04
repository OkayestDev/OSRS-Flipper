package com.flipper.controllers;

import java.io.IOException;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;
import java.util.function.Consumer;

import javax.swing.SwingUtilities;

import com.flipper.helpers.GrandExchange;
import com.flipper.helpers.Persistor;
import com.flipper.models.Flip;
import com.flipper.models.Transaction;
import com.flipper.views.flips.FlipPage;
import com.flipper.views.flips.FlipPanel;

import lombok.Getter;
import lombok.Setter;
import net.runelite.client.game.ItemManager;

public class FlipsController {
    @Getter
    @Setter
    private List<Flip> flips;
    private FlipPage flipPage;
    private ItemManager itemManager;
    private Consumer<UUID> removeFlipConsumer;
    private int totalProfit = 0;

    public FlipsController(ItemManager itemManager) throws IOException {
        this.itemManager = itemManager;
        this.removeFlipConsumer = id -> this.removeFlip(id);
        this.flipPage = new FlipPage();
        this.loadFlips();
    }

    public void addFlip(Flip flip) {
        this.flips.add(flip);
        this.buildView();
    }

    public boolean removeFlip(UUID flipId) {
        ListIterator<Flip> flipsIterator = this.flips.listIterator();

        while (flipsIterator.hasNext()) {
            Flip iterFlip = flipsIterator.next();

            if (iterFlip.getId().equals(flipId)) {
                flipsIterator.remove();
                this.buildView();
                return true;
            }
        }

        return false;
    }

    public FlipPage getPanel() {
        return this.flipPage;
    }

    private void loadFlips() throws IOException {
        this.flips = Persistor.loadFlips();
        this.buildView();
    }

    private Flip updateFlip(Transaction sell, Transaction buy, Flip flip) {
        // Flip updatedFlip = flip.updateFlip(sell, buy);
        // this.buildView();
        // return updatedFlip;
    }

    /**
     * Potentially creates a flip if the sell is complete and has a corresponding
     * buy
     * 
     * @param sell
     * @param buys
     */
    public Flip createFlip(Transaction sell, List<Transaction> buys) {
        // ListIterator<Transaction> buysIterator = buys.listIterator(buys.size());
        // // If sell has already been flipped, look for it's corresponding buy and update
        // // the flip
        // if (sell.isFlipped()) {
        //     ListIterator<Flip> flipsIterator = flips.listIterator(flips.size());
        //     while (flipsIterator.hasPrevious()) {
        //         Flip flip = flipsIterator.previous();
        //         if (flip.getSell().id.equals(sell.id)) {
        //             // Now find the corresponding buy
        //             while (buysIterator.hasPrevious()) {
        //                 Transaction buy = buysIterator.previous();
        //                 if (buy.id.equals(flip.getBuy().id)) {
        //                     Flip updatedFlip = updateFlip(sell, buy, flip);
        //                     flipsIterator.set(updatedFlip);
        //                     if (updatedFlip.isMarginCheck()) {
        //                         flipsIterator.remove();
        //                     } else {
        //                         flipsIterator.set(updatedFlip);
        //                     }
        //                     return updatedFlip;
        //                 }
        //             }
        //         }
        //     }
        // } else {
        //     // Attempt to match sell to a buy
        //     while (buysIterator.hasPrevious()) {
        //         Transaction buy = buysIterator.previous();
        //         if (GrandExchange.checkIsSellAFlipOfBuy(sell, buy)) {
        //             Flip flip = new Flip(buy, sell);
        //             buy.setIsFlipped(true);
        //             sell.setIsFlipped(true);
        //             if (!flip.isMarginCheck()) {
        //                 this.addFlip(flip);
        //             }
        //             return flip;
        //         }
        //     }
        // }

        // return null;
    }

    public void buildView() {
        SwingUtilities.invokeLater(() -> {
            this.flipPage.removeAll();
            this.flipPage.build();

            ListIterator<Flip> flipsIterator = flips.listIterator(flips.size());
            this.totalProfit = 0;

            while (flipsIterator.hasPrevious()) {
                Flip flip = flipsIterator.previous();
                FlipPanel flipPanel = new FlipPanel(flip, itemManager, this.removeFlipConsumer);
                this.flipPage.addFlipPanel(flipPanel);
                this.totalProfit += flip.getTotalProfit();
            }

            this.flipPage.setTotalProfit(totalProfit);
        });
    }
}