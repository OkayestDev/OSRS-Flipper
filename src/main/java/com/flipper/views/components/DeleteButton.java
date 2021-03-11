package com.flipper.views.components;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import com.flipper.helpers.UiUtilities;

import net.runelite.client.util.ImageUtil;
import java.awt.event.*;

public class DeleteButton extends JButton {
    public DeleteButton(ActionListener onClick) {
        ImageIcon deleteIcon = new ImageIcon(ImageUtil.getResourceStreamFromClass(getClass(), UiUtilities.deleteX));
        setIcon(deleteIcon);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(true);
        setOpaque(false);
        addActionListener(onClick);
    }
}
