package fundstarter;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by xavier on 25-10-2015.
 */
public class Command implements Serializable{
    private static final long serialVersionUID = 1L;
    protected String command;
    protected ArrayList<String> arguments;

    public Command(String command, ArrayList<String> arguments) {
        this.command = command;
        this.arguments = arguments;
    }

    public Command() {
        this.command = "";
        this.arguments = new ArrayList<>();
    }


    public String getCommand() {
        return this.command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public ArrayList<String> getArguments() {
        return arguments;
    }

    public void setArguments(ArrayList<String> arguments) {
        this.arguments = arguments;
    }
}
