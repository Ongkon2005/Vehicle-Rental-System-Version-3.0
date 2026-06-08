import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/** Reusable Swing widgets for DriveEase. */
public final class UiComponents {

    private UiComponents() {}

    // ── Panels ──────────────────────────────────────────────────────────────

    public static JPanel cardPanel() {
        JPanel p = new JPanel();
        p.setBackground(UiTheme.SURFACE);
        p.setBorder(BorderFactory.createCompoundBorder(
            new ShadowBorder(4, 0.06f),
            BorderFactory.createEmptyBorder(UiTheme.PAD_LG, UiTheme.PAD_LG, UiTheme.PAD_LG, UiTheme.PAD_LG)));
        return p;
    }

    public static JPanel heroPanel() {
        return new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, UiTheme.PRIMARY_DARK, getWidth(), getHeight(), UiTheme.PRIMARY_LIGHT);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(255, 255, 255, 12));
                g2.fillOval(-60, getHeight() / 2 - 120, 280, 280);
                g2.fillOval(getWidth() - 180, -80, 260, 260);
                g2.dispose();
            }
        };
    }

    // ── Labels ──────────────────────────────────────────────────────────────

    public static JLabel title(String text) {
        JLabel l = new JLabel(text);
        l.setFont(UiTheme.h2());
        l.setForeground(UiTheme.TEXT_PRIMARY);
        return l;
    }

    public static JLabel subtitle(String text) {
        JLabel l = new JLabel(text);
        l.setFont(UiTheme.body());
        l.setForeground(UiTheme.TEXT_SECONDARY);
        return l;
    }

    public static JLabel fieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(UiTheme.small());
        l.setForeground(UiTheme.TEXT_SECONDARY);
        return l;
    }

    public static JLabel badge(String text, Color bg, Color fg) {
        JLabel l = new JLabel("  " + text + "  ", SwingConstants.CENTER) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        l.setFont(UiTheme.caption());
        l.setForeground(fg);
        l.setOpaque(false);
        return l;
    }

    // ── Inputs ──────────────────────────────────────────────────────────────

    public static JTextField textField(String placeholder) {
        JTextField f = new JTextField();
        styleField(f, placeholder, false);
        return f;
    }

    public static JPasswordField passwordField(String placeholder) {
        JPasswordField f = new JPasswordField();
        styleField(f, placeholder, true);
        return f;
    }

    private static void styleField(JTextField f, String placeholder, boolean isPassword) {
        f.setFont(UiTheme.body());
        f.setForeground(UiTheme.TEXT_PRIMARY);
        f.setCaretColor(UiTheme.PRIMARY);
        f.setBackground(UiTheme.SURFACE_ALT);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UiTheme.BORDER, 1, true),
            BorderFactory.createEmptyBorder(10, 14, 10, 14)));
        f.setPreferredSize(new Dimension(0, 44));

        if (placeholder != null && !placeholder.isEmpty()) {
            f.setForeground(UiTheme.TEXT_SECONDARY);
            f.setText(placeholder);
            if (isPassword) ((JPasswordField) f).setEchoChar((char) 0);

            f.addFocusListener(new FocusAdapter() {
                @Override public void focusGained(FocusEvent e) {
                    if (f.getText().equals(placeholder)) {
                        f.setText("");
                        f.setForeground(UiTheme.TEXT_PRIMARY);
                        if (isPassword) ((JPasswordField) f).setEchoChar('●');
                    }
                    f.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(UiTheme.PRIMARY_LIGHT, 2, true),
                        BorderFactory.createEmptyBorder(9, 13, 9, 13)));
                }
                @Override public void focusLost(FocusEvent e) {
                    f.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(UiTheme.BORDER, 1, true),
                        BorderFactory.createEmptyBorder(10, 14, 10, 14)));
                    if (f.getText().isEmpty()) {
                        f.setText(placeholder);
                        f.setForeground(UiTheme.TEXT_SECONDARY);
                        if (isPassword) ((JPasswordField) f).setEchoChar((char) 0);
                    }
                }
            });
        }
    }

    public static String fieldValue(JTextField f, String placeholder) {
        String t = f.getText().trim();
        return t.equals(placeholder) ? "" : t;
    }

    public static String passwordValue(JPasswordField f, String placeholder) {
        String t = new String(f.getPassword()).trim();
        return t.equals(placeholder) ? "" : t;
    }

    // ── Buttons ─────────────────────────────────────────────────────────────

    public static JButton primaryButton(String text) {
        JButton btn = new JButton(text) {
            boolean hover = false;
            {
                setOpaque(false);
                setContentAreaFilled(false);
                setBorderPainted(false);
                setFocusPainted(false);
                setFont(UiTheme.bodyBold());
                setForeground(UiTheme.TEXT_ON_PRIMARY);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                setPreferredSize(new Dimension(0, 46));
                addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
                    @Override public void mouseExited(MouseEvent e)  { hover = false; repaint(); }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hover ? UiTheme.PRIMARY_LIGHT : UiTheme.PRIMARY);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), UiTheme.RADIUS, UiTheme.RADIUS);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        return btn;
    }

    public static JButton secondaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(UiTheme.body());
        btn.setForeground(UiTheme.PRIMARY);
        btn.setBackground(UiTheme.SURFACE);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UiTheme.PRIMARY_LIGHT, 1, true),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(0, 44));
        return btn;
    }

    public static JButton ghostButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(UiTheme.small());
        btn.setForeground(UiTheme.TEXT_SECONDARY);
        btn.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static JButton sidebarNav(String icon, String label, boolean active) {
        JButton btn = new JButton("  " + icon + "   " + label) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (Boolean.TRUE.equals(getClientProperty("navActive"))) {
                    g2.setColor(UiTheme.SIDEBAR_ACTIVE);
                    g2.fillRoundRect(8, 4, getWidth() - 16, getHeight() - 8, 8, 8);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        setSidebarActive(btn, active);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFont(UiTheme.body());
        btn.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        return btn;
    }

    public static void setSidebarActive(JButton btn, boolean active) {
        btn.putClientProperty("navActive", active);
        btn.setForeground(active ? Color.WHITE : UiTheme.SIDEBAR_TEXT);
        btn.repaint();
    }

    // ── Alerts ──────────────────────────────────────────────────────────────

    public static JLabel inlineMessage(String text, boolean error) {
        JLabel l = new JLabel(text);
        l.setFont(UiTheme.small());
        l.setForeground(error ? UiTheme.ERROR : UiTheme.SUCCESS);
        l.setVisible(false);
        return l;
    }

    public static void showToast(Component parent, String msg, boolean success) {
        JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(parent), msg,
            success ? "Success" : "Notice",
            success ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
    }

    // ── Misc ────────────────────────────────────────────────────────────────

    public static JSeparator divider() {
        JSeparator s = new JSeparator();
        s.setForeground(UiTheme.BORDER);
        s.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return s;
    }

    /** Small stat card with teal left accent for dashboard metrics. */
    public static JPanel statCard(String label, String value) {
        JLabel val = new JLabel(value);
        val.setFont(UiTheme.h2());
        val.setForeground(UiTheme.PRIMARY);
        return statCard(label, val);
    }

    public static JPanel statCard(String label, JLabel valueLbl) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UiTheme.SURFACE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 4, 0, 0, UiTheme.PRIMARY),
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.BORDER, 1, true),
                BorderFactory.createEmptyBorder(14, 16, 14, 16))));
        valueLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lbl = new JLabel(label);
        lbl.setFont(UiTheme.caption());
        lbl.setForeground(UiTheme.TEXT_SECONDARY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(valueLbl);
        card.add(Box.createVerticalStrut(4));
        card.add(lbl);
        return card;
    }

    public static JPanel summaryRow(String key, String value, boolean highlight) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        JLabel kl = new JLabel(key);
        kl.setFont(highlight ? UiTheme.bodyBold() : UiTheme.small());
        kl.setForeground(UiTheme.TEXT_SECONDARY);
        JLabel vl = new JLabel(value);
        vl.setFont(highlight ? UiTheme.h3() : UiTheme.body());
        vl.setForeground(highlight ? UiTheme.PRIMARY : UiTheme.TEXT_PRIMARY);
        row.add(kl, BorderLayout.WEST);
        row.add(vl, BorderLayout.EAST);
        return row;
    }

    /** Soft drop shadow for cards. */
    static class ShadowBorder extends AbstractBorder {
        private final int spread;
        private final float alpha;

        ShadowBorder(int spread, float alpha) {
            this.spread = spread;
            this.alpha = alpha;
        }

        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            for (int i = spread; i > 0; i--) {
                g2.setColor(new Color(0, 0, 0, (int) (alpha * 255 / i)));
                g2.draw(new RoundRectangle2D.Float(x + i, y + i, w - 2 * i - 1, h - 2 * i - 1, UiTheme.RADIUS, UiTheme.RADIUS));
            }
            g2.setColor(UiTheme.BORDER);
            g2.draw(new RoundRectangle2D.Float(x, y, w - 1, h - 1, UiTheme.RADIUS, UiTheme.RADIUS));
            g2.dispose();
        }

        @Override public Insets getBorderInsets(Component c) {
            return new Insets(spread + 1, spread + 1, spread + 1, spread + 1);
        }
    }
}
