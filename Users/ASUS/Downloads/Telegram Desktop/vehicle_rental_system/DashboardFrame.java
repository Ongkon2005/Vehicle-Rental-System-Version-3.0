import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import javax.imageio.ImageIO;

public class DashboardFrame extends JFrame {

    private static final String VIEW_BROWSE = "BROWSE";
    private static final String VIEW_BOOKINGS = "BOOKINGS";

    private final String userEmail;
    private final String userName;

    private Vehicle selectedVehicle = null;
    private JLabel summaryVehicle;
    private JLabel summaryRate;
    private JLabel summaryDays;
    private JLabel summarySubtotal;
    private JLabel summaryTax;
    private JLabel summaryTotal;
    private JLabel statsBookingsLbl;
    private JSpinner daysSpinner;

    private CardLayout mainLayout;
    private JPanel mainContent;
    private JPanel bookingsListPanel;
    private JLabel headingLbl;
    private JLabel hintLbl;
    private JButton backBtn;
    private JButton browseNavBtn;
    private JButton bookingsNavBtn;
    private String currentView = VIEW_BROWSE;

    private static final Vehicle[][] CATALOG = {
        {new Car("Toyota Camry", 5000),   new Car("Honda Civic", 4500)},
        {new Bike("Yamaha R15", 1000),    new Bike("Suzuki Gixxer", 1200)},
        {new Truck("Volvo Truck", 8000),  new Truck("Tata Truck", 7000)}
    };

    private static final String[][] IMAGES = {
        {"images/car_toyota.png",  "images/car_honda.png"},
        {"images/bike_yamaha.png", "images/bike_suzuki.png"},
        {"images/truck_volvo.png", "images/truck_tata.png"}
    };

    private static final String[] CATEGORIES = {"Cars", "Bikes", "Trucks"};
    private static final String[] CATEGORY_ICONS = {"🚗", "🏍", "🚛"};

    public DashboardFrame(String email, String name) {
        this.userEmail = email;
        this.userName = name;

        setTitle(UiTheme.APP_NAME + " – Dashboard");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 700);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UiTheme.BG);
        setContentPane(root);

        root.add(buildSidebar(), BorderLayout.WEST);
        root.add(buildMain(), BorderLayout.CENTER);
        showView(VIEW_BROWSE);
        updateSummary();
    }

    /** Called after a successful payment so My Bookings stays up to date. */
    public void onBookingCompleted() {
        refreshBookingsList();
        refreshStatsBar();
    }

    public void showBookingsView() {
        showView(VIEW_BOOKINGS);
    }

    private void showView(String view) {
        currentView = view;
        boolean browse = VIEW_BROWSE.equals(view);

        mainLayout.show(mainContent, view);
        updateNavButtons(browse);

        if (browse) {
            headingLbl.setText("Browse Vehicles");
            hintLbl.setText("Select a category, pick a vehicle, and book");
            backBtn.setText("← Back to Login");
            backBtn.setToolTipText("Return to sign-in page");
        } else {
            headingLbl.setText("My Bookings");
            hintLbl.setText("Your completed rental bookings");
            backBtn.setText("← Back to Browse");
            backBtn.setToolTipText("Return to vehicle browser");
            refreshBookingsList();
        }
    }

    private void updateNavButtons(boolean browseActive) {
        UiComponents.setSidebarActive(browseNavBtn, browseActive);
        UiComponents.setSidebarActive(bookingsNavBtn, !browseActive);
    }

    private JPanel buildSidebar() {
        JPanel side = new JPanel();
        side.setBackground(UiTheme.SIDEBAR_BG);
        side.setPreferredSize(new Dimension(240, 0));
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBorder(BorderFactory.createEmptyBorder(24, 12, 24, 12));

        JLabel logo = new JLabel("  🚗  " + UiTheme.APP_NAME);
        logo.setFont(UiTheme.h3());
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        side.add(logo);
        side.add(Box.createVerticalStrut(32));

        browseNavBtn = UiComponents.sidebarNav("📋", "Browse Vehicles", true);
        browseNavBtn.addActionListener(e -> showView(VIEW_BROWSE));
        side.add(browseNavBtn);
        side.add(Box.createVerticalStrut(4));

        bookingsNavBtn = UiComponents.sidebarNav("📅", "My Bookings", false);
        bookingsNavBtn.addActionListener(e -> showView(VIEW_BOOKINGS));
        side.add(bookingsNavBtn);
        side.add(Box.createVerticalGlue());

        JPanel userCard = new JPanel();
        userCard.setLayout(new BoxLayout(userCard, BoxLayout.Y_AXIS));
        userCard.setBackground(new Color(255, 255, 255, 10));
        userCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 20), 1, true),
            BorderFactory.createEmptyBorder(12, 14, 12, 14)));
        userCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        userCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        JLabel avatar = new JLabel("👤");
        avatar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        JLabel nameLbl = new JLabel(userName);
        nameLbl.setFont(UiTheme.bodyBold());
        nameLbl.setForeground(Color.WHITE);
        JLabel emailLbl = new JLabel(userEmail);
        emailLbl.setFont(UiTheme.caption());
        emailLbl.setForeground(UiTheme.SIDEBAR_TEXT);

        userCard.add(avatar);
        userCard.add(Box.createVerticalStrut(4));
        userCard.add(nameLbl);
        userCard.add(emailLbl);
        side.add(userCard);
        side.add(Box.createVerticalStrut(12));

        JButton logout = UiComponents.secondaryButton("Logout");
        logout.setForeground(UiTheme.SIDEBAR_TEXT);
        logout.setBackground(new Color(0, 0, 0, 0));
        logout.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 30), 1, true));
        logout.setAlignmentX(Component.LEFT_ALIGNMENT);
        logout.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        logout.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        side.add(logout);

        return side;
    }

    private JPanel buildMain() {
        JPanel main = new JPanel(new BorderLayout(0, 0));
        main.setBackground(UiTheme.BG);
        main.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        backBtn = UiComponents.secondaryButton("← Back to Login");
        backBtn.addActionListener(e -> {
            if (VIEW_BROWSE.equals(currentView)) {
                dispose();
                new LoginFrame().setVisible(true);
            } else {
                showView(VIEW_BROWSE);
            }
        });

        headingLbl = new JLabel("Browse Vehicles");
        headingLbl.setFont(UiTheme.h2());
        headingLbl.setForeground(UiTheme.TEXT_PRIMARY);
        hintLbl = new JLabel("Select a category, pick a vehicle, and book");
        hintLbl.setFont(UiTheme.small());
        hintLbl.setForeground(UiTheme.TEXT_SECONDARY);

        JPanel titles = new JPanel();
        titles.setOpaque(false);
        titles.setLayout(new BoxLayout(titles, BoxLayout.Y_AXIS));
        titles.add(headingLbl);
        titles.add(hintLbl);

        JPanel headerLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        headerLeft.setOpaque(false);
        headerLeft.add(backBtn);
        headerLeft.add(titles);

        header.add(headerLeft, BorderLayout.WEST);
        main.add(header, BorderLayout.NORTH);

        mainLayout = new CardLayout();
        mainContent = new JPanel(mainLayout);
        mainContent.setOpaque(false);
        mainContent.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        mainContent.add(buildBrowsePage(), VIEW_BROWSE);
        mainContent.add(buildBookingsPage(), VIEW_BOOKINGS);

        main.add(mainContent, BorderLayout.CENTER);
        return main;
    }

    private JPanel buildBrowsePage() {
        JPanel page = new JPanel(new BorderLayout(0, 16));
        page.setOpaque(false);
        page.add(buildStatsBar(), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(20, 0));
        content.setOpaque(false);
        content.add(buildVehicleArea(), BorderLayout.CENTER);
        content.add(buildBookingPanel(), BorderLayout.EAST);
        page.add(content, BorderLayout.CENTER);
        return page;
    }

    private static int countCatalogVehicles() {
        int n = 0;
        for (Vehicle[] row : CATALOG) n += row.length;
        return n;
    }

    private JPanel buildStatsBar() {
        JPanel bar = new JPanel(new GridLayout(1, 3, 16, 0));
        bar.setOpaque(false);
        bar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 88));

        bar.add(UiComponents.statCard("Available Vehicles", String.valueOf(countCatalogVehicles())));

        statsBookingsLbl = new JLabel(String.valueOf(BookingStore.countByUser(userEmail)));
        bar.add(UiComponents.statCard("My Bookings", statsBookingsLbl));

        bar.add(UiComponents.statCard("Vehicle Types", String.valueOf(CATEGORIES.length)));
        return bar;
    }

    private void refreshStatsBar() {
        if (statsBookingsLbl != null) {
            statsBookingsLbl.setText(String.valueOf(BookingStore.countByUser(userEmail)));
        }
    }

    private JPanel buildBookingsPage() {
        JPanel page = new JPanel(new BorderLayout());
        page.setOpaque(false);

        bookingsListPanel = new JPanel();
        bookingsListPanel.setOpaque(false);
        bookingsListPanel.setLayout(new BoxLayout(bookingsListPanel, BoxLayout.Y_AXIS));

        JScrollPane scroll = new JScrollPane(bookingsListPanel);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        page.add(scroll, BorderLayout.CENTER);
        return page;
    }

    private void refreshBookingsList() {
        if (bookingsListPanel == null) return;

        bookingsListPanel.removeAll();
        List<Booking> bookings = BookingStore.getByUser(userEmail);

        if (bookings.isEmpty()) {
            JPanel empty = UiComponents.cardPanel();
            empty.setLayout(new BoxLayout(empty, BoxLayout.Y_AXIS));
            empty.setAlignmentX(Component.LEFT_ALIGNMENT);
            empty.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));

            JLabel icon = new JLabel("📅", SwingConstants.CENTER);
            icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
            icon.setAlignmentX(Component.CENTER_ALIGNMENT);
            JLabel msg = new JLabel("No bookings yet", SwingConstants.CENTER);
            msg.setFont(UiTheme.h3());
            msg.setForeground(UiTheme.TEXT_PRIMARY);
            msg.setAlignmentX(Component.CENTER_ALIGNMENT);
            JLabel sub = new JLabel("Book a vehicle from Browse Vehicles to see it here.", SwingConstants.CENTER);
            sub.setFont(UiTheme.body());
            sub.setForeground(UiTheme.TEXT_SECONDARY);
            sub.setAlignmentX(Component.CENTER_ALIGNMENT);

            empty.add(icon);
            empty.add(Box.createVerticalStrut(12));
            empty.add(msg);
            empty.add(Box.createVerticalStrut(6));
            empty.add(sub);

            bookingsListPanel.add(empty);
        } else {
            for (int i = bookings.size() - 1; i >= 0; i--) {
                bookingsListPanel.add(buildBookingCard(bookings.get(i)));
                bookingsListPanel.add(Box.createVerticalStrut(12));
            }
        }

        bookingsListPanel.revalidate();
        bookingsListPanel.repaint();
    }

    private JPanel buildBookingCard(Booking b) {
        JPanel card = UiComponents.cardPanel();
        card.setLayout(new BorderLayout(16, 0));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel idLbl = new JLabel(b.bookingId);
        idLbl.setFont(UiTheme.bodyBold());
        idLbl.setForeground(UiTheme.PRIMARY);

        JLabel vehicleLbl = new JLabel(b.vehicleName);
        vehicleLbl.setFont(UiTheme.h3());
        vehicleLbl.setForeground(UiTheme.TEXT_PRIMARY);

        JLabel meta = new JLabel(b.days + (b.days == 1 ? " day" : " days") + "  •  " + b.dateTime);
        meta.setFont(UiTheme.small());
        meta.setForeground(UiTheme.TEXT_SECONDARY);

        left.add(idLbl);
        left.add(Box.createVerticalStrut(4));
        left.add(vehicleLbl);
        left.add(Box.createVerticalStrut(4));
        left.add(meta);

        JPanel right = new JPanel();
        right.setOpaque(false);
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));

        JLabel totalLbl = new JLabel(String.format("%,.0f Tk", b.total));
        totalLbl.setFont(UiTheme.h3());
        totalLbl.setForeground(UiTheme.PRIMARY);
        totalLbl.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JLabel methodLbl = new JLabel(b.paymentMethod);
        methodLbl.setFont(UiTheme.caption());
        methodLbl.setForeground(UiTheme.TEXT_SECONDARY);
        methodLbl.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JLabel status = UiComponents.badge("Confirmed", UiTheme.SUCCESS_BG, UiTheme.SUCCESS);
        status.setAlignmentX(Component.RIGHT_ALIGNMENT);

        right.add(totalLbl);
        right.add(Box.createVerticalStrut(4));
        right.add(methodLbl);
        right.add(Box.createVerticalStrut(6));
        right.add(status);

        card.add(left, BorderLayout.CENTER);
        card.add(right, BorderLayout.EAST);
        return card;
    }

    private JPanel buildVehicleArea() {
        JPanel area = new JPanel(new BorderLayout(0, 16));
        area.setOpaque(false);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(UiTheme.bodyBold());
        tabs.setBackground(UiTheme.BG);
        tabs.setForeground(UiTheme.TEXT_PRIMARY);

        for (int i = 0; i < CATEGORIES.length; i++) {
            final int idx = i;
            JPanel tabContent = new JPanel(new BorderLayout());
            tabContent.setOpaque(false);

            JPanel grid = new JPanel(new GridLayout(1, 2, 16, 0));
            grid.setOpaque(false);
            ButtonGroup group = new ButtonGroup();

            for (int j = 0; j < 2; j++) {
                Vehicle v = CATALOG[idx][j];
                JToggleButton card = buildVehicleCard(v, IMAGES[idx][j], CATEGORY_ICONS[idx]);
                group.add(card);
                final Vehicle vehicle = v;
                card.addItemListener(ev -> {
                    if (card.isSelected()) {
                        selectedVehicle = vehicle;
                        updateSummary();
                    }
                });
                grid.add(card);
            }

            tabContent.add(grid, BorderLayout.NORTH);
            tabs.addTab(CATEGORY_ICONS[i] + "  " + CATEGORIES[i], tabContent);
        }

        tabs.addChangeListener(e -> {
            selectedVehicle = null;
            updateSummary();
        });

        area.add(tabs, BorderLayout.NORTH);
        return area;
    }

    private JToggleButton buildVehicleCard(Vehicle v, String imgPath, String fallbackIcon) {
        JToggleButton card = new JToggleButton() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(isSelected() ? new Color(20, 184, 166, 25) : UiTheme.SURFACE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), UiTheme.RADIUS, UiTheme.RADIUS);
                g2.setColor(isSelected() ? UiTheme.PRIMARY : UiTheme.BORDER);
                g2.setStroke(new BasicStroke(isSelected() ? 2f : 1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, UiTheme.RADIUS, UiTheme.RADIUS);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setLayout(new BorderLayout());
        card.setOpaque(false);
        card.setContentAreaFilled(false);
        card.setBorderPainted(false);
        card.setFocusPainted(false);
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.setPreferredSize(new Dimension(280, 220));

        JPanel imgArea = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                BufferedImage img = loadImage(imgPath);
                if (img != null) {
                    int pad = 16;
                    int aw = getWidth() - 2 * pad;
                    int ah = getHeight() - 2 * pad;
                    double scale = Math.min((double) aw / img.getWidth(), (double) ah / img.getHeight());
                    int dw = (int) (img.getWidth() * scale);
                    int dh = (int) (img.getHeight() * scale);
                    int x = pad + (aw - dw) / 2;
                    int y = pad + (ah - dh) / 2;
                    g2.drawImage(img, x, y, dw, dh, null);
                } else {
                    g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
                    g2.setColor(UiTheme.TEXT_SECONDARY);
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(fallbackIcon, (getWidth() - fm.stringWidth(fallbackIcon)) / 2, getHeight() / 2 + 12);
                }
                g2.dispose();
            }
        };
        imgArea.setOpaque(false);
        imgArea.setPreferredSize(new Dimension(0, 130));

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBorder(BorderFactory.createEmptyBorder(8, 16, 14, 16));

        JLabel nameLbl = new JLabel(v.vehicleName);
        nameLbl.setFont(UiTheme.bodyBold());
        nameLbl.setForeground(UiTheme.TEXT_PRIMARY);
        JLabel priceLbl = new JLabel(String.format("%,.0f Tk / day", v.getPricePerDay()));
        priceLbl.setFont(UiTheme.small());
        priceLbl.setForeground(UiTheme.PRIMARY);
        JLabel selLbl = new JLabel("Click to select");
        selLbl.setFont(UiTheme.caption());
        selLbl.setForeground(UiTheme.TEXT_SECONDARY);

        info.add(nameLbl);
        info.add(Box.createVerticalStrut(2));
        info.add(priceLbl);
        info.add(Box.createVerticalStrut(2));
        info.add(selLbl);

        card.add(imgArea, BorderLayout.CENTER);
        card.add(info, BorderLayout.SOUTH);
        card.addItemListener(ev -> card.repaint());

        return card;
    }

    private JPanel buildBookingPanel() {
        JPanel panel = UiComponents.cardPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(280, 0));

        JLabel title = new JLabel("Booking Summary");
        title.setFont(UiTheme.h3());
        title.setForeground(UiTheme.TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalStrut(20));

        JPanel row1 = UiComponents.summaryRow("Vehicle", "—", false);
        JPanel row2 = UiComponents.summaryRow("Daily rate", "—", false);
        JPanel row3 = UiComponents.summaryRow("Duration", "—", false);
        row1.setAlignmentX(Component.LEFT_ALIGNMENT);
        row2.setAlignmentX(Component.LEFT_ALIGNMENT);
        row3.setAlignmentX(Component.LEFT_ALIGNMENT);
        summaryVehicle = (JLabel) ((BorderLayout) row1.getLayout()).getLayoutComponent(BorderLayout.EAST);
        summaryRate    = (JLabel) ((BorderLayout) row2.getLayout()).getLayoutComponent(BorderLayout.EAST);
        summaryDays    = (JLabel) ((BorderLayout) row3.getLayout()).getLayoutComponent(BorderLayout.EAST);
        panel.add(row1);
        panel.add(Box.createVerticalStrut(8));
        panel.add(row2);
        panel.add(Box.createVerticalStrut(8));
        panel.add(row3);
        panel.add(Box.createVerticalStrut(16));
        panel.add(UiComponents.divider());
        panel.add(Box.createVerticalStrut(12));

        JPanel daysRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        daysRow.setOpaque(false);
        daysRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        daysRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        JLabel daysLbl = UiComponents.fieldLabel("Rental days");
        daysSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 90, 1));
        daysSpinner.setFont(UiTheme.bodyBold());
        daysSpinner.setPreferredSize(new Dimension(70, 36));
        daysSpinner.addChangeListener(e -> updateSummary());
        daysRow.add(daysLbl);
        daysRow.add(daysSpinner);
        panel.add(daysRow);
        panel.add(Box.createVerticalStrut(12));
        panel.add(UiComponents.divider());
        panel.add(Box.createVerticalStrut(12));

        JPanel subRow = UiComponents.summaryRow("Subtotal", "—", false);
        JPanel taxRow = UiComponents.summaryRow("Tax (5%)", "—", false);
        subRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        taxRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        summarySubtotal = (JLabel) ((BorderLayout) subRow.getLayout()).getLayoutComponent(BorderLayout.EAST);
        summaryTax      = (JLabel) ((BorderLayout) taxRow.getLayout()).getLayoutComponent(BorderLayout.EAST);
        panel.add(subRow);
        panel.add(Box.createVerticalStrut(6));
        panel.add(taxRow);
        panel.add(Box.createVerticalStrut(8));

        summaryTotal = new JLabel("Total: —");
        summaryTotal.setFont(UiTheme.h3());
        summaryTotal.setForeground(UiTheme.PRIMARY);
        summaryTotal.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(summaryTotal);
        panel.add(Box.createVerticalGlue());

        JButton bookBtn = UiComponents.primaryButton("Proceed to Payment");
        bookBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        bookBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        bookBtn.addActionListener(e -> {
            if (selectedVehicle == null) {
                UiComponents.showToast(this, "Please select a vehicle first.", false);
                return;
            }
            int days = (int) daysSpinner.getValue();
            double subtotal = selectedVehicle.getPricePerDay() * days;
            double total = subtotal + subtotal * 0.05;
            PaymentDialog dlg = new PaymentDialog(this, selectedVehicle, days, total, userEmail, userName);
            dlg.setVisible(true);
        });
        panel.add(bookBtn);

        return panel;
    }

    private void updateSummary() {
        int days = (int) daysSpinner.getValue();
        if (selectedVehicle == null) {
            summaryVehicle.setText("—");
            summaryRate.setText("—");
            summaryDays.setText(days + (days == 1 ? " day" : " days"));
            summarySubtotal.setText("—");
            summaryTax.setText("—");
            summaryTotal.setText("Total: —");
            return;
        }
        summaryVehicle.setText(selectedVehicle.vehicleName);
        summaryRate.setText(String.format("%,.0f Tk", selectedVehicle.getPricePerDay()));
        summaryDays.setText(days + (days == 1 ? " day" : " days"));
        double subtotal = selectedVehicle.getPricePerDay() * days;
        double tax = subtotal * 0.05;
        double total = subtotal + tax;
        summarySubtotal.setText(String.format("%,.0f Tk", subtotal));
        summaryTax.setText(String.format("%,.0f Tk", tax));
        summaryTotal.setText(String.format("Total: %,.0f Tk", total));
    }

    private final java.util.Map<String, BufferedImage> imageCache = new java.util.HashMap<>();

    private BufferedImage loadImage(String path) {
        if (imageCache.containsKey(path)) return imageCache.get(path);
        try {
            File f = new File(path);
            if (!f.exists()) return null;
            BufferedImage img = ImageIO.read(f);
            imageCache.put(path, img);
            return img;
        } catch (Exception e) {
            return null;
        }
    }
}
