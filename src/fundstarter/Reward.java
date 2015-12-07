package fundstarter;

import java.io.Serializable;

/**
 * Created by sergiopires on 26/10/15.
 */
public class Reward implements Serializable{
    private static final long serialVersionUID = 1L;

    private int userId;
    private int projectId;
    private double minAmount;
    private String description;

    private double done;

    public Reward() {
    }

    public Reward(int userId, int projectId, double minAmount, String description) {
        this.userId = userId;
        this.projectId = projectId;
        this.minAmount = minAmount;
        this.description = description;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public double getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(double minAmount) {
        this.minAmount = minAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getDone() {
        return done;
    }

    public void setDone(double done) {
        this.done = done;
    }

    @Override
    public String toString() {
        return "Reward{" +
                "description='" + description + '\'' +
                ", userId=" + userId +
                ", projectId=" + projectId +
                ", minAmount=" + minAmount +
                ", done=" + done +
                '}';
    }
}
