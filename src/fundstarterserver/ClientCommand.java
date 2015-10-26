package fundstarterserver;

import fundstarter.Command;
import fundstarter.RMI_interface;
import fundstarter.ServerMessage;

import java.net.ConnectException;
import java.rmi.Naming;
import java.util.ArrayList;
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

    private Command command;
    private String[] commandParsed;
    private ServerMessage output;


    public ClientCommand(Command command) {
        this.command = command;
        this.output = new ServerMessage();
        checkArgsNumber;
    }

    @Deprecated
    /*
    private void rawCommandParsing() {
        commandParsed = rawCommand.split(" ");
        assert commandParsed.length > 0;
        assert commandToNumberOfArgs.get(commandParsed[0]) == (commandParsed.length - 1);
    }
    */


    public void run(ClientSession clientSession) {
        RMI_interface remoteObject = LookupRemoteObject();
        ArrayList<String> argumentos = new ArrayList<String>();

        for (String argumento : command.getArguments())
            argumentos.add(argumento);

        switch (command.getCommand()) {
            case "login":
                System.out.println("login(" + argumentos.get(0) + ", " + argumentos.get(0) + ")");
                Boolean rmiReturnValue = remoteObject.login(argumentos.get(0), argumentos.get(1));
                if (rmiReturnValue) {
                    output.setContent(new String("Welcome, " + argumentos.get(0)));
                    clientSession.setUsernameLoggedIn(argumentos.get(0));
                }
                else {
                    output.setContent(new String("Invalid username or password."));
                }
                clientSession.setSessionLoggedIn(true);
                output.setRepeatAnswerToPrevious(!rmiReturnValue);
                break;

            default:
                assert false;
        }

    }

    private RMI_interface LookupRemoteObject() {
        RMI_interface remoteObject = null;

        try {
            //System.getProperties().put("java.security.policy", "policy.all");
            //System.setSecurityManager(new RMISecurityManager());
            remoteObject = (RMI_interface) Naming.lookup("rmi://192.168.1.102:7000/benfica");

        } catch (Exception e) {
            output.setContent("Something bad happened to our dataserver: " + e.getMessage());
            output.setRepeatAnswerToPrevious(true);
        }
        return remoteObject;
    }

    public ServerMessage getServerMessage() {
        return new ServerMessage(output);
    }
}
