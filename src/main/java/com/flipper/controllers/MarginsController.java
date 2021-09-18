package com.flipper.controllers;

import com.flipper.FlipperConfig;
import com.flipper.helpers.Persistor;
import com.flipper.helpers.UiUtilities;
import com.flipper.models.Flip;
import com.flipper.views.components.Pagination;
import com.flipper.views.margins.MarginPage;
import com.flipper.views.margins.MarginPanel;
import java.awt.BorderLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;
import java.util.function.Consumer;
import javax.swing.SwingUtilities;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.ItemComposition;
import net.runelite.client.game.ItemManager;

public class MarginsController {

    @Getter
    @Setter
    private List<Flip> margins = new ArrayList<Flip>();

    private List<Flip> filteredMargins = new ArrayList<Flip>();
    private MarginPage marginPage;
    private Consumer<UUID> removeMarginConsumer;
    private Pagination pagination;
    private ItemManager itemManager;
    private String searchText;
    private Consumer<String> onSearchTextChangedCallback;

    public MarginsController(ItemManager itemManager, FlipperConfig config, Consumer<Flip> convertToFlipConsumer) throws IOException {
        this.removeMarginConsumer = id -> this.removeMargin(id);
        this.itemManager = itemManager;
        this.onSearchTextChangedCallback = searchText -> this.onSearchTextChanged(searchText);
        this.marginPage = new MarginPage(this.onSearchTextChangedCallback);

        Consumer<Object> renderItemCallback = (Object margin) -> {
            MarginPanel marginPanel = new MarginPanel(
                (Flip) margin,
                itemManager,
                this.removeMarginConsumer,
                config.isPromptDeleteMargin(),
                convertToFlipConsumer
            );
            this.marginPage.addMarginPanel(marginPanel);
        };
        Runnable buildViewCallback = () -> this.buildView();
        this.pagination = new Pagination(renderItemCallback, UiUtilities.ITEMS_PER_PAGE, buildViewCallback);
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

    public void onSearchTextChanged(String searchText) {
        this.searchText = searchText;
        this.pagination.resetPage();
        this.buildView();
    }

    private boolean isRender(Flip margin) {
        ItemComposition itemComp = this.itemManager.getItemComposition(margin.getItemId());
        String itemName = itemComp.getName();

        if (this.searchText != null && itemName.toLowerCase().contains(this.searchText.toLowerCase())) {
            return true;
        } else if (this.searchText != null && this.searchText != "") {
            return false;
        }

        return true;
    }

    public MarginPage getPage() {
        return this.marginPage;
    }

    private void loadMargins() throws IOException {
        this.margins = Persistor.loadMargins();
        this.filteredMargins = this.margins;
        this.buildView();
    }

    public void saveMargins() {
        Persistor.saveMargins(margins);
    }

    public void filterList() {
        if (this.searchText == "" || this.searchText == null) {
            this.filteredMargins = this.margins;
        } else {
            Iterator<Flip> marginsIter = this.margins.iterator();
            this.filteredMargins = new ArrayList<Flip>();
            while (marginsIter.hasNext()) {
                Flip currentMargin = marginsIter.next();
                if (this.isRender(currentMargin)) {
                    filteredMargins.add(currentMargin);
                }
            }
        }
    }

    public void buildView() {
        SwingUtilities.invokeLater(
            () -> {
                this.filterList();
                this.marginPage.resetContainer();
                this.marginPage.add(this.pagination.getComponent(this.filteredMargins), BorderLayout.SOUTH);
                this.pagination.renderList(this.filteredMargins);
                this.marginPage.revalidate();
            }
        );
    }
}
