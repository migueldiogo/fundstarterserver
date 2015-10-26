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
    public void closeConnection() throws SQLException, RemoteException;

    public double viewBalance(String username) throws SQLException, RemoteException;
    public boolean newProject(String creator, String name, String description, String date, double goal) throws SQLException, RemoteException;
    public boolean addGift(String projectName, float pledgeMin, String giftName) throws RemoteException, SQLException;
    public ArrayList<AttributedReward> viewRewards(String username) throws SQLException, RemoteException;
    public boolean sendReward(String receiver, String username) throws SQLException, RemoteException;


    public Project viewProjectDetails(String projectName) throws RemoteException, SQLException;

    public ArrayList<Project> listExpired() throws SQLException, RemoteException;
    public ArrayList<Project> listInProgress() throws SQLException, RemoteException;

    public boolean pledge(Pledge pledge) throws SQLException, RemoteException;

    public ArrayList<Message> viewMessages(String username) throws SQLException, RemoteException;

    public boolean sendMessage(Message message) throws SQLException, RemoteException;



}

