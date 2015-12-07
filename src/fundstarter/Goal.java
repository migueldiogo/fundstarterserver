package fundstarter;

import java.io.Serializable;

/**
 * Created by sergiopires on 26/10/15.
 */
public class Goal implements Serializable{
    private static final long serialVersionUID = 1L;

    private double amount;
    private int projectId;
    private String extraDescription;


    public Goal() {
    }

    public Goal(double amount, int projectId, String extraDescription) {
        this.amount = amount;
        this.projectId = projectId;
        this.extraDescription = extraDescription;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getExtraDescription() {
        return extraDescription;
    }

    public void setExtraDescription(String extraDescription) {
        this.extraDescription = extraDescription;
    }

    @Override
    public String toString() {
        return "Goal{" +
                "amount=" + amount +
                ", projectId=" + projectId +
                ", extraDescription='" + extraDescription + '\'' +
                '}';
    }
}
