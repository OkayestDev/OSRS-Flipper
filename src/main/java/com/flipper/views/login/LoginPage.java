package com.flipper.views.login;

import java.util.function.Consumer;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.KeyEvent;
import java.net.URI;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.flipper.helpers.UiUtilities;

import net.runelite.client.input.KeyListener;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.util.ImageUtil;

public class LoginPage extends JPanel {
    private final static int PAGE_WIDTH = 225;

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
        this.setSize(PAGE_WIDTH, 250);

        this.onEmailTextChangedListener = onEmailTextChangedListener;
        this.onPasswordTextChangedListener = onPasswordTextChangedListener;
        this.onLoginPressedListener = onLoginPressedListener;

        this.createLogo();
        this.createEmailInput();
        this.createPasswordInput();
        this.createLoginButton();
        this.createCreateAccountLink();
    }

    public void createLogo() {
        JPanel container = new JPanel();
        ImageIcon flipperIcon = new ImageIcon(
            ImageUtil.getResourceStreamFromClass(
                getClass(), 
                UiUtilities.flipperNavIcon
            )
        );
        Image resizedLogo = flipperIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(resizedLogo));
        logoLabel.setSize(40, 40);
        container.add(logoLabel, BorderLayout.CENTER);
        container.setBorder(new EmptyBorder(10, 0, 10, 0));
        add(container);
    }

    public void createEmailInput() {
        JPanel container = new JPanel(new GridLayout(2, 1));
        container.setBorder(new EmptyBorder(0, 0, 10, 0));
        emailTextField = new JTextField();
        emailTextField.setSize(PAGE_WIDTH, 40);
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
        JLabel emailLabel = new JLabel("Email");
        container.add(emailLabel, BorderLayout.WEST);
        container.add(emailTextField);
        add(container);
    }

    public void createPasswordInput() {
        JPanel container = new JPanel(new GridLayout(2, 1));
        container.setBorder(new EmptyBorder(0, 0, 10, 0));
        passwordTextField = new JPasswordField();
        passwordTextField.setSize(PAGE_WIDTH, 40);
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
        JLabel passwordLabel = new JLabel("Password");
        container.add(passwordLabel);
        container.add(passwordTextField);
        add(container);
    }

    public void createCreateAccountLink() {
        JPanel container = new JPanel();
        JLabel createAccountLabel = new JLabel("Don't have an account? Create one.");
        createAccountLabel.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
        createAccountLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop desktop = Desktop.getDesktop();
                    URI flipperFrontendURI = new URI("https://www.osrs-flipper.com");
                    desktop.browse(flipperFrontendURI);
                } catch (Exception error) {
        
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                createAccountLabel.setForeground(ColorScheme.BRAND_ORANGE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                createAccountLabel.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
            }
        }); 
        container.add(createAccountLabel);
        add(container);
    }

    public void createLoginButton() {
        JPanel container = new JPanel();
        loginButton = new JButton("Login");
        loginButton.addActionListener(this.onLoginPressedListener);
        container.add(loginButton);
        add(container);
    }
}
