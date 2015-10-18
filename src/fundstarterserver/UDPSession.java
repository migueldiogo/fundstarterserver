package fundstarterserver;

import java.io.IOException;
import java.net.*;

/**
 * Created by Miguel Prata Leal on 16/10/15.
 */
public class UDPSession extends Thread {
    private InetAddress alternativeServerAddress;
    private DatagramSocket socket;
    private int udpPort;
    private boolean primaryServerMode;
    private byte[] buffer;


    public UDPSession(boolean primaryServerMode, InetAddress alternativeServerAddress) {
        this.primaryServerMode = primaryServerMode;
        this.alternativeServerAddress = alternativeServerAddress;
        udpPort = 6000;
        try {
            socket = primaryServerMode ? new DatagramSocket() : new DatagramSocket(udpPort);
            socket.setSoTimeout(7000);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        buffer = primaryServerMode ? new String("1").getBytes() : new byte[1];

        this.start();
    }

    @Override
    public void run() {

        System.out.println("Starting server...");
        try {
            if (!primaryServerMode) {
                System.out.println("Looking for servers already running FundStarter...");
                receiveSignalFromPrimaryServer();
                System.out.println("Found one server running FundStarter.\nRunning as backup server...");
            }

            while(true) {
                if (primaryServerMode)
                    sendSignalToBackupServer();
                else
                    receiveSignalFromPrimaryServer();
            }

        } catch (InterruptedException e) {
                System.out.println("Thread interruption not expected: " + e.getMessage());
        } catch (SocketTimeoutException e) {
            System.out.println("No signal from any primary server running Fundstarter.");
            System.out.println("Running as primary server...");
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (socket != null)
                socket.close();
        }

    }

    private void sendSignalToBackupServer() throws IOException, InterruptedException {
        DatagramPacket packetToSend = new DatagramPacket(buffer, buffer.length, alternativeServerAddress, udpPort);
        socket.send(packetToSend);
        Thread.sleep(1000);
    }

    private void receiveSignalFromPrimaryServer() throws IOException, InterruptedException {
        DatagramPacket packetToReceive = new DatagramPacket(buffer, buffer.length);
        socket.receive(packetToReceive);
        Thread.sleep(1000);
    }


}
