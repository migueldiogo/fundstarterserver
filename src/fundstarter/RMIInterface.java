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

    boolean addGoalToProject(Goal goal, int projectId, int currentUserId) throws SQLException, RemoteException;

    boolean addRewardToProject(Reward reward, int projectId, int currentUserId) throws SQLException, RemoteException;

    boolean addQuestionToProject(String question, int projectId, int currentUserId) throws  SQLException, RemoteException;

    boolean addOptionToProject(DecisionOption option, int projectId, int currentUserId) throws  SQLException, RemoteException;

    boolean removeGoalFromProject(Goal goal, int projectId, int currentUserId) throws SQLException, RemoteException;

    boolean removeRewardFromProject(int rewardId, int currentUserId, int projectId) throws SQLException, RemoteException;

    boolean removeUser(String username) throws SQLException, RemoteException;

    boolean removeAdminFromProject(int adminId, int projectId) throws SQLException, RemoteException;

    Project getProjectDetails(int projectId) throws RemoteException, SQLException;

    ArrayList<Reward> getRewardsFromProject(int projectId) throws SQLException, RemoteException;

    ArrayList<Goal> getGoalsFromProject(int projectId) throws SQLException, RemoteException;

    ArrayList<Pledge> getPledgesFromUser(int userId) throws RemoteException, SQLException;

    ArrayList<Reward> getRewardsFromUser(int userId) throws SQLException, RemoteException;

    ArrayList<Message> getProjectMessages(int projectId, int userId) throws RemoteException, SQLException;

    ArrayList<Message> getUserMessages(int userId) throws SQLException, RemoteException;

    ArrayList<Project> getExpiredProjects() throws SQLException, RemoteException;

    ArrayList<Project> getInProgressProjects() throws SQLException, RemoteException;

    ArrayList<DecisionOption> getProjectOptions(int projectId) throws SQLException, RemoteException;

    ArrayList<Project> getMyProjects(int adminUserId) throws SQLException, RemoteException;

    ArrayList<User> getAdminsOfProject(int projectId) throws SQLException, RemoteException;

    boolean sendMessage(Message message, boolean isMessageFromProject, int currentUserId) throws SQLException, RemoteException;

    boolean sendRewardToUser(int currentUserId, int pledgeId, String toUsername) throws SQLException, RemoteException;

    boolean pledge(Pledge pledge, boolean withDecisionId) throws SQLException, RemoteException;

    //TODO for SD Project
    boolean connectUserToTumblr(int userId) throws SQLException, RemoteException;

    User loginWithTumblr(String externalServiceUsername) throws SQLException, RemoteException;

        //TODO for SD Project
    User signUpWithTumblr(String localUsername, String externalServiceUsername) throws SQLException, RemoteException;

    boolean testRMIConnection() throws RemoteException;
}

