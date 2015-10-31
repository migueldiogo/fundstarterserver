package fundstarterserver;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {

        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("resources/config.properties"));
            ServerConfigProperties.THIS_SERVER_TCP_PORT = Integer.parseInt(properties.getProperty("THIS_SERVER_TCP_PORT"));
            ServerConfigProperties.THIS_SERVER_UDP_PORT = Integer.parseInt(properties.getProperty("THIS_SERVER_UDP_PORT"));
            ServerConfigProperties.ALTERNATIVE_SERVER_IP = properties.getProperty("ALTERNATIVE_SERVER_IP");
            ServerConfigProperties.RMI_SERVER_IP = properties.getProperty("RMI_SERVER_IP");
            ServerConfigProperties.RMI_SERVER_PORT = Integer.parseInt(properties.getProperty("RMI_SERVER_PORT"));
            ServerConfigProperties.ALTERNATIVE_SERVER_PING_TIMEOUT = Integer.parseInt(properties.getProperty("ALTERNATIVE_SERVER_PING_TIMEOUT"));



        } catch (IOException e) {
            if (args.length != 3) {
                System.out.println("No properties file found. Please rerun the program with the following arguments: \n" +
                        "\t-This server tcp port;\n" + "\t-This server udp port;\n" + "\t-Alternative Server IP;\n" +
                        "\t-RMIServerIP;\n" + "\t-RMI server port;\n" + "\t-Alternative Server Ping Timeout.");
                System.exit(1);
            } else {
                ServerConfigProperties.THIS_SERVER_TCP_PORT = Integer.parseInt(args[0]);
                ServerConfigProperties.THIS_SERVER_UDP_PORT = Integer.parseInt(args[1]);
                ServerConfigProperties.ALTERNATIVE_SERVER_IP = args[2];
                ServerConfigProperties.RMI_SERVER_IP = args[3];
                ServerConfigProperties.RMI_SERVER_PORT = Integer.parseInt(args[4]);
                ServerConfigProperties.ALTERNATIVE_SERVER_PING_TIMEOUT = Integer.parseInt(args[5]);

            }

        }

        printRunningConfiguration();

        try {
            InetAddress alternativeServerAddress = InetAddress.getByName(ServerConfigProperties.ALTERNATIVE_SERVER_IP);

            // behave like a secondary server first of all
            Thread thread = new UDPSession(false, alternativeServerAddress);
            thread.join();

            // behave like a primary server
            new UDPSession(true, alternativeServerAddress);

            ServerSocket listenSocket = new ServerSocket(ServerConfigProperties.THIS_SERVER_TCP_PORT);
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

    private static void printRunningConfiguration() {
        System.out.println();
        System.out.print("This Server TCP IP: ");
        try {
            System.out.println(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            System.out.println("Unknown Local Adress.");
        }
        System.out.println("This Server TCP Port: " + ServerConfigProperties.THIS_SERVER_TCP_PORT);
        System.out.println("This Server UDP Port: " + ServerConfigProperties.THIS_SERVER_UDP_PORT);
        System.out.println("Alternative Server IP: " + ServerConfigProperties.ALTERNATIVE_SERVER_IP);
        System.out.println("RMI Server IP: " + ServerConfigProperties.RMI_SERVER_IP);
        System.out.println("RMI Server Port: " + ServerConfigProperties.RMI_SERVER_PORT);
        System.out.println("Alternative Server Ping Timeout: " + ServerConfigProperties.ALTERNATIVE_SERVER_PING_TIMEOUT);
        System.out.println();

    }
}
