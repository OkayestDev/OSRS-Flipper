package com.flipper.views.alchs;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Font;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.flipper.helpers.Numbers;
import com.flipper.helpers.UiUtilities;

import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.util.ImageUtil;

public class AlchPage extends JPanel {
    private JPanel container;
    private JLabel totalProfitValueLabel;

    private Runnable refreshAlchsRunnable;

    public AlchPage(Runnable refreshAlchsRunnable) {
        this.refreshAlchsRunnable = refreshAlchsRunnable;
        this.setLayout(new BorderLayout());
        this.setBackground(ColorScheme.DARK_GRAY_COLOR);
    }

    public void addFlipPanel(AlchPanel alchPanel) {
        container.add(alchPanel, BorderLayout.CENTER);
        this.revalidate();
    }

    public void constructTotalProfit(String totalProfit) {
        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        JPanel totalProfitContainer = new JPanel(new GridLayout(2, 1));

        JPanel totalProfitHeader = new JPanel();
        
        JLabel totalProfitLabel = new JLabel("Alch Profit");
        totalProfitLabel.setFont(new Font(FontManager.getRunescapeBoldFont().getName(),
                FontManager.getRunescapeBoldFont().getStyle(), 16));
        totalProfitLabel.setHorizontalAlignment(JLabel.CENTER);
        totalProfitLabel.setForeground(Color.WHITE);
        

        ImageIcon refreshIcon = new ImageIcon(ImageUtil.loadImageResource(getClass(), UiUtilities.refreshIcon));
        JLabel refreshFlips = new JLabel();
        refreshFlips.setToolTipText("Refresh alchs");
        refreshFlips.setIcon(refreshIcon);
        refreshFlips.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    refreshAlchsRunnable.run();
                } catch (Exception error) {}
            }
        });

        totalProfitHeader.add(totalProfitLabel);
        totalProfitHeader.add(refreshFlips);

        totalProfitValueLabel = new JLabel("0");
        totalProfitValueLabel.setFont(
                new Font(FontManager.getRunescapeFont().getName(), FontManager.getRunescapeFont().getStyle(), 24));
        totalProfitValueLabel.setHorizontalAlignment(JLabel.CENTER);
        Color totalProfitColor = ColorScheme.GRAND_EXCHANGE_ALCH;
        totalProfitValueLabel.setForeground(totalProfitColor);

        totalProfitContainer.add(totalProfitHeader);
        totalProfitContainer.add(totalProfitValueLabel);
        totalProfitContainer.setBorder(UiUtilities.ITEM_INFO_BORDER);
        container.add(totalProfitContainer);

        container.setBorder(new EmptyBorder(0, 0, 3, 0));
        this.add(container, BorderLayout.NORTH);
    }

    public void setTotalProfit(String totalProfit) {
        totalProfitValueLabel.setText(Numbers.numberWithCommas(totalProfit));
    }

    public void build() {
        constructTotalProfit("0");
        container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));

        JScrollPane scrollPane = new JScrollPane(container);
        scrollPane.setBackground(ColorScheme.LIGHT_GRAY_COLOR);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        this.add(scrollPane, BorderLayout.CENTER);
    }
}
