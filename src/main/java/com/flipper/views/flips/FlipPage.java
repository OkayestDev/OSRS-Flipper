package com.flipper.views.flips;

import com.flipper.helpers.Numbers;
import com.flipper.helpers.UiUtilities;
import com.flipper.views.components.SearchBar;
import com.flipper.views.components.Toggle;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.util.ImageUtil;

public class FlipPage extends JPanel {

    private JPanel container;
    private JLabel totalProfitValueLabel;

    private Consumer<String> onSearchTextChanged;
    private Runnable refreshFlipsRunnable;
    private Runnable toggleIsTrackingFlipsRunnable;
    private Toggle trackFlipsToggle;

    public FlipPage(
        Runnable refreshFlipsRunnable,
        Consumer<String> onSearchTextChanged,
        Runnable toggleIsTrackingFlipsRunnable,
        Boolean isTrackingFlips
    ) {
        this.refreshFlipsRunnable = refreshFlipsRunnable;
        this.onSearchTextChanged = onSearchTextChanged;
        this.toggleIsTrackingFlipsRunnable = toggleIsTrackingFlipsRunnable;
        this.setLayout(new BorderLayout());
        this.setBackground(ColorScheme.DARK_GRAY_COLOR);
        constructTotalProfit("0");
        this.build(isTrackingFlips);
    }

    public void addFlipPanel(FlipPanel flipPanel) {
        container.add(flipPanel, BorderLayout.CENTER);
        this.revalidate();
    }

    public void resetContainer(boolean isTrackingFlips) {
        trackFlipsToggle.setSelected(isTrackingFlips);
        trackFlipsToggle.revalidate();
        container.removeAll();
    }

    private void constructTotalProfit(String totalProfit) {
        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        JPanel totalProfitContainer = new JPanel(new GridLayout(2, 1));

        JPanel totalProfitHeader = new JPanel();

        JLabel totalProfitLabel = new JLabel("Flip Profit");
        totalProfitLabel.setFont(new Font(FontManager.getRunescapeBoldFont().getName(), FontManager.getRunescapeBoldFont().getStyle(), 16));
        totalProfitLabel.setHorizontalAlignment(JLabel.CENTER);
        totalProfitLabel.setForeground(Color.WHITE);

        ImageIcon refreshIcon = new ImageIcon(ImageUtil.loadImageResource(getClass(), UiUtilities.refreshIcon));
        JLabel refreshFlips = new JLabel();
        refreshFlips.setToolTipText("Refresh flips");
        refreshFlips.setIcon(refreshIcon);
        refreshFlips.addMouseListener(
            new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    try {
                        refreshFlipsRunnable.run();
                    } catch (Exception error) {}
                }
            }
        );

        totalProfitHeader.add(totalProfitLabel);
        totalProfitHeader.add(refreshFlips);

        totalProfitValueLabel = new JLabel("0");
        totalProfitValueLabel.setFont(new Font(FontManager.getRunescapeFont().getName(), FontManager.getRunescapeFont().getStyle(), 24));
        totalProfitValueLabel.setHorizontalAlignment(JLabel.CENTER);
        Color totalProfitColor = ColorScheme.GRAND_EXCHANGE_ALCH;
        totalProfitValueLabel.setForeground(totalProfitColor);

        totalProfitContainer.add(totalProfitHeader);
        totalProfitContainer.add(totalProfitValueLabel);
        totalProfitContainer.setBorder(UiUtilities.ITEM_INFO_BORDER);
        container.add(totalProfitContainer, BorderLayout.NORTH);

        SearchBar searchBar = new SearchBar(this.onSearchTextChanged);
        container.add(searchBar, BorderLayout.CENTER);

        this.trackFlipsToggle = new Toggle("Track Flips", true, this.toggleIsTrackingFlipsRunnable);
        container.add(trackFlipsToggle, BorderLayout.SOUTH);

        container.setBorder(new EmptyBorder(0, 0, 3, 0));
        this.add(container, BorderLayout.NORTH);
    }

    public void setTotalProfit(String totalProfit) {
        totalProfitValueLabel.setText(Numbers.numberWithCommas(totalProfit));
    }

    public void build(Boolean isTrackingFlips) {
        JPanel scrollContainer = new JPanel();
        scrollContainer.setLayout(new BorderLayout());
        container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
        container.setBackground(ColorScheme.DARK_GRAY_COLOR);

        JScrollPane scrollPane = new JScrollPane(scrollContainer);
        scrollPane.setBackground(ColorScheme.LIGHT_GRAY_COLOR);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        scrollContainer.add(container, BorderLayout.PAGE_START);
        this.add(scrollPane, BorderLayout.CENTER);

        this.trackFlipsToggle.setSelected(isTrackingFlips);
    }
}
