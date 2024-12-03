package Code;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

public abstract class User{

    protected String name;
    protected String id;
    protected String password;
    protected String email;
    protected String phoneNumber;
    protected String address;
    protected Date dateOfBirth;

    public User(String name, String id, String password, String email, String phoneNumber, String address, Date dateOfBirth) {
        if (!isValidName(name)) {
            throw new IllegalArgumentException("Invalid name. Name must be at least 3 characters long.");
        }
        if (!isValidPassword(password)) {
            throw new IllegalArgumentException("Invalid password. Password must be at least 6 characters long.");
        }
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format.");
        }
        if (!isValidPhoneNumber(phoneNumber)) {
            throw new IllegalArgumentException("Invalid phone number. Must be 10 digits.");
        }
        if (dateOfBirth == null) {
            throw new IllegalArgumentException("Date of birth cannot be null.");
        }

        this.name = name;
        this.id = id;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
    }

    public abstract void displayUserDetails();

    // Validation methods
    public static boolean isValidName(String name) {
        return name != null && name.length() >= 3;
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$");
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && phoneNumber.matches("\\d{10}");
    }
}
