package fundstarterserver;


public class OnlyForLoggedInUsersException extends Exception {
    public OnlyForLoggedInUsersException(String message) {
        super(message);
    }
}
