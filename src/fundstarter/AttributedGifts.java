package fundstarter;

import java.io.Serializable;

/**
 * Created by sergiopires on 26/10/15.
 */
public class AttributedGifts implements Serializable{
    private String username;
    private String projectName;
    private String giftName;
    private String sender;

    private static final long serialVersionUID = 1L;


    public AttributedGifts(String username, String projectName, String giftName, String sender) {
        this.username = username;
        this.projectName = projectName;
        this.giftName = giftName;
        this.sender = sender;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
