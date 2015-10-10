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
            else {
                sendMessageToClient("Welcome to FundStarter, " + clientUsername + "!\n>>>");
                // TODO Login login = new Login(Username);
            }
        }

        if (!accessAllowed) {
            System.exit(1);
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
}


