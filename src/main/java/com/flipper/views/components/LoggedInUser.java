package com.flipper.views.components;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.event.ActionEvent;

import java.awt.GridLayout;

import com.flipper.api.Api;
import com.flipper.models.User;

public class LoggedInUser extends JPanel {
    private User loggedInUser;
    private Runnable changeToLoggedOutView;

    public LoggedInUser(Runnable changeToLoggedOutView) {
        this.changeToLoggedOutView = changeToLoggedOutView;
        this.loggedInUser = Api.loginResponse.user;
        this.constructHeader();
        this.constructLogoutButton();
    }

    public void constructHeader() {
        JPanel container = new JPanel(new GridLayout(1, 1));
        JLabel userDisplayName = new JLabel(this.loggedInUser.getDisplayName());
        container.add(userDisplayName);
        add(container);
    }

    public void constructLogoutButton() {
        JButton logout = new JButton("x");
        logout.addActionListener((ActionEvent event) -> {
            changeToLoggedOutView.run();
        });
        add(logout);
    }
}
