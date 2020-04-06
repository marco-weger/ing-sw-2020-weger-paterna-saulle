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
    public void startTurnTestGoOn() {
        initialize();
        controller.getMatch().getPlayers().get(0).setActive(true);
        controller.startTurn(true);
        assertEquals(controller.getMatch().getPlayers().get(1), controller.getMatch().getCurrentPlayer());
    }

    //TODO perchè se elimino prima il primo player e poi il secondo genero errore?
    @Test
    public void startTurnTestPlayerWin() {
        initialize();
        controller.getMatch().getPlayers().get(0).setActive(true);
        controller.getMatch().setLosers(controller.getMatch().getPlayers().get(2));
        controller.getMatch().setLosers(controller.getMatch().getPlayers().get(1));
        controller.startTurn(false);
        assertTrue(controller.getMatch().isEnded());
        assertEquals(Status.END, controller.getMatch().getStatus());
    }

    @Test
    public void startTurnTestSTART() {
        initialize();
        controller.getMatch().getPlayers().get(0).setActive(true);
        for(Cell ce: controller.getMatch().getBoard().getField()){
            if(ce.getRow() == 0 && ce.getColumn() == 0)
                ce.setLevel(0);
            else if(ce.getRow() == 0 && ce.getColumn() == 1)
                ce.setLevel(0);
            else if(ce.getRow() == 0 && ce.getColumn() == 2)
                ce.setLevel(0);
            else if(ce.getRow() == 0 && ce.getColumn() == 3)
                ce.setLevel(0);
            else if(ce.getRow() == 0 && ce.getColumn() == 4)
                ce.setLevel(0);
            else if(ce.getRow() == 1 && ce.getColumn() == 0)
                ce.setLevel(0);
            else if(ce.getRow() == 1 && ce.getColumn() == 1)
                ce.setLevel(0);
            else if(ce.getRow() == 1 && ce.getColumn() ==2)
                ce.setLevel(0);
            else if(ce.getRow() == 1 && ce.getColumn() == 3)
                ce.setLevel(0);
            else if(ce.getRow() == 1 && ce.getColumn() == 4)
                ce.setLevel(0);
        }
        controller.startTurn(false);
        //assertFalse(controller.getMatch().getCurrentPlayer().getCard().hasLost(controller.getMatch().getPlayers(),controller.getMatch().getBoard()));
        assertEquals(Status.START, controller.getMatch().getStatus());
    }

    @Test
    public void startTurnTestCurrentPlayerHasLost() {
        initialize();
        controller.getMatch().getPlayers().get(0).setActive(true);
        controller.getMatch().getPlayers().get(0).getWorker1().move(0,0);
        controller.getMatch().getPlayers().get(0).getWorker2().move(1,0);
        controller.getMatch().getPlayers().get(1).getWorker1().move(2,0);
        controller.getMatch().getPlayers().get(1).getWorker2().move(0,1);
        controller.getMatch().getPlayers().get(2).getWorker1().move(2,1);
        controller.getMatch().getPlayers().get(2).getWorker2().move(1,1);
        for(Cell ce: controller.getMatch().getBoard().getField()){
            if(ce.getRow() == 0 && ce.getColumn() == 0)
                ce.setLevel(0);
            else if(ce.getRow() == 1 && ce.getColumn() == 0)
                ce.setLevel(0);
            else if(ce.getRow() == 2 && ce.getColumn() == 0)
                ce.setLevel(0);
            else if(ce.getRow() == 0 && ce.getColumn() == 1)
                ce.setLevel(0);
            else if(ce.getRow() == 2 && ce.getColumn() == 1)
                ce.setLevel(0);
            else if(ce.getRow() == 1 && ce.getColumn() == 2)
                ce.setLevel(0);
            else if(ce.getRow() == 1 && ce.getColumn() == 1)
                ce.setLevel(0);
            else if(ce.getRow() == 3 && ce.getColumn() == 0)
                ce.setLevel(0);
            else if(ce.getRow() == 3 && ce.getColumn() == 1)
                ce.setLevel(0);
            else if(ce.getRow() == 0 && ce.getColumn() == 2)
                ce.setLevel(0);
        }
        controller.startTurn(false);
        assertEquals(1,controller.getMatch().getLosers().size());
    }
}