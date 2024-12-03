package Code;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Admin extends User {
    private static final String DEFAULT_NAME = "admin";
    private static final String DEFAULT_PASSWORD = "admin2023";

    public Admin() {
        super(DEFAULT_NAME, "ADMIN001", DEFAULT_PASSWORD, "admin@fitness.com", "1234567890", "Address, Street, Country", new Date());
    }

    @Override
    public void displayUserDetails() {
        System.out.println("Admin Name: " + name + ", ID: " + id);
    }

    public void approveGoal(Goal goal) {
        if (goal == null) {
            throw new IllegalArgumentException("Goal cannot be null.");
        }
        goal.setStatus("Approved");
        System.out.println("Goal Approved: " + goal.getDescription());
    }

    public void rejectGoal(Goal goal) {
        if (goal == null) {
            throw new IllegalArgumentException("Goal cannot be null.");
        }
        goal.setStatus("Rejected");
        System.out.println("Goal Rejected: " + goal.getDescription());
    }

    public void approveActivity(FitnessActivity activity) {
        if (activity == null) {
            throw new IllegalArgumentException("Activity cannot be null.");
        }
        System.out.println("Activity Approved: " + activity.getActivityName());
    }

    public void rejectActivity(FitnessActivity activity) {
        if (activity == null) {
            throw new IllegalArgumentException("Activity cannot be null.");
        }
        System.out.println("Activity Rejected: " + activity.getActivityName());
    }

    public void deleteUser(String userId, ArrayList<GeneralUser> users) {
        GeneralUser userToRemove = null;
        for (GeneralUser user : users) {
            if (user.getId().equals(userId)) {
                userToRemove = user;
                break;
            }
        }
        if (userToRemove != null) {
            users.remove(userToRemove);

            // Remove user from the main user file
            FileHandler.removeUserFromFile("users.txt", userId);

            // Remove associated data (activities and goals)
            FileHandler.deleteUserData(userId);

            System.out.println("User " + userToRemove.getName() + " and their data deleted successfully.");
        } else {
            System.out.println("User not found.");
        }
    }
    public String searchUserByUsernameOrEmail(String query, ArrayList<GeneralUser> users) {
        for (GeneralUser user : users) {
            if (user.getName().equalsIgnoreCase(query) || user.getEmail().equalsIgnoreCase(query)) {
                // Format user details
                return formatUserDetails(user);
            }
        }
        return "User not found"; // Message when no user is found
    }

    private String formatUserDetails(GeneralUser user) {
        return "===== User Details =====\n" +
                "Name: " + user.getName() + "\n" +
                "ID: " + user.getId() + "\n" +
                "Email: " + user.getEmail() + "\n" +
                "Phone: " + user.getPhoneNumber() + "\n" +
                "Address: " + user.getAddress() + "\n" +
                "Date of Birth: " + new SimpleDateFormat("yyyy-MM-dd").format(user.getDateOfBirth()) + "\n" +
                "=========================";
    }


}
