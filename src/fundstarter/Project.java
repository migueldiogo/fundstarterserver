package fundstarter;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sergiopires on 26/10/15.
 */
public class Project implements Serializable{
    private static final long serialVersionUID = 1L;

    private int projectId;
    private String name;
    private String description;
    private String expirationDate;
    private double firstGoal;
    private double totalAmountEarned;

    public Project() {
    }

    public Project(String name, String description, String expirationDate, double firstGoal) {
        this.name = name;
        this.description = description;
        this.expirationDate = expirationDate;
        this.firstGoal = firstGoal;
    }

    public Project(int projectId, String name, String description, String expirationDate, double firstGoal, double totalAmountEarned) {
        this.projectId = projectId;
        this.name = name;
        this.description = description;
        this.expirationDate = expirationDate;
        this.firstGoal = firstGoal;
        this.totalAmountEarned = totalAmountEarned;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public double getFirstGoal() {
        return firstGoal;
    }

    public void setFirstGoal(double firstGoal) {
        this.firstGoal = firstGoal;
    }

    public double getTotalAmountEarned() {
        return totalAmountEarned;
    }

    public void setTotalAmountEarned(double totalAmountEarned) {
        this.totalAmountEarned = totalAmountEarned;
    }


    public double getPercentageOfProgress(){
        return (totalAmountEarned/ firstGoal)*100;
    }


    @Override
    public String toString() {
        return "Project{" +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", expirationDate='" + expirationDate + '\'' +
                ", firstGoal=" + firstGoal +
                ", totalAmountEarned=" + totalAmountEarned +
                '}';
    }
}
