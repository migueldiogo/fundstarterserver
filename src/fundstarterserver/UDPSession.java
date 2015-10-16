package fundstarterserver;

import java.io.IOException;
import java.net.*;

/**
 * Created by Miguel Prata Leal on 16/10/15.
 */
public class UDPSession extends Thread {
    private int serverNumber;
    private InetAddress alternativeServerAddress;


    public UDPSession(int serverNumber, InetAddress alternativeServerAddress) {
        this.serverNumber = serverNumber;
        this.alternativeServerAddress = alternativeServerAddress;
        this.start();
    }

    @Override
    public void run() {
        DatagramSocket socket = null;
        int udpPort = 6000;

        System.out.println("Starting server...");

        try {

            if (serverNumber == 2) {
                socket = new DatagramSocket(udpPort);
                System.out.println("Looking for servers already running FundStarter...");
                byte[] receiveBuffer = new byte[100];
                socket.setSoTimeout(7000);

                while (true) {
                    DatagramPacket packetToReceive = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                    socket.receive(packetToReceive);
                    Thread.sleep(1000);
                }
            } else {
                socket = new DatagramSocket();
                byte[] signal = new String("1").getBytes();

                while (true) {
                    DatagramPacket packetToSend = new DatagramPacket(signal, signal.length, alternativeServerAddress, udpPort);
                    socket.send(packetToSend);
                    Thread.sleep(1000);

                }
            }


        } catch (InterruptedException e) {
                System.out.println("Thread interruption not expected: " + e.getMessage());
        } catch (SocketTimeoutException e) {
            System.out.println("Taking primary server post...");
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
