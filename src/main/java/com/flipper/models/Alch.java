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
    public int quantity;
    public int buyPrice;
    public int natureRunePrice;
    public int alchPrice;
    private Timestamp updatedAt;
    private Timestamp createdAt;

    public Alch() {};

    public Alch(Transaction transaction, ItemManager itemManager) {
        ItemComposition itemComposition = itemManager.getItemComposition(transaction.getItemId());
        this.buyId = transaction.getId();
        this.itemId = transaction.getItemId();
        this.quantity = transaction.getQuantity();
        this.buyPrice = transaction.getPricePer();
        this.natureRunePrice = itemManager.getItemPriceWithSource(
            UiUtilities.NATURE_RUNE_ID,
            true
        );
        this.alchPrice = itemComposition.getHaPrice();
    }

    public String describeAlch(String itemName) {
        return String.valueOf(quantity) + " " + itemName + "(s)";
    }

    public int getTotalProfit() {
        return (alchPrice - buyPrice - natureRunePrice) * quantity;
    }
}
