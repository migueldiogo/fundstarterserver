package fundstarterserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * Created by Miguel Prata Leal on 16/10/15.
 */
public class UDPListener extends Thread{
    private Object conditionVariableLock;

    public UDPListener(Object lock) {
        conditionVariableLock = lock;
        this.start();
    }

    @Override
    public void run() {
        int udpPort = 5000;
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(udpPort);
            socket.setSoTimeout(7000);
            byte[] receiverBuffer = new byte[5];

            while(true) {
                DatagramPacket packetReceived = new DatagramPacket(receiverBuffer, receiverBuffer.length);
                socket.receive(packetReceived);
            }


        } catch (SocketTimeoutException e) {
            System.out.println("Primary Server Timeout: " + e.getMessage());
            startBackup();
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (socket != null)
                socket.close();
        }

    }


    private void startBackup() {
        notifyDataServer();
        takePrimaryServerPost();
    }

    // TODO Waiting for Sergio:
    private void notifyDataServer() {
        // RMIObject.serverDown(String serverDownIP);
    }

    private void takePrimaryServerPost() {
        conditionVariableLock.notify();
    }


}
