package com.flipper.api;

import com.flipper.models.User;

public class UserApi {
    public static String login(String email, String password) {
        User user = new User();
        user.setEmailAndPassword(email, password);
        String loginResponse = Api.post("/login", user);
        return loginResponse;
    }
}
