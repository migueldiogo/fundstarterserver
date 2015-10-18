package fundstarterserver;

import org.junit.Test;

import static org.junit.Assert.*;


/**
 * Created by Miguel Prata Leal on 18/10/15.
 */
public class ClientCommandTest {

    @Test
    public void testRun() throws Exception {
        ClientCommand clientCommand = new ClientCommand("login miguel 123456");
        clientCommand.run();
    }


}