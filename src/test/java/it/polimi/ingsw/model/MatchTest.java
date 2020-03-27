package it.polimi.ingsw.model;
import it.polimi.ingsw.cards.CardName;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;


public class MatchTest {


    @Test
    public void testSetNextPlayer03() {
        Match m = new Match();
        List<Player> p = new ArrayList<>();
        m.setPlayers(p);
        p.add(new Player("player1", CardName.APOLLO,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player2",CardName.ARTEMIS,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player3",CardName.ATLAS,new Worker(0,0),new Worker(0,0)));
        m.setCurrentPlayer(p.get(0));
        m.setNextPlayer();
        assertEquals(m.getCurrentPlayer(), p.get(1));
    }

    @Test
    public void testSetNextPlayer13() {
        Match m = new Match();
        List<Player> p = new ArrayList<>();
        m.setPlayers(p);
        p.add(new Player("player1", CardName.APOLLO,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player2",CardName.ARTEMIS,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player3",CardName.ATLAS,new Worker(0,0),new Worker(0,0)));
        m.setCurrentPlayer(p.get(1));
        m.setNextPlayer();
        assertEquals(m.getCurrentPlayer(), p.get(2));
    }

    @Test
    public void testSetNextPlayer23() {
        Match m = new Match();
        List<Player> p = new ArrayList<>();
        m.setPlayers(p);
        p.add(new Player("player1", CardName.APOLLO,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player2",CardName.ARTEMIS,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player3",CardName.ATLAS,new Worker(0,0),new Worker(0,0)));
        m.setCurrentPlayer(p.get(2));
        m.setNextPlayer();
        assertEquals(m.getCurrentPlayer(), p.get(0));
    }

    @Test
    public void testSetNextPlayer02() {
        Match m = new Match();
        List<Player> p = new ArrayList<>();
        m.setPlayers(p);
        p.add(new Player("player1", CardName.APOLLO,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player2",CardName.ARTEMIS,new Worker(0,0),new Worker(0,0)));
        m.setCurrentPlayer(p.get(0));
        m.setNextPlayer();
        assertEquals(m.getCurrentPlayer(), p.get(1));
    }

    @Test
    public void testSetNextPlayer12() {
        Match m = new Match();
        List<Player> p = new ArrayList<>();
        m.setPlayers(p);
        p.add(new Player("player1", CardName.APOLLO,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player2",CardName.ARTEMIS,new Worker(0,0),new Worker(0,0)));
        m.setCurrentPlayer(p.get(1));
        m.setNextPlayer();
        assertEquals(m.getCurrentPlayer(), p.get(0));
    }

    @Test
    public void testCheckCurrentPlayerWin2() {
        Match m = new Match();
        List<Player> p = new ArrayList<>();
        m.setPlayers(p);
        p.add(new Player("player1", CardName.APOLLO,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player2",CardName.ARTEMIS,new Worker(0,0),new Worker(0,0)));
        m.setCurrentPlayer(p.get(0));
        p.get(1).getWorker1().setActive(false);
        p.get(1).getWorker2().setActive(false);
        assertTrue(m.checkCurrentPlayerWin());

    }

    @Test
    public void testCheckCurrentPlayerWin3() {
        Match m = new Match();
        List<Player> p = new ArrayList<>();
        m.setPlayers(p);
        p.add(new Player("player1", CardName.APOLLO,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player2",CardName.ARTEMIS,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player3",CardName.ATLAS,new Worker(0,0),new Worker(0,0)));
        m.setCurrentPlayer(p.get(0));
        p.get(1).getWorker1().setActive(false);
        p.get(1).getWorker2().setActive(false);
        p.get(2).getWorker1().setActive(false);
        p.get(2).getWorker2().setActive(false);
        assertTrue(m.checkCurrentPlayerWin());

    }

    @Test
    public void testCheckCurrentPlayerLoseTrue() {
        Match m = new Match();
        List<Player> p = new ArrayList<>();
        m.setPlayers(p);
        p.add(new Player("player1", CardName.APOLLO,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player2",CardName.ARTEMIS,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player3",CardName.ATLAS,new Worker(0,0),new Worker(0,0)));
        m.setCurrentPlayer(p.get(0));
        p.get(0).getWorker1().setActive(false);
        p.get(0).getWorker2().setActive(false);
        assertTrue(m.checkCurrentPlayerLose());

    }

    @Test
    public void testCheckCurrentPlayerLoseFalse() {
        Match m = new Match();
        List<Player> p = new ArrayList<>();
        m.setPlayers(p);
        p.add(new Player("player1", CardName.APOLLO,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player2",CardName.ARTEMIS,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player3",CardName.ATLAS,new Worker(0,0),new Worker(0,0)));
        m.setCurrentPlayer(p.get(0));
        p.get(0).getWorker1().setActive(false);
        p.get(0).getWorker2().setActive(true);
        assertTrue(m.checkCurrentPlayerLose());

    }

}