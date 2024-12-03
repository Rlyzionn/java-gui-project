package GUI;

import Code.FileHandler;
import Code.GeneralUser;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class UserLogin {
    private final JPanel rootPanel;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton backButton;
    private JLabel emailLabel;
    private JLabel passwordLabel;

    private final ArrayList<GeneralUser> users;

    public UserLogin(ArrayList<GeneralUser> finalUsers, JPanel cardPanel, CardLayout cardLayout) {
        this.users = FileHandler.parseUsersFromTextFile("users.txt");
        rootPanel = new JPanel();

        // Initialize components
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");
        backButton = new JButton("Back");
        emailLabel = new JLabel("Email: ");
        passwordLabel = new JLabel("Password: ");

        // Set layout for the root panel
        rootPanel.setLayout(new GridLayout(3, 2)); // You can use other layouts if needed

        // Add components to the panel
        rootPanel.add(emailLabel);
        rootPanel.add(emailField);
        rootPanel.add(passwordLabel);
        rootPanel.add(passwordField);
        rootPanel.add(loginButton);
        rootPanel.add(backButton);

        // Login Button Action Listener
        loginButton.addActionListener(e -> {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            GeneralUser loggedInUser = validateUser(email, password);
            if (loggedInUser != null) {
                JOptionPane.showMessageDialog(rootPanel, "Login Successful!");

                // Navigate to UserDashboard
                UserDashboard dashboard = new UserDashboard(loggedInUser, cardPanel, cardLayout);
                cardPanel.add(dashboard.getRootPanel(), "UserDashboard");
                cardLayout.show(cardPanel, "UserDashboard");
            } else {
                JOptionPane.showMessageDialog(rootPanel, "Invalid Credentials!");
            }
        });

        // Back Button Action Listener
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "Welcome"));
    }

    private GeneralUser validateUser(String email, String password) {
        for (GeneralUser user : users) {
            if (user.getEmail().trim().equalsIgnoreCase(email.trim()) &&
                    user.getPassword().trim().equals(password.trim())) {
                return user; // Login successful
            }
        }
        return null; // Login failed
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}
