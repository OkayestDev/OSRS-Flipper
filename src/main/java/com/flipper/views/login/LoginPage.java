package com.flipper.views.login;

import java.util.function.Consumer;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import net.runelite.client.input.KeyListener;

import net.runelite.client.ui.ColorScheme;

public class LoginPage extends JPanel {
    private static final long serialVersionUID = 2898009846682375972L;
    
    private JButton loginButton;
    private JTextField emailTextField;
    private JTextField passwordTextField;

    Consumer<String> onEmailTextChangedListener;
    Consumer<String> onPasswordTextChangedListener;
    ActionListener onLoginPressedListener;

    public LoginPage(
        Consumer<String> onEmailTextChangedListener,
        Consumer<String> onPasswordTextChangedListener,
        ActionListener onLoginPressedListener
    ) {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setBackground(ColorScheme.DARK_GRAY_COLOR);

        this.onEmailTextChangedListener = onEmailTextChangedListener;
        this.onPasswordTextChangedListener = onPasswordTextChangedListener;

        loginButton = new JButton("Login");
        loginButton.addActionListener(onLoginPressedListener);
        emailTextField = new JTextField();
        emailTextField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent event) {}
        
            @Override
            public void keyReleased(KeyEvent event) {
                JTextField textField = (JTextField) event.getSource();
                String text = textField.getText();
                onEmailTextChangedListener.accept(text);
            }
        
            @Override
            public void keyPressed(KeyEvent event) {}
        });
        passwordTextField = new JPasswordField();
        passwordTextField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent event) {}
        
            @Override
            public void keyReleased(KeyEvent event) {
                JTextField textField = (JTextField) event.getSource();
                String text = textField.getText();
                onPasswordTextChangedListener.accept(text);
            }
        
            @Override
            public void keyPressed(KeyEvent event) {}
        });

        add(emailTextField);
        add(passwordTextField);
        add(loginButton);
    }
}
