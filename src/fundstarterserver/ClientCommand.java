package fundstarterserver;

import fundstarter.ServerMessage;

import java.util.HashMap;

/**
 * Created by Miguel Prata Leal on 18/10/15.
 */
public class ClientCommand {
    final private static HashMap<String, Integer> commandToNumberOfArgs = new HashMap<String, Integer>() {
        {
            put("login", 2);
        }
    };

    private String rawCommand;
    private String[] commandParsed;
    private ServerMessage output;


    public ClientCommand(String rawCommand) {
        this.rawCommand = rawCommand;
        this.output = new ServerMessage();

        rawCommandParsing();
    }



    private void rawCommandParsing() {
        commandParsed = rawCommand.split(" ");
        assert commandParsed.length > 0;
        assert commandToNumberOfArgs.get(commandParsed[0]) == (commandParsed.length - 1);
    }

    public void run() {
        switch (commandParsed[0]) {
            case "login":
                System.out.println("login(" + commandParsed[1] + ", " + commandParsed[2] + ")");
                //TODO output = RMIObject.login(commandParsed[1], commandParsed[2]);
                break;

            default:
                assert false;
        }
    }

    public ServerMessage getOutput() {
        return new ServerMessage(output);
    }
}
