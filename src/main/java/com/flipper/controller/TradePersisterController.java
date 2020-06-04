package com.flipper.controller;

import net.runelite.client.RuneLite;

import java.io.File;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.RuneLite;

/**
 * Read/Writes information to json file for storage
 */
@Slf4j
public class TradePersisterController {
    public static final File PARENT_DIRECTORY = new File(RuneLite.RUNELITE_DIR, "flipper");

    /**
     * Creates flipping directory if it doesn't exist and partitions trades.json into individual files
     * for each account, if it exists.
     *
     * @throws IOException handled in FlippingPlugin
     */
    public static void setup() throws IOException {
        if (!PARENT_DIRECTORY.exists()) {
            log.info("Flipper directory doesn't exist yet so it's being created");
            if (!PARENT_DIRECTORY.mkdir()) {
                throw new IOException("unable to create parent directory!");
            }
        }
    }
}
