package com.flipper.model;

/**
 * Represents a buy and sell (flip) of an item
 */
public class Flip {
    final private Transaction buy;
    final private Transaction sell;
    private boolean isMarginCheck;

    public Flip(Transaction buy, Transaction sell) {
        this.buy = buy;
        this.sell = sell;
    }

    public int getTotalProfit() {
        return sell.getTotalPrice() - buy.getTotalPrice();
    }

    public int getProfitPer() {
        return 0;
    }
}
