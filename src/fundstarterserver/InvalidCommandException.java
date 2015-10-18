package fundstarterserver;

/**
 * Created by Miguel Prata Leal on 18/10/15.
 */
@Deprecated
public class InvalidCommandException extends Exception {
    public InvalidCommandException(String message) {
        super(message);
    }
}
