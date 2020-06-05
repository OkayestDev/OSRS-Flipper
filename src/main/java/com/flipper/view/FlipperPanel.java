package com.flipper.view;

import com.flipper.FlipperPlugin;
import com.google.common.base.Strings;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.IconTextField;
import net.runelite.client.ui.components.PluginErrorPanel;

public class FlipperPanel extends JPanel {
    private FlipperPlugin plugin;

    @Getter
	private static final String WELCOME_PANEL = "WELCOME_PANEL";
	private static final String ITEMS_PANEL = "ITEMS_PANEL";
	private static final int DEBOUNCE_DELAY_MS = 250;
	private static final Border TOP_PANEL_BORDER = new CompoundBorder(
		BorderFactory.createMatteBorder(0, 0, 1, 0, ColorScheme.BRAND_ORANGE),
		BorderFactory.createEmptyBorder(4, 0, 0, 0));

	//Main item panel that holds all the shown items.
	private final JPanel flippingItemsPanel = new JPanel();

	private final IconTextField searchBar = new IconTextField();
	private Future<?> runningRequest = null;

	//Constraints for items in the item panel.
	private final GridBagConstraints constraints = new GridBagConstraints();
	public final CardLayout cardLayout = new CardLayout();

	@Getter
	public final JPanel centerPanel = new JPanel(cardLayout);

	// // Keeps track of all items currently displayed on the panel.
	// private ArrayList<FlippingItemPanel> activePanels = new ArrayList<>();

	@Getter
	JLabel resetIcon;

    public FlipperPanel(final FlipperPlugin plugin, ScheduledExecutorService executor) {
        super(false);
        this.plugin = plugin;

        setLayout(new BorderLayout());
        setBackground(ColorScheme.DARK_GRAY_COLOR);

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;
        constraints.gridx = 0;
        constraints.gridy = 0;

        // Contains the main content panel and top panel
        JPanel container = new JPanel();
        container.setLayout(new BorderLayout(0, 5));
        container.setBorder(new EmptyBorder(0, 0, 5, 0));
        container.setBackground(ColorScheme.DARK_GRAY_COLOR);

        // Holds all the item panels
        flippingItemsPanel.setLayout(new GridBagLayout());
        flippingItemsPanel.setBorder((new EmptyBorder(0, 5, 0, 3)));
        flippingItemsPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(ColorScheme.DARK_GRAY_COLOR);
        wrapper.add(flippingItemsPanel, BorderLayout.NORTH);

        JScrollPane scrollWrapper = new JScrollPane(wrapper);
        scrollWrapper.setBackground(ColorScheme.DARK_GRAY_COLOR);
        scrollWrapper.getVerticalScrollBar().setPreferredSize(new Dimension(5, 0));
        scrollWrapper.getVerticalScrollBar().setBorder(new EmptyBorder(0, 0, 0, 0));

        // Search bar beneath the tab manager.
        // searchBar.setIcon(IconTextField.Icon.SEARCH);
        // searchBar.setPreferredSize(new Dimension(PluginPanel.PANEL_WIDTH - 20, 30));
        // searchBar.setBackground(ColorScheme.DARK_GRAY_COLOR);
        // searchBar.setBorder(BorderFactory.createMatteBorder(0, 5, 5, 5, ColorScheme.DARKER_GRAY_COLOR.darker()));
        // searchBar.setHoverBackgroundColor(ColorScheme.DARKER_GRAY_HOVER_COLOR);
        // searchBar.setMinimumSize(new Dimension(0, 35));
        // searchBar.addActionListener(e -> executor.execute(this::updateSearch));
        // searchBar.addClearListener(e -> updateSearch());
        // searchBar.addKeyListener(key -> {
        //     if (runningRequest != null) {
        //         runningRequest.cancel(false);
        //     }
        //     runningRequest = executor.schedule(this::updateSearch, DEBOUNCE_DELAY_MS, TimeUnit.MILLISECONDS);
        // });

        // Contains a greeting message when the items panel is empty.
        JPanel welcomeWrapper = new JPanel(new BorderLayout());
        welcomeWrapper.setBackground(ColorScheme.DARK_GRAY_COLOR);
        PluginErrorPanel welcomePanel = new PluginErrorPanel();
        welcomeWrapper.add(welcomePanel, BorderLayout.NORTH);

        welcomePanel.setContent("Flipping Utilities", "For items to show up, margin check an item.");

        // Clears the config and resets the items panel.
        // resetIcon = new JLabel(RESET_ICON);
        // resetIcon.setToolTipText("Reset trade history");
        // resetIcon.setPreferredSize(ICON_SIZE);
        // resetIcon.addMouseListener(new MouseAdapter() {
        //     @Override
        //     public void mousePressed(MouseEvent e) {
        //         if (SwingUtilities.isLeftMouseButton(e)) {
        //             // Display warning message
        //             final int result = JOptionPane.showOptionDialog(resetIcon,
        //                     "Are you sure you want to reset the flipping panel?", "Are you sure?",
        //                     JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, new String[] { "Yes", "No" },
        //                     "No");

        //             // If the user pressed "Yes"
        //             if (result == JOptionPane.YES_OPTION) {
        //                 resetPanel();
        //                 cardLayout.show(centerPanel, FlippingPanel.getWELCOME_PANEL());
        //                 rebuild(plugin.getTradesForCurrentView());
        //             }
        //         }
        //     }

        //     @Override
        //     public void mouseEntered(MouseEvent e) {
        //         resetIcon.setIcon(RESET_HOVER_ICON);
        //     }

        //     @Override
        //     public void mouseExited(MouseEvent e) {
        //         resetIcon.setIcon(RESET_ICON);
        //     }
        // });

        // final JMenuItem clearMenuOption = new JMenuItem("Reset all panels");
        // clearMenuOption.addActionListener(e -> {
        //     resetPanel();
        //     plugin.getStatPanel().resetPanel();
        //     rebuild(plugin.getTradesForCurrentView());
        //     plugin.getStatPanel().rebuild(plugin.getTradesForCurrentView());
        // });

        // final JPopupMenu popupMenu = new JPopupMenu();
        // popupMenu.setBorder(new EmptyBorder(5, 5, 5, 5));
        // popupMenu.add(clearMenuOption);

        // resetIcon.setComponentPopupMenu(popupMenu);

        // // Top panel that holds the plugin title and reset button.
        // final JPanel topPanel = new JPanel(new BorderLayout());
        // topPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR.darker());
        // topPanel.add(resetIcon, BorderLayout.EAST);
        // topPanel.add(searchBar, BorderLayout.CENTER);
        // topPanel.setBorder(TOP_PANEL_BORDER);

        // centerPanel.add(scrollWrapper, ITEMS_PANEL);
        // centerPanel.add(welcomeWrapper, WELCOME_PANEL);

        // // To switch between greeting and items panels
        // cardLayout.show(centerPanel, WELCOME_PANEL);

        // container.add(topPanel, BorderLayout.NORTH);
        // container.add(centerPanel, BorderLayout.CENTER);

        add(container, BorderLayout.CENTER);
    }
}