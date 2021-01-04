package com.flipper.api;

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

    public static FlipResponse deleteFlip(Flip flip) {
        String json = Api.delete("/flips/" + flip.id.toString(), null);
        FlipResponse flipResponse = gson.fromJson(json, FlipResponse.class);
        return flipResponse;
    }

    public static FlipResponse getFlips() {
        String json = Api.get("/flips", null);
        FlipResponse flipResponse = gson.fromJson(json, FlipResponse.class);
        return flipResponse;
    }
}
