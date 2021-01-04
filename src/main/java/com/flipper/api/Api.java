package com.flipper.api;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.flipper.responses.LoginResponse;
import com.google.gson.Gson;

public class Api {
    public static String baseUrl = "http://localhost:8071";
    
    // public static String baseUrl = "https://api.osrs-flipper.com";

    public static OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static String jwt = "";
    public static LoginResponse loginResponse;
    public static Gson gson = new Gson();

    public static void setJwt(String jwt) {
        Api.jwt = jwt;
    }

    public static void setLoginResponse(LoginResponse loginResponse) {
        Api.loginResponse = loginResponse;
    }

    public static String createUrl(String route) {
        return baseUrl + route;
    }

    public static String get(String route, Object params) {
        String url = createUrl(route);
        Request request = new Request
            .Builder()
            .url(url)
            .header("Authorization", jwt)
            .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (Exception error) {
            return null;
        }
    }

    public static String post(String route, Object params) {
        String url = createUrl(route);
        String paramsJson = gson.toJson(params);
        RequestBody body = RequestBody.create(JSON, paramsJson);
        Request request = new Request
            .Builder()
            .url(url)
            .header("Authorization", jwt)
            .post(body)
            .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (Exception error) {
            return null;
        }
    }

    public static String put(String route, Object params) {
        String url = createUrl(route);
        String paramsJson = gson.toJson(params);
        RequestBody body = RequestBody.create(JSON, paramsJson);
        Request request = new Request
            .Builder()
            .url(url)
            .header("Authorization", jwt)
            .put(body)
            .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (Exception error) {
            return null;
        }
    }

    public static String delete(String route, Object params) {
        String url = createUrl(route);
        String paramsJson = gson.toJson(params);
        RequestBody body = RequestBody.create(JSON, paramsJson);
        Request request = new Request
            .Builder()
            .url(url)
            .header("Authorization", jwt)
            .delete(body)
            .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (Exception error) {
            return null;
        }
    }
}
