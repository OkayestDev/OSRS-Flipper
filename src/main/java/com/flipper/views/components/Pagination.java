package com.flipper.views.components;

import java.util.List;
import java.util.function.Consumer;

import javax.swing.JPanel;

public class Pagination {
	private Consumer<Object> renderItemCallback;
	private int itemsPerPage;
	private int page;

	public Pagination(Consumer<Object> renderItemCallback, int itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
		this.renderItemCallback = renderItemCallback;
		this.page = 1;
	}

	// @todo render list starting at the end
	public <T> void renderList(List<T> items) {
		int startIndex = (page - 1) * itemsPerPage;
		int endIndex = Math.min(startIndex + itemsPerPage, items.size());
		for (int i = startIndex; i < endIndex; i++) {
			this.renderItemCallback.accept(items.get(i));
		}
	}

	public JPanel getComponent() {
		return new JPanel();
	}
}