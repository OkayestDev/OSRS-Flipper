package com.flipper.controller;

import net.runelite.api.GrandExchangeOffer;
import net.runelite.api.GrandExchangeOfferState;
import net.runelite.api.events.GrandExchangeOfferChanged;

/**
 * Handles GrandExchange events
 */
public class GrandExchangeController {
    public static boolean checkIsBuy(GrandExchangeOfferState state) {
        return state == GrandExchangeOfferState.BOUGHT
                || state == GrandExchangeOfferState.CANCELLED_BUY
                || state == GrandExchangeOfferState.BUYING;
    }

    public static boolean handleOnGrandExchangeOfferChanged(GrandExchangeOfferChanged newOfferEvent) {
        GrandExchangeOffer offer = newOfferEvent.getOffer();
        boolean isBuy = checkIsBuy(offer.getState());

        // Only track completed offers (offers fully sold/bought or cancelled)
        // boolean isComplete
        return true;
    }

    public static void handleOnBuy(GrandExchangeOffer buy) {

    }

    public static void handleOnSell(GrandExchangeOffer sell) {

    }
}
