package fundstarterserver;

import fundstarter.Command;
import fundstarter.ServerMessage;

import java.io.*;
import java.net.Socket;


public class Connection extends Thread{
    private Socket clientSocket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private ClientSession clientSession;

    public Connection(Socket clientSocket) {
        clientSession = new ClientSession();
        try {
            this.clientSocket = clientSocket;
            outputStream = new ObjectOutputStream((clientSocket.getOutputStream()));
            inputStream = new ObjectInputStream(clientSocket.getInputStream());
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
        Command command = readMessageFromClient();
        ClientCommand clientCommand = new ClientCommand(command);
        clientCommand.run(clientSession);
        ServerMessage commandResponse = clientCommand.getServerMessage();
        //ONLY FOR TESTS: commandResponse.setRepeatAnswerToPrevious(false);
        sendMessageToClient(commandResponse);
    }




    private Command readMessageFromClient() {
        Command command = null;

        try {
            command = (Command)inputStream.readObject();
        } catch (EOFException e) {
            System.out.println("EOF: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + e.getMessage());
        }

        return command;
    }

    private void sendMessageToClient(ServerMessage message) {
        try {
            outputStream.writeObject(message);
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
    }



}


