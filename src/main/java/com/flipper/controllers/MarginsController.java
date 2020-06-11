package com.flipper.controllers;

import java.io.IOException;
import java.util.List;

import com.flipper.helpers.TradePersister;
import com.flipper.models.Flip;
import com.flipper.views.margins.MarginsPanel;

import lombok.Getter;
import lombok.Setter;
import net.runelite.client.game.ItemManager;

public class MarginsController {
    @Getter
    @Setter
    private List<Flip> margins;
    private MarginsPanel marginsPanel;

    public MarginsController(ItemManager itemManager) throws IOException {
        this.marginsPanel = new MarginsPanel(itemManager);
        this.loadMargins();
    }

    public void addMargin(Flip margin) {
        this.margins.add(margin);
        this.marginsPanel.rebuildPanel(margins);
    }

    public MarginsPanel getPanel() {
        return this.marginsPanel;
    }

    private void loadMargins() throws IOException {
        this.margins = TradePersister.loadMargins();
        this.marginsPanel.rebuildPanel(margins);
    }

    public void saveMargins() {
        TradePersister.saveMargins(margins);
    }
}