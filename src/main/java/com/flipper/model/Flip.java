package com.flipper.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a buy and sell (flip) of an item
 */
@Data
@AllArgsConstructor
public class Flip {
    final private Transaction buy;
    final private Transaction sell;

    public int getTotalProfit() {
        return sell.getTotalPrice() - buy.getTotalPrice();
    }

    public int getProfitPer() {
        return 0;
    }
}
