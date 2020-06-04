package com.flipper.controller;

import net.runelite.api.GrandExchangeOffer;
import net.runelite.api.GrandExchangeOfferState;
import net.runelite.api.events.GrandExchangeOfferChanged;
import lombok.extern.slf4j.Slf4j;

/**
 * Gets and parses the information from a GE event
 */
@Slf4j
public class OfferInfo {
    public static void fromGrandExchangeEvent(GrandExchangeOfferChanged event) {
        GrandExchangeOffer offer = event.getOffer();

        boolean isBuy = offer.getState() == GrandExchangeOfferState.BOUGHT
                || offer.getState() == GrandExchangeOfferState.CANCELLED_BUY
                || offer.getState() == GrandExchangeOfferState.BUYING;

        log.info(offer.toString());

//        return new OfferInfo(
//                isBuy,
//                offer.getItemId(),
//                offer.getQuantitySold(),
//                offer.getQuantitySold() == 0 ? 0 : offer.getSpent() / offer.getQuantitySold(),
//                Instant.now().truncatedTo(ChronoUnit.SECONDS),
//                event.getSlot(),
//                offer.getState(),
//                0,
//                0,
//                offer.getTotalQuantity(),
//                0,
//                true,
//                true,
//                null);
    }
}
