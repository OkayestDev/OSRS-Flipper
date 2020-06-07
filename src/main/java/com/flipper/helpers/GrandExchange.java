package com.flipper.helpers;

import com.flipper.model.Transaction;

import net.runelite.api.GrandExchangeOffer;
import net.runelite.api.GrandExchangeOfferState;
import net.runelite.api.ItemComposition;
import net.runelite.api.events.GrandExchangeOfferChanged;
import net.runelite.client.game.ItemManager;

import java.time.Instant;

/**
 * Handles GrandExchange events
 */
public class GrandExchange {
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

    /**
     * Potentially creates a transaction based on the GrandExchange event
     * @param newOfferEvent
     * @return null or newly created transaction
     */
    public static Transaction handleOnGrandExchangeOfferChanged(GrandExchangeOfferChanged newOfferEvent, ItemManager itemManager) {
        GrandExchangeOffer offer = newOfferEvent.getOffer();
        GrandExchangeOfferState state = offer.getState();
        boolean isBuy = checkIsBuy(state);
        boolean isComplete = checkIsComplete(state);
        // Create a transaction only if the exchange is complete
        if (isComplete) {
            ItemComposition itemComposition = itemManager.getItemComposition(offer.getItemId());
            Transaction transaction = new Transaction(
                offer.getTotalQuantity(),
                offer.getItemId(),
                offer.getPrice(),
                offer.getPrice(), // @todo figure if this is total price or price per
                itemComposition.getName(),
                Instant.now(),
                isBuy
            );

            return transaction;
        }

        return null;
    }
}
