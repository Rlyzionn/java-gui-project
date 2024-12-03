package Code;

import java.io.Serial;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Goal  {

    private String goalId;
    private String description;
    private Date startDate;
    private Date endDate;
    private String status; // Pending, Approved, Completed
    private int progress; // New field for progress percentage (0-100)

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public Goal(String goalId, String description, Date startDate, Date endDate) {
        if (goalId == null || goalId.isEmpty()) {
            throw new IllegalArgumentException("Goal ID cannot be empty.");
        }
        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty.");
        }
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start and end dates cannot be null.");
        }
        if (startDate.after(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date.");
        }

        this.goalId = goalId;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = "Pending"; // Default status
        this.progress = 0;       // Default progress

    }



    public String getGoalId() {
        return goalId;
    }

    public String getDescription() {
        return description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setGoalId(String goalId) {
        this.goalId = goalId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setStatus(String status) {
        if (!status.equals("Pending") && !status.equals("Approved") && !status.equals("Completed")) {
            throw new IllegalArgumentException("Invalid status. Must be Pending, Approved, or Completed.");
        }
        this.status = status;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        if (progress < 0 || progress > 100) {
            throw new IllegalArgumentException("Progress must be between 0 and 100.");
        }
        this.progress = progress;
    }

    public int calculateProgress() {
        Date currentDate = new Date();
        if (currentDate.before(startDate)) {
            return 0; // Goal hasn't started yet
        } else if (currentDate.after(endDate)) {
            return 100; // Goal is completed
        } else {
            long totalDuration = endDate.getTime() - startDate.getTime();
            long elapsedDuration = currentDate.getTime() - startDate.getTime();
            return (int) ((elapsedDuration * 100) / totalDuration);
        }
    }

    @Override
    public String toString() {
        return "Goal ID: " + goalId +
                " | Description: " + description +
                " | Start Date: " + sdf.format(startDate) +
                " | End Date: " + sdf.format(endDate) +
                " | Status: " + status;
    }
}
