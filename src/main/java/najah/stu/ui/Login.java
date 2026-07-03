package najah.stu.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import najah.stu.service.ManagerService;

public class Login extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    private ManagerService managerService;

    // Palette
    private static final Color BG_DARK      = new Color(0x1B1F27);
    private static final Color PANEL_LIGHT  = new Color(0xFFFFFF);
    private static final Color ACCENT       = new Color(0x2E6BE6);
    private static final Color ACCENT_HOVER = new Color(0x2557C0);
    private static final Color TEXT_MUTED   = new Color(0x8A8F98);
    private static final Color TEXT_DARK    = new Color(0x1B1F27);
    private static final Color FIELD_BORDER = new Color(0xD9DCE1);

    public Login() {

        managerService = new ManagerService();

        setTitle("Vehicle Rental System - Login");
        setSize(900, 560);
        setMinimumSize(new Dimension(760, 480));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(buildBrandPanel(), BorderLayout.WEST);
        add(buildFormPanel(), BorderLayout.CENTER);

        getRootPane().setDefaultButton(loginButton);
        setVisible(true);
    }

    /** Left side: dark brand / hero panel */
    private JPanel buildBrandPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(
                        0, 0, BG_DARK,
                        getWidth(), getHeight(), new Color(0x2A3142));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setPreferredSize(new Dimension(340, 0));
        panel.setLayout(new GridBagLayout());

        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));

        JLabel logo = new JLabel("\uD83D\uDE97");
        logo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("<html>Vehicle<br>Rental<br>System</html>");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        title.setBorder(new EmptyBorder(20, 0, 15, 0));

        JLabel subtitle = new JLabel("<html>Manager console for fleet,<br>bookings &amp; customers.</html>");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(0xB7BCC8));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        inner.add(logo);
        inner.add(title);
        inner.add(subtitle);

        panel.add(inner);
        return panel;
    }

    /** Right side: white form panel */
    private JPanel buildFormPanel() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(PANEL_LIGHT);

        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new GridBagLayout());
        form.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(0, 0, 6, 0);

        JLabel heading = new JLabel("Welcome back");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 26));
        heading.setForeground(TEXT_DARK);

        JLabel sub = new JLabel("Sign in to manage your rentals");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(TEXT_MUTED);
        sub.setBorder(new EmptyBorder(0, 0, 25, 0));

        JLabel userLabel = fieldLabel("USERNAME");
        usernameField = styledTextField();

        JLabel passLabel = fieldLabel("PASSWORD");
        passwordField = styledPasswordField();

        loginButton = buildLoginButton();

        gc.gridy = 0; form.add(heading, gc);
        gc.gridy = 1; form.add(sub, gc);
        gc.gridy = 2; form.add(userLabel, gc);
        gc.gridy = 3; gc.insets = new Insets(0, 0, 18, 0); form.add(usernameField, gc);
        gc.gridy = 4; gc.insets = new Insets(0, 0, 6, 0); form.add(passLabel, gc);
        gc.gridy = 5; gc.insets = new Insets(0, 0, 28, 0); form.add(passwordField, gc);
        gc.gridy = 6; gc.insets = new Insets(0, 0, 0, 0); form.add(loginButton, gc);

        form.setPreferredSize(new Dimension(340, 380));
        wrapper.add(form);

        attachLoginAction();
        return wrapper;
    }

    private JLabel fieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));
        label.setForeground(TEXT_MUTED);
        return label;
    }

    private JTextField styledTextField() {
        JTextField field = new JTextField();
        styleField(field);
        return field;
    }

    private JPasswordField styledPasswordField() {
        JPasswordField field = new JPasswordField();
        styleField(field);
        return field;
    }

    private void styleField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        field.setForeground(TEXT_DARK);
        field.setBackground(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(FIELD_BORDER, 1, true),
                new EmptyBorder(10, 12, 10, 12)));
        field.setPreferredSize(new Dimension(0, 42));
    }

    private JButton buildLoginButton() {
        JButton button = new JButton("Log In") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = getModel().isRollover() ? ACCENT_HOVER : ACCENT;
                g2.setColor(bg);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(0, 44));
        return button;
    }

    private void attachLoginAction() {
        loginButton.addActionListener(e -> {

            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (managerService.login(username, password)) {
                dispose();
                new ManagerDashboard();
                JOptionPane.showMessageDialog(null, "Login Successful");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Username or Password",
                        "Login Failed", JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
            }
        });
    }
}