package fundstarterserver;

import com.sun.tools.doclets.formats.html.SourceToHTMLConverter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RMISecurityManager;

public class Main {

    public static void main(String[] args) {


        try {
            InetAddress alternativeServerAddress = InetAddress.getByName(args[0]);

            // behave like a secondary server first of all
            Thread thread = new UDPSession(false, alternativeServerAddress);
            thread.join();

            // behave like a primary server
            new UDPSession(true, alternativeServerAddress);

            int serverPort = 8200;
            ServerSocket listenSocket = new ServerSocket(serverPort);
            while(true) {
                Socket clientSocket = listenSocket.accept();
                System.out.println("Novo Cliente: " + clientSocket.getInetAddress().getHostName());
                new Connection(clientSocket);

            }
        } catch (IOException e) {
            System.out.println("Listen: " + e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("Interrupted: " + e.getMessage());

        }

    }
}
