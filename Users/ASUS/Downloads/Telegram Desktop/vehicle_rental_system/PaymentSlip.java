import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PaymentSlip extends JDialog {

    private final DashboardFrame dashboard;
    private final Booking booking;
    private final String userName;

    public PaymentSlip(DashboardFrame parent, Booking booking, String name) {
        super(parent, "Payment Receipt", ModalityType.APPLICATION_MODAL);
        this.dashboard = parent;
        this.booking = booking;
        this.userName = name;

        setSize(440, 620);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UiTheme.BG);
        setContentPane(root);

        root.add(buildHeader(), BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(buildReceipt());
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        root.add(scroll, BorderLayout.CENTER);

        root.add(buildFooter(), BorderLayout.SOUTH);
    }

    private JPanel buildHeader() {
        JPanel h = new JPanel(new BorderLayout());
        h.setBackground(UiTheme.SURFACE);
        h.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, UiTheme.BORDER),
            BorderFactory.createEmptyBorder(14, 20, 14, 20)));

        JButton back = UiComponents.secondaryButton("← Back");
        back.addActionListener(e -> goBackToDashboard());

        JLabel title = new JLabel("Payment Receipt");
        title.setFont(UiTheme.h3());
        title.setForeground(UiTheme.TEXT_PRIMARY);

        JLabel badge = UiComponents.badge("PAID", UiTheme.SUCCESS_BG, UiTheme.SUCCESS);

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        left.setOpaque(false);
        left.add(back);
        left.add(title);

        h.add(left, BorderLayout.WEST);
        h.add(badge, BorderLayout.EAST);
        return h;
    }

    private JPanel buildReceipt() {
        JPanel outer = new JPanel();
        outer.setBackground(UiTheme.BG);
        outer.setLayout(new BoxLayout(outer, BoxLayout.Y_AXIS));
        outer.setBorder(BorderFactory.createEmptyBorder(20, 24, 12, 24));

        JPanel paper = UiComponents.cardPanel();
        paper.setLayout(new BoxLayout(paper, BoxLayout.Y_AXIS));

        JLabel logo = new JLabel("🚗  " + UiTheme.APP_NAME, SwingConstants.CENTER);
        logo.setFont(UiTheme.h3());
        logo.setForeground(UiTheme.PRIMARY);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        paper.add(logo);
        paper.add(Box.createVerticalStrut(4));
        JLabel sub = new JLabel("Vehicle Rental", SwingConstants.CENTER);
        sub.setFont(UiTheme.caption());
        sub.setForeground(UiTheme.TEXT_SECONDARY);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        paper.add(sub);
        paper.add(Box.createVerticalStrut(20));
        paper.add(UiComponents.divider());
        paper.add(Box.createVerticalStrut(12));

        paper.add(slipRow("Booking ID", booking.bookingId, true));
        paper.add(slipRow("Date", booking.dateTime, false));
        paper.add(slipRow("Customer", userName, false));
        paper.add(slipRow("Contact", booking.userEmail, false));
        paper.add(Box.createVerticalStrut(8));
        paper.add(UiComponents.divider());
        paper.add(Box.createVerticalStrut(8));

        paper.add(sectionHead("Vehicle"));
        paper.add(slipRow("Name", booking.vehicleName, false));
        paper.add(slipRow("Rate", String.format("%,.0f Tk / day", booking.pricePerDay), false));
        paper.add(slipRow("Period", booking.days + (booking.days == 1 ? " day" : " days"), false));

        LocalDateTime start = LocalDateTime.now();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd MMM yyyy");
        paper.add(slipRow("From", start.format(df), false));
        paper.add(slipRow("Until", start.plusDays(booking.days).format(df), false));
        paper.add(Box.createVerticalStrut(8));
        paper.add(UiComponents.divider());
        paper.add(Box.createVerticalStrut(8));

        paper.add(sectionHead("Payment"));
        paper.add(slipRow("Method", booking.paymentMethod, false));
        paper.add(slipRow("Reference", booking.reference, false));
        paper.add(Box.createVerticalStrut(12));
        paper.add(UiComponents.summaryRow("Total Paid", String.format("%,.0f Tk", booking.total), true));
        paper.add(Box.createVerticalStrut(16));

        JLabel thanks = new JLabel("Thank you for choosing " + UiTheme.APP_NAME + "!", SwingConstants.CENTER);
        thanks.setFont(UiTheme.small());
        thanks.setForeground(UiTheme.TEXT_SECONDARY);
        thanks.setAlignmentX(Component.CENTER_ALIGNMENT);
        paper.add(thanks);

        outer.add(paper);
        return outer;
    }

    private JPanel slipRow(String key, String val, boolean highlight) {
        JPanel row = UiComponents.summaryRow(key, val, highlight);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        return row;
    }

    private JLabel sectionHead(String text) {
        JLabel l = new JLabel(text.toUpperCase());
        l.setFont(UiTheme.caption());
        l.setForeground(UiTheme.TEXT_SECONDARY);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JPanel buildFooter() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        bar.setBackground(UiTheme.SURFACE);
        bar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UiTheme.BORDER));

        JButton myBookings = UiComponents.secondaryButton("View My Bookings");
        myBookings.addActionListener(e -> {
            dispose();
            dashboard.showBookingsView();
            dashboard.toFront();
        });

        JButton close = UiComponents.primaryButton("Done");
        close.setPreferredSize(new Dimension(120, 44));
        close.addActionListener(e -> goBackToDashboard());

        bar.add(myBookings);
        bar.add(close);
        return bar;
    }

    private void goBackToDashboard() {
        dispose();
        dashboard.setVisible(true);
        dashboard.toFront();
    }
}
