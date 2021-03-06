package it.polimi.ingsw.controller;
import it.polimi.ingsw.commons.Status;
import it.polimi.ingsw.commons.clientmessages.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.CardName;
import it.polimi.ingsw.network.*;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

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
    public void mode(){
        vw = new VirtualView(null);
        Match m = new Match(42,vw);
        //generate a Controller for that Match
        controller = new Controller(vw);
        controller.setMatch(m);
        controller.update(new ModeChoseClient("Giulio",3));
        assertEquals(1, m.getPlayers().size());
        controller.update(new ModeChoseClient("Marco",3));
        assertEquals(2, m.getPlayers().size());
        controller.update(new ModeChoseClient("Francesco",3));
        assertEquals(3, m.getPlayers().size());
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
        assertEquals(0,controller.getMatch().getSelectedCard().size());
        controller.update(new ChallengerChoseClient("Marco",new ArrayList<>(Arrays.asList(CardName.ATLAS,CardName.PAN,CardName.HEPHAESTUS))));
        assertTrue(controller.getMatch().getSelectedCard().containsAll(Arrays.asList(CardName.ATLAS,CardName.PAN,CardName.HEPHAESTUS)));
    }

    @Test
    public void challengerChoseClient() {
        initialize();
        controller.getMatch().setStatus(Status.CARD_CHOICE);
        controller.handleMessage(new ChallengerChoseClient("Giulio",new ArrayList<>(Arrays.asList(CardName.ATLAS,CardName.PAN,CardName.HEPHAESTUS))));
        assertEquals(0,controller.getMatch().getSelectedCard().size());
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
        assertEquals(CardName.HEPHAESTUS,controller.getMatch().getPlayers().get(2).getCard().getName());
        controller.handleMessage(new PlayerChoseClient("Francesco",CardName.HEPHAESTUS));
        assertNull(controller.getMatch().getPlayers().get(1).getCard());
        controller.handleMessage(new PlayerChoseClient("Francesco",CardName.ATLAS));
        assertEquals(CardName.ATLAS,controller.getMatch().getPlayers().get(1).getCard().getName());
        assertEquals(CardName.PAN,controller.getMatch().getPlayers().get(0).getCard().getName());
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
        assertEquals(0,controller.getMatch().getSelectedCard().size());
        controller.handleMessage(new ChallengerChoseClient("Marco",new ArrayList<>(Arrays.asList(CardName.ATLAS,CardName.PAN,CardName.HEPHAESTUS))));
        assertTrue(controller.getMatch().getSelectedCard().containsAll(Arrays.asList(CardName.ATLAS,CardName.PAN,CardName.HEPHAESTUS)));
        controller.handleMessage(new PlayerChoseClient("Francesco",CardName.ATLAS));
        assertNull(controller.getMatch().getPlayers().get(1).getCard());
        controller.handleMessage(new PlayerChoseClient("Giulio",CardName.HEPHAESTUS));
        assertEquals(CardName.HEPHAESTUS,controller.getMatch().getPlayers().get(2).getCard().getName());
        controller.handleMessage(new PlayerChoseClient("Francesco",CardName.HEPHAESTUS));
        assertNull(controller.getMatch().getPlayers().get(1).getCard());
        controller.handleMessage(new PlayerChoseClient("Francesco",CardName.ATLAS));
        assertEquals(CardName.ATLAS,controller.getMatch().getPlayers().get(1).getCard().getName());
        assertEquals(CardName.PAN,controller.getMatch().getPlayers().get(0).getCard().getName());

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
            assertEquals(4,p.getWorker1().getRow()+p.getWorker1().getColumn());
            assertEquals(2,p.getWorker2().getRow()+p.getWorker2().getColumn());
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

    @Test
    public void workerChoseClientPrometheus() {
        initialize();
        controller.getMatch().getPlayers().get(0).setCard(CardName.PROMETHEUS,vw);
        controller.getMatch().getPlayers().get(1).setCard(CardName.ATLAS,vw);
        controller.getMatch().getPlayers().get(2).setCard(CardName.HEPHAESTUS,vw);

        controller.getMatch().getPlayers().get(0).setWorker1(new Worker(0,0));
        controller.getMatch().getPlayers().get(0).setWorker2(new Worker(1,0));
        controller.getMatch().getPlayers().get(1).setWorker1(new Worker(4,0));
        controller.getMatch().getPlayers().get(1).setWorker2(new Worker(1,1));
        controller.getMatch().getPlayers().get(2).setWorker1(new Worker(3,3));
        controller.getMatch().getPlayers().get(2).setWorker2(new Worker(4,4));

        controller.getMatch().getPlayers().get(0).setCurrent(true);
        controller.getMatch().setStatus(Status.CHOSEN);
        controller.getMatch().getBoard().getCell(0,0).setLevel(1);
        controller.getMatch().getBoard().getCell(1,0).setLevel(1);
        controller.getMatch().getBoard().getCell(1,1).setLevel(1);
        controller.getMatch().getBoard().getCell(0,1).setLevel(1);
        controller.getMatch().getPlayers().get(0).setCurrentWorker(1);
        assertFalse(controller.getMatch().getCurrentPlayer().getCard().activable(controller.getMatch().getPlayers(),controller.getMatch().getBoard()));

    }

    @Test
    public void loadMatch(){
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
            player.setCard(CardName.ARTEMIS,vw);
        }

        //generate a Match
        Match m = new Match(42,vw);
        m.setPlayers(players);
        //generate a Controller for that Match
        controller = new Controller(vw,m);
        assertEquals(controller.getMatch().getId(),42);
    }

    @Test
    public void disconnection(){
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
            player.setCard(CardName.ARTEMIS,vw);
        }

        //generate a Match
        Match m = new Match(42,vw);
        m.setPlayers(players);
        //generate a Controller for that Match
        controller = new Controller(vw,m);
        m.setStatus(Status.START);
        m.getPlayers().get(0).setCurrent(true);
        controller.handleMessage(new DisconnectionClient("Marco",true));
        assertEquals(2,controller.getMatch().getPlayers().size());
        assertEquals(1,controller.getMatch().getLosers().size());
    }

    @Test
    public void disconnectionNAME_CHOICE(){
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
            player.setCard(CardName.ARTEMIS,vw);
        }

        //generate a Match
        Match m = new Match(42,vw);
        m.setPlayers(players);
        //generate a Controller for that Match
        controller = new Controller(vw,m);
        m.setStatus(Status.NAME_CHOICE);
        controller.handleMessage(new DisconnectionClient("Marco",false));
        assertEquals(controller.getMatch().getPlayers().size(),2);
    }

    @Test
    public void modeChose(){
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
            player.setCard(CardName.ARTEMIS,vw);
        }

        //generate a Match
        Match m = new Match(42,vw);
        m.setPlayers(players);
        //generate a Controller for that Match
        controller = new Controller(vw,m);
        m.setStatus(Status.NAME_CHOICE);
        ModeChoseClient mcc = new ModeChoseClient("Giulio",2);
        mcc.refused = true;
        controller.handleMessage(mcc);
        assertTrue(controller.getMatch().isEnded());
    }

    //Marco=PAN
    //Francesco=ATLAS
    //Giulio=HEPHAESTUS



    @Test
    public void disconnectionClient() {
        initialize();
        challengerChoseClient();
    }

    @Test
    public void reConnectionClient() {
        initialize();
        challengerChoseClient();
    }

    @Test
    public void pingclient() {
        initialize();
        challengerChoseClient();
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
        assertEquals("Giulio",controller.getMatch().getCurrentPlayer().getName());
        controller.getMatch().setStatus(Status.START);
         controller.handleMessage(new AnswerAbilityClient(controller.getMatch().getCurrentPlayer().getName(), false, controller.getMatch().getStatus()));
         assertFalse(controller.getMatch().getCurrentPlayer().getCard().isActive());
         assertEquals(Status.CHOSEN,controller.getMatch().getStatus());
        //Giulio has answered yes to the question
         // so it will activate his ability, and switch to next turn
        controller.handleMessage(new AnswerAbilityClient(controller.getMatch().getCurrentPlayer().getName(), true, controller.getMatch().getStatus()));
         assertTrue(controller.getMatch().getCurrentPlayer().getCard().isActive());
        assertEquals(Status.QUESTION_M,controller.getMatch().getStatus());
        controller.getMatch().setStatus(Status.MOVED);
         controller.handleMessage(new AnswerAbilityClient(controller.getMatch().getCurrentPlayer().getName(), true, controller.getMatch().getStatus()));
         assertEquals(Status.QUESTION_B,controller.getMatch().getStatus());
         controller.getMatch().getPlayers().get(1).setCurrent(true);
         controller.getMatch().getCurrentPlayer().getCard().setActive(true);
         assertTrue(controller.getMatch().getCurrentPlayer().getCard().isActive());
         controller.getMatch().setStatus(Status.START);
         controller.handleMessage(new AnswerAbilityClient("Giulio", false, controller.getMatch().getStatus()));
         assertTrue(controller.getMatch().getCurrentPlayer().getCard().isActive());
         assertEquals(Status.CHOSEN,controller.getMatch().getStatus());
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
        assertEquals(1, controller.getMatch().getBoard().getCell(1, 0).getLevel());
        assertTrue(controller.getMatch().getCurrentPlayer().getCard().build(controller.getMatch().getPlayers(),controller.getMatch().getBoard(),controller.getMatch().getBoard().getCell(1,0)));
        assertEquals(2, controller.getMatch().getBoard().getCell(1, 0).getLevel());
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
        assertTrue(controller.getMatch().getCurrentPlayer().getCard().build(controller.getMatch().getPlayers(),controller.getMatch().getBoard(),controller.getMatch().getBoard().getCell(1,0)));
        assertEquals(2, controller.getMatch().getBoard().getCell(1, 0).getLevel());
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

        controller.getMatch().setLosers(new ArrayList<>(Collections.singletonList(controller.getMatch().getPlayers().get(0))),false);
        controller.getMatch().setLosers(new ArrayList<>(Collections.singletonList(controller.getMatch().getPlayers().get(0))),false);
        controller.startTurn(true);
        assertEquals(2, controller.getMatch().getLosers().size());
        assertTrue(controller.getMatch().isEnded());
        assertEquals("Giulio",controller.getMatch().getPlayers().get(0).getName());
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
        controller.getMatch().setLosers(new ArrayList<>(Arrays.asList(controller.getMatch().getPlayers().get(1),controller.getMatch().getPlayers().get(2))),false);
        assertEquals(1,controller.getMatch().getPlayers().size());
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
        assertFalse(controller.getMatch().getCurrentPlayer().getCard().hasLost(controller.getMatch().getPlayers(),controller.getMatch().getBoard()));
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