package com.flipper.views.buys;

import com.flipper.models.Transaction;
import com.flipper.views.ViewTestSetup;

import java.util.ArrayList;
import java.util.List;

public class BuysPanelTest {
    static Transaction buy;
    static List<Transaction> buys;

    public static void setUp() {
        buys = new ArrayList<Transaction>();
        buys.add(new Transaction(
            1,
            ViewTestSetup.mockItemId,
            100,
            1000,
            ViewTestSetup.testItemName,
            true,
            true
        ));
        buys.add(new Transaction(
            1,
            ViewTestSetup.mockItemId,
            101,
            1001,
            ViewTestSetup.testItemName,
            true,
            false
        ));
    }

    public static void main(String[] args) throws Exception {
        setUp();
        ViewTestSetup.setUp();
        BuysPanel buysPanel = new BuysPanel(ViewTestSetup.itemManager);
        buysPanel.rebuildPanel(buys);
        ViewTestSetup.launch(buysPanel);
    }
}