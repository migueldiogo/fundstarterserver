package fundstarterserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) {

        try {
            int serverPort = 8100;
            ServerSocket listenSocket = new ServerSocket(serverPort);
            while(true) {
                Socket clientSocket = listenSocket.accept();
                new Connection(clientSocket);

            }
        } catch (IOException e) {
            System.out.println("Listen: " + e.getMessage());
        }

    }
}
