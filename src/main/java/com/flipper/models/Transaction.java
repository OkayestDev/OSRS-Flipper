package com.flipper.models;

import lombok.Data;
import lombok.Getter;
import net.runelite.api.GrandExchangeOffer;

import java.time.Instant;
import java.util.UUID;

import com.flipper.helpers.GrandExchange;

/**
 * Represents either a buy or sell of an item(s) on the GE
 */
@Data
public class Transaction {
    final public UUID id;
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
    }

    public Transaction updateTransaction(GrandExchangeOffer offer) {
        this.quantity = offer.getQuantitySold();
        this.totalQuantity = offer.getTotalQuantity();
        this.pricePer = offer.getSpent() / offer.getQuantitySold();
        this.isComplete = GrandExchange.checkIsComplete(offer.getState());
        if (this.isComplete) {
            completedTime = Instant.now();
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
