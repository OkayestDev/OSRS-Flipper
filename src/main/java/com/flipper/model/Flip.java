package com.flipper.model;

import lombok.Data;

/**
 * Represents a buy and sell (flip) of an item
 */
@Data
public class Flip {
    final private Transaction buy;
    final private Transaction sell;
    private boolean isMarginCheck;

    public Flip(Transaction buy, Transaction sell) {
        this.buy = buy;
        this.sell = sell;
        setMarginCheck();
    }

    /**
     * We know a flip is a margin check when only 1 is bought
     * and it's bought for a higher price than it's sold for
     */
    private void setMarginCheck() {
        isMarginCheck = buy.getQuantity() == 1
                && buy.getPrice() > sell.getPrice();
    }

    /**
     * We only concern ourselves with the amount sold (ignore extra bought and kept)
     * @return profit of flip
     */
    public int getTotalProfit() {
        int quantitySold = sell.getQuantity();
        return sell.getTotalPrice() - (quantitySold * buy.getPrice());
    }
}
