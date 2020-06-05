package com.flipper.controller;

import com.flipper.model.Transaction;
import net.runelite.api.GrandExchangeOffer;
import net.runelite.api.GrandExchangeOfferState;
import net.runelite.api.events.GrandExchangeOfferChanged;

import java.time.Instant;

/**
 * Handles GrandExchange events
 */
public class GrandExchangeController {
    public static boolean checkIsBuy(GrandExchangeOfferState state) {
        return state == GrandExchangeOfferState.BOUGHT ||
                state == GrandExchangeOfferState.CANCELLED_BUY ||
                state == GrandExchangeOfferState.BUYING;
    }

    public static boolean checkIsComplete(GrandExchangeOfferState state) {
        return state == GrandExchangeOfferState.BOUGHT ||
                state == GrandExchangeOfferState.SOLD ||
                state == GrandExchangeOfferState.CANCELLED_SELL ||
                state == GrandExchangeOfferState.CANCELLED_BUY;
    }

    public static Transaction handleOnGrandExchangeOfferChanged(GrandExchangeOfferChanged newOfferEvent) {
        GrandExchangeOffer offer = newOfferEvent.getOffer();
        GrandExchangeOfferState state = offer.getState();
        boolean isBuy = checkIsBuy(state);
        boolean isComplete = checkIsComplete(state);

        // Create a transaction only if the exchange is complete
        if (isComplete) {
            Transaction transaction = new Transaction(
                offer.getTotalQuantity(),
                offer.getItemId(),
                offer.getPrice(),
                Instant.now(),
                isBuy
            );

            // Handle potential flip here?
            if (!isBuy) {
            }

            return transaction;
        }

        return null;
    }

    public static void handleOnBuy(GrandExchangeOffer buy) {

    }

    public static void handleOnSell(GrandExchangeOffer sell) {

    }
}
