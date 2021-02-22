package com.flipper.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;
import java.util.function.Consumer;

import javax.swing.SwingUtilities;

import com.flipper.helpers.GrandExchange;
import com.flipper.helpers.UiUtilities;
import com.flipper.models.Flip;
import com.flipper.models.Transaction;
import com.flipper.responses.FlipResponse;
import com.flipper.views.components.Pagination;
import com.flipper.views.flips.FlipPage;
import com.flipper.views.flips.FlipPanel;
import com.flipper.api.FlipApi;

import lombok.Getter;
import lombok.Setter;

import net.runelite.client.game.ItemManager;

public class FlipsController {
    @Getter
    @Setter
    private List<Flip> flips;
    private FlipPage flipPage;
    private Consumer<UUID> removeFlipConsumer;
    private double totalProfit = 0;
    private Pagination pagination;

    public FlipsController(ItemManager itemManager) throws IOException {
        this.flips = new ArrayList<Flip>();
        this.removeFlipConsumer = id -> this.removeFlip(id);
        this.flipPage = new FlipPage();
        Consumer<Object> renderItemCallback = (Object flip) -> {
            FlipPanel flipPanel = new FlipPanel((Flip) flip, itemManager, this.removeFlipConsumer);
            this.flipPage.add(flipPanel);
        };
        Runnable buildViewCallback = () -> this.buildView();
        this.pagination = new Pagination(renderItemCallback, UiUtilities.ITEMS_PER_PAGE, buildViewCallback);
        this.loadFlips();
    }

    public void addFlip(Flip flip) {
        FlipResponse flipResponse = FlipApi.createFlip(flip);
        this.totalProfit = flipResponse.totalProfit;
        this.flips.add(flipResponse.flip);
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

    public void loadFlips() throws IOException {
        FlipResponse flipResponse = FlipApi.getFlips();

        if (flipResponse != null) {
            this.totalProfit = flipResponse.totalProfit;
            this.flips = flipResponse.flips;
        }

        this.buildView();
    }

    private Flip updateFlip(Transaction sell, Transaction buy, Flip flip) {
        flip.sellPrice = sell.getPricePer();
        flip.buyPrice = buy.getPricePer();
        flip.quantity = sell.getQuantity();
        flip.itemId = sell.getItemId();
        FlipResponse flipResponse = FlipApi.updateFlip(flip);

        if (flipResponse != null) {
            this.totalProfit = flipResponse.totalProfit;
            this.buildView();
        }

        return flip;
    }


    /**
     * Potentially creates a flip if the sell is complete and has a corresponding
     * buy
     * 
     * @param sell
     * @param buys
     */
    public Flip upsertFlip(Transaction sell, List<Transaction> buys) {
        ListIterator<Transaction> buysIterator = buys.listIterator(buys.size());
        // If sell has already been flipped, look for it's corresponding buy and update
        // the flip
        if (sell.isFlipped()) {
            ListIterator<Flip> flipsIterator = flips.listIterator(flips.size());
            while (flipsIterator.hasPrevious()) {
                Flip flip = flipsIterator.previous();
                if (flip.getSellId().equals(sell.id)) {
                    // Now find the corresponding buy
                    while (buysIterator.hasPrevious()) {
                        Transaction buy = buysIterator.previous();
                        if (buy.id.equals(flip.getBuyId())) {
                            Flip updatedFlip = updateFlip(sell, buy, flip);
                            flipsIterator.set(updatedFlip);
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

    public void buildView() {
        SwingUtilities.invokeLater(() -> {
            this.flipPage.removeAll();
            this.flipPage.build();
            this.pagination.renderList(this.flips);
            this.flipPage.add(this.pagination.getComponent(this.flips));
            this.flipPage.setTotalProfit(totalProfit);
        });
    }
}