package Code;

import java.io.Serial;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FitnessActivity {

    private String activityName;
    private double duration; // In hours
    private double caloriesBurned;
    private Date date;
    private String time;

    // Constructor
    public FitnessActivity(String activityName, double duration, double caloriesBurned, Date date, String time) {
        if (activityName == null || activityName.isEmpty()) {
            throw new IllegalArgumentException("Activity name cannot be empty.");
        }
        if (duration <= 0) {
            throw new IllegalArgumentException("Duration must be greater than 0.");
        }
        if (caloriesBurned <= 0) {
            throw new IllegalArgumentException("Calories burned must be greater than 0.");
        }
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null.");
        }
        if (time == null || time.isEmpty()) {
            throw new IllegalArgumentException("Time cannot be empty.");
        }

        this.activityName = activityName;
        this.duration = duration;
        this.caloriesBurned = caloriesBurned;
        this.date = date;
        this.time = time;
    }

    // Getters and Setters
    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        if (activityName == null || activityName.isEmpty()) {
            throw new IllegalArgumentException("Activity name cannot be empty.");
        }
        this.activityName = activityName;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        if (duration <= 0) {
            throw new IllegalArgumentException("Duration must be greater than 0.");
        }
        this.duration = duration;
    }

    public double getCaloriesBurned() {
        return caloriesBurned;
    }

    public void setCaloriesBurned(double caloriesBurned) {
        if (caloriesBurned <= 0) {
            throw new IllegalArgumentException("Calories burned must be greater than 0.");
        }
        this.caloriesBurned = caloriesBurned;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null.");
        }
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        if (time == null || time.isEmpty()) {
            throw new IllegalArgumentException("Time cannot be empty.");
        }
        this.time = time;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return "Activity: " + activityName +
                " | Duration: " + duration + " hrs" +
                " | Calories: " + caloriesBurned +
                " | Date: " + sdf.format(date) +
                " | Time: " + time;
    }
}
