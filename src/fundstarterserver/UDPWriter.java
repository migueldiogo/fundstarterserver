package fundstarterserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Created by Miguel Prata Leal on 16/10/15.
 */
public class UDPWriter extends Thread {

    public UDPWriter() {
        this.start();
    }

    @Override
    public void run() {
        DatagramSocket socket = null;
        int serverPort = 5000;

        try {
            while(true) {
                socket = new DatagramSocket();

                String serverHealthFlag = "" + (ServerStateMonitor.isServerHealthy() ? 1 : 0);
                byte[] messageInBytes = serverHealthFlag.getBytes();
                String backupServerIP = ServerStateMonitor.getBackupServerIp();
                InetAddress backupServerInetAdress = InetAddress.getByName(backupServerIP);

                DatagramPacket serverIsAliveSignal = new DatagramPacket(messageInBytes, messageInBytes.length, backupServerInetAdress, serverPort);
                socket.send(serverIsAliveSignal);
            }

        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (socket != null)
                socket.close();
        }

    }
}
