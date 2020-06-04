package com.flipper.mocks;

import net.runelite.api.events.GrandExchangeOfferChanged;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class MockGrandExchangeOfferChanged extends GrandExchangeOfferChanged {
    private int slot;
    private MockGrandExchangeOffer offer;
}
