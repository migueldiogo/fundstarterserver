package fundstarterserver;

/**
 * Created by Miguel Prata Leal on 22/10/15.
 */
public class ClientSession {
    String usernameLoggedIn;
    boolean sessionLoggedIn;

    public ClientSession() {
        sessionLoggedIn = false;
    }

    public String getUsernameLoggedIn() {
        return usernameLoggedIn;
    }

    public void setUsernameLoggedIn(String usernameLoggedIn) {
        this.usernameLoggedIn = usernameLoggedIn;
    }

    public Boolean getSessionLoggedIn() {
        return sessionLoggedIn;
    }

    public void setSessionLoggedIn(Boolean sessionLoggedIn) {
        this.sessionLoggedIn = sessionLoggedIn;
    }
}
