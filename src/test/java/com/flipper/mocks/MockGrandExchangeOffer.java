package com.flipper.mocks;

import net.runelite.api.GrandExchangeOffer;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.runelite.api.GrandExchangeOfferState;

@Data
@AllArgsConstructor
public class MockGrandExchangeOffer implements GrandExchangeOffer {
    private int quantitySold;
    private int itemId;
    private int totalQuantity;
    private int price;
    private int spent;
    private GrandExchangeOfferState state;
}
