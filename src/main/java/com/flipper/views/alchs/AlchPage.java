package com.flipper.views.alchs;

import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.BorderLayout;

import net.runelite.client.ui.ColorScheme;

public class AlchPage extends JPanel {
    private JPanel container;
    private JLabel totalProfitValueLabel;

    private Runnable refreshAlchsRunnable;

    public AlchPage(Runnable refreshAlchsRunnable) {
        this.refreshAlchsRunnable = refreshAlchsRunnable;
        this.setLayout(new BorderLayout());
        this.setBackground(ColorScheme.DARK_GRAY_COLOR);
    }

    public void addFlipPanel(AlchPanel alchPanel) {
        container.add(alchPanel, BorderLayout.CENTER);
        this.revalidate();
    }
}
