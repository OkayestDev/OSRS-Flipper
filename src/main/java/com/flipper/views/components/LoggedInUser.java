package com.flipper.views.components;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.event.ActionEvent;

import java.awt.GridLayout;

import com.flipper.api.Api;
import com.flipper.models.User;

public class LoggedInUser extends JPanel {
    private User loggedInUser;
    private Runnable changeToLoggedOutView;

    public LoggedInUser(Runnable changeToLoggedOutView) {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(0, 0, 5, 0));
        this.changeToLoggedOutView = changeToLoggedOutView;
        this.loggedInUser = Api.loginResponse.user;
        this.constructHeader();
        this.constructLogoutButton();
    }

    public void constructHeader() {
        JPanel container = new JPanel(new GridLayout(1, 1));
        JLabel userDisplayName = new JLabel(this.loggedInUser.getDisplayName());
        container.add(userDisplayName);
        add(container, BorderLayout.WEST);
    }

    public void constructLogoutButton() {
        JButton logout = new JButton("x");
        logout.setPreferredSize(new Dimension(40, 20));
        logout.addActionListener((ActionEvent event) -> {
            changeToLoggedOutView.run();
        });
        add(logout, BorderLayout.EAST);
    }
}
