package com.flipper;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("Flipper")
public interface FlipperConfig extends Config {
    @ConfigItem(
        keyName = "isPromptDeleteBuy",
        name = "Delete Buy Prompt",
        description = "Shows confirmation prompt before deleting buy"
    )
    default boolean isPromptDeleteBuy() {
        return true;
    }

    @ConfigItem(
        keyName = "isPromptDeleteSell",
        name = "Delete Sell Prompt",
        description = "Shows confirmation prompt before deleting sell"
    )
    default boolean isPromptDeleteSell() {
        return true;
    }

    @ConfigItem(
        keyName = "isPromptDeleteAlch",
        name = "Delete Alch Prompt",
        description = "Shows confirmation prompt before deleting alch"
    )
    default boolean isPromptDeleteAlch() {
        return true;
    }

    @ConfigItem(
        keyName = "isPromptDeleteFlip",
        name = "Delete Flip Prompt",
        description = "Shows confirmation prompt before deleting flip"
    )
    default boolean isPromptDeleteFlip() {
        return true;
    }

    @ConfigItem(
        keyName = "isPromptDeleteMargin",
        name = "Delete Margin Prompt",
        description = "Shows confirmation prompt before deleting margin"
    )
    default boolean isPromptDeleteMargin() {
        return true;
    }
}
