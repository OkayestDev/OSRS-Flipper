package com.flipper.views.components;

import com.flipper.helpers.UiUtilities;
import java.awt.event.*;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import net.runelite.client.util.ImageUtil;

public class DeleteButton extends JButton {

    public DeleteButton(ActionListener onClick) {
        ImageIcon deleteIcon = new ImageIcon(ImageUtil.loadImageResource(getClass(), UiUtilities.deleteX));
        setIcon(deleteIcon);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(true);
        setOpaque(false);
        addActionListener(onClick);
    }
}
