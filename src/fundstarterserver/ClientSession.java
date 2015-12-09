package fundstarterserver;

/**
 * Created by Miguel Prata Leal on 22/10/15.
 */
public class ClientSession {
    private static ClientSession ourInstance = new ClientSession();

    String usernameLoggedIn;
    int userIDLoggedIn;
    boolean sessionLoggedIn;

    private ClientSession() {
        usernameLoggedIn = "";
        sessionLoggedIn = false;
    }

    public static ClientSession getInstance() {
        return ourInstance;
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

    public int getUserIDLoggedIn() {
        return userIDLoggedIn;
    }

    public void setUserIDLoggedIn(int userIDLoggedIn) {
        this.userIDLoggedIn = userIDLoggedIn;
    }
}
