package com.flipper;

import java.io.IOException;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class FlipperPluginTest {
    /**
     * Loads plugin with mock data from mocks/*.json
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception, IOException {
        ExternalPluginManager.loadBuiltin(FlipperPlugin.class);
        RuneLite.main(args);
    }
}