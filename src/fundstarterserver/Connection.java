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

    public Connection(Socket clientSocket) {
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
            String rawClientCommand = readMessageFromClient();
            String commandResponse = "";
            try {
                // TODO ClientCommand clientCommand = new ClientCommand(rawClientCommand);
                // TODO clientCommand.run();
                // TODO commandResponse = clientCommand.output();
            } catch (NotRecognizedCommandException e) {
                commandResponse = "Not Recognized Command: " + e.getMessage();
            } catch (OnlyForLoggedInUsersException e) {
                commandResponse = "You must login to execute this kind of commands. Use the command 'login' or 'sign up'.";
            } finally {
                sendMessageToClient(commandResponse + "\n>>>");
            }
        }

    }


    private void sendMessageToClient(String message) {
        try {
            outputStream.writeUTF(message);
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
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


    /**
     * TODO Move to ClientCommand class.
     */
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





}


