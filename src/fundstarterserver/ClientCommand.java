package fundstarterserver;

import com.sun.deploy.util.SessionState;
import fundstarter.*;

import java.net.ConnectException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.sql.SQLException;
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

    //private String[] commandParsed;
    private ServerMessage output;
    private RMIInterface remoteObject;
    private String command;
    private ArrayList<String> arguments;
    private ClientSession clientSession;


    public ClientCommand(Command rawCommand, ClientSession clientSession) {
        rawCommand.getCommand();
        this.command = new String(rawCommand.getCommand());
        this.output = new ServerMessage();
        this.clientSession = clientSession;
        //checkArgsNumber();

        arguments = new ArrayList<String>();

        for (String argument : rawCommand.getArguments())
            arguments.add(argument);

        System.out.println("Command: " + rawCommand.getCommand());

    }

/*
    private void checkArgsNumber() {
        assert commandParsed.length > 0;
        assert commandToNumberOfArgs.get(commandParsed[0]) == (commandParsed.length - 1);
    }
*/


    public void run() {
        remoteObject = LookupRemoteObject();


        switch (command) {
            case "login":
                login();
                break;
            case "signup":
                signUP();
                break;
            case "view messages":
                viewMessages();
                break;
            case "view balance":
                //viewBalance();
                break;
            case "view rewards":
               // viewRewards();
                break;
            case "sendreward":
                //sendRewards();
                break;
            case "listInProgress":
                listInProgress();
                break;
            case "listExpired":
                listExpired();
                break;
            case "newproject":
               // newProject();
                break;
            case "pledge":
                pledge();
                break;
            case "messages":
                //messages();
                break;
            case "details":
                //details();
                break;
            case "sendText":
               // sendText();
                break;

            default:
                assert false;
        }


    }

    private void pledge() {
        System.out.println("pledge(" + arguments.get(0) + ", " + arguments.get(1) + arguments.get(2) + ")");
        boolean rmiReturnObject = false;
        Pledge pledge = new Pledge();
        pledge.setUsername(clientSession.getUsernameLoggedIn());
        pledge.setProjectName(arguments.get(0));
        pledge.setAmount(Double.parseDouble(arguments.get(1)));
        pledge.setAnswer(arguments.get(2));

        try {
            rmiReturnObject = remoteObject.pledge(pledge);
            System.out.println("Sent Response: " + rmiReturnObject);

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (RemoteException e) {
            System.out.println("Remote Exception: " + e.getMessage());
        }
        if (rmiReturnObject) {
            output.setContent(new String("Pledge successful"));
        } else {
            output.setContent(new String("Error"));
        }
        output.setErrorHappened(!rmiReturnObject);
    }

    private void listExpired() {
        System.out.println("listExpired()");
        ArrayList<Project> rmiReturnObject = null;
        try {
            rmiReturnObject = remoteObject.listExpired();
            System.out.println(rmiReturnObject);
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (RemoteException e) {
            System.out.println("Remote Exception: " + e.getMessage());
        }
        System.out.println(rmiReturnObject);
        if (!rmiReturnObject.isEmpty()) {
            output.setContent(rmiReturnObject);
            output.setErrorHappened(false);

        } else {
            output.setContent(new String("Não existem projetos."));
            output.setErrorHappened(true);

        }
    }

    private void listInProgress() {
        System.out.println("listInProgress()");
        ArrayList<Project> rmiReturnObject = null;
        try {
            rmiReturnObject = remoteObject.listInProgress();
            System.out.println(rmiReturnObject);
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (RemoteException e) {
            System.out.println("Remote Exception: " + e.getMessage());
        }
        System.out.println(rmiReturnObject);
        if (!rmiReturnObject.isEmpty()) {
            output.setContent(rmiReturnObject);
            output.setErrorHappened(false);

        } else {
            output.setContent(new String("Não existem projetos."));
            output.setErrorHappened(true);

        }
    }


    private RMIInterface LookupRemoteObject() {
        RMIInterface remoteObject = null;

        try {
            //System.getProperties().put("java.security.policy", "policy.all");
            //System.setSecurityManager(new RMISecurityManager());
            remoteObject = (RMIInterface) Naming.lookup("rmi://192.168.1.102:7000/sd");

        } catch (Exception e) {
            output.setContent("Something bad happened to our dataserver: " + e.getMessage());
            output.setErrorHappened(true);
        }
        return remoteObject;
    }



    public void login() {
        System.out.println("login(" + arguments.get(0) + ", " + arguments.get(1) + ")");
        boolean rmiReturnObject = false;
        try {
            rmiReturnObject = remoteObject.login(arguments.get(0), arguments.get(1));
            System.out.println("Sent Response: " + rmiReturnObject);

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (RemoteException e) {
            System.out.println("Remote Exception: " + e.getMessage());
        }
        if (rmiReturnObject) {
            output.setContent(new String("Welcome, " + arguments.get(0)));
            clientSession.setUsernameLoggedIn(arguments.get(0));
        } else {
            output.setContent(new String("Invalid username or password."));
        }
        clientSession.setSessionLoggedIn(true);
        output.setErrorHappened(!rmiReturnObject);
    }

    public void signUP() {
        System.out.println("signup(" + arguments.get(0) + ", " + arguments.get(1) + ")");
        boolean rmiReturnObject = false;
        try {
            rmiReturnObject = remoteObject.signUp(arguments.get(0), arguments.get(1));
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (RemoteException e) {
            System.out.println("Remote Exception: " + e.getMessage());
        }
        System.out.println(rmiReturnObject);
        if (rmiReturnObject) {
            output.setContent(new String("User, " + arguments.get(0) + " registered."));
        } else {
            output.setContent(new String("Username already exists"));
        }
        clientSession.setSessionLoggedIn(false);
        output.setErrorHappened(!rmiReturnObject);
    }

    private void viewMessages() {
    }





    public ServerMessage getServerMessage() {
        return new ServerMessage(output);
    }

}
