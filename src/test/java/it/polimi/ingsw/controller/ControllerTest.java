package it.polimi.ingsw.controller;
import it.polimi.ingsw.model.*;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;


public class ControllerTest {

    //create a list of players with the developers
    ArrayList<Player> players = new ArrayList<>(Arrays.asList(
            new Player("Marco"),
            new Player("Francesco"),
            new Player("Giulio")
    ));

    //generate a Match
    Match m = new Match(42);
    //generate a Controller for that Match
    Controller controller = new Controller(m);

    @Test
    public void update() {
    }

    @Test
    public void handleMessage() {
    }

    @Test
    public void testHandleMessage() {
    }

    @Test
    public void testHandleMessage1() {
    }

    @Test
    public void testHandleMessage2() {
    }

    @Test
    public void testHandleMessage3() {
    }

    @Test
    public void testHandleMessage4() {
    }

    @Test
    public void testHandleMessage5() {
    }

    @Test
    public void testHandleMessage6() {
    }

    @Test
    public void testHandleMessage7() {
    }

    @Test
    public void testHandleMessage8() {
    }

    @Test
    public void testHandleMessage9() {
    }

    @Test
    public void endGameTest() {
        controller.getMatch().setPlayers(players);

        System.out.println(m.getPlayers().get(0).getName());
        System.out.println(m.getPlayers().get(1).getName());
        System.out.println(m.getPlayers().get(2).getName() + "\n");

        controller.endGame(m.getPlayers().get(0));

        System.out.println(m.getLosers().get(0).getName());
        //System.out.println(m.getLosers().get(1).getName());

       // assertEquals(2, m.getLosers().size());
        assertTrue(m.isEnded());
        assertEquals(Status.END,m.getStatus());
    }

    @Test
    public void inizializeMatch() {
    }

    @Test
    public void startTurn() {
    }
}