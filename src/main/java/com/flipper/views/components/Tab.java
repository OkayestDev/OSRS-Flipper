package com.flipper.views.components;

import com.google.common.base.Strings;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.BooleanSupplier;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import lombok.Getter;
import lombok.Setter;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.components.materialtabs.MaterialTab;
import net.runelite.client.ui.components.materialtabs.MaterialTabGroup;

public class Tab extends MaterialTab {

    private static final Border SELECTED_BORDER = new CompoundBorder(
        BorderFactory.createMatteBorder(0, 0, 1, 0, ColorScheme.BRAND_ORANGE),
        BorderFactory.createEmptyBorder(5, 5, 4, 5)
    );

    private static final Border UNSELECTED_BORDER = BorderFactory.createEmptyBorder(5, 5, 5, 5);

    /* The tab's containing group */
    private final MaterialTabGroup group;

    /* The tab's associated content display */
    @Getter
    private final JComponent content;

    /* To be executed when the tab is selected */
    @Setter
    private BooleanSupplier onSelectEvent;

    @Getter
    private boolean selected;

    public Tab(String string, MaterialTabGroup group, JComponent content) {
        super(string, group, content);
        this.group = group;
        this.content = content;

        if (selected) {
            select();
        } else {
            unselect();
        }

        addMouseListener(
            new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent mouseEvent) {
                    group.select(Tab.this);
                }
            }
        );

        if (!Strings.isNullOrEmpty(string)) {
            addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        Tab tab = (Tab) e.getSource();
                        tab.setForeground(Color.WHITE);
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        Tab tab = (Tab) e.getSource();
                        if (!tab.isSelected()) {
                            tab.setForeground(Color.GRAY);
                        }
                    }
                }
            );
        }
    }

    public Tab(ImageIcon icon, MaterialTabGroup group, JComponent content) {
        this("", group, content);
        setIcon(icon);
        setOpaque(true);
        setVerticalAlignment(SwingConstants.CENTER);
        setHorizontalAlignment(SwingConstants.CENTER);
        setBackground(ColorScheme.DARKER_GRAY_COLOR);

        addMouseListener(
            new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    Tab tab = (Tab) e.getSource();
                    tab.setBackground(ColorScheme.DARKER_GRAY_HOVER_COLOR);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    Tab tab = (Tab) e.getSource();
                    tab.setBackground(ColorScheme.DARKER_GRAY_COLOR);
                }
            }
        );
    }

    @Override
    public boolean select() {
        if (onSelectEvent != null) {
            if (!onSelectEvent.getAsBoolean()) {
                return false;
            }
        }

        setBorder(SELECTED_BORDER);
        setForeground(Color.WHITE);
        return selected = true;
    }

    @Override
    public void unselect() {
        setBorder(UNSELECTED_BORDER);
        setForeground(Color.GRAY);
        selected = false;
    }
}
