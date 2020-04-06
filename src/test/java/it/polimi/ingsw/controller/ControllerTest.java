package it.polimi.ingsw.controller;
import it.polimi.ingsw.commons.clientMessages.*;
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
    public void challengerChoseClient() {
        initialize();
        controller.getMatch().setStatus(Status.CARD_CHOICE);
        controller.handleMessage(new ChallengerChoseClient("Giulio",new ArrayList<>(Arrays.asList(CardName.ATLAS,CardName.MINOTAUR,CardName.HEPHAESTUS))));
        assertEquals(controller.getMatch().getSelectedCard().size(),0);
        controller.handleMessage(new ChallengerChoseClient("Marco",new ArrayList<>(Arrays.asList(CardName.ATLAS,CardName.MINOTAUR,CardName.HEPHAESTUS))));
        assertTrue(controller.getMatch().getSelectedCard().containsAll(Arrays.asList(CardName.ATLAS,CardName.MINOTAUR,CardName.HEPHAESTUS)));
    }

    @Test
    public void playerChoseClient() {
        initialize();
        challengerChoseClient();
        controller.getMatch().setStatus(Status.CARD_CHOICE);
        controller.handleMessage(new PlayerChoseClient("Francesco",CardName.ATLAS));
        assertNull(controller.getMatch().getPlayers().get(1).getCard());
        controller.handleMessage(new PlayerChoseClient("Giulio",CardName.HEPHAESTUS));
        assertEquals(controller.getMatch().getPlayers().get(2).getCard().getName(),CardName.HEPHAESTUS);
        controller.handleMessage(new PlayerChoseClient("Francesco",CardName.HEPHAESTUS));
        assertNull(controller.getMatch().getPlayers().get(1).getCard());
        controller.handleMessage(new PlayerChoseClient("Francesco",CardName.ATLAS));
        assertEquals(controller.getMatch().getPlayers().get(1).getCard().getName(),CardName.ATLAS);
        assertEquals(controller.getMatch().getPlayers().get(0).getCard().getName(),CardName.MINOTAUR);
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