package com.flipper.models;

import java.sql.Timestamp;
import java.util.UUID;

import lombok.Data;

@Data
public class Alch {
    public UUID id;
    public UUID userId;
    public UUID buyId;
    public int itemId;
    public int quantity;
    public int buyPrice;
    public int natureRunePrice;
    public int alchPrice;
    private Timestamp updatedAt;
    private Timestamp createdAt;

    public Alch() {};

    public String describeAlch(String itemName) {
        return String.valueOf(quantity) + " " + itemName + "(s)";
    }

    public int getTotalProfit() {
        return (alchPrice - buyPrice - natureRunePrice) * quantity;
    }
}
