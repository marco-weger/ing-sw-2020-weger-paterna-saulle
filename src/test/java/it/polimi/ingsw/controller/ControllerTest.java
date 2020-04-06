package it.polimi.ingsw.controller;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.CardName;
import org.junit.Test;

import javax.naming.ldap.Control;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;


public class ControllerTest {

    Controller controller;

    public void initialize(){
        //create a list of players with the developers
        ArrayList<Player> players = new ArrayList<>(Arrays.asList(
                new Player("Marco"),
                new Player("Francesco"),
                new Player("Giulio")
        ));

        for(Player player:players){
            player.setWorker1(new Worker(0,0));
            player.setWorker2(new Worker(0,0));
        }
        players.get(0).setCard(CardName.PAN);
        players.get(1).setCard(CardName.ARTEMIS);
        players.get(2).setCard(CardName.ATLAS);

        //generate a Match
        Match m = new Match(42);
        m.setPlayers(players);
        //generate a Controller for that Match
        controller = new Controller(m);
    }

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
        initialize();
        controller.endGame(controller.getMatch().getPlayers().get(2));
        assertEquals(2, controller.getMatch().getLosers().size());
        assertTrue(controller.getMatch().isEnded());
        assertEquals(controller.getMatch().getPlayers().get(0).getName(),"Giulio");
    }

    @Test
    public void inizializeMatch() {
    }

    @Test
    public void startTurn() {
    }
}