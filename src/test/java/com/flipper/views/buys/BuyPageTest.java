package com.flipper.views.buys;

import com.flipper.models.Transaction;
import com.flipper.views.ViewTestSetup;

import java.util.ArrayList;
import java.util.List;

public class BuyPageTest {
    static Transaction buy;
    static List<Transaction> buys;

    public static void setUp() {
        buys = new ArrayList<Transaction>();
        buys.add(new Transaction(1, ViewTestSetup.mockItemId, 100, 1000, ViewTestSetup.testItemName, true, true));
        buys.add(new Transaction(1, ViewTestSetup.mockItemId, 101, 1001, ViewTestSetup.testItemName, true, false));
    }

    public static void main(String[] args) throws Exception {
        // setUp();
        // ViewTestSetup.setUp();
        // BuyPage buyPage = new BuyPage(ViewTestSetup.itemManager);
        // buyPage.rebuildPanel(buys);
        // ViewTestSetup.launch(buyPage);
    }
}