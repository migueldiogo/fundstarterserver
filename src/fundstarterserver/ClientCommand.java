package fundstarterserver;

import fundstarter.*;

import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
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

    private Connection connection;
    private ServerMessage output;
    private RMIInterface remoteObject;
    private String command;
    private ArrayList<String> arguments;
    private Object attachedObject;
    private ClientSession clientSession;

    //private int RMIReconnectionAttemptsRemaining = 2;


    public ClientCommand(Connection connection, Command rawCommand, Object attachedObject, ClientSession clientSession) {
        rawCommand.getCommand();
        this.connection = connection;
        this.command = new String(rawCommand.getCommand());
        this.attachedObject = attachedObject;
        this.output = new ServerMessage();
        this.clientSession = clientSession;
        this.arguments = new ArrayList<String>();

        for (String argument : rawCommand.getArguments())
            arguments.add(argument);

    }




    public void run() {
        remoteObject = LookupRemoteObject();

        switch (command) {
            case "login":
                login();
                break;
            case "signup":
                signUP();
                break;
            case "getBalance":
                viewBalance();
                break;
            case "getRewardsFromUser":
                viewRewards();
                break;
            case "sendRewardToUser":
                sendReward();
                break;
            case "getInProgressProjects":
                listInProgress();
                break;
            case "gerExpiredProjects":
                listExpired();
                break;
            case "newProject":
                newProject();
                break;
            case "pledge":
                pledge();
                break;
            case "getUserMessages":
                viewMessages();
                break;
            case "getProjectMessages":
                viewProjectMessages();
                break;
            case "details":
                details();
                break;
            case "sendMessage":
                sendMessage();
                break;
            case "sendMessageToProject":
                sendMessageToProject();
                break;
            case "sendMessageFromProject":
                sendMessageFromProject();
                break;
            case "addAdminToProject":
                addAdminToProject();
                break;
            case "logout":
                logoutUser();
                break;
            case "cancelProject":
                cancelProject();
                break;
            case "addRewardToProject":
                addRewardToProject();
                break;
            case "removeRewardFromProject":
                removeRewardFromProject();
                break;
            case "removeExtraFromProject":
                removeExtraFromProject();
                break;
            case "addExtraToProject":
                addExtraToProject();
                break;
            case "loginFailOver":
                loginFailOver();
                break;
            case "getPledgesFromUser":
                viewPledges();
                break;
            default:
                assert false;
        }

    }


    public void login() {
        System.out.println("login(" + arguments.get(0) + ", " + arguments.get(1) + ")");
        boolean rmiReturnObject = false;
        try {
            rmiReturnObject = remoteObject.login(arguments.get(0), arguments.get(1));
            System.out.println("DataServer Response: " + rmiReturnObject);

            if (rmiReturnObject) {
                clientSession.setUsernameLoggedIn(arguments.get(0));
                output.setContent(new String("Welcome, " + arguments.get(0)));
            } else {
                output.setContent(new String("Invalid username or password."));
            }
            clientSession.setSessionLoggedIn(true);
            output.setErrorHappened(!rmiReturnObject);

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (ConnectException e) {
            tryToRecoverRMIConnection();
        } catch (RemoteException e) {
            System.out.println("Remote Exception: " + e.getMessage());
        }



    }

    public void signUP() {
        System.out.println("signup(" + arguments.get(0) + ", " + arguments.get(1) + ")");
        boolean rmiReturnObject = false;
        try {
            rmiReturnObject = remoteObject.signUp(arguments.get(0), arguments.get(1));

            System.out.println(rmiReturnObject);
            if (rmiReturnObject) {
                output.setContent(new String("User, " + arguments.get(0) + " registered."));
            } else {
                output.setContent(new String("Username already exists"));
            }
            clientSession.setSessionLoggedIn(false);
            output.setErrorHappened(!rmiReturnObject);
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (ConnectException e) {
            tryToRecoverRMIConnection();
        } catch (RemoteException e) {
            System.out.println("Remote Exception: " + e.getMessage());
        }

    }

    private void logoutUser() {
        System.out.println(clientSession.getUsernameLoggedIn() + ": logout()");
        if (clientSession.getSessionLoggedIn() != null) {
            output.setContent("Bye, " + clientSession.getUsernameLoggedIn());
            clientSession.setSessionLoggedIn(false);
            clientSession.setUsernameLoggedIn("");

        } else {
            output.setContent("Something went wrong. You're already logged out.");
        }
    }

    private void viewBalance() {
        System.out.println("getBalance()");
        double rmiReturnObject = -1;


        try {
            rmiReturnObject = remoteObject.getBalance(clientSession.getUsernameLoggedIn());
            System.out.println("DataServer Response: " + rmiReturnObject);

            if (rmiReturnObject >= 0) {
                output.setContent("" + rmiReturnObject);
                output.setErrorHappened(false);
            } else {
                output.setContent(new String("Error"));
            }
            output.setErrorHappened(true);

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (ConnectException e) {
            tryToRecoverRMIConnection();
        } catch (RemoteException e) {
            System.out.println("Remote Exception: " + e.getMessage());
        }

    }

    private void newProject() {
        System.out.println("newProject()");

        Project project = (Project)attachedObject;
        project.setCreator(clientSession.getUsernameLoggedIn());


        boolean rmiReturnObject = false;

        try {

            rmiReturnObject = remoteObject.newProject(project);
            System.out.println("DataServer Response: " + rmiReturnObject);

            if (rmiReturnObject) {
                output.setContent(new String("Project successfully created."));
            } else {
                output.setContent(new String("Project not created. Something went wrong."));
            }
            output.setErrorHappened(!rmiReturnObject);

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (ConnectException e) {
            tryToRecoverRMIConnection();
        } catch (RemoteException e) {
            System.out.println("Remote Exception: " + e.getMessage());
        }

    }

    private void addExtraToProject() {
        System.out.println("addExtraToProject()");
        boolean rmiReturnObject = false;

        try {
            rmiReturnObject = remoteObject.addGoalToProject((Goal) attachedObject);
            System.out.println("Sent Response: " + rmiReturnObject);

            if (rmiReturnObject) {
                output.setContent(new String("Goal added to " + ((Goal) attachedObject).getProjectName()) + ".");
            } else {
                output.setContent(new String("Goal not added. Something went wrong."));
            }
            output.setErrorHappened(!rmiReturnObject);

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (ConnectException e) {
            tryToRecoverRMIConnection();
        } catch (RemoteException e) {
            System.out.println("Remote Exception: " + e.getMessage());
        }

    }

    private void addRewardToProject() {
        System.out.println("addRewardToProject()");
        boolean rmiReturnObject = false;

        try {
            rmiReturnObject = remoteObject.addRewardToProject((Reward) attachedObject);
            System.out.println("DataServer Response: " + rmiReturnObject);

            if (rmiReturnObject) {
                output.setContent(new String("Reward added to " + ((Reward) attachedObject).getProjectName()) + ".");
            } else {
                output.setContent(new String("Reward not added. Something went wrong."));
            }
            output.setErrorHappened(!rmiReturnObject);

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (ConnectException e) {
            tryToRecoverRMIConnection();
        } catch (RemoteException e) {
            System.out.println("Remote Exception: " + e.getMessage());
        }

    }

    private void addAdminToProject() {
        System.out.println("addAdminToProject(" + clientSession.getUsernameLoggedIn() + ", " +  arguments.get(0) + ", " + arguments.get(1) + ")");
        boolean rmiReturnObject = false;

        try {
            rmiReturnObject = remoteObject.addAdminToProject(clientSession.getUsernameLoggedIn(), arguments.get(0), arguments.get(1));
            System.out.println("DataServer Response: " + rmiReturnObject);

            if (rmiReturnObject) {
                output.setContent(new String("The username " + arguments.get(1) + " is now admin of " + arguments.get(0) + "."));
            } else {
                output.setContent(new String("The project wasn't found or you don't have enough privileges to perform this action."));
            }
            output.setErrorHappened(!rmiReturnObject);

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (ConnectException e) {
            tryToRecoverRMIConnection();
        } catch (RemoteException e) {
            System.out.println("Remote Exception: " + e.getMessage());
        }

    }

    private void removeExtraFromProject() {
        System.out.println("removeExtraFromProject()");
        boolean rmiReturnObject = false;

        try {
            rmiReturnObject = remoteObject.removeGoalFromProject((Goal) attachedObject);
            System.out.println("DataServer Response: " + rmiReturnObject);

            if (rmiReturnObject) {
                output.setContent(new String("Goal removed from " + ((Goal) attachedObject).getProjectName()) + ".");
            } else {
                output.setContent(new String("Goal not removed. Something went wrong."));
            }
            output.setErrorHappened(!rmiReturnObject);

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (ConnectException e) {
            tryToRecoverRMIConnection();
        } catch (RemoteException e) {
            System.out.println("Remote Exception: " + e.getMessage());
        }

    }

    private void removeRewardFromProject() {
        System.out.println("removeRewardFromProject()");
        boolean rmiReturnObject = false;

        try {
            rmiReturnObject = remoteObject.removeRewardFromProject((Reward) attachedObject);
            System.out.println("DataServer Response: " + rmiReturnObject);

            if (rmiReturnObject) {
                output.setContent(new String("Reward removed from " + ((Reward) attachedObject).getProjectName()) + ".");
            } else {
                output.setContent(new String("Reward not removed. Something went wrong."));
            }
            output.setErrorHappened(!rmiReturnObject);

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (ConnectException e) {
            tryToRecoverRMIConnection();
        } catch (RemoteException e) {
            System.out.println("Remote Exception: " + e.getMessage());
        }

    }

    private void viewRewards() {
        System.out.println("getRewardsFromUser()");
        ArrayList<AttributedReward> rmiReturnObject = null;
        try {
            rmiReturnObject = remoteObject.getRewardsFromUser(clientSession.getUsernameLoggedIn());
            System.out.println(rmiReturnObject);
            if (!rmiReturnObject.isEmpty()) {
                output.setContent(rmiReturnObject);
                output.setErrorHappened(false);

            } else {
                output.setContent(new String("You don't have any rewards.."));
                output.setErrorHappened(true);

            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (ConnectException e) {
            tryToRecoverRMIConnection();
        } catch (RemoteException e) {
            System.out.println("Remote Exception: " + e.getMessage());
        }

    }

    private void viewPledges() {
        System.out.println("getPledgesFromUser()");
        ArrayList<Pledge> rmiReturnObject = null;
        try {
            rmiReturnObject = remoteObject.getPledgesFromUser(clientSession.getUsernameLoggedIn());
            System.out.println(rmiReturnObject);
            if (!rmiReturnObject.isEmpty()) {
                output.setContent(rmiReturnObject);
                output.setErrorHappened(false);

            } else {
                output.setContent(new String("You don't have any pledges.."));
                output.setErrorHappened(true);

            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (ConnectException e) {
            tryToRecoverRMIConnection();
        } catch (RemoteException e) {
            System.out.println("Remote Exception: " + e.getMessage());
        }
    }

    private void cancelProject() {
        System.out.println("cancelProject(" + arguments.get(0) + ")");
        boolean rmiReturnObject = false;

        try {
            rmiReturnObject = remoteObject.cancelProject(arguments.get(0), clientSession.getUsernameLoggedIn());
            System.out.println("DataServer Response: " + rmiReturnObject);

            if (rmiReturnObject) {
                output.setContent(new String("Project canceled"));
            } else {
                output.setContent(new String("An error has occurred because that project doesn't exists or you don't have enough privileges."));
            }
            output.setErrorHappened(!rmiReturnObject);

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (ConnectException e) {
            tryToRecoverRMIConnection();
        } catch (RemoteException e) {
            System.out.println("Remote Exception: " + e.getMessage());
        }

    }

    private void details() {
        System.out.println("getProjectDetails(" + clientSession.getUsernameLoggedIn() + ": " +  arguments.get(0) + ")");

        Project rmiReturnObject = null;

        try {

            rmiReturnObject = remoteObject.getProjectDetails(arguments.get(0));
            System.out.println("DataServer Response: " + rmiReturnObject);

            if (rmiReturnObject != null) {
                output.setContent(rmiReturnObject);
                output.setErrorHappened(false);

            } else {
                output.setContent(new String("That project doesn't exist."));
                output.setErrorHappened(true);

            }

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (ConnectException e) {
            tryToRecoverRMIConnection();
        } catch (RemoteException e) {
            System.out.println("Remote Exception: " + e.getMessage());
        }

    }

    private void pledge() {
        System.out.println("pledge(" + arguments.get(0) + ", " + arguments.get(1) + ", " + arguments.get(2) + ")");
        boolean rmiReturnObject = false;
        Pledge pledge = new Pledge();
        pledge.setUsername(clientSession.getUsernameLoggedIn());
        pledge.setProjectName(arguments.get(0));
        pledge.setAmount(Double.parseDouble(arguments.get(1)));
        pledge.setDecision(arguments.get(2));

        try {
            rmiReturnObject = remoteObject.pledge(pledge);
            System.out.println("DataServer Response: " + rmiReturnObject);

            if (rmiReturnObject) {
                output.setContent(new String("Pledge successful to " + arguments.get(0) + "."));
            } else {
                output.setContent(new String("Not enough credits or project has expired."));
            }
            output.setErrorHappened(!rmiReturnObject);

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (ConnectException e) {
            tryToRecoverRMIConnection();
        } catch (RemoteException e) {
            System.out.println("Remote Exception: " + e.getMessage());
        }

    }

    private void sendReward() {
        System.out.println("sendRewardToUser(" + arguments.get(0) + ", " + arguments.get(1) + ")");
        boolean rmiReturnObject = false;

        try {
            rmiReturnObject = remoteObject.sendRewardToUser(arguments.get(0), arguments.get(1), clientSession.getUsernameLoggedIn(), arguments.get(2));
            System.out.println("DataServer Response: " + rmiReturnObject);

            if (rmiReturnObject) {
                output.setContent(new String("Reward successfully sent to " + arguments.get(2)));
            } else {
                output.setContent(new String("Reward not sent."));
            }
            output.setErrorHappened(!rmiReturnObject);

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (ConnectException e) {
            tryToRecoverRMIConnection();
        } catch (RemoteException e) {
            System.out.println("Remote Exception: " + e.getMessage());
        }

    }

    private void sendMessage() {
        System.out.println(clientSession.getUsernameLoggedIn() + ": " + "sendmessage(" + attachedObject + ")");
        boolean rmiReturnObject = false;
        Message message = (Message)attachedObject;
        message.setSendFrom(clientSession.getUsernameLoggedIn());

        try {
            rmiReturnObject = remoteObject.sendMessage(message);
            System.out.println("DataServer Response: " + rmiReturnObject);

            if (rmiReturnObject) {
                output.setContent(new String("Message sent."));
            } else {
                output.setContent(new String("Message not sent."));
            }
            output.setErrorHappened(!rmiReturnObject);

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (ConnectException e) {
            tryToRecoverRMIConnection();
        } catch (RemoteException e) {
            System.out.println("Remote Exception: " + e.getMessage());
        }

    }

    private void sendMessageToProject() {
        System.out.println(clientSession.getUsernameLoggedIn() + ": " + "sendMessageToProject(" + attachedObject + ")");
        boolean rmiReturnObject = false;
        Message message = (Message)attachedObject;
        message.setSendFrom(clientSession.getUsernameLoggedIn());

        try {
            rmiReturnObject = remoteObject.sendMessageToProject(message);
            System.out.println("DataServer Response: " + rmiReturnObject);

            if (rmiReturnObject) {
                output.setContent(new String("Message sent."));
            } else {
                output.setContent(new String("Message not sent."));
            }
            output.setErrorHappened(!rmiReturnObject);

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (ConnectException e) {
            tryToRecoverRMIConnection();
        } catch (RemoteException e) {
            System.out.println("Remote Exception: " + e.getMessage());
        }

    }

    private void sendMessageFromProject() {
        System.out.println(clientSession.getUsernameLoggedIn() + ": " + "sendMessageFromProject(" + attachedObject + ")");
        boolean rmiReturnObject = false;
        Message message = (Message)attachedObject;
        message.setSendFrom(clientSession.getUsernameLoggedIn());

        try {
            rmiReturnObject = remoteObject.sendMessageFromProject(message);
            System.out.println("DataServer Response: " + rmiReturnObject);

            if (rmiReturnObject) {
                output.setContent(new String("Message sent."));
            } else {
                output.setContent(new String("Message not sent. Are you admin of this project?"));
            }
            output.setErrorHappened(!rmiReturnObject);

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (ConnectException e) {
            tryToRecoverRMIConnection();
        } catch (RemoteException e) {
            System.out.println("Remote Exception: " + e.getMessage());
        }
    }

    private void viewMessages() {
        System.out.println("getUserMessages()");
        ArrayList<Message> rmiReturnObject = null;
        try {
            rmiReturnObject = remoteObject.getUserMessages(clientSession.getUsernameLoggedIn());

            System.out.println(rmiReturnObject);
            if (!rmiReturnObject.isEmpty()) {
                output.setContent(rmiReturnObject);
                output.setErrorHappened(false);

            } else {
                output.setContent(new String("There is no messages."));
                output.setErrorHappened(true);

            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (ConnectException e) {
            tryToRecoverRMIConnection();
        } catch (RemoteException e) {
            System.out.println("Remote Exception: " + e.getMessage());
        }

    }

    private void viewProjectMessages() {
        System.out.println("getProjectMessages()");
        ArrayList<Message> rmiReturnObject;
        try {
            rmiReturnObject = remoteObject.getProjectMessages(arguments.get(0), clientSession.getUsernameLoggedIn());

            System.out.println(rmiReturnObject);
            if (!rmiReturnObject.isEmpty()) {
                output.setContent(rmiReturnObject);
                output.setErrorHappened(false);

            } else {
                output.setContent(new String("There is no messages or you don't have enough privileges to see them."));
                output.setErrorHappened(true);

            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (ConnectException e) {
            tryToRecoverRMIConnection();
        } catch (RemoteException e) {
            System.out.println("Remote Exception: " + e.getMessage());
        }

    }

    private void listExpired() {
        System.out.println("gerExpiredProjects()");
        ArrayList<Project> rmiReturnObject = null;
        try {
            rmiReturnObject = remoteObject.gerExpiredProjects();

            System.out.println(rmiReturnObject);
            if (!rmiReturnObject.isEmpty()) {
                output.setContent(rmiReturnObject);
                output.setErrorHappened(false);

            } else {
                output.setContent(new String("Não existem projetos."));
                output.setErrorHappened(true);

            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (ConnectException e) {
            tryToRecoverRMIConnection();
        } catch (RemoteException e) {
            System.out.println("Remote Exception: " + e.getMessage());
        }

    }

    private void listInProgress() {
        System.out.println("getInProgressProjects()");
        ArrayList<Project> rmiReturnObject = null;
        try {
            rmiReturnObject = remoteObject.getInProgressProjects();

            System.out.println(rmiReturnObject);
            if (!rmiReturnObject.isEmpty()) {
                output.setContent(rmiReturnObject);
                output.setErrorHappened(false);

            } else {
                output.setContent(new String("Não existem projetos."));
                output.setErrorHappened(true);

            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (ConnectException e) {
            tryToRecoverRMIConnection();
        } catch (RemoteException e) {
            System.out.println("Remote Exception: " + e.getMessage());
        }

    }


    private void loginFailOver() {
        System.out.println("loginFailOver(" + arguments.get(0) + ")");
        clientSession.setUsernameLoggedIn(arguments.get(0));
        clientSession.setSessionLoggedIn(true);
        output.setErrorHappened(false);
        output.setContent("");
    }



    private RMIInterface LookupRemoteObject() {
        RMIInterface remoteObject = null;

        try {
            //System.getProperties().put("java.security.policy", "policy.all");
            //System.setSecurityManager(new RMISecurityManager());
            remoteObject = (RMIInterface) Naming.lookup("rmi://" + ServerConfigProperties.RMI_SERVER_IP +
                    ":" + ServerConfigProperties.RMI_SERVER_PORT + "/sd");
        } catch (ConnectException e) {
            tryToRecoverRMIConnection();
        } catch (RemoteException e) {
            output.setContent("Something bad happened to our dataserver: " + e.getMessage());
            output.setErrorHappened(true);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }

        return remoteObject;

    }


    public void tryToRecoverRMIConnection() {
        System.out.println("Not reaching RMI server. Trying to reconnect...");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted while sleeping: " + e.getMessage());
        }
        try{
            remoteObject = (RMIInterface) Naming.lookup("rmi://" + ServerConfigProperties.RMI_SERVER_IP +
                            ":" + ServerConfigProperties.RMI_SERVER_PORT + "/sd");
            remoteObject.testRMIConnection();
            run();
        } catch (RemoteException e) {
            System.out.println("RMI server is definitely not available. Try FundStarter later...");
            output.setContent(new String("RMI server is not available. We tried to reconnect without you knowing..."));
            output.setErrorHappened(true);

        } catch (MalformedURLException e) {
            System.out.println("MalformedURLException: " + e.getMessage());
        } catch (NotBoundException e) {
            System.out.println("NotBoundException: " + e.getMessage());
        }

    }

    public ServerMessage getServerMessage() {
        return output;
    }



}
