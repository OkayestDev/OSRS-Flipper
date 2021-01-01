package com.flipper.api;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.simple.*;

public class Api {
    public static String baseUrl = "http://localhost:8071"
    // public static String baseUrl = "https://api.osrs-flipper.com";

    public static OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static String jwt = "";

    public static void setJwt(String jwt) {
        Api.jwt = jwt;
    }

    public static String get(String route, Object params) {
        String url = baseUrl + route;

        Request request = new Request.Builder().url(url).header("Authorization", jwt).build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public static void post(String route, Object params) {
        String paramsJson = "";
        RequestBody body = RequestBody.create(JSON, paramsJson);

    }

    public static void put(String route, Object params) {

    }
}
