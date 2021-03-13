package com.flipper.controllers;

import java.util.function.Consumer;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import com.flipper.api.Api;
import com.flipper.api.UploadApi;
import com.flipper.api.UserApi;
import com.flipper.helpers.Log;
import com.flipper.helpers.Persistor;
import com.flipper.responses.LoginResponse;
import com.flipper.views.login.LoginPage;

import javax.swing.JOptionPane;

public class LoginController {
    LoginPage loginPage;

    private String email;
    private String password;

    Runnable changeToLoggedInViewRunnable;

    public LoginController(Runnable changeToLoggedInViewRunnable) {
        this.changeToLoggedInViewRunnable = changeToLoggedInViewRunnable;
        Consumer<String> onEmailTextChangedListener = text -> onEmailTextChanged(text);
        Consumer<String> onPasswordTextChangedListener = text -> onPasswordTextChanged(text);
        ActionListener onLoginPressedListener = (ActionEvent event) -> onLoginPressed(event);
        this.loginPage = new LoginPage(
            onEmailTextChangedListener,
            onPasswordTextChangedListener,
            onLoginPressedListener
        );
    }

    public void onLoginPressed(ActionEvent event) {
        Consumer<LoginResponse> loginCallback = loginResponse -> {
            if (loginResponse != null && !loginResponse.error) {
                Api.setLoginResponse(loginResponse);
                changeToLoggedInViewRunnable.run();
            } else {
                JOptionPane.showMessageDialog(null, loginResponse.message);
            }
        };

        if (this.email == null || this.password == null) {
            return;
        }

        UserApi.login(this.email, this.password, loginCallback);
    }

    public void onEmailTextChanged(String newEmail) {
        this.email = newEmail;
    }

    public void onPasswordTextChanged(String newPassword) {
        this.password = newPassword;
    }

    public LoginPage getPanel() {
        return this.loginPage;
    }
}
