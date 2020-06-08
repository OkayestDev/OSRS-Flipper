package com.flipper.models;

import lombok.Data;
import net.runelite.api.GrandExchangeOffer;

import java.time.Instant;

import com.flipper.helpers.GrandExchange;

/**
 * Represents either a buy or sell of an item(s) on the GE
 */
@Data
public class Transaction {
    private int quantity;
    private int totalQuantity;
    private int itemId;
    private int pricePer;
    private String itemName;
    private boolean isBuy;
    private boolean isComplete;
    Instant completedTime;
    Instant createdTime;

    public Transaction(
        int quantity,
        int totalQuantity,
        int itemId,
        int pricePer,
        String itemName,
        boolean isBuy,
        boolean isComplete
    ) {
        this.quantity = quantity;
        this.totalQuantity = totalQuantity;
        this.itemId = itemId;
        this.pricePer = pricePer;
        this.itemName = itemName;
        this.isBuy = isBuy;
        this.isComplete = isComplete;
        this.createdTime = Instant.now();
    }

    public Transaction updateTransaction(GrandExchangeOffer offer) {
        this.quantity = offer.getQuantitySold();
        this.totalQuantity = offer.getTotalQuantity();
        this.isComplete = GrandExchange.checkIsComplete(offer.getState());
        return this;
    }

    public int getTotalPrice() {
        return pricePer * quantity;
    }
}
