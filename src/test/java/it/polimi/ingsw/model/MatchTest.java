package it.polimi.ingsw.model;
import it.polimi.ingsw.model.cards.CardName;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;


public class MatchTest {

    @Test
    public void getter_setter(){
        Match m = new Match(0, new Board(), null,false, null, Status.START);
        m.setId(1);
        assertEquals(m.getId(),1);
        m.setStatus(Status.CHOSEN);
        assertEquals(Status.CHOSEN, m.getStatus());
        Board b = new Board();
        b.getCell(0,0).setLevel(4);
        m.setBoard(b);
        assertEquals(m.getBoard().getCell(0,0).getLevel(),4);
        List<Player> p = new ArrayList<>();
        p.add(new Player("player1", CardName.APOLLO,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player2",CardName.ARTEMIS,new Worker(0,0),new Worker(0,0)));
        m.setPlayers(p);
        assertEquals(p,m.getPlayers());
        m.setCurrentPlayer(p.get(0));
        assertEquals(p.get(0),m.getCurrentPlayer());
        m.setEnded(true);
        assertTrue(m.isEnded());
    }

    @Test
    public void testSetNextPlayer03() {
        List<Player> p = new ArrayList<>();
        p.add(new Player("player1", CardName.APOLLO,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player2",CardName.ARTEMIS,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player3",CardName.ATLAS,new Worker(0,0),new Worker(0,0)));
        Match m = new Match(0, new Board(), p,false, p.get(0), Status.START);
        m.setCurrentPlayer(p.get(0));
        m.setNextPlayer();
        assertEquals(m.getCurrentPlayer(), p.get(1));
    }

    @Test
    public void testSetNextPlayer13() {
        List<Player> p = new ArrayList<>();
        p.add(new Player("player1", CardName.APOLLO,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player2",CardName.ARTEMIS,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player3",CardName.ATLAS,new Worker(0,0),new Worker(0,0)));
        Match m = new Match(0, new Board(), p,false, p.get(0), Status.START);
        m.setCurrentPlayer(p.get(1));
        m.setNextPlayer();
        assertEquals(m.getCurrentPlayer(), p.get(2));
    }

    @Test
    public void testSetNextPlayer23() {
        List<Player> p = new ArrayList<>();
        p.add(new Player("player1", CardName.APOLLO,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player2",CardName.ARTEMIS,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player3",CardName.ATLAS,new Worker(0,0),new Worker(0,0)));
        Match m = new Match(0, new Board(), p,false, p.get(0), Status.START);
        m.setCurrentPlayer(p.get(2));
        m.setNextPlayer();
        assertEquals(m.getCurrentPlayer(), p.get(0));
    }

    @Test
    public void testSetNextPlayer02() {
        List<Player> p = new ArrayList<>();
        p.add(new Player("player1", CardName.APOLLO,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player2",CardName.ARTEMIS,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player3",CardName.ATLAS,new Worker(0,0),new Worker(0,0)));
        Match m = new Match(0, new Board(), p,false, p.get(0), Status.START);
        m.setCurrentPlayer(p.get(0));
        m.setNextPlayer();
        assertEquals(m.getCurrentPlayer(), p.get(1));
    }

    @Test
    public void testSetNextPlayer12() {
        List<Player> p = new ArrayList<>();
        p.add(new Player("player1", CardName.APOLLO,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player2",CardName.ARTEMIS,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player3",CardName.ATLAS,new Worker(0,0),new Worker(0,0)));
        Match m = new Match(0, new Board(), p,false, p.get(0), Status.START);
        m.setCurrentPlayer(p.get(1));
        m.setNextPlayer();
        assertEquals(m.getCurrentPlayer(), p.get(2));
    }

    @Test
    public void testCheckCurrentPlayerWin2() {
        List<Player> p = new ArrayList<>();
        p.add(new Player("player1", CardName.APOLLO,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player2",CardName.ARTEMIS,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player3",CardName.ATLAS,new Worker(0,0),new Worker(0,0)));
        Match m = new Match(0, new Board(), p,false, p.get(0), Status.START);
        m.setCurrentPlayer(p.get(0));
        p.get(1).setCurrentWorker(0);
        assertTrue(m.checkCurrentPlayerWin());

    }

    @Test
    public void testCheckCurrentPlayerWin3() {
        List<Player> p = new ArrayList<>();
        p.add(new Player("player1", CardName.APOLLO,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player2",CardName.ARTEMIS,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player3",CardName.ATLAS,new Worker(0,0),new Worker(0,0)));
        Match m = new Match(0, new Board(), p,false, p.get(0), Status.START);
        m.setCurrentPlayer(p.get(0));
        p.get(1).setCurrentWorker(0);
        p.get(2).setCurrentWorker(0);
        assertTrue(m.checkCurrentPlayerWin());

    }

    @Test
    public void testCheckCurrentPlayerLoseFalse() {
        List<Player> p = new ArrayList<>();
        p.add(new Player("player1", CardName.APOLLO,new Worker(0,0),new Worker(1,0)));
        p.add(new Player("player2",CardName.ARTEMIS,new Worker(1,4),new Worker(4,4)));
        Match m = new Match(0, new Board(), p,false, p.get(0), Status.START);
        m.setCurrentPlayer(p.get(0));
        assertFalse(m.checkCurrentPlayerLose());
    }

    @Test
    public void testCheckCurrentPlayerLoseTrue() {
        List<Player> p = new ArrayList<>();
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

}