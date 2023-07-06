package com.flipper.models;

import java.sql.Timestamp;
import java.util.UUID;

import com.flipper.helpers.UiUtilities;

import lombok.Data;
import net.runelite.api.ItemComposition;
import net.runelite.client.game.ItemManager;

@Data
public class Alch {
    public UUID id;
    public UUID userId;
    public UUID buyId;
    public int itemId;
    String itemName;
    public int quantity;
    public int buyPrice;
    public int natureRunePrice;
    public int alchPrice;
    private Timestamp updatedAt;
    private Timestamp createdAt;

    public Alch() {};

    public Alch(Transaction transaction, int haPrice, int natPrice) {
        this.buyId = transaction.getId();
        this.itemId = transaction.getItemId();
        this.itemName = transaction.getItemName();
        this.quantity = transaction.getQuantity();
        this.buyPrice = transaction.getPricePer();
        this.natureRunePrice = natPrice;
        this.alchPrice = haPrice;
    }

    public String describeAlch() {
        return String.valueOf(quantity) + " " + this.itemName + "(s)";
    }

    public int getTotalProfit() {
        return (alchPrice - buyPrice - natureRunePrice) * quantity;
    }
}
