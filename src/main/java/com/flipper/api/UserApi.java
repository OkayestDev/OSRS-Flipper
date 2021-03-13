package com.flipper.api;

import java.util.function.Consumer;

import com.flipper.models.User;
import com.flipper.responses.LoginResponse;
import com.google.gson.Gson;

public class UserApi {
    public static Gson gson = new Gson();

    public static void login(String email, String password, Consumer<LoginResponse> loginCallback) {
        User user = new User();
        user.setEmailAndPassword(email, password);
        Api.post("/login", loginCallback, LoginResponse.class, user);
    }
}
