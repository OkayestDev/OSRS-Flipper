package com.flipper.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;
import java.util.function.Consumer;

import javax.swing.SwingUtilities;

import com.flipper.helpers.GrandExchange;
import com.flipper.helpers.Log;
import com.flipper.helpers.Persistor;
import com.flipper.helpers.UiUtilities;
import com.flipper.models.Flip;
import com.flipper.models.Transaction;
import com.flipper.responses.FlipResponse;
import com.flipper.views.components.Pagination;
import com.flipper.views.flips.FlipPage;
import com.flipper.views.flips.FlipPanel;
import com.flipper.api.FlipApi;
import com.flipper.api.UploadApi;

import lombok.Getter;
import lombok.Setter;

import java.awt.BorderLayout;

import net.runelite.client.game.ItemManager;

public class FlipsController {
    @Getter
    @Setter
    private List<Flip> flips;
    private FlipPage flipPage;
    private Consumer<UUID> removeFlipConsumer;
    private Runnable refreshFlipsRunnable;
    private String totalProfit = "0";
    private Pagination pagination;

    public FlipsController(ItemManager itemManager, boolean isPrompt) throws IOException {
        this.flips = new ArrayList<Flip>();
        this.removeFlipConsumer = id -> this.removeFlip(id);
        this.refreshFlipsRunnable = () -> this.loadFlips();

        this.flipPage = new FlipPage(refreshFlipsRunnable);
        Consumer<Object> renderItemCallback = (Object flip) -> {
            FlipPanel flipPanel = new FlipPanel(
                (Flip) flip,
                itemManager,
                this.removeFlipConsumer,
                isPrompt
            );
            this.flipPage.addFlipPanel(flipPanel);
        };
        Runnable buildViewCallback = () -> this.buildView();

        try {
            if (Persistor.checkDoesFlipsFileExist()) {
                Consumer<FlipResponse> flipUploadCallback = flipResponse -> {
                    if (flipResponse != null) {
                        this.totalProfit = flipResponse.totalProfit;
                        this.flips = flipResponse.flips;
                        try {
                            Persistor.deleteFlipsJsonFile();
                        } catch (IOException error) {
                            Log.info("Failed to delete flips json file");
                        }
                    }
            
                    this.buildView();
                };

                UploadApi.uploadFlips(flipUploadCallback);
            }
        } catch (Exception error) {
            Log.info("Failed to upload flips");
        }

        this.pagination = new Pagination(renderItemCallback, UiUtilities.ITEMS_PER_PAGE, buildViewCallback);
        this.loadFlips();
    }

    public void addFlip(Flip flip) {
        Consumer<FlipResponse> createFlipCallback = flipResponse -> {
            this.totalProfit = flipResponse.totalProfit;
            this.flips.add(0, flipResponse.flip);
            this.buildView();
        };

        FlipApi.createFlip(flip, createFlipCallback);
    }

    public void removeFlip(UUID flipId) {
        Consumer<FlipResponse> deleteFlipCallback = flipResponse -> {
            if (flipResponse != null) {
                this.totalProfit = flipResponse.totalProfit;
                
                Iterator<Flip> flipsIter = this.flips.iterator();
                while (flipsIter.hasNext()) {
                    Flip flip = flipsIter.next();
                    if (flip.getId().equals(flipId)) {
                        flipsIter.remove();
                        this.buildView();
                    }
                }
            }
        };

        FlipApi.deleteFlip(flipId, deleteFlipCallback);
    }

    public FlipPage getPage() {
        return this.flipPage;
    }

    public void loadFlips() {
        Consumer<FlipResponse> getFlipsCallback = flipResponse -> {
            if (flipResponse != null) {
                this.totalProfit = flipResponse.totalProfit;
                this.flips = flipResponse.flips;
            }
    
            this.buildView();
        };

        FlipApi.getFlips(getFlipsCallback);
    }

    private void updateFlip(Transaction sell, Transaction buy, Flip flip) {
        Consumer<FlipResponse> updateFlipCallback = flipResponse -> {
            if (flipResponse != null) {
                this.totalProfit = flipResponse.totalProfit;
                this.buildView();
            }
        };

        flip.sellPrice = sell.getPricePer();
        flip.buyPrice = buy.getPricePer();
        flip.quantity = sell.getQuantity();
        flip.itemId = sell.getItemId();
        FlipApi.updateFlip(flip, updateFlipCallback);
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
            ListIterator<Flip> flipsIterator = flips.listIterator();
            while (flipsIterator.hasNext()) {
                Flip flip = flipsIterator.next();
                if (flip.getSellId().equals(sell.id)) {
                    // Now find the corresponding buy
                    while (buysIterator.hasPrevious()) {
                        Transaction buy = buysIterator.previous();
                        if (buy.id.equals(flip.getBuyId())) {
                            updateFlip(sell, buy, flip);
                            flipsIterator.set(flip);
                            return flip;
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
                    if (!flip.isMarginCheck()) {
                        this.addFlip(flip);
                        buy.setIsFlipped(true);
                        sell.setIsFlipped(true);
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
            this.flipPage.add(this.pagination.getComponent(this.flips), BorderLayout.SOUTH);
            this.pagination.renderFromBeginning(this.flips);
            this.flipPage.setTotalProfit(totalProfit);
            this.flipPage.revalidate();
        });
    }
}