package com.flipper.api;

import java.util.UUID;
import java.util.function.Consumer;

import com.flipper.models.Alch;
import com.flipper.responses.AlchResponse;

public class AlchApi {
    public static void createAlch(Alch alch, Consumer<AlchResponse> createAlchCallback) {
        Api.post("/alchs", createAlchCallback, AlchResponse.class, alch);
    }

    public static void deleteAlch(UUID alchId, Consumer<AlchResponse> deleteAlchCallback) {
        String route = "/alchs/" + alchId.toString();
        Api.delete(route, deleteAlchCallback, AlchResponse.class, null);
    }

    public static void getAlchs(Consumer<AlchResponse> getAlchsCallback) {
        Api.get("/alchs", getAlchsCallback, AlchResponse.class, null);
    }

    public static void updateAlch(Alch alch, Consumer<AlchResponse> updateAlchCallback) {
        String route = "/alchs/" + alch.getId().toString();
        Api.put(route, updateAlchCallback, AlchResponse.class, alch);
    }
}
