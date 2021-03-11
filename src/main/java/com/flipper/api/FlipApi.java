package com.flipper.api;

import java.util.UUID;

import com.flipper.models.Flip;
import com.flipper.responses.FlipResponse;
import com.google.gson.Gson;

public class FlipApi {
    public static Gson gson = new Gson();

    public static FlipResponse createFlip(Flip flip) {
        String json = Api.post("/flips", flip);
        FlipResponse flipResponse = gson.fromJson(json, FlipResponse.class);
        return flipResponse;
    }

    public static FlipResponse deleteFlip(UUID flipId) {
        String json = Api.delete("/flips/" + flipId.toString(), null);
        FlipResponse flipResponse = gson.fromJson(json, FlipResponse.class);
        return flipResponse;
    }

    public static FlipResponse getFlips() {
        String json = Api.get("/flips", null);
        FlipResponse flipResponse = gson.fromJson(json, FlipResponse.class);
        return flipResponse;
    }

    public static FlipResponse updateFlip(Flip flip) {
        String route = "/flips/" + flip.getId().toString();
        String json = Api.put(route, flip);
        FlipResponse flipResponse = gson.fromJson(json, FlipResponse.class);
        return flipResponse;
    }
}
