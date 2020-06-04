package com.flipper.model;

import lombok.AllArgsConstructor;
import net.runelite.api.Item;
import lombok.Data;

import java.time.Instant;

/**
 * Represents either a buy or sell of an item(s) on the GE
 */
@Data
@AllArgsConstructor
public class Transaction {
    private int quantity;
    private int itemId;
    private int price;
    Instant time;
    private boolean isBuy;

    public int getTotalPrice() {
        return this.quantity * this.price;
    }
}
