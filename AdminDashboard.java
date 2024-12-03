package GUI;

import Code.Admin;
import Code.FileHandler;
import Code.GeneralUser;
import Code.Goal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdminDashboard {
    private final JPanel rootPanel;
    private JLabel titleLabel;
    private JTabbedPane tabbedPane;
    private JButton logoutButton;  // Declare logoutButton here

    private final Admin admin;
    private final ArrayList<GeneralUser> users;

    public AdminDashboard(Admin admin, ArrayList<GeneralUser> users, JPanel cardPanel, CardLayout cardLayout) {
        this.admin = admin;
        this.users = users;

        // Ensure rootPanel is initialized and layout set
        rootPanel = new JPanel(new BorderLayout());

        // Initialize components like tabbedPane and logoutButton
        titleLabel = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        tabbedPane = new JTabbedPane();
        logoutButton = new JButton("Logout");

        // Add components to the rootPanel
        rootPanel.add(titleLabel, BorderLayout.NORTH);
        rootPanel.add(tabbedPane, BorderLayout.CENTER);
        rootPanel.add(logoutButton, BorderLayout.SOUTH);

        setupTabs();  // Populate the tabs

        // Logout Button Action Listener
        logoutButton.addActionListener(e -> cardLayout.show(cardPanel, "Welcome"));
    }

    private void setupTabs() {
        // User Management Tab
        JPanel userPanel = createUserManagementPanel();
        tabbedPane.addTab("Manage Users", userPanel);

        // Goal Management Tab
        JPanel goalPanel = createGoalManagementPanel();
        tabbedPane.addTab("Manage Goals", goalPanel);

        // Search User Tab
        JPanel searchPanel = createSearchUserPanel();
        tabbedPane.addTab("Search User", searchPanel);
    }

    private JPanel createSearchUserPanel() {
        JPanel searchPanel = new JPanel(new BorderLayout());

        JLabel searchLabel = new JLabel("Search User by Username or Email:");
        JTextField searchField = new JTextField();
        JButton searchButton = new JButton("Search");
        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(searchLabel, BorderLayout.WEST);
        inputPanel.add(searchField, BorderLayout.CENTER);
        inputPanel.add(searchButton, BorderLayout.EAST);

        searchPanel.add(inputPanel, BorderLayout.NORTH);
        searchPanel.add(new JScrollPane(resultArea), BorderLayout.CENTER);

        searchButton.addActionListener(e -> {
            String query = searchField.getText().trim();
            String foundUser = admin.searchUserByUsernameOrEmail(query, users);
            if (foundUser != null) {
                resultArea.setText("User Found:\n" + foundUser);
            } else {
                resultArea.setText("No user found with the given username or email.");
            }
        });

        return searchPanel;
    }

    private JPanel createUserManagementPanel() {
        JPanel userPanel = new JPanel(new BorderLayout());

        // User Table
        DefaultTableModel userTableModel = new DefaultTableModel(new Object[][]{},
                new String[]{"User ID", "Name", "Email", "Phone", "Address"});
        JTable userTable = new JTable(userTableModel);
        JScrollPane userScrollPane = new JScrollPane(userTable);
        userPanel.add(userScrollPane, BorderLayout.CENTER);

        // Populate user table
        for (GeneralUser user : users) {
            userTableModel.addRow(new Object[]{
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getPhoneNumber(),
                    user.getAddress()
            });
        }

        // Buttons for user actions
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addUserButton = new JButton("Add User");
        JButton editUserButton = new JButton("Edit User");
        JButton deleteUserButton = new JButton("Delete User");
        buttonPanel.add(addUserButton);
        buttonPanel.add(editUserButton);
        buttonPanel.add(deleteUserButton);

        // Add Action Listeners for Buttons
        addUserButton.addActionListener(e -> openAddUserDialog(userTableModel));
        editUserButton.addActionListener(e -> openEditUserDialog(userTable, userTableModel));
        deleteUserButton.addActionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow != -1) {
                String userId = (String) userTableModel.getValueAt(selectedRow, 0);
                admin.deleteUser(userId, users);
                userTableModel.removeRow(selectedRow);
                JOptionPane.showMessageDialog(rootPanel, "User and their data deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(rootPanel, "Please select a user to delete.");
            }
        });

        userPanel.add(buttonPanel, BorderLayout.SOUTH);
        return userPanel;
    }

    // Dialog to Add User
    private void openAddUserDialog(DefaultTableModel userTableModel) {
        JDialog addUserDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(rootPanel), "Add User", true);
        addUserDialog.setLayout(new GridLayout(6, 2, 10, 10));
        addUserDialog.setSize(400, 300);

        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextField phoneField = new JTextField();
        JTextField addressField = new JTextField();

        addUserDialog.add(new JLabel("Name:"));
        addUserDialog.add(nameField);
        addUserDialog.add(new JLabel("Email:"));
        addUserDialog.add(emailField);
        addUserDialog.add(new JLabel("Password:"));
        addUserDialog.add(passwordField);
        addUserDialog.add(new JLabel("Phone:"));
        addUserDialog.add(phoneField);
        addUserDialog.add(new JLabel("Address:"));
        addUserDialog.add(addressField);

        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        addUserDialog.add(saveButton);
        addUserDialog.add(cancelButton);

        saveButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String phone = phoneField.getText().trim();
            String address = addressField.getText().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                JOptionPane.showMessageDialog(addUserDialog, "All fields are required!");
                return;
            }

            GeneralUser newUser = new GeneralUser(name, "U" + (users.size() + 1), password, email, phone, address, new Date());
            users.add(newUser);
            FileHandler.saveToTextFile("users.txt", users);

            userTableModel.addRow(new Object[]{
                    newUser.getId(),
                    newUser.getName(),
                    newUser.getEmail(),
                    newUser.getPhoneNumber(),
                    newUser.getAddress()
            });

            JOptionPane.showMessageDialog(addUserDialog, "User added successfully!");
            addUserDialog.dispose();
        });

        cancelButton.addActionListener(e -> addUserDialog.dispose());

        addUserDialog.setLocationRelativeTo(rootPanel);
        addUserDialog.setVisible(true);
    }

    // Dialog to Edit User
    private void openEditUserDialog(JTable userTable, DefaultTableModel userTableModel) {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(rootPanel, "Please select a user to edit.");
            return;
        }

        String userId = (String) userTableModel.getValueAt(selectedRow, 0);
        GeneralUser user = findUserById(userId);

        if (user == null) {
            JOptionPane.showMessageDialog(rootPanel, "User not found.");
            return;
        }

        JDialog editUserDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(rootPanel), "Edit User", true);
        editUserDialog.setLayout(new GridLayout(6, 2, 10, 10));
        editUserDialog.setSize(400, 300);

        JTextField nameField = new JTextField(user.getName());
        JTextField emailField = new JTextField(user.getEmail());
        JPasswordField passwordField = new JPasswordField(user.getPassword());
        JTextField phoneField = new JTextField(user.getPhoneNumber());
        JTextField addressField = new JTextField(user.getAddress());

        editUserDialog.add(new JLabel("Name:"));
        editUserDialog.add(nameField);
        editUserDialog.add(new JLabel("Email:"));
        editUserDialog.add(emailField);
        editUserDialog.add(new JLabel("Password:"));
        editUserDialog.add(passwordField);
        editUserDialog.add(new JLabel("Phone:"));
        editUserDialog.add(phoneField);
        editUserDialog.add(new JLabel("Address:"));
        editUserDialog.add(addressField);

        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        editUserDialog.add(saveButton);
        editUserDialog.add(cancelButton);

        saveButton.addActionListener(e -> {
            user.setName(nameField.getText().trim());
            user.setEmail(emailField.getText().trim());
            user.setPassword(new String(passwordField.getPassword()).trim());
            user.setPhoneNumber(phoneField.getText().trim());
            user.setAddress(addressField.getText().trim());

            FileHandler.saveToTextFile("users.txt", users);

            userTableModel.setValueAt(user.getName(), selectedRow, 1);
            userTableModel.setValueAt(user.getEmail(), selectedRow, 2);
            userTableModel.setValueAt(user.getPhoneNumber(), selectedRow, 3);
            userTableModel.setValueAt(user.getAddress(), selectedRow, 4);

            JOptionPane.showMessageDialog(editUserDialog, "User updated successfully!");
            editUserDialog.dispose();
        });

        cancelButton.addActionListener(e -> editUserDialog.dispose());

        editUserDialog.setLocationRelativeTo(rootPanel);
        editUserDialog.setVisible(true);
    }



    private JPanel createGoalManagementPanel() {
        JPanel goalPanel = new JPanel(new BorderLayout());

        // Goal Table
        DefaultTableModel goalTableModel = new DefaultTableModel(new Object[][]{},
                new String[]{"Goal ID", "Description", "User ID", "Start Date", "End Date", "Status"});
        JTable goalTable = new JTable(goalTableModel);
        JScrollPane goalScrollPane = new JScrollPane(goalTable);
        goalPanel.add(goalScrollPane, BorderLayout.CENTER);

        // Populate the goal table
        for (GeneralUser user : users) {
            for (Goal goal : user.getGoals()) {
                goalTableModel.addRow(new Object[]{
                        goal.getGoalId(),
                        goal.getDescription(),
                        user.getId(),
                        goal.getStartDate(),
                        goal.getEndDate(),
                        goal.getStatus()
                });
            }
        }

        // Action Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addGoalButton = new JButton("Add Goal");
        JButton deleteGoalButton = new JButton("Delete Goal");
        JButton updateGoalButton = new JButton("Update Goal");
        JButton approveGoalButton = new JButton("Approve Goal");

        buttonPanel.add(addGoalButton);
        buttonPanel.add(deleteGoalButton);
        buttonPanel.add(updateGoalButton);
        buttonPanel.add(approveGoalButton);

        goalPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add Action Listeners
        addGoalButton.addActionListener(e -> openAddGoalDialog(goalTableModel));
        deleteGoalButton.addActionListener(e -> deleteGoal(goalTable, goalTableModel));
        updateGoalButton.addActionListener(e -> openUpdateGoalDialog(goalTable, goalTableModel));
        approveGoalButton.addActionListener(e -> approveSelectedGoal(goalTable, goalTableModel));

        return goalPanel;
    }

    private void openAddGoalDialog(DefaultTableModel goalTableModel) {
        JDialog addGoalDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(rootPanel), "Add Goal", true);
        addGoalDialog.setLayout(new GridLayout(5, 2, 10, 10));
        addGoalDialog.setSize(400, 300);

        JTextField goalIdField = new JTextField();
        JTextField descriptionField = new JTextField();
        JTextField startDateField = new JTextField("yyyy-MM-dd");
        JTextField endDateField = new JTextField("yyyy-MM-dd");

        addGoalDialog.add(new JLabel("Goal ID:"));
        addGoalDialog.add(goalIdField);
        addGoalDialog.add(new JLabel("Description:"));
        addGoalDialog.add(descriptionField);
        addGoalDialog.add(new JLabel("Start Date:"));
        addGoalDialog.add(startDateField);
        addGoalDialog.add(new JLabel("End Date:"));
        addGoalDialog.add(endDateField);

        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        addGoalDialog.add(saveButton);
        addGoalDialog.add(cancelButton);

        saveButton.addActionListener(e -> {
            try {
                String goalId = goalIdField.getText();
                String description = descriptionField.getText();
                Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDateField.getText());
                Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDateField.getText());

                if (goalId.isEmpty() || description.isEmpty()) {
                    JOptionPane.showMessageDialog(addGoalDialog, "All fields are required!");
                    return;
                }

                Goal newGoal = new Goal(goalId, description, startDate, endDate);
                GeneralUser user = users.get(0); // Example: Assign to the first user (modify as needed)
                user.getGoals().add(newGoal);

                goalTableModel.addRow(new Object[]{
                        goalId,
                        description,
                        user.getId(),
                        startDate,
                        endDate,
                        "Pending"
                });

                JOptionPane.showMessageDialog(addGoalDialog, "Goal added successfully!");
                addGoalDialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(addGoalDialog, "Invalid input: " + ex.getMessage());
            }
        });

        cancelButton.addActionListener(e -> addGoalDialog.dispose());

        addGoalDialog.setLocationRelativeTo(rootPanel);
        addGoalDialog.setVisible(true);
    }

    private void deleteGoal(JTable goalTable, DefaultTableModel goalTableModel) {
        int selectedRow = goalTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(rootPanel, "Please select a goal to delete.");
            return;
        }

        String goalId = (String) goalTableModel.getValueAt(selectedRow, 0);
        String userId = (String) goalTableModel.getValueAt(selectedRow, 2);

        GeneralUser user = findUserById(userId);
        if (user != null) {
            Goal goalToRemove = findGoalById(userId, goalId);
            if (goalToRemove != null) {
                user.getGoals().remove(goalToRemove);
                goalTableModel.removeRow(selectedRow);
                JOptionPane.showMessageDialog(rootPanel, "Goal deleted successfully!");
            }
        }
    }

    private void openUpdateGoalDialog(JTable goalTable, DefaultTableModel goalTableModel) {
        int selectedRow = goalTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(rootPanel, "Please select a goal to update.");
            return;
        }

        String goalId = (String) goalTableModel.getValueAt(selectedRow, 0);
        String userId = (String) goalTableModel.getValueAt(selectedRow, 2);

        GeneralUser user = findUserById(userId);
        Goal goal = findGoalById(userId, goalId);

        if (goal == null) {
            JOptionPane.showMessageDialog(rootPanel, "Goal not found.");
            return;
        }

        JDialog updateGoalDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(rootPanel), "Update Goal", true);
        updateGoalDialog.setLayout(new GridLayout(5, 2, 10, 10));
        updateGoalDialog.setSize(400, 300);

        JTextField descriptionField = new JTextField(goal.getDescription());
        JTextField startDateField = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(goal.getStartDate()));
        JTextField endDateField = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(goal.getEndDate()));

        updateGoalDialog.add(new JLabel("Description:"));
        updateGoalDialog.add(descriptionField);
        updateGoalDialog.add(new JLabel("Start Date:"));
        updateGoalDialog.add(startDateField);
        updateGoalDialog.add(new JLabel("End Date:"));
        updateGoalDialog.add(endDateField);

        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        updateGoalDialog.add(saveButton);
        updateGoalDialog.add(cancelButton);

        saveButton.addActionListener(e -> {
            try {
                goal.setDescription(descriptionField.getText());
                goal.setStartDate(new SimpleDateFormat("yyyy-MM-dd").parse(startDateField.getText()));
                goal.setEndDate(new SimpleDateFormat("yyyy-MM-dd").parse(endDateField.getText()));

                goalTableModel.setValueAt(goal.getDescription(), selectedRow, 1);
                goalTableModel.setValueAt(goal.getStartDate(), selectedRow, 3);
                goalTableModel.setValueAt(goal.getEndDate(), selectedRow, 4);

                JOptionPane.showMessageDialog(updateGoalDialog, "Goal updated successfully!");
                updateGoalDialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(updateGoalDialog, "Invalid input: " + ex.getMessage());
            }
        });

        cancelButton.addActionListener(e -> updateGoalDialog.dispose());

        updateGoalDialog.setLocationRelativeTo(rootPanel);
        updateGoalDialog.setVisible(true);
    }

    private void approveSelectedGoal(JTable goalTable, DefaultTableModel goalTableModel) {
        int selectedRow = goalTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(rootPanel, "Please select a goal to approve.");
            return;
        }

        String goalId = (String) goalTableModel.getValueAt(selectedRow, 0);
        String userId = (String) goalTableModel.getValueAt(selectedRow, 2);

        Goal goal = findGoalById(userId, goalId);
        if (goal != null) {
            admin.approveGoal(goal);
            goalTableModel.setValueAt("Approved", selectedRow, 5);
            JOptionPane.showMessageDialog(rootPanel, "Goal approved successfully!");
        }
    }


    private GeneralUser findUserById(String userId) {
        for (GeneralUser user : users) {
            if (user.getId().equals(userId)) {
                return user;
            }
        }
        return null;
    }

    private Goal findGoalById(String userId, String goalId) {
        GeneralUser user = findUserById(userId);
        if (user != null) {
            for (Goal goal : user.getGoals()) {
                if (goal.getGoalId().equals(goalId)) {
                    return goal;
                }
            }
        }
        return null;
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}
