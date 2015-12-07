package fundstarter;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sergiopires on 26/10/15.
 */
public class DecisionOption implements Serializable{
    private static final long serialVersionUID = 1L;

    private int optionId;
    private int projectId;
    private String description;

    public DecisionOption() {
    }

    public DecisionOption(int projectId, String description) {
        this.projectId = projectId;
        this.description = description;
    }

    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "DecisionOption{" +
                "optionId=" + optionId +
                ", projectId=" + projectId +
                ", description='" + description + '\'' +
                '}';
    }
}

