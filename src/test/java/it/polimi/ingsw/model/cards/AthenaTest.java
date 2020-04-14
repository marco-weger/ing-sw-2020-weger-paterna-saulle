package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.commons.Status;
import it.polimi.ingsw.model.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class AthenaTest {
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
        p.get(0).setCard(CardName.ATHENA,null);
        p.get(1).setCard(CardName.HEPHAESTUS,null);
        p.get(2).setCard(CardName.DEMETER,null);
    }

    @Test
    public void initializeTurn_turnoffpower() {
        initialize();
        p.get(0).getCard().setActive(true);
        p.get(0).getCard().initializeTurn();
        assertFalse(p.get(0).getCard().isActive());
    }


    @Test
    public void initializeTurn_NOpower() {
        initialize();
        p.get(0).getCard().setActive(false);
        p.get(0).getCard().initializeTurn();
        assertFalse(p.get(0).getCard().isActive());
    }

    @Test
    public void ActiveBlock() {
        initialize();
        p.get(0).setWorker1(new Worker(2,3));
        p.get(0).setWorker2(new Worker(0,0));
        p.get(1).setWorker1(new Worker(4,4));
        p.get(1).setWorker2(new Worker(0,1));
        p.get(2).setWorker1(new Worker(1,0));
        p.get(2).setWorker2(new Worker(0,2));
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
        initialize();
        p.get(0).setWorker1(new Worker(2,3));
        p.get(0).setWorker2(new Worker(0,0));
        p.get(1).setWorker1(new Worker(4,4));
        p.get(1).setWorker2(new Worker(0,1));
        p.get(2).setWorker1(new Worker(3,4));
        p.get(2).setWorker2(new Worker(0,2));
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
        initialize();
        p.get(0).setWorker1(new Worker(2,3));
        p.get(0).setWorker2(new Worker(0,0));
        p.get(1).setWorker1(new Worker(4,4));
        p.get(1).setWorker2(new Worker(0,1));
        p.get(2).setWorker1(new Worker(1,0));
        p.get(2).setWorker2(new Worker(0,2));
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