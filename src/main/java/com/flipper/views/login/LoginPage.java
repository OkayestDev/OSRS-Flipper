package com.flipper.views.login;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import net.runelite.client.ui.ColorScheme;

public class LoginPage extends JPanel {
    public LoginPage() {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setBackground(ColorScheme.DARK_GRAY_COLOR);
    }
}
