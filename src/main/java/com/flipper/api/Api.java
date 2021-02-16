package com.flipper.api;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Request.Builder;

import com.flipper.responses.LoginResponse;
import com.google.gson.Gson;


public class Api {
    public static String baseUrl = "http://localhost:8071";
    // @todo
    // public static String baseUrl = "https://api.osrs-flipper.com";

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

    public static String request(String method, String route, Object params) {
        String url = createUrl(route);
        Builder requestBuilder = new Builder()
            .url(url)
            .header("Authorization", jwt);
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
            
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (Exception error) {
            return null;
        }
    }

    public static String get(String route, Object params) {
        return Api.request(GET, route, params);
    }

    public static String post(String route, Object params) {
        return Api.request(POST, route, params);
    }

    public static String put(String route, Object params) {
        return Api.request(PUT, route, params);
    }

    public static String delete(String route, Object params) {
        return Api.request(DELETE, route, params);
    }
}
