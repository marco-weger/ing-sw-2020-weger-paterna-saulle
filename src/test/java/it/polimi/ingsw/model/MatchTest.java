package it.polimi.ingsw.model;

import it.polimi.ingsw.commons.Status;
import it.polimi.ingsw.model.cards.CardName;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class MatchTest {
    ArrayList<Player> p = new ArrayList<>(Arrays.asList(
            new Player("player1",null),
            new Player("player2",null),
            new Player("player3",null)
    ));

    public void initialize(){
        for(Player player:p){
            player.setWorker1(new Worker(0,0));
            player.setWorker2(new Worker(0,0));
        }
        p.get(0).setCard(CardName.PAN,null);
        p.get(1).setCard(CardName.ARTEMIS,null);
        p.get(2).setCard(CardName.ATLAS,null);
    }
    @Test
    public void getter_setter(){
        Match m = new Match(1,null);

        //m.setId(1);
        assertEquals(1,m.getId());
        m.setStatus(Status.CHOSEN);
        assertEquals(Status.CHOSEN, m.getStatus());
        Board b = new Board();
        b.getCell(0,0).setLevel(4);
        m.setBoard(b);
        assertEquals(4,m.getBoard().getCell(0,0).getLevel());
        initialize();
        m.setPlayers(p);
        assertEquals(p,m.getPlayers());
        m.setEnded(true);
        assertTrue(m.isEnded());
        m.setSelectedCards(new ArrayList<>(Arrays.asList(CardName.ATHENA,CardName.ATLAS)));
        assertEquals(CardName.ATHENA,m.getSelectedCard().get(0));
        assertEquals( CardName.ATLAS,m.getSelectedCard().get(1));
        m.setLosers(new ArrayList<>(Collections.singletonList(m.getPlayers().get(1))),false);
        assertEquals(2,m.getPlayers().size());
        assertEquals(1,m.getLosers().size());
        assertEquals(CardName.ATLAS,m.getPlayers().get(1).getCard().getName());
        assertEquals(CardName.ARTEMIS,m.getLosers().get(0).getCard().getName());
    }

    @Test
    public void testSetNextPlayer03() {
        initialize();
        Match m = new Match(0,null);
        m.setPlayers(p);
        m.setNextPlayer();
        assertEquals(m.getCurrentPlayer(), p.get(0));
        m.setNextPlayer();
        assertEquals(m.getCurrentPlayer(), p.get(1));
    }

    @Test
    public void testSetNextPlayer13() {
        initialize();
        Match m = new Match(0,null);
        m.setPlayers(p);
        m.setNextPlayer();
        m.setNextPlayer();
        m.setNextPlayer();
        assertEquals(m.getCurrentPlayer(), p.get(2));
    }

    @Test
    public void testSetNextPlayer23() {
        initialize();
        Match m = new Match(0,null);
        m.setPlayers(p);
        m.setNextPlayer();
        m.setNextPlayer();
        m.setNextPlayer();
        m.setNextPlayer();
        assertEquals(m.getCurrentPlayer(), p.get(0));
    }

    @Test
    public void testSetNextPlayer02() {
        initialize();
        Match m = new Match(0,null);
        m.setPlayers(p);
        m.setNextPlayer();
        m.setNextPlayer();
        assertEquals(m.getCurrentPlayer(), p.get(1));
    }

    @Test
    public void testSetNextPlayer12() {
        initialize();
        Match m = new Match(0,null);
        m.setPlayers(p);
        m.setNextPlayer();
        m.setNextPlayer();
        m.setNextPlayer();
        assertEquals(m.getCurrentPlayer(), p.get(2));
    }

    @Test
    public void testCheckCurrentPlayerWin2() {
        initialize();
        Match m = new Match(0,null);
        m.setPlayers(p);
        m.setNextPlayer();
        m.setNextPlayer();
        m.setLosers(new ArrayList<>(Collections.singletonList(p.get(2))),false);
        m.setLosers(new ArrayList<>(Collections.singletonList(p.get(0))),false);
        assertTrue(m.checkCurrentPlayerWin());

    }

    @Test
    public void testCheckCurrentPlayerWin3() {
        initialize();
        Match m = new Match(0,null);
        m.setPlayers(p);
        m.setNextPlayer();
        m.setLosers(new ArrayList<>(Collections.singletonList(p.get(1))),false);
        m.setLosers(new ArrayList<>(Collections.singletonList(p.get(1))),false);
        assertTrue(m.checkCurrentPlayerWin());
    }

    @Test
    public void testSetLoser(){
        initialize();
        Match m = new Match(0,null);
        m.setPlayers(p);
        m.setNextPlayer(); //0
        m.setNextPlayer(); //1
        m.setNextPlayer(); //2

        m.setLosers(new ArrayList<>(Collections.singletonList(p.get(2))),false);
        assertEquals(m.getCurrentPlayer(),m.getPlayers().get(0));
        m.setLosers(new ArrayList<>(Collections.singletonList(p.get(1))),false);
        assertTrue(m.checkCurrentPlayerWin());
    }

    @Test
    public void removeAndAddPlayer(){
        initialize();
        Match m = new Match(0,null);
        m.setPlayers(p);
        m.setNextPlayer(); //0
        m.setNextPlayer(); //1
        m.setNextPlayer(); //2
        m.setStatus(Status.NAME_CHOICE);
        assertEquals(Status.NAME_CHOICE,m.getStatus());
        assertEquals(3,m.getPlayers().size());
        m.removePlayer("player1");
        assertEquals(2,m.getPlayers().size());
        Player p = new Player("Marco",null);
        m.addPlayer(p,false);
        assertEquals(3,m.getPlayers().size());
    }
/*
    @Test
    public void testCheckCurrentPlayerLoseFalse() {
        ArrayList<Player> p = new ArrayList<>();
        p.add(new Player("player1", CardName.APOLLO,new Worker(0,0),new Worker(1,0)));
        p.add(new Player("player2",CardName.ARTEMIS,new Worker(1,4),new Worker(4,4)));
        Match m = new Match(0, new Board(), p,false, p.get(0), Status.START);
        m.setCurrentPlayer(p.get(0));
        assertFalse(m.checkCurrentPlayerLose());
    }

    @Test
    public void testCheckCurrentPlayerLoseTrue() {
        ArrayList<Player> p = new ArrayList<>();
        p.add(new Player("player1", CardName.APOLLO,new Worker(0,0),new Worker(1,0)));
        p.add(new Player("player2",CardName.ARTEMIS,new Worker(1,4),new Worker(4,4)));
        Match m = new Match(0, new Board(), p,false, p.get(0), Status.START);
        for(Cell c : m.getBoard().getField())
            if(!c.isOccupied(p))
                c.setLevel(4);
        m.setCurrentPlayer(p.get(0));
        assertTrue(m.checkCurrentPlayerLose());
        for(Cell c : m.getBoard().getField())
            if(c.getRow() == 2 && c.getColumn() == 0)
                c.setLevel(0);
        assertFalse(m.checkCurrentPlayerLose());
    }

    */

    // clone
    /*
    @Test
    public void testClone(){
        ArrayList<Player> p = new ArrayList<>();
        p.add (new Player("player1", CardName.APOLLO,new Worker(0,0),new Worker(1,0)));
        Match m = new Match(
               0,
                new Board(),
                p,
                false,
                new Player("player1", CardName.APOLLO,new Worker(0,0),new Worker(1,0)),
                Status.START
        );
        m.getPlayers().get(0).getWorker1().setActive(false);
        assertFalse(m.getPlayers().get(0).getWorker1().isActive());
        Match m2 = m.clone();
        assertNotNull(m2);
        m2.setId(1);
        assertEquals(m.getId(),0);
        assertEquals(m2.getId(),1);

        m2.getCurrentPlayer().setName("AAAA");
        assertEquals(m.getCurrentPlayer().getName(),"player1");
        assertEquals(m2.getCurrentPlayer().getName(),"AAAA");

        m2.getPlayers().get(0).getWorker1().setActive(true);
        m2.getPlayers().get(0).setName("TEST");
        assertFalse(m.getPlayers().get(0).getWorker1().isActive());
        assertEquals(m.getPlayers().get(0).getName(),"player1");
        assertTrue(m2.getPlayers().get(0).getWorker1().isActive());
        assertEquals(m2.getPlayers().get(0).getName(),"TEST");

    }
    */
}