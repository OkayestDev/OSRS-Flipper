package com.flipper.models;

import java.time.Instant;
import java.util.UUID;

import lombok.Data;

/**
 * Represents a buy and sell (flip) of an item
 */
@Data
public class Flip {
    final UUID id;
    private Transaction buy;
    private Transaction sell;
    private Instant createdTime;
    private Instant completedTime;
    private boolean isMarginCheck;

    public Flip(Transaction buy, Transaction sell) {
        id = UUID.randomUUID();
        this.buy = buy;
        this.sell = sell;
        this.createdTime = Instant.now();
        setMarginCheck();
    }

    public Flip updateFlip(Transaction sell, Transaction buy) {
        this.buy = buy;
        this.sell = sell;
        if (buy.isComplete() && sell.isComplete()) {
            completedTime = Instant.now();
        }
        return this;
    }

    /**
     * We know a flip is a margin check when only 1 is bought
     * and it's bought for a higher price than it's sold for
     */
    private void setMarginCheck() {
        isMarginCheck = buy.getQuantity() == 1
                && buy.getPricePer() > sell.getPricePer();
    }

    /**
     * We only concern ourselves with the amount sold (ignore extra bought and kept)
     * @return profit of flip
     */
    public int getTotalProfit() {
        int quantitySold = sell.getQuantity();
        return sell.getTotalPrice() - (quantitySold * buy.getPricePer());
    }
}
