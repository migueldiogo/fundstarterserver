package fundstarterserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;


public class Connection extends Thread{
    private Socket clientSocket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private String usernameLoggedIn;

    public Connection(Socket clientSocket) {
        usernameLoggedIn = null;
        try {
            this.clientSocket = clientSocket;
            inputStream = new DataInputStream(clientSocket.getInputStream());
            outputStream = new DataOutputStream((clientSocket.getOutputStream()));
            this.start();
        } catch (IOException e){
            System.out.println("Connection: " + e.getMessage());
        }

    }

    @Override
    public void run() {

        while(true) {
            handleClientCommand();
        }

    }

    private void handleClientCommand() {
        String rawClientCommand = readMessageFromClient();
        String commandResponse = "";
        try {
            // TODO ClientCommand clientCommand = new ClientCommand(rawClientCommand, usernameLoggedIn);
            // TODO clientCommand.run();
            // TODO commandResponse = clientCommand.output();
        } catch (SignOutException e) {
            usernameLoggedIn = null;
            sendMessageToClient(commandResponse + "\n>>>");
        } catch (ClientLeavingException e) {
            sendMessageToClient(commandResponse);
            System.exit(0);
        }
    }

    private String readMessageFromClient() {
        String message = "";

        try {
            message = inputStream.readUTF();
        } catch (EOFException e) {
            System.out.println("EOF: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }

        return message;
    }

    private void sendMessageToClient(String message) {
        try {
            outputStream.writeUTF(message);
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
    }






    /**
     * TODO Move to ClientCommand class.

    private void loginRoutine() {
        int maxAttempts = 3;
        boolean accessAllowed = false;

        for (int i = 0; i < maxAttempts && !accessAllowed; i++) {
            if (i != 0)
                sendMessageToClient("Username: ");
            String clientUsername = readMessageFromClient();
            sendMessageToClient("Password: ");
            String clientPassword = readMessageFromClient();

            // TODO accessAllowed = authenticateClient(clientUsername, clientPassword);

            if (accessAllowed)
                sendMessageToClient("Access Denied! Please try again.\nUsername:");
            else if (accessAllowed && i < 3){
                sendMessageToClient("Welcome to FundStarter, " + clientUsername + "!\n>>>");
                // TODO Login login = new Login(Username);
            }
            else
                System.exit(1);

        }
    }
     */


}


