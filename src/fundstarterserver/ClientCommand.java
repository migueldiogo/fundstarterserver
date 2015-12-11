package fundstarterserver;

import fundstarter.*;

import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
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


    public ClientCommand(Command rawCommand, Object attachedObject) {
        rawCommand.getCommand();
        this.command = new String(rawCommand.getCommand());
        this.attachedObject = attachedObject;
        this.output = new ServerMessage();
        this.arguments = new ArrayList<>();

        for (String argument : rawCommand.getArguments())
            arguments.add(argument);

        clientSession = ClientSession.getInstance();
    }


    public ClientCommand(Connection connection, Command rawCommand, Object attachedObject) {
        rawCommand.getCommand();
        this.connection = connection;
        this.command = new String(rawCommand.getCommand());
        this.attachedObject = attachedObject;
        this.output = new ServerMessage();
        this.arguments = new ArrayList<>();

        for (String argument : rawCommand.getArguments())
            arguments.add(argument);

        clientSession = ClientSession.getInstance();

    }

    public void run() {
        remoteObject = LookupRemoteObject();

        printClientCommand();

        try {
            switch (command) {
                case "login":
                    login();
                    break;
                case "signup":
                    signUP();
                    break;
                case "logout":
                    logoutUser();
                    break;
                case "getBalance":
                    getBalance();
                    break;
                case "newProject":
                    newProject();
                    break;
                case "cancelProject":
                    cancelProject();
                    break;
                case "addAdminToProject":
                    addAdminToProject();
                    break;
                case "addGoalToProject":
                    addGoalToProject();
                    break;
                case "addRewardToProject":
                    addRewardToProject();
                    break;
                case "addQuestionToProject":           //TODO no cliente
                    addQuestionToProject();
                    break;
                case "addOptionToProject":             //TODO no cliente
                    addOptionToProject();
                    break;
                case "removeGoalFromProject":
                    removeGoalFromProject();
                    break;
                case "removeRewardFromProject":
                    removeRewardFromProject();
                    break;
                case "getProject":
                    getProject();
                    break;
                case "getRewardsFromProject":           //TODO no cliente
                    getRewardsFromProject();
                    break;
                case "getGoalsFromProject":             //TODO no cliente
                    getGoalsFromProject();
                    break;
                case "getPledgesFromUser":
                    getPledgesFromUser();
                    break;
                case "getRewardsFromUser":
                    getRewardsFromUser();
                    break;
                case "getProjectMessages":
                    getProjectMessages();
                    break;
                case "getUserMessages":
                    getUserMessages();
                    break;
                case "getExpiredProjects":
                    getExpiredProjects();
                    break;
                case "getInProgressProjects":
                    getInProgressProjects();
                    break;
                case "getProjectOptions":
                    getProjectOptions();
                    break;
                case "sendMessageFromProject":
                    sendMessageFromProject();
                    break;
                case "sendMessageToProject":
                    sendMessageToProject();
                    break;
                case "sendRewardToUser":
                    sendRewardToUser();
                    break;
                case "pledge":
                    pledge();
                    break;

                case "sendMessage":
                    sendMessage();
                    break;
                case "loginFailOver":
                    loginFailOver();
                    break;
                default:
                    assert false;
            }
            output.setErrorHappened(false);

        } catch (SQLException e) {
            System.out.println("[DataServer] SQL Exception: " + e.getMessage());
            output.setContent(e.getMessage());
            output.setErrorHappened(true);
        } catch (ConnectException e) {
            tryToRecoverRMIConnection();
        } catch (RemoteException e) {
            System.out.println("[DataServer] Remote Exception: " + e.getMessage());
        }


    }

    void printClientCommand() {
        System.out.print("[Client: " + clientSession.getUsernameLoggedIn() + " (id:" + clientSession.getUserIDLoggedIn() + ")] ");
        System.out.print(command + " (");
        for (int i = 0; i < arguments.size(); i++ ) {
            System.out.print(arguments.get(i));
            if (i != (arguments.size() - 1))
                System.out.print(", ");
        }
        System.out.println(")");
    }

    void printDataServerResponse(Object object) {
        System.out.println("[DataServer] " + object);
    }


    /** CLIENT INTERFACE **/

    public void login() throws RemoteException, SQLException {
        int userID = remoteObject.login(arguments.get(0), arguments.get(1));
        printDataServerResponse(userID);

        clientSession.setUserIDLoggedIn(userID);
        output.setContent(new String("Welcome, " + arguments.get(0)));
        clientSession.setSessionLoggedIn(true);
        clientSession.setUsernameLoggedIn(arguments.get(0));

    }

    public void signUP() throws RemoteException, SQLException {
        boolean rmiReturnObject = remoteObject.signUp(arguments.get(0), arguments.get(1));
        printDataServerResponse(rmiReturnObject);
        output.setContent(new String("User " + arguments.get(0) + " registered."));
    }

    private void logoutUser() {
        if (clientSession.getSessionLoggedIn()) {
            output.setContent("Bye, " + clientSession.getUsernameLoggedIn());
            clientSession.setSessionLoggedIn(false);
            clientSession.setUsernameLoggedIn("");
            clientSession.setUserIDLoggedIn(0);
        } else {
            output.setContent("Something went wrong. You're already logged out.");
        }
    }

    private void getBalance() throws SQLException, RemoteException {
        double rmiReturnObject = remoteObject.getBalance(clientSession.getUserIDLoggedIn());
        printDataServerResponse(rmiReturnObject);
        output.setContent("" + rmiReturnObject);
    }

    private void newProject() throws SQLException, RemoteException {
        Project project = (Project)attachedObject;

        int rmiReturnObject = remoteObject.newProject(project, clientSession.getUserIDLoggedIn());

        printDataServerResponse(rmiReturnObject);
        output.setContent("Project successfully created. Project id: " + rmiReturnObject + ".");
    }

    private void cancelProject() throws SQLException, RemoteException {
        boolean rmiReturnObject = remoteObject.cancelProject(Integer.parseInt(arguments.get(0)), clientSession.getUserIDLoggedIn());
        printDataServerResponse(rmiReturnObject);
        output.setContent("Project canceled.");

    }

    private void addAdminToProject() throws SQLException, RemoteException {
        boolean rmiReturnObject = remoteObject.addAdminToProject(arguments.get(0), Integer.parseInt(arguments.get(1)), clientSession.getUserIDLoggedIn());
        printDataServerResponse(rmiReturnObject);
        output.setContent("The username " + arguments.get(0) + " is now admin of the project with id " + arguments.get(1));
    }

    private void addGoalToProject() throws SQLException, RemoteException {
        boolean rmiReturnObject = remoteObject.addGoalToProject((Goal)attachedObject, Integer.parseInt(arguments.get(0)), clientSession.getUserIDLoggedIn());
        printDataServerResponse(rmiReturnObject);
        output.setContent("Goal added to project " + arguments.get(0) + ".");
        output.setErrorHappened(!rmiReturnObject);
    }

    private void addRewardToProject() throws SQLException, RemoteException {
        boolean rmiReturnObject = remoteObject.addRewardToProject((Reward)attachedObject, Integer.parseInt(arguments.get(0)), clientSession.getUserIDLoggedIn());
        printDataServerResponse(rmiReturnObject);
        output.setContent("Reward added to project " + arguments.get(0) + ".");
    }

    // TODO no cliente
    private void addQuestionToProject() throws SQLException, RemoteException {
        boolean rmiReturnObject = remoteObject.addQuestionToProject(arguments.get(0), Integer.parseInt(arguments.get(1)), clientSession.getUserIDLoggedIn());
        printDataServerResponse(rmiReturnObject);
        output.setContent("Question added to project " + arguments.get(0) + ".");
    }

    // TODO no cliente
    private void addOptionToProject() throws SQLException, RemoteException {
        boolean rmiReturnObject = remoteObject.addOptionToProject((DecisionOption)attachedObject, Integer.parseInt(arguments.get(0)), clientSession.getUserIDLoggedIn());
        printDataServerResponse(rmiReturnObject);
        output.setContent("Option added to project " + arguments.get(0) + ".");
    }

    private void removeGoalFromProject() throws SQLException, RemoteException {
        boolean rmiReturnObject = remoteObject.removeGoalFromProject((Goal)attachedObject, Integer.parseInt(arguments.get(0)), clientSession.getUserIDLoggedIn());
        System.out.println("DataServer Response: " + rmiReturnObject);
        output.setContent("Goal successfully removed from project.");
    }

    //TODO verificar com cliente e dataserver
    private void removeRewardFromProject() throws SQLException, RemoteException {
        boolean rmiReturnObject = remoteObject.removeRewardFromProject(Integer.parseInt(arguments.get(0)), clientSession.getUserIDLoggedIn(), Integer.parseInt(arguments.get(1)));
        System.out.println("DataServer Response: " + rmiReturnObject);
        output.setContent("Reward successfully removed from project.");
    }

    private void getProject() throws RemoteException, SQLException {
        Project rmiReturnObject = remoteObject.getProjectDetails(Integer.parseInt(arguments.get(0)));
        printDataServerResponse(rmiReturnObject);
        output.setContent(rmiReturnObject);
    }

    // TODO no cliente
    private void getRewardsFromProject() throws RemoteException, SQLException {
        ArrayList<Reward> rmiReturnObject = remoteObject.getRewardsFromProject(Integer.parseInt(arguments.get(0)));
        printDataServerResponse(rmiReturnObject);
        output.setContent(rmiReturnObject);
    }

    //TODO no cliente
    private void getGoalsFromProject() throws SQLException, RemoteException {
        ArrayList<Goal> rmiReturnObject = remoteObject.getGoalsFromProject(Integer.parseInt(arguments.get(0)));
        printDataServerResponse(rmiReturnObject);
        output.setContent(rmiReturnObject);
    }

    private void getPledgesFromUser() throws RemoteException, SQLException {
        ArrayList<Pledge> rmiReturnObject = remoteObject.getPledgesFromUser(clientSession.getUserIDLoggedIn());
        printDataServerResponse(rmiReturnObject);
        output.setContent(rmiReturnObject);
    }

    private void getRewardsFromUser() throws SQLException, RemoteException {
        ArrayList<Reward> rmiReturnObject = remoteObject.getRewardsFromUser(clientSession.getUserIDLoggedIn());
        printDataServerResponse(rmiReturnObject);
        output.setContent(rmiReturnObject);
    }

    private void getProjectMessages() throws RemoteException, SQLException {
        ArrayList<Message> rmiReturnObject = remoteObject.getProjectMessages(Integer.parseInt(arguments.get(0)), clientSession.getUserIDLoggedIn());
        printDataServerResponse(rmiReturnObject);
        output.setContent(rmiReturnObject);
    }

    private void getUserMessages() throws SQLException, RemoteException {
        ArrayList<Message> rmiReturnObject = remoteObject.getUserMessages(clientSession.getUserIDLoggedIn());
        printDataServerResponse(rmiReturnObject);
        output.setContent(rmiReturnObject);
    }

    private void getExpiredProjects() throws SQLException, RemoteException {
        ArrayList<Project> rmiReturnObject = remoteObject.getExpiredProjects();
        printDataServerResponse(rmiReturnObject);
        output.setContent(rmiReturnObject);
    }

    private void getInProgressProjects() throws SQLException, RemoteException {
        ArrayList<Project> rmiReturnObject = remoteObject.getInProgressProjects();
        printDataServerResponse(rmiReturnObject);
        output.setContent(rmiReturnObject);
    }

    private void getProjectOptions() throws SQLException, RemoteException {
        ArrayList<DecisionOption> rmiReturnObject = remoteObject.getProjectOptions(Integer.parseInt(arguments.get(0)));
        printDataServerResponse(rmiReturnObject);
        output.setContent(rmiReturnObject);
    }

    // TODO melhorar o conceito cliente-servidor
    private void sendMessageFromProject() throws SQLException, RemoteException {
        Message message = (Message)attachedObject;
        message.setDateTime(new Timestamp(new Date().getTime()));

        boolean rmiReturnObject = remoteObject.sendMessage(message, true, clientSession.getUserIDLoggedIn());
        printDataServerResponse(rmiReturnObject);
        output.setContent("Message from project successfully sent.");
    }

    private void sendMessageToProject() throws SQLException, RemoteException {
        Message message = (Message)attachedObject;
        message.setDateTime(new Timestamp(new Date().getTime()));
        message.setPledgerUserId(clientSession.getUserIDLoggedIn());

        boolean rmiReturnObject = remoteObject.sendMessage(message, false, clientSession.getUserIDLoggedIn());
        printDataServerResponse(rmiReturnObject);
        output.setContent("Message to project successfully sent.");
    }

    private void sendRewardToUser() throws SQLException, RemoteException {
        boolean rmiReturnObject = remoteObject.sendRewardToUser(clientSession.getUserIDLoggedIn(), Integer.parseInt(arguments.get(0)), arguments.get(1));
        printDataServerResponse(rmiReturnObject);
        output.setContent("Reward successfully sent to user.");
    }

    private void pledge() throws SQLException, RemoteException {
        Pledge pledge = (Pledge)attachedObject;
        pledge.setPledgerUserId(clientSession.getUserIDLoggedIn());

        boolean rmiReturnObject = remoteObject.pledge(pledge);
        printDataServerResponse(rmiReturnObject);
        output.setContent("You have successfully pledged this project.");
    }

    @Deprecated
    private void sendMessage() {
    }


    private void loginFailOver() {
        System.out.println("loginFailOver(" + arguments.get(0) + ")");
        clientSession.setUsernameLoggedIn(arguments.get(0));
        clientSession.setSessionLoggedIn(true);
        output.setErrorHappened(false);
        output.setContent("");
    }

    /**** ************************************ ****/

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
