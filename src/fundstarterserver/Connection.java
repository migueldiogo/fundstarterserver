package fundstarterserver;

import fundstarter.Command;
import fundstarter.ServerMessage;

import java.awt.geom.CubicCurve2D;
import java.io.*;
import java.net.Socket;


public class Connection extends Thread{
    private Socket clientSocket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private ClientSession clientSession;
    private OnlineClientsReport allOnlineClientsReport;

    public Connection(Socket clientSocket, OnlineClientsReport allOnlineClientsReport) {
        clientSession = new ClientSession();
        this.allOnlineClientsReport = allOnlineClientsReport;
        try {
            this.clientSocket = clientSocket;
            outputStream = new ObjectOutputStream((clientSocket.getOutputStream()));
            inputStream = new ObjectInputStream(clientSocket.getInputStream());
            this.start();
        } catch (IOException e){
            System.out.println("Connection: " + e.getMessage());
            handleIOException();
        }

    }

    @Override
    public void run() {
        String clientAddress = clientSocket.getInetAddress().getHostAddress();
        String clientUsername = clientSession.getUsernameLoggedIn();
        allOnlineClientsReport.addClient(clientAddress, (clientUsername.equals("")) ? "Not logged in" : clientUsername, clientSocket);
        try {
            while(true) {
                    handleClientCommand();
            }
        } catch (IOException e) {
            handleIOException();
        }
    }

    private void handleIOException() {
        String clientAddress = clientSocket.getInetAddress().getHostAddress();
        String clientUsername = (clientSession.getUsernameLoggedIn().equals("")) ? "Not logged in" : clientSession.getUsernameLoggedIn();
        System.out.println("Client from " + clientAddress + "(" + clientUsername + ")" + " has disconnected from this server.");
        allOnlineClientsReport.removeClient(clientAddress, clientUsername, clientSocket);
    }

    private void handleClientCommand() throws IOException{
        Command command = readMessageFromClient();
        String clientUsername = (clientSession.getUsernameLoggedIn().equals("")) ? "Not logged in" : clientSession.getUsernameLoggedIn();

        System.out.println("New request from " + clientSocket.getInetAddress().getHostAddress() + "(" + clientUsername + ")" + ": " + command.toString());
        ClientCommand clientCommand = new ClientCommand(this, command, command.getAttachedObject(), clientSession);
        clientCommand.run();
        ServerMessage commandResponse = clientCommand.getServerMessage();
        sendMessageToClient(commandResponse);

        System.out.println("Response to " + clientSocket.getInetAddress().getHostAddress() + "(" + clientUsername + ")" + ": " + commandResponse.toString());

        allOnlineClientsReport.addClient(clientSocket.getInetAddress().getHostAddress(), clientSession.getUsernameLoggedIn(), clientSocket);
    }




    public Command readMessageFromClient() throws IOException{
        Command command = null;

        try {
            command = (Command)inputStream.readObject();
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + e.getMessage());
        }

        return command;
    }

    public void sendMessageToClient(ServerMessage message) throws IOException{
        outputStream.writeObject(message);
    }

    public Object readObjectFromClient() throws IOException{
        Object object = null;

        try {
            object = (Object)inputStream.readObject();
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + e.getMessage());
        }

        return object;
    }

    public OnlineClientsReport getAllOnlineClientsReport() {
        return allOnlineClientsReport;
    }

    public void setAllOnlineClientsReport(OnlineClientsReport allOnlineClientsReport) {
        this.allOnlineClientsReport = allOnlineClientsReport;
    }
}


