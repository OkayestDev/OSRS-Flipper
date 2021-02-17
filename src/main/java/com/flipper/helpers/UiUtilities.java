package com.flipper.helpers;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

import net.runelite.client.ui.ColorScheme;

public class UiUtilities {
    public static final Dimension ICON_SIZE = new Dimension(32, 32);
    public static final Border ITEM_INFO_BORDER = new CompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 0, ColorScheme.DARK_GRAY_COLOR),
            BorderFactory.createLineBorder(ColorScheme.DARKER_GRAY_COLOR.darker(), 4));
    public static final String flipperNavIcon = "/flipper_nav_button.png";
    public static final String deleteX = "/delete_x.png";
    public static final int ITEMS_PER_PAGE = 15;
}