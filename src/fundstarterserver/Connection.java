package fundstarterserver;

import fundstarter.ServerMessage;

import java.io.*;
import java.net.Socket;


public class Connection extends Thread{
    private Socket clientSocket;
    private DataInputStream inputStream;
    private ObjectOutputStream outputStream;
    private ClientSession clientSession;

    public Connection(Socket clientSocket) {
        clientSession = new ClientSession();
        try {
            this.clientSocket = clientSocket;
            outputStream = new ObjectOutputStream((clientSocket.getOutputStream()));
            inputStream = new DataInputStream(clientSocket.getInputStream());
            this.start();
        } catch (IOException e){
            System.out.println("Connection: " + e.getMessage());
        }

    }

    @Override
    public void run() {

        handleClientCommand();
    }

    private void handleClientCommand() {
        String rawClientCommand = readMessageFromClient();
        ClientCommand clientCommand = new ClientCommand(rawClientCommand);
        clientCommand.run(clientSession);
        ServerMessage commandResponse = clientCommand.getServerMessage();
        //ONLY FOR TESTS: commandResponse.setRepeatAnswerToPrevious(false);
        sendMessageToClient(commandResponse);
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

    private void sendMessageToClient(ServerMessage message) {
        try {
            outputStream.writeObject(message);
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
    }



}


