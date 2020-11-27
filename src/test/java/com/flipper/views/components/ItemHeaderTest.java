package com.flipper.views.components;

import javax.swing.JButton;

import com.flipper.models.Transaction;
import com.flipper.views.ViewTestSetup;

public class ItemHeaderTest {
    static Transaction buy;

    public static void setUp() {
        buy = new Transaction(1, 1000, ViewTestSetup.mockItemId, 100, ViewTestSetup.testItemName, true, true);
    }

    public static void main(String[] args) throws Exception {
        setUp();
        ViewTestSetup.setUp();
        ItemHeader itemHeader = new ItemHeader(buy, ViewTestSetup.itemManager, true, new JButton());
        ViewTestSetup.launch(itemHeader);
    }
}