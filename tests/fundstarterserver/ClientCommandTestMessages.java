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
 * Created by Miguel Prata Leal on 11/12/15.
 */
public class ClientCommandTestMessages{
    Project project;

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
        //newProject
        project = new Project("testProject", "testDescription", new Date(new java.util.Date().getTime()), 600);
        clientCommand = new ClientCommand(new Command("newProject", new ArrayList<>()), project);
        clientCommand.run();
        assertTrue(!clientCommand.getServerMessage().isErrorHappened());
        Pattern p = Pattern.compile("(\\d+)");
        Matcher m = p.matcher((String)clientCommand.getServerMessage().getContent());
        if (m.find())
            project.setProjectId(Integer.parseInt(m.group(1)));

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
    }


    @Test
    public void testSendMessages() throws Exception {
        int userId2 = ClientSession.getInstance().getUserIDLoggedIn();
        //sendMessageToProject
        Message message = new Message();
        message.setProjectId(project.getProjectId());
        message.setText("Ola");
        ClientCommand clientCommand = new ClientCommand(new Command("sendMessageToProject", new ArrayList<>()), message);
        clientCommand.run();
        assertTrue(!clientCommand.getServerMessage().isErrorHappened());

        //logout user2
        clientCommand = new ClientCommand(new Command("logout", new ArrayList<>()), null);
        clientCommand.run();
        assertTrue(ClientSession.getInstance().getUserIDLoggedIn() == 0 && ClientSession.getInstance().getUsernameLoggedIn() == "" &&
                !ClientSession.getInstance().getSessionLoggedIn());


        // login user1
        ArrayList<String> arguments = new ArrayList<>();
        arguments.add("testUsername");
        arguments.add("testPassword");
        clientCommand = new ClientCommand(new Command("login", arguments), null);
        clientCommand.run();


        //getProjectMessages
        arguments = new ArrayList<>();
        arguments.add("" + project.getProjectId());
        clientCommand = new ClientCommand(new Command("getProjectMessages", arguments), null);
        clientCommand.run();
        ArrayList<Message> messages = (ArrayList<Message>) clientCommand.getServerMessage().getContent();
        assertTrue(!clientCommand.getServerMessage().isErrorHappened());
        assertTrue(messages.get(0).getText().equals(message.getText()));
        assertTrue(messages.get(0).getPledgerUserId() == userId2);
        assertTrue(messages.get(0).getProjectId() == project.getProjectId());
        assertTrue(messages.get(0).getMessageId() > 0);
        assertTrue(!messages.get(0).isMessageFromProject());


        //sendMessageToUser
        message = new Message();
        message.setProjectId(project.getProjectId());
        message.setText("OlaResposta");
        message.setPledgerUserId(userId2);
        clientCommand = new ClientCommand(new Command("sendMessageFromProject", new ArrayList<>()), message);
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

        //getUserMessages
        arguments = new ArrayList<>();
        clientCommand = new ClientCommand(new Command("getUserMessages", arguments), null);
        clientCommand.run();
        messages = (ArrayList<Message>) clientCommand.getServerMessage().getContent();
        assertTrue(!clientCommand.getServerMessage().isErrorHappened());
        assertTrue(messages.get(0).getText().equals(message.getText()));
        assertTrue(messages.get(0).getPledgerUserId() == userId2);
        assertTrue(messages.get(0).getProjectId() == project.getProjectId());
        assertTrue(messages.get(0).getMessageId() > 0);
        assertTrue(messages.get(0).isMessageFromProject());




    }

    @After
    public void tearDown() throws Exception {
        // login do user2 para continuar
        ArrayList<String> arguments = new ArrayList<>();
        arguments.add("testUsername");
        arguments.add("testPassword");

        ClientCommand clientCommand = new ClientCommand(new Command("login", arguments), null);
        clientCommand.run();


        //cancelaProjeto
        arguments = new ArrayList<>();
        arguments.add("" + project.getProjectId());

        clientCommand = new ClientCommand(new Command("cancelProject", arguments), null);
        clientCommand.run();
        assertTrue(!clientCommand.getServerMessage().isErrorHappened());

    }
}
