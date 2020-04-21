package it.polimi.ingsw.controller;
import it.polimi.ingsw.commons.Status;
import it.polimi.ingsw.commons.clientMessages.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.CardName;
import it.polimi.ingsw.network.*;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

public class ControllerTest {

    Controller controller;
    VirtualView vw;


    public void initialize(){
        //create a list of players with the developers
        vw = new VirtualView(null);
        ArrayList<Player> players = new ArrayList<>(Arrays.asList(
                new Player("Marco",vw),
                new Player("Francesco",vw),
                new Player("Giulio",vw)
        ));

        for(Player player:players){
            player.setWorker1(new Worker(0,0));
            player.setWorker2(new Worker(0,0));
        }

        //generate a Match
        Match m = new Match(42,vw);
        m.setPlayers(players);
        //generate a Controller for that Match
        controller = new Controller(vw);
        controller.setMatch(m);
    }


    @Test
    public void getter_setter(){
        initialize();
        Match m = new Match(42,vw);
        controller = new Controller(vw);
        controller.setMatch(m);
        m = new Match(22,vw);
        controller.setMatch(m);
        assertEquals(m,controller.getMatch());
        VirtualView vv = new VirtualView(null);
        controller.setVirtualView(vv);
        assertEquals(vv,controller.getVirtualView());
    }

    @Test
    public void update() {
        initialize();
        controller.getMatch().setStatus(Status.CARD_CHOICE);
        controller.update(new ChallengerChoseClient("Giulio",new ArrayList<>(Arrays.asList(CardName.ATLAS,CardName.PAN,CardName.HEPHAESTUS))));
        assertEquals(controller.getMatch().getSelectedCard().size(),0);
        controller.update(new ChallengerChoseClient("Marco",new ArrayList<>(Arrays.asList(CardName.ATLAS,CardName.PAN,CardName.HEPHAESTUS))));
        assertTrue(controller.getMatch().getSelectedCard().containsAll(Arrays.asList(CardName.ATLAS,CardName.PAN,CardName.HEPHAESTUS)));
    }

    @Test
    public void challengerChoseClient() {
        initialize();
        controller.getMatch().setStatus(Status.CARD_CHOICE);
        controller.handleMessage(new ChallengerChoseClient("Giulio",new ArrayList<>(Arrays.asList(CardName.ATLAS,CardName.PAN,CardName.HEPHAESTUS))));
        assertEquals(controller.getMatch().getSelectedCard().size(),0);
        controller.handleMessage(new ChallengerChoseClient("Marco",new ArrayList<>(Arrays.asList(CardName.ATLAS,CardName.PAN,CardName.HEPHAESTUS))));
        assertTrue(controller.getMatch().getSelectedCard().containsAll(Arrays.asList(CardName.ATLAS,CardName.PAN,CardName.HEPHAESTUS)));
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
        assertEquals(controller.getMatch().getPlayers().get(0).getCard().getName(),CardName.PAN);
    }

    @Test
    public void workerInitializeClient() {
        // initialize
        vw = new VirtualView(null);
        controller = new Controller(vw);
        controller.setMatch(new Match(42,vw));
        controller.getMatch().setPlayers(
                new ArrayList<>(
                        Arrays.asList(new Player("Marco",vw),new Player("Francesco",vw),new Player("Giulio",vw))
                )
        );
        // card chose
        controller.getMatch().setStatus(Status.CARD_CHOICE);
        controller.handleMessage(new ChallengerChoseClient("Giulio",new ArrayList<>(Arrays.asList(CardName.ATLAS,CardName.PAN,CardName.HEPHAESTUS))));
        assertEquals(controller.getMatch().getSelectedCard().size(),0);
        controller.handleMessage(new ChallengerChoseClient("Marco",new ArrayList<>(Arrays.asList(CardName.ATLAS,CardName.PAN,CardName.HEPHAESTUS))));
        assertTrue(controller.getMatch().getSelectedCard().containsAll(Arrays.asList(CardName.ATLAS,CardName.PAN,CardName.HEPHAESTUS)));
        controller.handleMessage(new PlayerChoseClient("Francesco",CardName.ATLAS));
        assertNull(controller.getMatch().getPlayers().get(1).getCard());
        controller.handleMessage(new PlayerChoseClient("Giulio",CardName.HEPHAESTUS));
        assertEquals(controller.getMatch().getPlayers().get(2).getCard().getName(),CardName.HEPHAESTUS);
        controller.handleMessage(new PlayerChoseClient("Francesco",CardName.HEPHAESTUS));
        assertNull(controller.getMatch().getPlayers().get(1).getCard());
        controller.handleMessage(new PlayerChoseClient("Francesco",CardName.ATLAS));
        assertEquals(controller.getMatch().getPlayers().get(1).getCard().getName(),CardName.ATLAS);
        assertEquals(controller.getMatch().getPlayers().get(0).getCard().getName(),CardName.PAN);

        controller.getMatch().setStatus(Status.WORKER_CHOICE);
        controller.handleMessage(new WorkerInitializeClient("Francesco",1,1));
        for(Player p:controller.getMatch().getPlayers()){
            assertNull(p.getWorker1());
            assertNull(p.getWorker2());
        }
        controller.handleMessage(new WorkerInitializeClient("Marco",4,0));
        controller.handleMessage(new WorkerInitializeClient("Marco",1,1));
        controller.handleMessage(new WorkerInitializeClient("Francesco",2,2));
        controller.handleMessage(new WorkerInitializeClient("Francesco",2,0));
        controller.handleMessage(new WorkerInitializeClient("Giulio",0,4));
        controller.handleMessage(new WorkerInitializeClient("Giulio",0,2));
        for(Player p:controller.getMatch().getPlayers()){
            assertEquals(p.getWorker1().getRow()+p.getWorker1().getColumn(),4);
            assertEquals(p.getWorker2().getRow()+p.getWorker2().getColumn(),2);
        }
    }

    @Test
    public void workerChoseClient() {
        vw = new VirtualView(null);
        workerInitializeClient();
        controller.getMatch().setStatus(Status.START);
        controller.getMatch().getPlayers().get(0).setCurrent(true);
        controller.handleMessage(new WorkerChoseClient("Marco",1));
        assertTrue(controller.getMatch().getPlayers().get(0).getWorker1().isActive());
        assertFalse(controller.getMatch().getPlayers().get(0).getWorker2().isActive());
        controller.getMatch().setNextPlayer();
        controller.handleMessage(new WorkerChoseClient("Francesco",1));
        assertTrue(controller.getMatch().getPlayers().get(1).getWorker1().isActive());
        assertFalse(controller.getMatch().getPlayers().get(1).getWorker2().isActive());
        controller.getMatch().setNextPlayer();
        controller.handleMessage(new WorkerChoseClient("Giulio",2));
        assertFalse(controller.getMatch().getPlayers().get(2).getWorker1().isActive());
        assertTrue(controller.getMatch().getPlayers().get(2).getWorker2().isActive());
    }

    //Marco=PAN
    //Francesco=ATLAS
    //Giulio=HEPHAESTUS



    @Test
    public void disconnectionClient() {
        initialize();
        challengerChoseClient();
        // TODO: this message will be implemented
        //controller.handleMessage(new DisconnectionClient());
    }

    @Test
    public void reConnectionClient() {
        initialize();
        challengerChoseClient();
        // TODO: this message will be implemented
        // controller.handleMessage(new ReConnectionClient());
    }

    @Test
    public void pingclient() {
        initialize();
        challengerChoseClient();
        // TODO: this message will be implemented
        controller.handleMessage(new PingClient());
    }

    //Marco=PAN
    //Francesco=ATLAS
    //Giulio=HEPHAESTUS
     @Test
    public void answerAbilityClient() {
        initialize();
        playerChoseClient();
         controller.getMatch().getPlayers().get(0).setCurrent(false);
         controller.getMatch().getPlayers().get(1).setCurrent(false);
        controller.getMatch().getPlayers().get(2).setCurrent(true);
        assertEquals(controller.getMatch().getCurrentPlayer().getName(),"Giulio");
        controller.getMatch().setStatus(Status.START);
         controller.handleMessage(new AnswerAbilityClient(controller.getMatch().getCurrentPlayer().getName(), false, controller.getMatch().getStatus()));
         assertFalse(controller.getMatch().getCurrentPlayer().getCard().isActive());
         assertEquals(controller.getMatch().getStatus(), Status.CHOSEN);
        //Giulio has answered yes to the question
         //so it will activate his ability, and switch to next turn
        controller.handleMessage(new AnswerAbilityClient(controller.getMatch().getCurrentPlayer().getName(), true, controller.getMatch().getStatus()));
         assertTrue(controller.getMatch().getCurrentPlayer().getCard().isActive());
        assertEquals(controller.getMatch().getStatus(), Status.QUESTION_M);
        controller.getMatch().setStatus(Status.MOVED);
         controller.handleMessage(new AnswerAbilityClient(controller.getMatch().getCurrentPlayer().getName(), true, controller.getMatch().getStatus()));
         assertEquals(controller.getMatch().getStatus(), Status.QUESTION_B);
         controller.getMatch().getPlayers().get(1).setCurrent(true);
         controller.getMatch().getCurrentPlayer().getCard().setActive(true);
         assertTrue(controller.getMatch().getCurrentPlayer().getCard().isActive());
         controller.getMatch().setStatus(Status.START);
         controller.handleMessage(new AnswerAbilityClient("Giulio", false, controller.getMatch().getStatus()));
         assertTrue(controller.getMatch().getCurrentPlayer().getCard().isActive());
//         assertEquals(controller.getMatch().getStatus(), Status.START);
     }

    @Test
    public void MoveClientWin(){
        initialize();
        controller.getMatch().getPlayers().get(0).setCard(CardName.PAN,vw);
        controller.getMatch().getPlayers().get(1).setCard(CardName.ATLAS,vw);
        controller.getMatch().getPlayers().get(2).setCard(CardName.HEPHAESTUS,vw);

        controller.getMatch().getPlayers().get(0).setWorker1(new Worker(3,4));
        controller.getMatch().getPlayers().get(0).setWorker2(new Worker(4,3));
        controller.getMatch().getPlayers().get(1).setWorker1(new Worker(0,0));
        controller.getMatch().getPlayers().get(1).setWorker2(new Worker(1,1));
        controller.getMatch().getPlayers().get(2).setWorker1(new Worker(3,3));
        controller.getMatch().getPlayers().get(2).setWorker2(new Worker(4,4));

        controller.getMatch().getPlayers().get(1).setCurrent(true);
        controller.getMatch().setStatus(Status.QUESTION_M);
        controller.getMatch().getBoard().getCell(0,0).setLevel(2);
        controller.getMatch().getBoard().getCell(1,0).setLevel(3);

        controller.getMatch().getPlayers().get(1).setCurrentWorker(1);
        controller.handleMessage(new MoveClient("Francesco", 1, 0));

        assertEquals(1, controller.getMatch().getPlayers().size());
        assertEquals("Francesco", controller.getMatch().getPlayers().get(0).getName());
        assertEquals(Status.END, controller.getMatch().getStatus());
    }

    @Test
    public void MoveClientDoQuestion(){
        initialize();
        controller.getMatch().getPlayers().get(0).setCard(CardName.PAN,vw);
        controller.getMatch().getPlayers().get(1).setCard(CardName.ATLAS,vw);
        controller.getMatch().getPlayers().get(2).setCard(CardName.HEPHAESTUS,vw);

        controller.getMatch().getPlayers().get(0).setWorker1(new Worker(3,4));
        controller.getMatch().getPlayers().get(0).setWorker2(new Worker(4,3));
        controller.getMatch().getPlayers().get(1).setWorker1(new Worker(0,0));
        controller.getMatch().getPlayers().get(1).setWorker2(new Worker(1,1));
        controller.getMatch().getPlayers().get(2).setWorker1(new Worker(3,3));
        controller.getMatch().getPlayers().get(2).setWorker2(new Worker(4,4));

        controller.getMatch().getPlayers().get(1).setCurrent(true);
        controller.getMatch().setStatus(Status.QUESTION_M);
        controller.getMatch().getBoard().getCell(0,0).setLevel(2);
        controller.getMatch().getBoard().getCell(1,0).setLevel(2);

        controller.getMatch().getPlayers().get(1).setCurrentWorker(1);
        controller.handleMessage(new MoveClient("Francesco", 1, 0));

        assertEquals(3, controller.getMatch().getPlayers().size());
        assertEquals(Status.MOVED, controller.getMatch().getStatus());
    }


    @Test
    public void MoveClient() {
        initialize();
        controller.getMatch().getPlayers().get(0).setCard(CardName.ATHENA,vw);
        controller.getMatch().getPlayers().get(1).setCard(CardName.PAN,vw);
        controller.getMatch().getPlayers().get(2).setCard(CardName.HEPHAESTUS,vw);

        controller.getMatch().getPlayers().get(0).setWorker1(new Worker(3,4));
        controller.getMatch().getPlayers().get(0).setWorker2(new Worker(4,3));
        controller.getMatch().getPlayers().get(1).setWorker1(new Worker(0,0));
        controller.getMatch().getPlayers().get(1).setWorker2(new Worker(1,1));
        controller.getMatch().getPlayers().get(2).setWorker1(new Worker(3,3));
        controller.getMatch().getPlayers().get(2).setWorker2(new Worker(4,4));

        controller.getMatch().getPlayers().get(1).setCurrent(true);
        controller.getMatch().setStatus(Status.QUESTION_M);

        controller.getMatch().getPlayers().get(1).setCurrentWorker(1);

        controller.handleMessage(new MoveClient("Francesco", 1, 0));

        assertEquals(1, controller.getMatch().getPlayers().get(1).getWorker1().getRow());
        assertEquals(0, controller.getMatch().getPlayers().get(1).getWorker1().getColumn());
        assertEquals(Status.QUESTION_B, controller.getMatch().getStatus());
    }


    @Test
    public void BuildClient() {
        initialize();
        playerChoseClient();
        controller.getMatch().getPlayers().get(0).setCurrent(false);
        controller.getMatch().getPlayers().get(1).setCurrent(true);
        controller.getMatch().setStatus(Status.QUESTION_B);
        controller.getMatch().getPlayers().get(1).getWorker1().move(0,0);
        controller.getMatch().getPlayers().get(1).getWorker2().move(1,1);
        controller.getMatch().getPlayers().get(1).setCurrentWorker(1);
        controller.getMatch().getBoard().getCell(1,0).setLevel(0);
        controller.handleMessage(new BuildClient("Francesco", 1, 0));
        //assertTrue(controller.getMatch().getCurrentPlayer().getCard().build(controller.getMatch().getPlayers(),controller.getMatch().getBoard(),controller.getMatch().getBoard().getCell(1,0)));
        assertEquals(1, controller.getMatch().getBoard().getCell(1, 0).getLevel());
        assertEquals(Status.START, controller.getMatch().getStatus());
    }

    @Test
    public void BuildClientPrometheus() {
        initialize();
        playerChoseClient();
        controller.getMatch().getPlayers().get(0).setCurrent(false);
        controller.getMatch().getPlayers().get(1).setCard(CardName.PROMETHEUS,vw);
        controller.getMatch().getPlayers().get(1).getCard().setActive(true);
        controller.getMatch().getPlayers().get(1).setCurrent(true);
        controller.getMatch().setStatus(Status.QUESTION_B);
        controller.getMatch().getPlayers().get(1).getWorker1().move(0,0);
        controller.getMatch().getPlayers().get(1).getWorker2().move(1,1);
        controller.getMatch().getPlayers().get(1).setCurrentWorker(1);
        controller.getMatch().getBoard().getCell(1,0).setLevel(0);
        controller.handleMessage(new BuildClient("Francesco", 1, 0));
        //assertTrue(controller.getMatch().getCurrentPlayer().getCard().build(controller.getMatch().getPlayers(),controller.getMatch().getBoard(),controller.getMatch().getBoard().getCell(1,0)));
        assertEquals(1, controller.getMatch().getBoard().getCell(1, 0).getLevel());
        assertEquals(Status.QUESTION_M, controller.getMatch().getStatus());
    }

    @Test
    public void BuildClientImpossible() {
        initialize();
        playerChoseClient();
        controller.getMatch().getPlayers().get(0).setCurrent(false);
        controller.getMatch().getPlayers().get(1).setCurrent(true);
        controller.getMatch().setStatus(Status.BUILT);
        controller.getMatch().getPlayers().get(1).getWorker1().move(0,0);
        controller.getMatch().getPlayers().get(1).getWorker2().move(1,1);
        controller.getMatch().getPlayers().get(1).setCurrentWorker(1);
        controller.getMatch().getBoard().getCell(1,0).setLevel(4);
        controller.handleMessage(new BuildClient("Francesco", 1, 0));
        assertEquals(4, controller.getMatch().getBoard().getCell(1, 0).getLevel());
        assertEquals(Status.BUILT, controller.getMatch().getStatus());
    }

    @Test
    public void endGameTest() {
        initialize();
        playerChoseClient();

        controller.getMatch().setLosers(controller.getMatch().getPlayers().get(0));
        controller.getMatch().setLosers(controller.getMatch().getPlayers().get(0));
        controller.startTurn(true);
        //controller.endGame(controller.getMatch().getPlayers().get(2));
        assertEquals(2, controller.getMatch().getLosers().size());
        assertTrue(controller.getMatch().isEnded());
        assertEquals(controller.getMatch().getPlayers().get(0).getName(),"Giulio");
    }

    @Test
    public void initializeMatch() {
    }

    @Test
    public void startTurnTestGoOn() {
        initialize();
        playerChoseClient();
        controller.getMatch().getPlayers().get(0).setCurrent(true);
        controller.startTurn(true);
        assertEquals(controller.getMatch().getPlayers().get(1), controller.getMatch().getCurrentPlayer());
    }

    @Test
    public void startTurnTestPlayerWin() {
        initialize();
        playerChoseClient();
        controller.getMatch().getPlayers().get(0).setCurrent(true);
        controller.getMatch().setLosers(controller.getMatch().getPlayers().get(1));
        controller.getMatch().setLosers(controller.getMatch().getPlayers().get(1));
        assertEquals(controller.getMatch().getPlayers().size(),1);
        controller.startTurn(false);
        assertTrue(controller.getMatch().isEnded());
        assertEquals(Status.END, controller.getMatch().getStatus());
    }

    @Test
    public void startTurnTestSTART() {
        initialize();
        playerChoseClient();
        controller.getMatch().getPlayers().get(0).setCurrent(true);
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
        playerChoseClient();
        controller.getMatch().getPlayers().get(0).setCurrent(true);
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