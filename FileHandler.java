package Code;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FileHandler {

    public static <T> void saveToTextFile(String fileName, ArrayList<T> data) {
        try (PrintWriter writer = new PrintWriter(fileName)) {
            for (T item : data) {
                writer.println(item.toString());
            }
        } catch (IOException e) {
            System.out.println("Error saving to text file: " + e.getMessage());
        }
    }

    public static ArrayList<String> readFromTextFile(String fileName) {
        ArrayList<String> data = new ArrayList<>();
        try (FileReader reader = new FileReader(fileName)) {
            StringBuilder sb = new StringBuilder();
            int ch;
            while ((ch = reader.read()) != -1) {
                if (ch == '\n') {
                    data.add(sb.toString());
                    sb.setLength(0);
                } else {
                    sb.append((char) ch);
                }
            }
            if (sb.length() > 0) { // Add last line if no newline
                data.add(sb.toString());
            }
        } catch (IOException e) {
            System.out.println("Error reading from text file: " + e.getMessage());
        }
        return data;
    }

    public static ArrayList<GeneralUser> parseUsersFromTextFile(String fileName) {
        ArrayList<GeneralUser> users = new ArrayList<>();
        ArrayList<String> lines = readFromTextFile(fileName);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (String line : lines) {
            try {
                String[] parts = line.split(", ");
                String name = parts[0].split(": ")[1];
                String id = parts[1].split(": ")[1];
                String email = parts[2].split(": ")[1];
                String phone = parts[3].split(": ")[1];
                String address = parts[4].split(": ")[1];
                Date dob = sdf.parse(parts[5].split(": ")[1]);
                String password = parts[6].split(": ")[1];

                users.add(new GeneralUser(name, id, password, email, phone, address, dob));
            } catch (Exception e) {
                System.out.println("Error parsing user from line: " + line);
            }
        }
        return users;
    }

    public static void saveUserActivities(String userId, ArrayList<FitnessActivity> activities) {
        String fileName = "activities.txt";
        ArrayList<String> allActivities = readFromTextFile(fileName);

        allActivities.removeIf(line -> line.startsWith(userId + " |"));

        for (FitnessActivity activity : activities) {
            allActivities.add(userId + " | " + activity.toString());
        }

        saveToTextFile(fileName, allActivities);
    }

    public static ArrayList<FitnessActivity> loadUserActivities(String userId) {
        String fileName = "activities.txt";
        ArrayList<String> lines = readFromTextFile(fileName);
        ArrayList<FitnessActivity> userActivities = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        for (String line : lines) {
            if (line.startsWith(userId + " |")) {
                try {
                    String[] parts = line.split(" \\| ");
                    String activityName = parts[1].split(": ")[1];
                    double duration = Double.parseDouble(parts[2].split(": ")[1].replace(" hrs", ""));
                    double caloriesBurned = Double.parseDouble(parts[3].split(": ")[1]);
                    Date date = sdf.parse(parts[4].split(": ")[1]);
                    String time = parts[5].split(": ")[1];

                    userActivities.add(new FitnessActivity(activityName, duration, caloriesBurned, date, time));
                } catch (Exception e) {
                    System.out.println("Error parsing activity from line: " + line);
                }
            }
        }
        return userActivities;
    }

    public static void saveUserGoals(String userId, ArrayList<Goal> goals) {
        String fileName = "goals.txt";
        ArrayList<String> allGoals = readFromTextFile(fileName);

        allGoals.removeIf(line -> line.startsWith(userId + " |"));

        for (Goal goal : goals) {
            allGoals.add(userId + " | " + goal.toString());
        }

        saveToTextFile(fileName, allGoals);
    }

    public static ArrayList<Goal> loadUserGoals(String userId) {
        String fileName = "goals.txt";
        ArrayList<String> lines = readFromTextFile(fileName);
        ArrayList<Goal> userGoals = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (String line : lines) {
            if (line.startsWith(userId + " |")) {
                try {
                    // Split by '|' and trim spaces to get each part
                    String[] parts = line.split(" \\| ");

                    // Extract goal details from the parts
                    String goalId = parts[1].split(": ")[1].trim();  // Extract goalId from "Goal ID: 12"
                    String description = parts[2].split(": ")[1].trim();  // Extract description
                    Date startDate = sdf.parse(parts[3].split(": ")[1].trim());  // Extract start date
                    Date endDate = sdf.parse(parts[4].split(": ")[1].trim());  // Extract end date
                    String status = parts[5].split(": ")[1].trim();  // Extract status

                    // Create a new Goal object
                    Goal goal = new Goal(goalId, description, startDate, endDate);
                    goal.setStatus(status);

                    // Add the goal to the userGoals list
                    userGoals.add(goal);
                } catch (Exception e) {
                    System.out.println("Error parsing goal from line: " + line);
                    e.printStackTrace(); // Print stack trace for debugging
                }
            }
        }
        return userGoals;
    }


    public static void removeUserFromFile(String fileName, String userId) {
        ArrayList<String> allUsers = readFromTextFile(fileName);
        ArrayList<String> updatedUsers = new ArrayList<>();

        for (String line : allUsers) {
            if (!line.contains("ID: " + userId)) {
                updatedUsers.add(line);
            }
        }

        saveToTextFile(fileName, updatedUsers);
    }

    public static void deleteUserData(String userId) {
        String activitiesFileName = "activities.txt";
        ArrayList<String> allActivities = readFromTextFile(activitiesFileName);
        ArrayList<String> updatedActivities = new ArrayList<>();

        for (String line : allActivities) {
            if (!line.startsWith(userId + " |")) {
                updatedActivities.add(line);
            }
        }

        saveToTextFile(activitiesFileName, updatedActivities);

        String goalsFileName = "goals.txt";
        ArrayList<String> allGoals = readFromTextFile(goalsFileName);
        ArrayList<String> updatedGoals = new ArrayList<>();

        for (String line : allGoals) {
            if (!line.startsWith(userId + " |")) {
                updatedGoals.add(line);
            }
        }

        saveToTextFile(goalsFileName, updatedGoals);
    }
}
