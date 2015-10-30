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

        while(true)
            handleClientCommand();
    }

    private void handleClientCommand() {
        Command command = readMessageFromClient();
        System.out.println("New request from " + clientSocket.getInetAddress().getHostName());
        ClientCommand clientCommand = new ClientCommand(this, command, command.getAttachedObject(), clientSession);
        clientCommand.run();
        ServerMessage commandResponse = clientCommand.getServerMessage();
        sendMessageToClient(commandResponse);
    }




    public Command readMessageFromClient() {
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

    public void sendMessageToClient(ServerMessage message) {
        try {
            outputStream.writeObject(message);
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
    }

    public Object readObjectFromClient() {
        Object object = null;

        try {
            object = (Object)inputStream.readObject();
        } catch (EOFException e) {
            System.out.println("EOF: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + e.getMessage());
        }

        return object;
    }



}


