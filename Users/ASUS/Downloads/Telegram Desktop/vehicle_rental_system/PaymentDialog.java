import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PaymentDialog extends JDialog {

    public String chosenMethod = null;
    public boolean confirmed = false;

    private final DashboardFrame dashboard;
    private final Vehicle vehicle;
    private final int days;
    private final double total;
    private final String userEmail;
    private final String userName;

    private CardLayout stepLayout;
    private JPanel stepPanel;
    private JLabel stepIndicator;

    public PaymentDialog(DashboardFrame parent, Vehicle v, int d, double tot, String email, String name) {
        super(parent, "Payment", true);
        this.dashboard = parent;
        this.vehicle = v;
        this.days = d;
        this.total = tot;
        this.userEmail = email;
        this.userName = name;

        setSize(520, 560);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UiTheme.BG);
        root.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        setContentPane(root);

        root.add(buildHeader(), BorderLayout.NORTH);

        stepLayout = new CardLayout();
        stepPanel = new JPanel(stepLayout);
        stepPanel.setBackground(UiTheme.BG);
        stepPanel.setBorder(BorderFactory.createEmptyBorder(20, 28, 20, 28));

        stepPanel.add(buildSummaryStep(), "SUMMARY");
        stepPanel.add(buildMethodStep("Cash", "💵", buildCashContent()), "CASH");
        stepPanel.add(buildMethodStep("Mobile Banking", "📱", buildMobileContent()), "MOBILE");
        stepPanel.add(buildMethodStep("Card", "💳", buildCardContent()), "CARD");

        root.add(stepPanel, BorderLayout.CENTER);
        stepLayout.show(stepPanel, "SUMMARY");
    }

    private JPanel buildHeader() {
        JPanel h = new JPanel(new BorderLayout());
        h.setBackground(UiTheme.SURFACE);
        h.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, UiTheme.BORDER),
            BorderFactory.createEmptyBorder(16, 24, 16, 24)));

        JButton back = UiComponents.secondaryButton("← Back to Dashboard");
        back.addActionListener(e -> dispose());

        JLabel title = new JLabel("Complete Payment");
        title.setFont(UiTheme.h3());
        title.setForeground(UiTheme.TEXT_PRIMARY);

        JPanel titleWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        titleWrap.setOpaque(false);
        titleWrap.add(back);
        titleWrap.add(title);

        stepIndicator = new JLabel("Step 1 of 2");
        stepIndicator.setFont(UiTheme.small());
        stepIndicator.setForeground(UiTheme.TEXT_SECONDARY);

        h.add(titleWrap, BorderLayout.WEST);
        h.add(stepIndicator, BorderLayout.EAST);
        return h;
    }

    private JPanel buildSummaryStep() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        p.add(UiComponents.subtitle("Review your booking before choosing a payment method."));
        p.add(Box.createVerticalStrut(20));

        JPanel summary = UiComponents.cardPanel();
        summary.setLayout(new BoxLayout(summary, BoxLayout.Y_AXIS));
        summary.setAlignmentX(Component.LEFT_ALIGNMENT);

        summary.add(UiComponents.summaryRow("Customer", userName, false));
        summary.add(Box.createVerticalStrut(6));
        summary.add(UiComponents.summaryRow("Vehicle", vehicle.vehicleName, false));
        summary.add(Box.createVerticalStrut(6));
        summary.add(UiComponents.summaryRow("Duration", days + (days == 1 ? " day" : " days"), false));
        summary.add(Box.createVerticalStrut(6));
        summary.add(UiComponents.summaryRow("Rate", String.format("%,.0f Tk / day", vehicle.getPricePerDay()), false));
        summary.add(Box.createVerticalStrut(10));
        summary.add(UiComponents.divider());
        summary.add(Box.createVerticalStrut(10));
        summary.add(UiComponents.summaryRow("Total due", String.format("%,.0f Tk", total), true));

        p.add(summary);
        p.add(Box.createVerticalStrut(24));

        p.add(UiComponents.fieldLabel("Select payment method"));
        p.add(Box.createVerticalStrut(10));

        JPanel methods = new JPanel(new GridLayout(1, 3, 12, 0));
        methods.setOpaque(false);
        methods.setAlignmentX(Component.LEFT_ALIGNMENT);
        methods.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        methods.add(methodCard("💵", "Cash", "CASH"));
        methods.add(methodCard("📱", "Mobile", "MOBILE"));
        methods.add(methodCard("💳", "Card", "CARD"));
        p.add(methods);

        return p;
    }

    private JPanel methodCard(String icon, String label, String step) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UiTheme.SURFACE_ALT);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UiTheme.BORDER, 1, true),
            BorderFactory.createEmptyBorder(14, 8, 14, 8)));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel ic = new JLabel(icon, SwingConstants.CENTER);
        ic.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        ic.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel lb = new JLabel(label, SwingConstants.CENTER);
        lb.setFont(UiTheme.small());
        lb.setForeground(UiTheme.TEXT_PRIMARY);
        lb.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(ic);
        card.add(Box.createVerticalStrut(4));
        card.add(lb);

        card.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(20, 184, 166, 30));
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(UiTheme.PRIMARY_LIGHT, 2, true),
                    BorderFactory.createEmptyBorder(13, 7, 13, 7)));
            }
            @Override public void mouseExited(MouseEvent e) {
                card.setBackground(UiTheme.SURFACE_ALT);
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(UiTheme.BORDER, 1, true),
                    BorderFactory.createEmptyBorder(14, 8, 14, 8)));
            }
            @Override public void mouseClicked(MouseEvent e) {
                chosenMethod = label;
                stepIndicator.setText("Step 2 of 2");
                stepLayout.show(stepPanel, step);
            }
        });
        return card;
    }

    private JPanel buildMethodStep(String title, String icon, JPanel content) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        JButton back = UiComponents.ghostButton("← Back");
        back.setAlignmentX(Component.LEFT_ALIGNMENT);
        back.addActionListener(e -> {
            stepIndicator.setText("Step 1 of 2");
            stepLayout.show(stepPanel, "SUMMARY");
        });
        p.add(back);
        p.add(Box.createVerticalStrut(12));

        JLabel heading = new JLabel(icon + "  " + title);
        heading.setFont(UiTheme.h3());
        heading.setForeground(UiTheme.TEXT_PRIMARY);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(heading);
        p.add(Box.createVerticalStrut(16));
        p.add(content);

        return p;
    }

    private JPanel buildCashContent() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel info = UiComponents.cardPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel msg = new JLabel("<html>Pay <b>" + String.format("%,.0f Tk", total) +
            "</b> at the rental counter when collecting your vehicle.</html>");
        msg.setFont(UiTheme.body());
        info.add(msg);
        p.add(info);
        p.add(Box.createVerticalStrut(20));
        p.add(confirmButton(() -> finish("Cash", "Counter payment")));
        return p;
    }

    private JPanel buildMobileContent() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel info = new JLabel("<html>Send <b>" + String.format("%,.0f Tk", total) +
            "</b> to bKash/Nagad: <b>01700-000000</b></html>");
        info.setFont(UiTheme.body());
        info.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(info);
        p.add(Box.createVerticalStrut(16));

        p.add(UiComponents.fieldLabel("Your mobile number"));
        p.add(Box.createVerticalStrut(6));
        JTextField mobile = UiComponents.textField("01XXXXXXXXX");
        mobile.setAlignmentX(Component.LEFT_ALIGNMENT);
        mobile.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        p.add(mobile);
        p.add(Box.createVerticalStrut(20));

        JLabel err = UiComponents.inlineMessage("", true);
        err.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(err);
        p.add(Box.createVerticalStrut(8));

        p.add(confirmButton(() -> {
            String mob = UiComponents.fieldValue(mobile, "01XXXXXXXXX");
            if (mob.isEmpty()) {
                err.setText("Enter your mobile number.");
                err.setVisible(true);
                return;
            }
            if (!mob.matches("^01[3-9]\\d{8}$")) {
                err.setText("Enter a valid 11-digit BD mobile number.");
                err.setVisible(true);
                return;
            }
            finish("Mobile Banking", mob);
        }));
        return p;
    }

    private JPanel buildCardContent() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        p.add(UiComponents.fieldLabel("Card number"));
        p.add(Box.createVerticalStrut(6));
        JTextField cardNum = UiComponents.textField("1234 5678 9012 3456");
        cardNum.setDocument(new LimitedDocument(19));
        cardNum.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) {
                String raw = cardNum.getText().replaceAll("[^\\d]", "");
                if (raw.length() > 16) raw = raw.substring(0, 16);
                StringBuilder fmt = new StringBuilder();
                for (int i = 0; i < raw.length(); i++) {
                    if (i > 0 && i % 4 == 0) fmt.append(' ');
                    fmt.append(raw.charAt(i));
                }
                cardNum.setText(fmt.toString());
                cardNum.setCaretPosition(cardNum.getText().length());
            }
        });
        cardNum.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardNum.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        p.add(cardNum);
        p.add(Box.createVerticalStrut(12));

        p.add(UiComponents.fieldLabel("Name on card"));
        p.add(Box.createVerticalStrut(6));
        JTextField name = UiComponents.textField("JOHN DOE");
        name.setAlignmentX(Component.LEFT_ALIGNMENT);
        name.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        p.add(name);
        p.add(Box.createVerticalStrut(12));

        JPanel row = new JPanel(new GridLayout(1, 2, 12, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JPanel exP = new JPanel();
        exP.setOpaque(false);
        exP.setLayout(new BoxLayout(exP, BoxLayout.Y_AXIS));
        exP.add(UiComponents.fieldLabel("Expiry (MM/YY)"));
        exP.add(Box.createVerticalStrut(6));
        JTextField expiry = UiComponents.textField("08/27");
        expiry.setDocument(new LimitedDocument(5));
        exP.add(expiry);

        JPanel cvP = new JPanel();
        cvP.setOpaque(false);
        cvP.setLayout(new BoxLayout(cvP, BoxLayout.Y_AXIS));
        cvP.add(UiComponents.fieldLabel("CVV"));
        cvP.add(Box.createVerticalStrut(6));
        JPasswordField cvv = UiComponents.passwordField("•••");
        cvv.setDocument(new LimitedDocument(4));
        cvP.add(cvv);

        row.add(exP);
        row.add(cvP);
        p.add(row);
        p.add(Box.createVerticalStrut(16));

        JLabel err = UiComponents.inlineMessage("", true);
        err.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(err);
        p.add(Box.createVerticalStrut(8));

        p.add(confirmButton(() -> {
            String num  = cardNum.getText().replaceAll("\\s", "");
            String nm   = UiComponents.fieldValue(name, "JOHN DOE");
            String exp  = UiComponents.fieldValue(expiry, "08/27");
            String cv   = UiComponents.passwordValue(cvv, "•••");
            if (num.length() < 16 || nm.isEmpty() || exp.length() < 5 || cv.length() < 3) {
                err.setText("Please fill in all card details correctly.");
                err.setVisible(true);
                return;
            }
            String masked = "**** **** **** " + num.substring(num.length() - 4);
            finish("Credit/Debit Card", masked);
        }));
        return p;
    }

    private JPanel confirmButton(Runnable action) {
        JButton btn = UiComponents.primaryButton("Confirm & Pay  " + String.format("%,.0f Tk", total));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        btn.addActionListener(e -> action.run());
        JPanel wrap = new JPanel();
        wrap.setOpaque(false);
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));
        wrap.add(btn);
        return wrap;
    }

    private void finish(String method, String ref) {
        confirmed = true;
        String bookingId = BookingStore.generateId();
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"));
        Booking booking = new Booking(bookingId, vehicle.vehicleName, vehicle.getPricePerDay(),
            days, total, method, ref, dateTime, userEmail);
        BookingStore.add(booking);
        dashboard.onBookingCompleted();
        dispose();
        new PaymentSlip(dashboard, booking, userName).setVisible(true);
    }

    static class LimitedDocument extends javax.swing.text.PlainDocument {
        final int max;
        LimitedDocument(int max) { this.max = max; }
        @Override public void insertString(int off, String str, javax.swing.text.AttributeSet a)
                throws javax.swing.text.BadLocationException {
            if (str != null && getLength() + str.length() <= max) super.insertString(off, str, a);
        }
    }
}
