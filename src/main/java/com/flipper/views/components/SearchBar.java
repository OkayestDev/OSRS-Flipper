package com.flipper.views.components;

import java.util.function.Consumer;

import javax.swing.JTextField;

import net.runelite.client.input.KeyListener;

import javax.swing.Timer;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class SearchBar extends JTextField {
    private Timer timer;
    private final String ph = "Search";

    public SearchBar(Consumer<String> onSearchTextChanged) {
        super();
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent event) {}
        
            @Override
            public void keyReleased(KeyEvent event) {
                if (timer == null) {
                    timer = new Timer(500, (ActionEvent action) -> {
                        String text = getText();
                        onSearchTextChanged.accept(text);
                    });
                    timer.setRepeats(false);
                } else {
                    timer.restart();
                }
            }
        
            @Override
            public void keyPressed(KeyEvent event) {}
        });
    }

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (super.getText().length() > 0 || ph == null) {
			return;
		}
		
		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(super.getDisabledTextColor());
		g2.drawString(ph, getInsets().left, g.getFontMetrics().getMaxAscent() + getInsets().top);
	}
}
