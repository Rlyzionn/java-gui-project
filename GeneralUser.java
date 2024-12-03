package Code;

import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

public class GeneralUser extends User {
    private ArrayList<FitnessActivity> activities;
    private ArrayList<Goal> goals;

    public GeneralUser(String name, String id, String password, String email, String phoneNumber, String address, Date dateOfBirth) {
        super(name, id, password, email, phoneNumber, address, dateOfBirth);
        this.activities = FileHandler.loadUserActivities(id); // Load activities for this user
        this.goals = FileHandler.loadUserGoals(id); // Load goals by user ID
    }

    @Override
    public void displayUserDetails() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println("===== User Details =====");
        System.out.println("Name: " + name);
        System.out.println("ID: " + id);
        System.out.println("Email: " + email);
        System.out.println("Phone: " + phoneNumber);
        System.out.println("Address: " + address);
        System.out.println("Date of Birth: " + sdf.format(dateOfBirth));
        System.out.println("Activities:");
        for (FitnessActivity activity : activities) {
            System.out.println(activity);
        }
        System.out.println("========================");
    }

    public void logActivity(FitnessActivity activity) {
        if (activity == null) {
            throw new IllegalArgumentException("Activity cannot be null.");
        }
        activities.add(activity);
        FileHandler.saveUserActivities(id, activities); // Save activities to unified file
    }

    public ArrayList<FitnessActivity> getActivities() {
        return activities;
    }

    public void setActivities(ArrayList<FitnessActivity> activities) {
        this.activities = activities;
    }

    public void submitActivity(FitnessActivity activity) {
        if (activity == null) {
            throw new IllegalArgumentException("Activity cannot be null.");
        }
        activities.add(activity);
        FileHandler.saveUserActivities(id, activities);
    }

    public ArrayList<Goal> getGoals() {
        return goals;
    }

    public void setGoals(ArrayList<Goal> goals) {
        this.goals = goals;
    }

    public void setGoal(Goal goal) {
        if (goal == null) {
            throw new IllegalArgumentException("Goal cannot be null.");
        }
        goal.setStatus("Pending");
        goals.add(goal);
        FileHandler.saveUserGoals(id, goals);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (!User.isValidName(name)) {
            throw new IllegalArgumentException("Invalid name. Name must be at least 3 characters long.");
        }
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (!User.isValidPassword(password)) {
            throw new IllegalArgumentException("Invalid password. Password must be at least 6 characters long.");
        }
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (!User.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format.");
        }
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (!User.isValidPhoneNumber(phoneNumber)) {
            throw new IllegalArgumentException("Invalid phone number. Must be 10 digits.");
        }
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        if (dateOfBirth == null) {
            throw new IllegalArgumentException("Date of birth cannot be null.");
        }
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return "Name: " + name +
                ", ID: " + id +
                ", Email: " + email +
                ", Phone: " + phoneNumber +
                ", Address: " + address +
                ", DOB: " + sdf.format(dateOfBirth) +
                ", Password: " + password;
    }
}


