package com.flipper.api;

import com.flipper.helpers.Log;
import com.flipper.helpers.Persistor;
import com.flipper.models.User;
import com.flipper.responses.LoginResponse;
import com.google.gson.Gson;

public class UserApi {
    public static Gson gson = new Gson();

    public static LoginResponse login(String email, String password) {
        User user = new User();
        user.setEmailAndPassword(email, password);
        String json = Api.post("/login", user);
        LoginResponse loginResponse = gson.fromJson(json, LoginResponse.class);

        if (loginResponse != null && !loginResponse.error) {
            Api.setLoginResponse(loginResponse);

            try {
                if (Persistor.checkDoesFlipsFileExist()) {
                    UploadApi.uploadFlips();
                }
            } catch (Exception error) {
                Log.info("Failed to upload flips");
            }
        }
        
        return loginResponse;
    }
}
