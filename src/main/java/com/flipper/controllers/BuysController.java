package com.flipper.controllers;

import java.io.IOException;
import java.util.function.Consumer;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import com.flipper.FlipperConfig;
import com.flipper.helpers.Persistor;
import com.flipper.helpers.UiUtilities;
import com.flipper.models.Transaction;

import net.runelite.client.game.ItemManager;
import net.runelite.client.util.ImageUtil;

public class BuysController extends TransactionController {
    Consumer<Transaction> highAlchCallback;

    public BuysController(ItemManager itemManager, Consumer<Transaction> highAlchCallback, FlipperConfig config) throws IOException {
        super("Buy", itemManager, config.isPromptDeleteBuy());
        this.highAlchCallback = highAlchCallback;
    }

    @Override
    public void extraComponentPressed(Transaction transaction) {
        transaction.setAlched(true);
        highAlchCallback.accept(transaction);
    }

    @Override
    public JButton renderExtraComponent() {
        JButton alchButton = new JButton();
        ImageIcon alchIcon = new ImageIcon(ImageUtil.loadImageResource(getClass(), UiUtilities.highAlchIcon));
        alchButton.setIcon(alchIcon);
        return alchButton;
    }

    @Override
    public void loadTransactions() throws IOException {
        this.transactions = Persistor.loadBuys();
        this.filteredTransactions = this.transactions;
        this.buildView();
    }

    @Override
    public void saveTransactions() {
        Persistor.saveBuys(this.transactions);
    }
}