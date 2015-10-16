package fundstarterserver;

import fundstarter.ServerMessage;

import java.io.*;
import java.net.Socket;
import java.util.InputMismatchException;


public class Connection extends Thread{
    private Socket clientSocket;
    private DataInputStream inputStream;
    private ObjectOutputStream outputStream;
    private String usernameLoggedIn;

    public Connection(Socket clientSocket) {
        usernameLoggedIn = null;
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

        initiateMenuDrivenIOWithClient();

    }

    private void initiateMenuDrivenIOWithClient() {
        ServerMessage messageToClient = new ServerMessage();

        Menu menuMain = new Menu();
        menuMain.addOption("Login");
        menuMain.addOption("Sign Up");
        menuMain.addOption("Quit");
        menuMain.setAnswerPrompt("Please enter your choice: ");

        messageToClient.setContent(menuMain.toString());
        messageToClient.setRepeatAnswerToPrevious(false);

        int optionChosen = messageInteractionWithClient(messageToClient, menuMain);


    }




    private int messageInteractionWithClient(ServerMessage serverMessageAssociated, Menu menuAssociated) {
        String rawOptionInput = "";
        int optionInput = 0;
        Boolean inputValidation;

        do {

            sendMessageToClient(serverMessageAssociated);
            try {
                rawOptionInput = inputStream.readUTF();
            } catch (EOFException e) {
                System.out.println("EOF: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("IO: " + e.getMessage());
            }

            try {
                optionInput = Integer.parseInt(rawOptionInput);
                inputValidation = true;

                OptionList menuAssociatedOptionList = menuAssociated.getOptionsList();
                if (optionInput <= 0 || optionInput > menuAssociatedOptionList.getSize())
                    throw new InputMismatchException();

            } catch (InputMismatchException e) {
                inputValidation = false;
                serverMessageAssociated.setRepeatAnswerToPrevious(true);
                serverMessageAssociated.setErrorMessageBefore("An error has occurred. Please try again.");
            }
        } while (!inputValidation);

        return optionInput;
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


