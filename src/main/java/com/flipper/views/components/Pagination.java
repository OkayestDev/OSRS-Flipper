package com.flipper.views.components;

import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.BorderLayout;

import java.util.List;
import java.util.function.Consumer;

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;

public class Pagination {
	private Consumer<Object> renderItemCallback;
	private Runnable buildViewCallback;
	private int itemsPerPage;
	private int page;

	public Pagination(
		Consumer<Object> renderItemCallback, 
		int itemsPerPage,
		Runnable buildViewCallback
	) {
		this.itemsPerPage = itemsPerPage;
		this.renderItemCallback = renderItemCallback;
		this.page = 1;
		this.buildViewCallback = buildViewCallback;
	}

	public <T> void renderList(List<T> items) {
		int endIndex = items.size() - (page * itemsPerPage) - 1;
		if (endIndex < 0) {
			endIndex = -1;
		}
		int startIndex = items.size() - ((page - 1) * itemsPerPage) - 1;
		for (int i = startIndex; i > endIndex; i--) {
			this.renderItemCallback.accept(items.get(i));
		}
	}

	public <T> void renderFromBeginning(List<T> items) {
		int startIndex = (page - 1) * itemsPerPage;
		int endIndex = (page * itemsPerPage) - 1;

		if (endIndex > items.size() - 1) {
			endIndex = items.size() - 1;
		}

		for (int i = startIndex; i < endIndex; i++) {
			this.renderItemCallback.accept(items.get(i));
		}
	}

	public <T> JPanel getComponent(List<T> items) {
		JPanel container = new JPanel(new BorderLayout());

		int numberOfPages = (int) Math.round(
			Math.ceil(
				items.size() * 1.0 / this.itemsPerPage
			)
		);

		JLabel previous = new JLabel("<");
		previous.setFont(FontManager.getRunescapeBoldFont());
		previous.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
		previous.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (page != 1) {
					page--;
					buildViewCallback.run();
				}
			}

            @Override
            public void mouseEntered(MouseEvent e) {
                previous.setForeground(ColorScheme.BRAND_ORANGE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                previous.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
            }
		});

		JLabel pageLabel = new JLabel(page + " of " + numberOfPages);
		pageLabel.setHorizontalAlignment(JLabel.CENTER);

		JLabel next = new JLabel(">");
		next.setFont(FontManager.getRunescapeBoldFont());
		next.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
		next.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (page < numberOfPages) {
					page++;
					buildViewCallback.run();
				}
			}

            @Override
            public void mouseEntered(MouseEvent e) {
                next.setForeground(ColorScheme.BRAND_ORANGE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                next.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
            }
		});

		container.add(previous, BorderLayout.WEST);
		container.add(pageLabel, BorderLayout.CENTER);
		container.add(next, BorderLayout.EAST);
		container.setBackground(ColorScheme.DARKER_GRAY_COLOR.darker());

		return container;
	}
}