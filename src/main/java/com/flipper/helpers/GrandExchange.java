package com.flipper.helpers;

import java.util.List;
import java.util.ListIterator;

import com.flipper.models.Flip;
import com.flipper.models.Transaction;

import net.runelite.api.GrandExchangeOffer;
import net.runelite.api.GrandExchangeOfferState;
import net.runelite.api.ItemComposition;
import net.runelite.client.game.ItemManager;

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
    public static Transaction createTransactionFromOffer(GrandExchangeOffer offer, ItemManager itemManager) {
        GrandExchangeOfferState state = offer.getState();
        boolean isBuy = checkIsBuy(state);
        boolean isComplete = checkIsComplete(state);

        ItemComposition itemComposition = itemManager.getItemComposition(offer.getItemId());
        return new Transaction(
            offer.getQuantitySold(),
            offer.getTotalQuantity(),
            offer.getItemId(),
            offer.getPrice(),
            itemComposition.getName(),
            isBuy,
            isComplete
        );
    }

    public static boolean checkIsOfferPartOfTransaction(Transaction transaction, GrandExchangeOffer offer) {
        return 
            !transaction.isComplete() && 
            transaction.getItemId() == offer.getItemId() &&
            transaction.getTotalQuantity() == offer.getTotalQuantity();
    }

    public static boolean checkIsSellAFlipOfBuy(Transaction sell, Transaction buy) {
        boolean isSameItem = sell.getItemId() == buy.getItemId();
        boolean hasTransactionsBeenFlipped = sell.isFlipped() && buy.isFlipped();
        return isSameItem && !hasTransactionsBeenFlipped;
    }
}
