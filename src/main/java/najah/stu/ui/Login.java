package najah.stu.ui;

import javax.swing.*;
import najah.stu.service.ManagerService;

public class Login extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public ManagerService managerService;

    public Login() {

        managerService = new ManagerService();

        setTitle("Vehicle Rental System - Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(40, 40, 100, 25);
        add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(150, 40, 180, 25);
        add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(40, 90, 100, 25);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 90, 180, 25);
        add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setBounds(150, 150, 100, 30);
        add(loginButton);

        loginButton.addActionListener(e -> {

            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (managerService.login(username, password)) {

            JOptionPane.showMessageDialog(
            this,
            "Login Successful",
            "Success",
            JOptionPane.INFORMATION_MESSAGE
            );

            dispose();
            new ManagerDashboard(managerService);

            } else {

            JOptionPane.showMessageDialog(
            this,
            "Invalid Username or Password",
            "Login Failed",
            JOptionPane.ERROR_MESSAGE
             );
            }

        });

        setVisible(true);
        }
}