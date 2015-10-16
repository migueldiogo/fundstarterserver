package fundstarterserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) {

        //TODO Waiting for Sergio: Checks how many servers are already running FundStarter
        if (ServerStateMonitor.getServerCheckInNumber() > 2) {
            System.out.println("Too many servers running FundStarter.");
            System.exit(0);
        }
        //TODO Waiting for Sergio:
        //ServerStateMonitor.setServerCheckInNumber(RMIObject.serverClusterCheckIn(InetAddress.getLocalHost()));
        //TODO Waiting for Sergio:
        // ServerStateMonitor.setBackupServerIp(RMIObject.getBackupServerIP());



        try {
            if (ServerStateMonitor.getServerCheckInNumber() == 1) {
                ServerStateMonitor.setPrimaryServer(true);
                new UDPWriter();
            }
            else {
                ServerStateMonitor.setPrimaryServer(false);

                Object lock = new Object();
                new UDPListener(lock);
                while(!ServerStateMonitor.isPrimaryServer())
                    lock.wait();
            }

            int serverPort = 8100;
            ServerSocket listenSocket = new ServerSocket(serverPort);
            while(true) {
                Socket clientSocket = listenSocket.accept();
                new Connection(clientSocket);

            }
        } catch (IOException e) {
            System.out.println("Listen: " + e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("Interrupted: " + e.getMessage());

        }

    }
}
