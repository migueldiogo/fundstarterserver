package fundstarterserver;

/**
 * Created by Miguel Prata Leal on 10/10/15.
 */
public class NotRecognizedCommandException extends Exception {
    public NotRecognizedCommandException(String message) {
        super(message);
    }
}
