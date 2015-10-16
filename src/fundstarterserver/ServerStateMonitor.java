package fundstarterserver;



public class ServerStateMonitor extends Thread{
    private static int serverCheckInNumber;
    private static boolean serverHealthy = true;
    private static boolean primaryServer;

    private static String backupServerIp;

    public static boolean isServerHealthy() {
        return serverHealthy;
    }

    public static void setServerHealthy(boolean serverHealthy) {
        ServerStateMonitor.serverHealthy = serverHealthy;
    }

    public static int getServerCheckInNumber() {
        return serverCheckInNumber;
    }

    public static void setServerCheckInNumber(int serverCheckInNumber) {
        ServerStateMonitor.serverCheckInNumber = serverCheckInNumber;
    }

    public static String getBackupServerIp() {
        return backupServerIp;
    }

    public static void setBackupServerIp(String backupServerIp) {
        ServerStateMonitor.backupServerIp = backupServerIp;
    }

    public synchronized static boolean isPrimaryServer() {
        return primaryServer;
    }

    public synchronized static void setPrimaryServer(boolean primaryServer) {
        ServerStateMonitor.primaryServer = primaryServer;
    }
}
