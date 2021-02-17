package com.flipper.controllers;

import java.io.IOException;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;
import java.util.function.Consumer;

import javax.swing.SwingUtilities;

import com.flipper.helpers.Persistor;
import com.flipper.helpers.UiUtilities;
import com.flipper.models.Flip;
import com.flipper.views.margins.MarginPage;
import com.flipper.views.margins.MarginPanel;

import lombok.Getter;
import lombok.Setter;
import net.runelite.client.game.ItemManager;

public class MarginsController {
    @Getter
    @Setter
    private List<Flip> margins;
    private MarginPage marginPage;
    private ItemManager itemManager;
    private Consumer<UUID> removeMarginConsumer;

    public MarginsController(ItemManager itemManager) throws IOException {
        this.itemManager = itemManager;
        this.removeMarginConsumer = id -> this.removeMargin(id);
        this.marginPage = new MarginPage();
        this.loadMargins();
    }

    public void addMargin(Flip margin) {
        this.margins.add(margin);
        this.buildView();
    }

    public boolean removeMargin(UUID marginId) {
        ListIterator<Flip> marginsIterator = this.margins.listIterator();

        while (marginsIterator.hasNext()) {
            Flip iterMargin = marginsIterator.next();

            if (iterMargin.getId().equals(marginId)) {
                marginsIterator.remove();
                this.buildView();
                return true;
            }
        }

        return false;
    }

    public MarginPage getPanel() {
        return this.marginPage;
    }

    private void loadMargins() throws IOException {
        this.margins = Persistor.loadMargins();
        this.buildView();
    }

    public void saveMargins() {
        Persistor.saveMargins(margins);
    }

    public void buildView() {
        SwingUtilities.invokeLater(() -> {
            this.marginPage.removeAll();
            this.marginPage.build();
            int currentRenderCount = 0;
            ListIterator<Flip> marginsIterator = margins.listIterator(margins.size());

            while (marginsIterator.hasPrevious() && currentRenderCount < UiUtilities.ITEMS_PER_PAGE) {
                Flip margin = marginsIterator.previous();
                MarginPanel marginPanel = new MarginPanel(margin, itemManager, this.removeMarginConsumer);
                this.marginPage.addMarginPanel(marginPanel);
                currentRenderCount++;
            }
        });
    }
}