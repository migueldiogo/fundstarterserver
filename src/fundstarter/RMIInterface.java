package fundstarter; /**
 * Created by sergiopires on 15/10/15.
 */

import javax.sql.rowset.CachedRowSet;
import java.awt.image.AreaAveragingScaleFilter;
import java.rmi.*;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;


public interface RMIInterface extends Remote {
    public boolean signUp(String username, String password) throws RemoteException, SQLException;
    public boolean login(String username, String password) throws RemoteException, SQLException;

    public double viewBalance(String username) throws SQLException, RemoteException;
    public boolean newProject(Project project) throws SQLException, RemoteException;
    public ArrayList<AttributedReward> viewRewards(String username) throws SQLException, RemoteException;
    public boolean sendReward(String projectName, String reward, String from, String sendTo) throws SQLException, RemoteException;


    public Project viewProjectDetails(String projectName) throws RemoteException, SQLException;

    public ArrayList<Project> listExpired() throws SQLException, RemoteException;
    public ArrayList<Project> listInProgress() throws SQLException, RemoteException;

    public boolean pledge(Pledge pledge) throws SQLException, RemoteException;

    public ArrayList<Message> viewMessages(String username) throws SQLException, RemoteException;

    public boolean sendMessage(Message message) throws SQLException, RemoteException;

    public boolean addAdmin(String adminUsername, String projeto, String username)  throws SQLException, RemoteException;

    boolean addReward(Reward reward) throws SQLException, RemoteException;
    boolean removeReward(Reward reward) throws SQLException, RemoteException;

    boolean addExtraReward(Extra extraReward) throws SQLException, RemoteException;
    boolean removeExtraReward(Extra reward) throws SQLException, RemoteException;

    boolean addQuestion(Question question) throws  SQLException, RemoteException;

    boolean cancelProject(String projectName, String username) throws SQLException, RemoteException;
    boolean sendMessageToProject(Message message) throws SQLException, RemoteException;

    public boolean testRMIConnection() throws RemoteException;

}

