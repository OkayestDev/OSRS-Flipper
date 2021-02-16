package com.flipper.controllers;

import java.util.function.Consumer;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import com.flipper.api.UserApi;
import com.flipper.views.login.LoginPage;

public class LoginController {
    LoginPage loginPage;

    public LoginController() {
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
        System.out.println("Logged in pressed");
    }

    public void onEmailTextChanged(String newEmail) {
        System.out.println(newEmail);
    }

    public void onPasswordTextChanged(String newPassword) {
        System.out.println(newPassword);
    }

    public LoginPage getPanel() {
        return this.loginPage;
    }
}
