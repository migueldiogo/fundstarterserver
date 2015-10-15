package fundstarterserver;

import java.io.Serializable;

/**
 * Created by Miguel Prata Leal on 15/10/15.
 */
public class ServerMessage implements Serializable{
    private String errorMessageBefore;
    private String content;
    private boolean repeatAnswerToPrevious;

    public ServerMessage() {
        this.errorMessageBefore = "";
        this.repeatAnswerToPrevious = false;
    }

    public ServerMessage(String errorMessageBefore, String content, boolean repeatAnswerToPrevious) {
        this.errorMessageBefore = errorMessageBefore;
        this.content = content;
        this.repeatAnswerToPrevious = repeatAnswerToPrevious;
    }

    public String getErrorMessageBefore() {
        return errorMessageBefore;
    }

    public void setErrorMessageBefore(String errorMessageBefore) {
        this.errorMessageBefore = errorMessageBefore;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isRepeatAnswerToPrevious() {
        return repeatAnswerToPrevious;
    }

    public void setRepeatAnswerToPrevious(boolean repeatAnswerToPrevious) {
        this.repeatAnswerToPrevious = repeatAnswerToPrevious;
    }

    @Override
    public String toString() {
        return errorMessageBefore + "\n" + content;
    }
}
