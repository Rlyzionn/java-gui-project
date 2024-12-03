package GUI;

import Code.Admin;
import Code.GeneralUser;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class AdminLogin {
    private JPanel rootPanel;
    private JLabel titleLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton backButton;
    private JLabel usernameLabel;
    private JLabel passwordLabel;

    public AdminLogin(ArrayList<GeneralUser> users, JPanel cardPanel, CardLayout cardLayout) {
        // Manually initialize the components
        rootPanel = new JPanel();
        rootPanel.setLayout(new BorderLayout()); // Set the layout for the rootPanel

        // Title label
        titleLabel = new JLabel("Admin Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        // Create the form panel with GridLayout
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 5, 5));

        // Initialize input fields and labels
        usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(); // Initialize username text field

        passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(); // Initialize password field

        // Initialize buttons
        loginButton = new JButton("Login");
        backButton = new JButton("Back");

        // Add components to the form panel
        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        formPanel.add(loginButton);
        formPanel.add(backButton);

        // Add the title label and form panel to the root panel
        rootPanel.add(titleLabel, BorderLayout.NORTH);
        rootPanel.add(formPanel, BorderLayout.CENTER);

        // Login Button Action Listener
        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (isValidAdminLogin(username, password)) {
                // Show a success message before navigating to the dashboard
                JOptionPane.showMessageDialog(rootPanel, "Login Successful!");

                // Create Admin and AdminDashboard
                Admin admin = new Admin();
                AdminDashboard adminDashboard = new AdminDashboard(admin, users, cardPanel, cardLayout);

                // Add the AdminDashboard panel with the name "AdminDashboard"
                cardPanel.add(adminDashboard.getRootPanel(), "AdminDashboard");

                // Show the AdminDashboard
                cardLayout.show(cardPanel, "AdminDashboard");
            } else {
                JOptionPane.showMessageDialog(rootPanel, "Invalid credentials. Please try again.");
            }
        });

        // Back Button Action Listener
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "Welcome"));
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    private boolean isValidAdminLogin(String username, String password) {
        // Validate admin credentials (currently hardcoded)
        return username.equals("admin") && password.equals("admin2023");
    }
}
