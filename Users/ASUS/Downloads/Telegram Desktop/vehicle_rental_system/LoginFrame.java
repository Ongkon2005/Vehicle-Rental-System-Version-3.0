import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private static String registeredEmail = "";
    private static String registeredPassword = "";
    private static String registeredName = "";

    private CardLayout cardLayout;
    private JPanel cardPanel;

    public LoginFrame() {
        setTitle(UiTheme.APP_NAME + " – Vehicle Rental");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(960, 640);
        setMinimumSize(new Dimension(860, 580));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UiTheme.BG);
        setContentPane(root);

        root.add(buildHero(), BorderLayout.WEST);
        root.add(buildFormArea(), BorderLayout.CENTER);
    }

    private JPanel buildHero() {
        JPanel hero = UiComponents.heroPanel();
        hero.setPreferredSize(new Dimension(380, 0));
        hero.setLayout(new BoxLayout(hero, BoxLayout.Y_AXIS));
        hero.setBorder(BorderFactory.createEmptyBorder(48, 40, 48, 40));

        JLabel icon = new JLabel("🚗", SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 56));
        icon.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel brand = new JLabel(UiTheme.APP_NAME);
        brand.setFont(UiTheme.h1());
        brand.setForeground(Color.WHITE);
        brand.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel tagline = new JLabel("<html>Your trusted partner for<br>cars, bikes &amp; trucks.</html>");
        tagline.setFont(UiTheme.body());
        tagline.setForeground(new Color(255, 255, 255, 200));
        tagline.setAlignmentX(Component.LEFT_ALIGNMENT);

        hero.add(icon);
        hero.add(Box.createVerticalStrut(20));
        hero.add(brand);
        hero.add(Box.createVerticalStrut(8));
        hero.add(tagline);
        hero.add(Box.createVerticalGlue());

        JPanel features = new JPanel();
        features.setOpaque(false);
        features.setLayout(new BoxLayout(features, BoxLayout.Y_AXIS));
        features.setAlignmentX(Component.LEFT_ALIGNMENT);
        for (String feat : new String[]{"✓  Instant booking", "✓  Flexible rental periods", "✓  Multiple payment options"}) {
            JLabel f = new JLabel(feat);
            f.setFont(UiTheme.small());
            f.setForeground(new Color(255, 255, 255, 180));
            f.setAlignmentX(Component.LEFT_ALIGNMENT);
            features.add(f);
            features.add(Box.createVerticalStrut(6));
        }
        hero.add(features);

        return hero;
    }

    private JPanel buildFormArea() {
        JPanel area = new JPanel(new GridBagLayout());
        area.setBackground(UiTheme.BG);
        area.setBorder(BorderFactory.createEmptyBorder(32, 48, 32, 48));

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setOpaque(false);
        cardPanel.setPreferredSize(new Dimension(420, 520));
        cardPanel.setMinimumSize(new Dimension(380, 400));
        cardPanel.add(buildLoginCard(), "LOGIN");
        cardPanel.add(buildRegisterCard(), "REGISTER");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        area.add(cardPanel, gbc);

        cardLayout.show(cardPanel, "LOGIN");
        return area;
    }

    private JPanel buildLoginCard() {
        JPanel card = UiComponents.cardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(420, 480));
        card.setMinimumSize(new Dimension(380, 400));

        JLabel welcome = UiComponents.title("Welcome back");
        welcome.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(welcome);
        card.add(Box.createVerticalStrut(4));
        JLabel signInHint = UiComponents.subtitle("Sign in to manage your rentals");
        signInHint.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(signInHint);
        card.add(Box.createVerticalStrut(28));

        JLabel emailLbl = UiComponents.fieldLabel("Email or Phone");
        emailLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(emailLbl);
        card.add(Box.createVerticalStrut(6));
        JTextField emailField = UiComponents.textField("you@email.com");
        emailField.setAlignmentX(Component.LEFT_ALIGNMENT);
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        card.add(emailField);
        card.add(Box.createVerticalStrut(16));

        JLabel passLbl = UiComponents.fieldLabel("Password");
        passLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(passLbl);
        card.add(Box.createVerticalStrut(6));
        JPasswordField passField = UiComponents.passwordField("Enter password");
        passField.setAlignmentX(Component.LEFT_ALIGNMENT);
        passField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        card.add(passField);
        card.add(Box.createVerticalStrut(8));

        JLabel loginMsg = UiComponents.inlineMessage("", false);
        loginMsg.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(loginMsg);
        card.add(Box.createVerticalStrut(16));

        JButton loginBtn = UiComponents.primaryButton("Sign In");
        loginBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        card.add(loginBtn);
        card.add(Box.createVerticalStrut(20));

        JPanel toggle = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        toggle.setOpaque(false);
        toggle.setAlignmentX(Component.LEFT_ALIGNMENT);
        toggle.add(UiComponents.subtitle("Don't have an account?"));
        JButton signUp = UiComponents.ghostButton("Create one");
        signUp.setForeground(UiTheme.PRIMARY);
        signUp.setFont(UiTheme.bodyBold());
        signUp.addActionListener(e -> {
            loginMsg.setVisible(false);
            cardLayout.show(cardPanel, "REGISTER");
        });
        toggle.add(signUp);
        card.add(toggle);

        loginBtn.addActionListener(e -> {
            String em = UiComponents.fieldValue(emailField, "you@email.com");
            String pw = UiComponents.passwordValue(passField, "Enter password");
            if (em.isEmpty() || pw.isEmpty()) {
                loginMsg.setText("Please fill in all fields.");
                loginMsg.setForeground(UiTheme.ERROR);
                loginMsg.setVisible(true);
                return;
            }
            if (!registeredEmail.isEmpty() && (!em.equals(registeredEmail) || !pw.equals(registeredPassword))) {
                loginMsg.setText("Invalid email or password.");
                loginMsg.setForeground(UiTheme.ERROR);
                loginMsg.setVisible(true);
                return;
            }
            loginMsg.setText("Login successful! Redirecting…");
            loginMsg.setForeground(UiTheme.SUCCESS);
            loginMsg.setVisible(true);
            Timer t = new Timer(800, ev -> {
                dispose();
                new DashboardFrame(em, registeredName.isEmpty() ? em : registeredName).setVisible(true);
            });
            t.setRepeats(false);
            t.start();
        });

        return card;
    }

    private JPanel buildRegisterCard() {
        JPanel card = UiComponents.cardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(420, 560));
        card.setMinimumSize(new Dimension(380, 480));

        JButton back = UiComponents.ghostButton("← Back to sign in");
        back.setAlignmentX(Component.LEFT_ALIGNMENT);
        back.addActionListener(e -> cardLayout.show(cardPanel, "LOGIN"));
        card.add(back);
        card.add(Box.createVerticalStrut(12));

        JLabel regTitle = UiComponents.title("Create account");
        regTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(regTitle);
        card.add(Box.createVerticalStrut(4));
        JLabel regHint = UiComponents.subtitle("Start renting in minutes");
        regHint.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(regHint);
        card.add(Box.createVerticalStrut(24));

        JLabel nameLbl = UiComponents.fieldLabel("Full Name");
        nameLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(nameLbl);
        card.add(Box.createVerticalStrut(6));
        JTextField nameField = UiComponents.textField("Your full name");
        nameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        card.add(nameField);
        card.add(Box.createVerticalStrut(14));

        JLabel regEmailLbl = UiComponents.fieldLabel("Email or Phone");
        regEmailLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(regEmailLbl);
        card.add(Box.createVerticalStrut(6));
        JTextField emailField = UiComponents.textField("you@email.com");
        emailField.setAlignmentX(Component.LEFT_ALIGNMENT);
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        card.add(emailField);
        card.add(Box.createVerticalStrut(14));

        JLabel regPassLbl = UiComponents.fieldLabel("Password");
        regPassLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(regPassLbl);
        card.add(Box.createVerticalStrut(6));
        JPasswordField passField = UiComponents.passwordField("Create a password");
        passField.setAlignmentX(Component.LEFT_ALIGNMENT);
        passField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        card.add(passField);
        card.add(Box.createVerticalStrut(8));

        JLabel regMsg = UiComponents.inlineMessage("", false);
        regMsg.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(regMsg);
        card.add(Box.createVerticalStrut(16));

        JButton regBtn = UiComponents.primaryButton("Create Account");
        regBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        regBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        card.add(regBtn);

        regBtn.addActionListener(e -> {
            String name = UiComponents.fieldValue(nameField, "Your full name");
            String em   = UiComponents.fieldValue(emailField, "you@email.com");
            String pw   = UiComponents.passwordValue(passField, "Create a password");
            if (name.isEmpty() || em.isEmpty() || pw.isEmpty()) {
                regMsg.setText("Please fill in all fields.");
                regMsg.setForeground(UiTheme.ERROR);
                regMsg.setVisible(true);
                return;
            }
            registeredEmail = em;
            registeredPassword = pw;
            registeredName = name;
            regMsg.setText("Account created! You can sign in now.");
            regMsg.setForeground(UiTheme.SUCCESS);
            regMsg.setVisible(true);
            Timer t = new Timer(1000, ev -> cardLayout.show(cardPanel, "LOGIN"));
            t.setRepeats(false);
            t.start();
        });

        return card;
    }
}
