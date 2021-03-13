package com.flipper.api;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Request.Builder;

import java.io.IOException;
import java.util.function.Consumer;

import com.flipper.helpers.Log;
import com.flipper.responses.LoginResponse;
import com.google.gson.Gson;


public class Api {
    // public static String baseUrl = "http://localhost:8071";
    // @todo
    public static String baseUrl = "https://api.osrs-flipper.com";

    public static OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static String jwt = "";
    public static LoginResponse loginResponse;
    public static Gson gson = new Gson();

    public static final String POST = "post";
    public static final String PUT = "put";
    public static final String DELETE = "delete";
    public static final String GET = "get";

    public static void setJwt(String jwt) {
        Api.jwt = jwt;
    }

    public static void setLoginResponse(LoginResponse loginResponse) {
        Api.loginResponse = loginResponse;
        Api.jwt = loginResponse.jwt;
    }

    public static String createUrl(String route) {
        return baseUrl + route;
    }

    public static <T> void request(
        String method, 
        String route, 
        Consumer<T> callback, 
        Class<T> jsonClass,
        Object params
    ) {
        String url = createUrl(route);
        Builder requestBuilder = new Builder()
            .url(url);
        
        if (jwt != null) {
            requestBuilder.header("Authorization", jwt);
        }

        String paramsJson = params == null ? "" : gson.toJson(params);
        RequestBody body = RequestBody.create(JSON, paramsJson);
        Request request;

        switch (method) {
            case POST:
                request = requestBuilder.post(body).build();
                break;
            case DELETE:
                request = requestBuilder.delete(body).build();
                break;
            case PUT:
                request = requestBuilder.put(body).build();
                break;
            case GET:
            default:
                request = requestBuilder.build();
                break;
        }

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            public void onResponse(Call call, Response response) throws IOException {
                callback.accept(
                    gson.fromJson(response.body().string(), jsonClass)
                );
            }

            public void onFailure(Call call, IOException error) {
                Log.info("Flipper Server Error");
            }
        });
    }

    public static <T> void get(
        String route, 
        Consumer<T> callback, 
        Class<T> jsonClass, 
        Object params
    ) {
        Api.request(
            GET, 
            route,
            callback, 
            jsonClass,
            params
        );
    }

    public static <T> void post(
        String route, 
        Consumer<T> callback,
        Class<T> jsonClass,
        Object params
    ) {
        Api.request(
            POST, 
            route, 
            callback, 
            jsonClass,
            params
        );
    }

    public static <T> void put(
        String route, 
        Consumer<T> callback,
        Class<T> jsonClass,
        Object params
    ) {
        Api.request(
            PUT, 
            route,
            callback, 
            jsonClass,
            params
        );
    }

    public static <T> void delete(
        String route, 
        Consumer<T> callback,
        Class<T> jsonClass,
        Object params
    ) {
        Api.request(
            DELETE, 
            route,
            callback, 
            jsonClass,
            params
        );
    }
}
