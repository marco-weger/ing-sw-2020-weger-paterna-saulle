package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.*;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class AthenaTest {


    @Test
    public void inizializeTurn_turnoffpower() {
        Card c = FactoryCard.getCard(CardName.ATHENA);
        ArrayList<Player> p = new ArrayList<>();
        p.add(new Player("player1", CardName.APOLLO, new Worker(0, 0), new Worker(0, 0)));
        p.add(new Player("player2", CardName.ARTEMIS, new Worker(0, 0), new Worker(0, 0)));
        p.add(new Player("player3", CardName.ATLAS, new Worker(0, 0), new Worker(0, 0)));
        assertNotNull(c);
        p.get(0).getCard().setActive(true);
        c.inizializeTurn();
        assertFalse(c.isActive());
    }


    @Test
    public void inizializeTurn_NOpower() {
        Card c = FactoryCard.getCard(CardName.ATHENA);
        ArrayList<Player> p = new ArrayList<>();
        p.add(new Player("player1", CardName.APOLLO, new Worker(0, 0), new Worker(0, 0)));
        p.add(new Player("player2", CardName.ARTEMIS, new Worker(0, 0), new Worker(0, 0)));
        p.add(new Player("player3", CardName.ATLAS, new Worker(0, 0), new Worker(0, 0)));
        assertNotNull(c);
        p.get(0).getCard().setActive(false);
        c.inizializeTurn();
        assertFalse(c.isActive());
    }

    @Test
    public void ActiveBlock() {
        ArrayList<Player> p = new ArrayList<>();
        p.add(new Player("player1", CardName.ATHENA, new Worker(2, 3), new Worker(0, 0)));
        p.add(new Player("player2", CardName.ARTEMIS, new Worker(4, 4), new Worker(0, 1)));
        p.add(new Player("player3", CardName.ATLAS, new Worker(1, 0), new Worker(0, 2)));
        p.get(1).setCurrentWorker(1);
        Worker w1 = p.get(1).getCurrentWorker();
        Board b = new Board();
        for (Cell c : b.getField()) {
            if (c.getRow() == 4 && c.getColumn() == 4)
                c.setLevel(2);
            else if (c.getRow() == 4 && c.getColumn() == 3)
                c.setLevel(3);
            else if (c.getRow() == 3 && c.getColumn() == 4)
                c.setLevel(3);
            else if (c.getRow() == 3 && c.getColumn() == 3)
                c.setLevel(1);
        }
        assertNotNull(p);
        assertNotNull(b);
        ArrayList<Cell> ret = p.get(0).getCard().activeBlock(p, b, w1, Status.QUESTION_M);
        assertEquals(2, ret.size());
    }

    @Test
    public void ActiveBlockWithOccupied() {
        ArrayList<Player> p = new ArrayList<>();
        p.add(new Player("player1", CardName.ATHENA, new Worker(2, 3), new Worker(0, 0)));
        p.add(new Player("player2", CardName.ARTEMIS, new Worker(4, 4), new Worker(0, 1)));
        p.add(new Player("player3", CardName.ATLAS, new Worker(3, 4), new Worker(0, 2)));
        p.get(1).setCurrentWorker(1);
        Worker w1 = p.get(1).getCurrentWorker();
        Board b = new Board();
        for (Cell c : b.getField()) {
            if (c.getRow() == 4 && c.getColumn() == 4)
                c.setLevel(2);
            else if (c.getRow() == 4 && c.getColumn() == 3)
                c.setLevel(3);
            else if (c.getRow() == 3 && c.getColumn() == 4)
                c.setLevel(3);
            else if (c.getRow() == 3 && c.getColumn() == 3)
                c.setLevel(1);
        }
        assertNotNull(p);
        assertNotNull(b);
        ArrayList<Cell> ret = p.get(0).getCard().activeBlock(p, b, w1, Status.QUESTION_M);
        assertEquals(1, ret.size());
    }


    @Test
    public void ActiveBlock_NULL() {
        ArrayList<Player> p = new ArrayList<>();
        p.add(new Player("player1", CardName.ATHENA, new Worker(2, 3), new Worker(0, 0)));
        p.add(new Player("player2", CardName.ARTEMIS, new Worker(4, 4), new Worker(0, 1)));
        p.add(new Player("player3", CardName.ATLAS, new Worker(1, 0), new Worker(0, 2)));
        p.get(1).setCurrentWorker(1);
        Board b = new Board();
        for (Cell c : b.getField()) {
            if (c.getRow() == 4 && c.getColumn() == 4)
                c.setLevel(2);
            else if (c.getRow() == 4 && c.getColumn() == 3)
                c.setLevel(3);
            else if (c.getRow() == 3 && c.getColumn() == 4)
                c.setLevel(3);
            else if (c.getRow() == 3 && c.getColumn() == 3)
                c.setLevel(1);
        }
        ArrayList<Cell> ret = p.get(0).getCard().activeBlock(null, null, null, null);
        assertEquals(ret.size(), 0);
    }

}