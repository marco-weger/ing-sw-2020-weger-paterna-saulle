package it.polimi.ingsw.model;
import it.polimi.ingsw.model.cards.CardName;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;


public class MatchTest {


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
        p.get(1).getWorker1().setActive(false);
        p.get(1).getWorker2().setActive(false);
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
        p.get(1).getWorker1().setActive(false);
        p.get(1).getWorker2().setActive(false);
        p.get(2).getWorker1().setActive(false);
        p.get(2).getWorker2().setActive(false);
        assertTrue(m.checkCurrentPlayerWin());

    }

    @Test
    public void testCheckCurrentPlayerLoseTrue() {
        List<Player> p = new ArrayList<>();
        p.add(new Player("player1", CardName.APOLLO,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player2",CardName.ARTEMIS,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player3",CardName.ATLAS,new Worker(0,0),new Worker(0,0)));
        Match m = new Match(0, new Board(), p,false, p.get(0), Status.START);
        m.setCurrentPlayer(p.get(0));
        p.get(0).getWorker1().setActive(false);
        p.get(0).getWorker2().setActive(false);
        assertTrue(m.checkCurrentPlayerLose());

    }

    @Test
    public void testCheckCurrentPlayerLoseFalse() {
        List<Player> p = new ArrayList<>();
        p.add(new Player("player1", CardName.APOLLO,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player2",CardName.ARTEMIS,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player3",CardName.ATLAS,new Worker(0,0),new Worker(0,0)));
        Match m = new Match(0, new Board(), p,false, p.get(0), Status.START);
        m.setCurrentPlayer(p.get(0));
        p.get(0).getWorker1().setActive(false);
        p.get(0).getWorker2().setActive(true);
        assertTrue(m.checkCurrentPlayerLose());

    }

}