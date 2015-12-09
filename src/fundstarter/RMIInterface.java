package fundstarter;

/**
 * Created by sergiopires on 15/10/15.
 */

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;


public interface RMIInterface extends Remote {
    int login(String username, String password) throws RemoteException, SQLException;

    boolean signUp(String username, String password) throws RemoteException, SQLException;

    double getBalance(int userId) throws SQLException, RemoteException;

    int newProject(Project project, int creatorUserId) throws SQLException, RemoteException;

    boolean cancelProject(int projectId, int adminUserId) throws SQLException, RemoteException;

    boolean addAdminToProject(String usernameToPromote, int projectId, int currentUserId)  throws SQLException, RemoteException;

    boolean addGoalToProject(Goal goal, int projectId) throws SQLException, RemoteException;

    boolean addRewardToProject(Reward reward, int projectId) throws SQLException, RemoteException;

    boolean addQuestionToProject(String question, int projectId) throws  SQLException, RemoteException;

    boolean addOptionToProject(DecisionOption option, int projectId) throws  SQLException, RemoteException;

    boolean removeGoalFromProject(Goal goal, int projectId) throws SQLException, RemoteException;

    boolean removeRewardFromProject(int rewardId, int projectId) throws SQLException, RemoteException;

    Project getProjectDetails(int projectId) throws RemoteException, SQLException;

    ArrayList<Reward> getRewardsFromProject(int projectId) throws SQLException, RemoteException;

    ArrayList<Goal> getGoalsFromProject(int projectId) throws SQLException, RemoteException;

    ArrayList<Pledge> getPledgesFromUser(int userId) throws RemoteException, SQLException;

    ArrayList<Reward> getRewardsFromUser(int userId) throws SQLException, RemoteException;

    ArrayList<Message> getProjectMessages(int projectId, int userId) throws RemoteException, SQLException;

    ArrayList<Message> getUserMessages(int userId) throws SQLException, RemoteException;

    ArrayList<Project> getExpiredProjects() throws SQLException, RemoteException;

    ArrayList<Project> getInProgressProjects() throws SQLException, RemoteException;

    boolean sendMessageFromProject(Message message) throws SQLException, RemoteException;

    boolean sendMessageToProject(Message message) throws SQLException, RemoteException;

    boolean sendRewardToUser(int pledgeId, int rewardId, int toUserId) throws SQLException, RemoteException;

    boolean pledge(Pledge pledge) throws SQLException, RemoteException;

    //TODO for SD Project
    boolean connectUserToTumblr(int userId) throws SQLException, RemoteException;

    //TODO for SD Project
    int loginWithTumblr(String username) throws SQLException, RemoteException;

    // TODO Apagar -  Não vai ser preciso
    boolean sendMessage(Message message) throws SQLException, RemoteException;

    boolean testRMIConnection() throws RemoteException;
}

