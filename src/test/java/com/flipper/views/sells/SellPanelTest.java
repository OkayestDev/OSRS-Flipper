package com.flipper.views.sells;

import com.flipper.models.Transaction;
import com.flipper.views.ViewTestSetup;

public class SellPanelTest {
    public static void main(String[] args) throws Exception {
        ViewTestSetup.setUp();
        Transaction sell = new Transaction(
            1,
            1000,
            ViewTestSetup.mockItemId,
            100,
            ViewTestSetup.testItemName,
            true,
            true
        );
        SellPanel sellPanel = new SellPanel(sell, ViewTestSetup.itemManager);
        ViewTestSetup.launch(sellPanel);
    }
}