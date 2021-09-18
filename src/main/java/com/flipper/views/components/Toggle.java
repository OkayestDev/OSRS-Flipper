package com.flipper.views.components;

import java.awt.event.*;
import javax.swing.JCheckBox;

public class Toggle extends JCheckBox {

    public Toggle(String label, Boolean isToggled, Runnable onToggleRunnable) {
        super(label, isToggled);
        setBounds(100, 100, 50, 50);

        addItemListener(
            new ItemListener() {
                public void itemStateChanged(ItemEvent event) {
                    onToggleRunnable.run();
                }
            }
        );
    }
}
