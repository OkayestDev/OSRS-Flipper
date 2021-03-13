package com.flipper.api;

import java.util.UUID;
import java.util.function.Consumer;

import com.flipper.models.Flip;
import com.flipper.responses.FlipResponse;

public class FlipApi {
    public static void createFlip(Flip flip, Consumer<FlipResponse> createFlipCallback) {
        Api.post("/flips", createFlipCallback, FlipResponse.class, flip);
    }

    public static void deleteFlip(UUID flipId, Consumer<FlipResponse> deleteFlipCallback) {
        String route = "/flips/" + flipId.toString();
        Api.delete(route, deleteFlipCallback, FlipResponse.class, null);
    }

    public static void getFlips(Consumer<FlipResponse> getFlipsCallback) {
        Api.get("/flips", getFlipsCallback, FlipResponse.class, null);
    }

    public static void updateFlip(Flip flip, Consumer<FlipResponse> updateFlipCallback) {
        String route = "/flips/" + flip.getId().toString();
        Api.put(route, updateFlipCallback, FlipResponse.class, flip);
    }
}
