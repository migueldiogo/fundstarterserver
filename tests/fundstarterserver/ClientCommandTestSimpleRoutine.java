package fundstarterserver;

import com.sun.deploy.util.SessionState;
import fundstarter.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RMISecurityManager;
import java.sql.Array;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;


/**
 * Created by Miguel Prata Leal on 18/10/15.
 */
public class ClientCommandTestSimpleRoutine {

    @Before
    public void setUp() throws Exception {
        Properties properties = new Properties();
        properties.load(new FileInputStream("resources/config.properties"));
        ServerConfigProperties.THIS_SERVER_TCP_PORT = Integer.parseInt(properties.getProperty("THIS_SERVER_TCP_PORT"));
        ServerConfigProperties.THIS_SERVER_UDP_PORT = Integer.parseInt(properties.getProperty("THIS_SERVER_UDP_PORT"));
        ServerConfigProperties.ALTERNATIVE_SERVER_IP = properties.getProperty("ALTERNATIVE_SERVER_IP");
        ServerConfigProperties.RMI_SERVER_IP = properties.getProperty("RMI_SERVER_IP");
        ServerConfigProperties.RMI_SERVER_PORT = Integer.parseInt(properties.getProperty("RMI_SERVER_PORT"));
        ServerConfigProperties.ALTERNATIVE_SERVER_PING_TIMEOUT = Integer.parseInt(properties.getProperty("ALTERNATIVE_SERVER_PING_TIMEOUT"));


        ArrayList<String> arguments = new ArrayList<>();
        arguments.add("testUsername");
        arguments.add("testPassword");

        ClientCommand clientCommand = new ClientCommand(new Command("signup", arguments), null);
        clientCommand.run();

        arguments = new ArrayList<>();
        arguments.add("testUsername2");
        arguments.add("testPassword2");

        clientCommand = new ClientCommand(new Command("signup", arguments), null);
        clientCommand.run();


        arguments = new ArrayList<>();
        arguments.add("testUsername");
        arguments.add("testPassword");

        clientCommand = new ClientCommand(new Command("login", arguments), null);
        clientCommand.run();


    }


    @Test
    public void testTwoUsersRoutine() throws Exception {
        //newProject
        Project project = new Project("testProject", "testDescription", new Date(new java.util.Date().getTime()), 600);
        ClientCommand clientCommand = new ClientCommand(new Command("newProject", new ArrayList<>()), project);
        clientCommand.run();
        assertTrue(!clientCommand.getServerMessage().isErrorHappened());
        Pattern p = Pattern.compile("(\\d+)");
        Matcher m = p.matcher((String)clientCommand.getServerMessage().getContent());
        if (m.find())
            project.setProjectId(Integer.parseInt(m.group(1)));

        //addQuestion
        ArrayList<String> arguments = new ArrayList<>();
        arguments.add("Cor para o logo?");
        arguments.add("" + project.getProjectId());
        clientCommand = new ClientCommand(new Command("addQuestionToProject", arguments), null);
        clientCommand.run();
        assertTrue(!clientCommand.getServerMessage().isErrorHappened());

        //add user2 as admin
        arguments = new ArrayList<>();
        arguments.add("testUsername2");
        arguments.add("" + project.getProjectId());
        clientCommand = new ClientCommand(new Command("addAdminToProject", arguments), null);
        clientCommand.run();
        assertTrue(!clientCommand.getServerMessage().isErrorHappened());

        //logout user1
        clientCommand = new ClientCommand(new Command("logout", new ArrayList<>()), null);
        clientCommand.run();
        assertTrue(ClientSession.getInstance().getUserIDLoggedIn() == 0 && ClientSession.getInstance().getUsernameLoggedIn() == "" &&
                !ClientSession.getInstance().getSessionLoggedIn());


        // login user2
        arguments = new ArrayList<>();
        arguments.add("testUsername2");
        arguments.add("testPassword2");
        clientCommand = new ClientCommand(new Command("login", arguments), null);
        clientCommand.run();


        //getProject
        arguments = new ArrayList<>();
        arguments.add("" + project.getProjectId());
        clientCommand = new ClientCommand(new Command("getProject", arguments), null);
        clientCommand.run();
        Project projectReceived = (Project)clientCommand.getServerMessage().getContent();
        assertTrue(project.getProjectId() == projectReceived.getProjectId());
        assertTrue(project.getName().equals(projectReceived.getName()));
        assertTrue("Cor para o logo?".equals(projectReceived.getQuestion()));
        assertTrue(project.getDescription().equals(projectReceived.getDescription()));
        //assertTrue(project.getExpirationDate().equals(projectReceived.getExpirationDate()));
        assertTrue(project.getFirstGoalValue() == projectReceived.getFirstGoalValue());
        assertTrue(project.getTotalAmountEarned() == projectReceived.getTotalAmountEarned());


        //addOption
        arguments = new ArrayList<>();
        arguments.add("" + project.getProjectId());
        clientCommand = new ClientCommand(new Command("addOptionToProject", arguments), new DecisionOption(project.getProjectId(), "Azul"));
        clientCommand.run();
        assertTrue(!clientCommand.getServerMessage().isErrorHappened());

        //addOption
        arguments = new ArrayList<>();
        arguments.add("" + project.getProjectId());
        clientCommand = new ClientCommand(new Command("addOptionToProject", arguments), new DecisionOption(project.getProjectId(), "Vermelho"));
        clientCommand.run();
        assertTrue(!clientCommand.getServerMessage().isErrorHappened());

        // TODO GET OPTIONS




        //addReward
        arguments = new ArrayList<>();
        arguments.add("" + project.getProjectId());
        Reward reward = new Reward();
        reward.setProjectId(project.getProjectId());
        reward.setDescription("Gelado");
        reward.setMinAmount(50);
        clientCommand = new ClientCommand(new Command("addRewardToProject", arguments), reward);
        clientCommand.run();
        assertTrue(!clientCommand.getServerMessage().isErrorHappened());

        //getReward
        arguments = new ArrayList<>();
        arguments.add("" + project.getProjectId());
        clientCommand = new ClientCommand(new Command("getRewardsFromProject", arguments), null);
        clientCommand.run();
        ArrayList<Reward> rewards = (ArrayList<Reward>) clientCommand.getServerMessage().getContent();
        reward = rewards.get(0);
        assertTrue(reward.getDescription().equals("Gelado") && reward.getProjectId() == project.getProjectId() &&
                    reward.getMinAmount() == 50);


        //pledge
        Pledge pledge = new Pledge(0, project.getProjectId(), 50, "azul");
        clientCommand = new ClientCommand(new Command("pledge", new ArrayList<>()), pledge);
        clientCommand.run();
        assertTrue(!clientCommand.getServerMessage().isErrorHappened());


        //getOption
        arguments = new ArrayList<>();
        arguments.add("" + project.getProjectId());
        clientCommand = new ClientCommand(new Command("getProjectOptions", arguments), null);
        clientCommand.run();
        ArrayList<DecisionOption> options = (ArrayList<DecisionOption>) clientCommand.getServerMessage().getContent();
        DecisionOption option = options.get(0);
        assertTrue(option.getDescription().equals("Azul") && option.getProjectId() == project.getProjectId() && option.getVotes() == 50);

        //getPledge
        arguments = new ArrayList<>();
        clientCommand = new ClientCommand(new Command("getPledgesFromUser", arguments), null);
        clientCommand.run();
        ArrayList<Pledge> pledges = (ArrayList<Pledge>) clientCommand.getServerMessage().getContent();
        pledge = pledges.get(0);
        assertTrue(pledge.getAmount() == 50 && pledge.getProjectId() == project.getProjectId() && pledge.getPledgerUserId() == ClientSession.getInstance().getUserIDLoggedIn() &&
                pledge.getDecisionDescription().equals("Azul") && pledge.getProjectName().equals(project.getName()) &&
                pledge.getDecision() == option.getOptionId());

        //getRewardsFromUser
        arguments = new ArrayList<>();
        clientCommand = new ClientCommand(new Command("getRewardsFromUser", arguments), null);
        clientCommand.run();
        rewards = (ArrayList<Reward>) clientCommand.getServerMessage().getContent();
        reward = rewards.get(0);
        assertTrue(reward.getDescription().equals("Gelado") && reward.getProjectId() == project.getProjectId() &&
                reward.getMinAmount() == 50 && !reward.isDone() && reward.getUserId() == ClientSession.getInstance().getUserIDLoggedIn());

        //sendRewardToUser
        arguments = new ArrayList<>();
        arguments.add("" + pledges.get(0).getPledgeId());
        arguments.add("testUsername");
        clientCommand = new ClientCommand(new Command("sendRewardToUser", arguments), null);
        clientCommand.run();
        assertTrue(!clientCommand.getServerMessage().isErrorHappened());


        //checkBalance
        clientCommand = new ClientCommand(new Command("getBalance", new ArrayList<>()), null);
        clientCommand.run();
        p = Pattern.compile("(\\d+)");
        m = p.matcher((String)clientCommand.getServerMessage().getContent());
        if (m.find())
            assertTrue(50 == Integer.parseInt(m.group(1)));
        assertTrue(!clientCommand.getServerMessage().isErrorHappened());

        //add extra Goal
        arguments = new ArrayList<>();
        arguments.add("" + project.getProjectId());
        clientCommand = new ClientCommand(new Command("addGoalToProject", arguments), new Goal(1000, "Vamos fazer uma festa!"));
        clientCommand.run();
        assertTrue(!clientCommand.getServerMessage().isErrorHappened());



        //getGoalsFromProject
        arguments = new ArrayList<>();
        arguments.add("" + project.getProjectId());
        clientCommand = new ClientCommand(new Command("getGoalsFromProject", arguments), null);
        clientCommand.run();
        ArrayList<Goal> goals = (ArrayList<Goal>) clientCommand.getServerMessage().getContent();
        assertTrue(goals.get(0).getAmount() == 600 && goals.get(0).getExtraDescription().equals(""));
        assertTrue(goals.get(1).getAmount() == 1000 && goals.get(1).getExtraDescription().equals("Vamos fazer uma festa!"));

        //add extra Goal
        arguments = new ArrayList<>();
        arguments.add("" + project.getProjectId());
        clientCommand = new ClientCommand(new Command("addGoalToProject", arguments), new Goal(2000, "Vamos fazer duas festas!"));
        clientCommand.run();
        assertTrue(!clientCommand.getServerMessage().isErrorHappened());

        //removeGoalFromProject
        arguments = new ArrayList<>();
        arguments.add("" + project.getProjectId());
        clientCommand = new ClientCommand(new Command("removeGoalFromProject", arguments), new Goal(2000, ""));
        clientCommand.run();
        assertTrue(!clientCommand.getServerMessage().isErrorHappened());

        //getGoalsFromProject
        arguments = new ArrayList<>();
        arguments.add("" + project.getProjectId());
        clientCommand = new ClientCommand(new Command("getGoalsFromProject", arguments), null);
        clientCommand.run();
        goals = (ArrayList<Goal>) clientCommand.getServerMessage().getContent();
        assertTrue(goals.get(0).getAmount() == 600 && goals.get(0).getExtraDescription().equals(""));
        assertTrue(goals.get(1).getAmount() == 1000 && goals.get(1).getExtraDescription().equals("Vamos fazer uma festa!"));




        //removeRewardFrom
        // TODO




        //logout user2
        clientCommand = new ClientCommand(new Command("logout", new ArrayList<>()), null);
        clientCommand.run();
        assertTrue(ClientSession.getInstance().getUserIDLoggedIn() == 0 && ClientSession.getInstance().getUsernameLoggedIn() == "" &&
                !ClientSession.getInstance().getSessionLoggedIn());


        // login do user1 para verificar o novo reward
        arguments = new ArrayList<>();
        arguments.add("testUsername");
        arguments.add("testPassword");

        clientCommand = new ClientCommand(new Command("login", arguments), null);
        clientCommand.run();


        //getRewardsFromUser verificacao
        arguments = new ArrayList<>();
        clientCommand = new ClientCommand(new Command("getRewardsFromUser", arguments), null);
        clientCommand.run();
        rewards = (ArrayList<Reward>) clientCommand.getServerMessage().getContent();
        reward = rewards.get(0);
        assertTrue(reward.getDescription().equals("Gelado") && reward.getProjectId() == project.getProjectId() &&
                reward.getMinAmount() == 50 && !reward.isDone() && reward.getUserId() == ClientSession.getInstance().getUserIDLoggedIn());


        //removeRewardFromProject
        arguments = new ArrayList<>();
        arguments.add("" + reward.getRewardId());
        arguments.add("" + project.getProjectId());
        clientCommand = new ClientCommand(new Command("removeRewardFromProject", arguments), null);
        clientCommand.run();
        assertTrue(!clientCommand.getServerMessage().isErrorHappened());


        //getReward
        arguments = new ArrayList<>();
        arguments.add("" + project.getProjectId());
        clientCommand = new ClientCommand(new Command("getRewardsFromProject", arguments), null);
        clientCommand.run();
        assertTrue(clientCommand.getServerMessage().isErrorHappened());



        //logout user1
        clientCommand = new ClientCommand(new Command("logout", new ArrayList<>()), null);
        clientCommand.run();
        assertTrue(ClientSession.getInstance().getUserIDLoggedIn() == 0 && ClientSession.getInstance().getUsernameLoggedIn() == "" &&
                !ClientSession.getInstance().getSessionLoggedIn());

        // login do user2 para continuar
        arguments = new ArrayList<>();
        arguments.add("testUsername2");
        arguments.add("testPassword2");

        clientCommand = new ClientCommand(new Command("login", arguments), null);
        clientCommand.run();

        //checkBalance
        clientCommand = new ClientCommand(new Command("getBalance", new ArrayList<>()), null);
        clientCommand.run();
        p = Pattern.compile("(\\d+)");
        m = p.matcher((String)clientCommand.getServerMessage().getContent());
        if (m.find())
            assertTrue(100 == Integer.parseInt(m.group(1)));
        assertTrue(!clientCommand.getServerMessage().isErrorHappened());


         /*       //addReward to remove
        arguments = new ArrayList<>();
        arguments.add("" + project.getProjectId());
        reward = new Reward();
        reward.setProjectId(project.getProjectId());
        reward.setDescription("2 Gelados");
        reward.setMinAmount(100);
        clientCommand = new ClientCommand(new Command("addRewardToProject", arguments), reward);
        clientCommand.run();
        assertTrue(!clientCommand.getServerMessage().isErrorHappened());
*/
/*        //getReward
        arguments = new ArrayList<>();
        arguments.add("" + project.getProjectId());
        clientCommand = new ClientCommand(new Command("getRewardsFromProject", arguments), null);
        clientCommand.run();
        rewards = (ArrayList<Reward>) clientCommand.getServerMessage().getContent();
        reward = rewards.get(1);
        assertTrue(reward.getDescription().equals("2 Gelados") && reward.getProjectId() == project.getProjectId() &&
                reward.getMinAmount() == 100);*/



        //cancelaProjeto
        arguments = new ArrayList<>();
        arguments.add("" + project.getProjectId());

        clientCommand = new ClientCommand(new Command("cancelProject", arguments), null);
        clientCommand.run();
        assertTrue(!clientCommand.getServerMessage().isErrorHappened());

        //checkBalanceAgain
        clientCommand = new ClientCommand(new Command("getBalance", new ArrayList<>()), null);
        clientCommand.run();
        p = Pattern.compile("(\\d+)");
        m = p.matcher((String)clientCommand.getServerMessage().getContent());
        if (m.find())
            assertTrue(100 == Integer.parseInt(m.group(1)));
        assertTrue(!clientCommand.getServerMessage().isErrorHappened());



    }

    @Test
    public void testFailSignUp() throws Exception {
        ArrayList<String> arguments = new ArrayList<>();
        arguments.add("testUsername");
        arguments.add("testPassword");

        ClientCommand clientCommand = new ClientCommand(new Command("signup", arguments), null);
        clientCommand.run();

        assertTrue(clientCommand.getServerMessage().isErrorHappened());
    }

    @Test
    public void testFailLogin() throws Exception {
        ArrayList<String> arguments = new ArrayList<>();
        arguments.add("testUsername10000");
        arguments.add("testPassword10000");

        ClientCommand clientCommand = new ClientCommand(new Command("login", arguments), null);
        clientCommand.run();

        assertTrue(clientCommand.getServerMessage().isErrorHappened());
    }
}