package fundstarter; /**
 * Created by sergiopires on 15/10/15.
 */

import java.rmi.Remote;
import java.util.ArrayList;

public interface RMI_interface extends Remote {
    public boolean login(String user, String password);
    public boolean register(String user, String password, String passowrdConfirmation);
    //public boolean createProject(String name, String description, String creator, double fundingGoal, Date expiration);
    //public ArrayList/*<projecto>*/ listAll();
    //public ArrayList/*<projecto>*/ listExpired();
    //public ArrayList /*<projecto>*/ listByDate(Date date);

    public String projectDetails(String projectName);
    public boolean pledge(String projectName, double amount, String userName);


}
