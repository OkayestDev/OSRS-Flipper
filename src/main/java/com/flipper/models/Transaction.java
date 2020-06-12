package com.flipper.models;

import lombok.Data;
import net.runelite.api.GrandExchangeOffer;

import java.time.Instant;
import java.util.UUID;

import com.flipper.helpers.GrandExchange;

/**
 * Represents either a buy or sell of an item(s) on the GE
 */
@Data
public class Transaction {
    public final UUID id;
    private int quantity;
    private int totalQuantity;
    private int itemId;
    private int pricePer;
    private String itemName;
    private boolean isBuy;
    private boolean isComplete;
    private boolean isFlipped;
    private Instant completedTime;
    private Instant createdTime;
    /**
     * RuneLite grand exchange subscribing is odd. 
     * Interactions are duplicated.
     * This is only an issue when cancelling buy/sells as the
     * Transaction is being marked as completed resulting in the second
     * GrandExchangeEvent duplicating the Transaction
     * See FlipperPlugin::onGrandExchangeOfferChanged
     */
    private boolean hasCancelledOnce = false;

    public Transaction(
        int quantity,
        int totalQuantity,
        int itemId,
        int pricePer,
        String itemName,
        boolean isBuy,
        boolean isComplete
    ) {
        id = UUID.randomUUID();
        this.quantity = quantity;
        this.totalQuantity = totalQuantity;
        this.itemId = itemId;
        this.pricePer = pricePer;
        this.itemName = itemName;
        this.isBuy = isBuy;
        this.isComplete = isComplete;
        this.createdTime = Instant.now();
        this.isFlipped = false;
        this.hasCancelledOnce = false;
    }

    public Transaction updateTransaction(GrandExchangeOffer offer) {
        this.quantity = offer.getQuantitySold();
        this.totalQuantity = offer.getTotalQuantity();
        this.pricePer = offer.getSpent() / offer.getQuantitySold();
        boolean isCancelState = GrandExchange.checkIsCancelState(offer.getState());

        if (!isCancelState || (this.hasCancelledOnce && isCancelState)) {
            this.isComplete = GrandExchange.checkIsComplete(offer.getState());
        }

        if (isCancelState) {
            this.hasCancelledOnce = true;
        }

        if (this.isComplete) {
            completedTime = Instant.now();
            this.totalQuantity = offer.getQuantitySold();
        }
        return this;
    }

    public void setIsFlipped(boolean isFlipped) {
        this.isFlipped = isFlipped;
    }

    public int getTotalPrice() {
        return pricePer * quantity;
    }
}
