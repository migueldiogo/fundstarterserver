package fundstarterserver;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Miguel Prata Leal on 31/10/15.
 */
public class OnlineClientsReport {
    private ArrayList<ClientReport> clientsOnline;
    private File reportFile;

    public OnlineClientsReport() {
        clientsOnline = new ArrayList<>();
        reportFile = new File("onlineClientsReport.txt");
    }

    public void addClient(String clientIP, String usernameLoggedIn, Socket clientSocket) {
        clientsOnline.remove(new ClientReport(clientIP, usernameLoggedIn, clientSocket));
        clientsOnline.add(new ClientReport(clientIP,usernameLoggedIn, clientSocket));
        updateFile();
    }


    public boolean removeClient(String clientIP, String usernameLoggedIn, Socket clientSocket) {
        boolean returnValue = clientsOnline.remove(new ClientReport(clientIP, usernameLoggedIn, clientSocket));
        updateFile();
        return returnValue;
    }

    private void updateFile() {
        try {
            BufferedWriter buffFileWriter = new BufferedWriter(new FileWriter(reportFile, false));

            StringBuilder clientLine = new StringBuilder();

            for (ClientReport client : clientsOnline) {
                clientLine.append(client.getClientIP() + ": " + client.getUsernameLoggedIn() + "\n");
            }

            buffFileWriter.write(clientLine.toString());
            buffFileWriter.flush();
            buffFileWriter.close();

        }
        catch (IOException e) {
            System.out.println("IO Error while writing to online clients report: " + e.getMessage());
        }
    }
}
