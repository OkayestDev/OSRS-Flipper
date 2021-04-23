package com.flipper.controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import javax.swing.SwingUtilities;

import com.flipper.models.Alch;
import com.flipper.responses.AlchResponse;
import com.flipper.views.alchs.AlchPage;
import com.flipper.views.alchs.AlchPanel;
import com.flipper.views.components.Pagination;
import com.flipper.api.AlchApi;
import com.flipper.helpers.UiUtilities;

import java.awt.BorderLayout;

import lombok.Getter;
import lombok.Setter;
import net.runelite.client.game.ItemManager;

public class AlchsController {
    @Getter
    @Setter
    private List<Alch> alchs;
    private AlchPage alchPage;
    private Consumer<UUID> removeAlchConsumer;
    private Runnable refreshAlchsRunnable;
    private String totalProfit = "0";
    private Pagination pagination;

    public AlchsController(ItemManager itemManager) {
        this.alchs = new ArrayList<Alch>();
        this.removeAlchConsumer = id -> this.removeAlch(id);
        this.refreshAlchsRunnable = () -> this.loadAlchs();

        this.alchPage = new AlchPage(refreshAlchsRunnable);
        Consumer<Object> renderItemCallback = (Object alch) -> {
            AlchPanel alchPanel = new AlchPanel((Alch) alch, itemManager, this.removeAlchConsumer);
            this.alchPage.addAlchPanel(alchPanel);
        };
        Runnable buildViewCallback = () -> this.buildView();

        this.pagination = new Pagination(renderItemCallback, UiUtilities.ITEMS_PER_PAGE, buildViewCallback);
        this.loadAlchs();
    }

    public void addAlch(Alch alch) {
        Consumer<AlchResponse> createAlchCallback = alchResponse -> {
            this.totalProfit = alchResponse.totalProfit;
            this.alchs.add(0, alchResponse.alch);
            this.buildView();
        };
        AlchApi.createAlch(alch, createAlchCallback);
    }

    public void removeAlch(UUID alchId) {
        Consumer<AlchResponse> deleteAlchCallback = alchResponse -> {
            if (alchResponse != null) {
                this.totalProfit = alchResponse.totalProfit;

                Iterator<Alch> alchsIter = this.alchs.iterator();
                while (alchsIter.hasNext()) {
                    Alch alch = alchsIter.next();
                    if (alch.getId().equals(alchId)) {
                        alchsIter.remove();
                        this.buildView();
                    }
                }
            }
        };

        AlchApi.deleteAlch(alchId, deleteAlchCallback);
    }

    public AlchPage getPage() {
        return this.alchPage;
    }

    public void loadAlchs() {
        Consumer<AlchResponse> getAlchsCallback = alchResponse -> {
            if (alchResponse != null) {
                this.totalProfit = alchResponse.totalProfit;
                this.alchs = alchResponse.alchs;
            }
            this.buildView();
        };

        AlchApi.getAlchs(getAlchsCallback);
    }

    public void buildView() {
        SwingUtilities.invokeLater(() -> {
            this.alchPage.removeAll();
            this.alchPage.build();
            this.alchPage.add(this.pagination.getComponent(this.alchs), BorderLayout.SOUTH);
            this.pagination.renderFromBeginning(this.alchs);
            this.alchPage.setTotalProfit(totalProfit);
            this.alchPage.revalidate();
        });
    }
}
