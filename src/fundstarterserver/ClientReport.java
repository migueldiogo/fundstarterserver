package fundstarterserver;

import java.net.Socket;

/**
 * Created by Miguel Prata Leal on 31/10/15.
 */
public class ClientReport {
    private String clientIP;
    private String usernameLoggedIn;
    private Socket clientSocket;

    public ClientReport(String clientIP, String usernameLoggedIn, Socket clientSocket) {
        this.clientIP = clientIP;
        this.usernameLoggedIn = usernameLoggedIn;
        this.clientSocket = clientSocket;
    }

    public String getClientIP() {
        return clientIP;
    }

    public void setClientIP(String clientIP) {
        this.clientIP = clientIP;
    }

    public String getUsernameLoggedIn() {
        return usernameLoggedIn;
    }

    public void setUsernameLoggedIn(String usernameLoggedIn) {
        this.usernameLoggedIn = usernameLoggedIn;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClientReport that = (ClientReport) o;

        return !(clientSocket != null ? !clientSocket.equals(that.clientSocket) : that.clientSocket != null);

    }

    @Override
    public int hashCode() {
        return clientSocket != null ? clientSocket.hashCode() : 0;
    }
}
