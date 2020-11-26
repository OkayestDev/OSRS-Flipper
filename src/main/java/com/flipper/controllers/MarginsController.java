package com.flipper.controllers;

import java.io.IOException;
import java.util.List;

import com.flipper.helpers.TradePersister;
import com.flipper.models.Flip;
import com.flipper.views.margins.MarginPage;

import lombok.Getter;
import lombok.Setter;
import net.runelite.client.game.ItemManager;

public class MarginsController {
    @Getter
    @Setter
    private List<Flip> margins;
    private MarginPage marginPage;

    public MarginsController(ItemManager itemManager) throws IOException {
        this.marginPage = new MarginPage(itemManager);
        this.loadMargins();
    }

    public void addMargin(Flip margin) {
        this.margins.add(margin);
        this.marginPage.rebuildPanel(margins);
    }

    public MarginPage getPanel() {
        return this.marginPage;
    }

    private void loadMargins() throws IOException {
        this.margins = TradePersister.loadMargins();
        this.marginPage.rebuildPanel(margins);
    }

    public void saveMargins() {
        TradePersister.saveMargins(margins);
    }
}