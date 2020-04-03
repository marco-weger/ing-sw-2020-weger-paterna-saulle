package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Worker;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class MinotaurTest {
    // checkMove
    @Test
    public void checkMove_paramsNull()
    {
        ArrayList<Player> p = new ArrayList<>();
        p.add(new Player("player1",CardName.MINOTAUR,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player2",CardName.ARTEMIS,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player3",CardName.ATLAS,new Worker(0,0),new Worker(0,0)));
        ArrayList<Cell> ret = p.get(0).getCard().checkMove(p,null);
        assertEquals(ret.size(),0);
        ret = p.get(0).getCard().checkMove(null,new Board());
        assertEquals(0, ret.size());
    }
    @Test
    public void checkMove_noCurrentPlayerWorker()
    {
        ArrayList<Player> p = new ArrayList<>();
        p.add(new Player("player1",CardName.MINOTAUR,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player2",CardName.ARTEMIS,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player3",CardName.ATLAS,new Worker(0,0),new Worker(0,0)));
        assertEquals(p.get(0).getCard().checkMove(p,new Board()).size(),0);
        assertEquals(p.get(0).getCard().checkMove(p,new Board()).size(),0);
    }
    @Test
    public void checkMove()
    {
        ArrayList<Player> p = new ArrayList<>();
        p.add(new Player("player1",CardName.MINOTAUR,new Worker(2,2),new Worker(2,1)));
        p.add(new Player("player2",CardName.ARTEMIS,new Worker(2,3),new Worker(1,2)));
        p.add(new Player("player3",CardName.ATLAS,new Worker(0,2),new Worker(4,4)));
        p.get(0).setCurrentWorker(1);
        Board b = new Board();
        for(Cell c:b.getField()){
            if(c.getRow() == 3)
                c.setLevel(3);
        }
        assertNotNull(p);
        assertNotNull(b);
        ArrayList<Cell> ret = p.get(0).getCard().checkMove(p,b);

        assertEquals(ret.size(),3);
        for(Cell c:ret)
            assertTrue((c.getRow() == 1 && c.getColumn() == 1) || (c.getRow() == 1 && c.getColumn() == 3) || (c.getRow() == 2 && c.getColumn() == 3));
    }
    // move
    @Test
    public void move_null()
    {
        ArrayList<Player> p = new ArrayList<>();
        p.add(new Player("player1",CardName.MINOTAUR,new Worker(2,3),new Worker(0,0)));
        p.add(new Player("player2",CardName.ARTEMIS,new Worker(4,0),new Worker(0,1)));
        p.add(new Player("player3",CardName.ATLAS,new Worker(2,2),new Worker(0,2)));
        p.get(0).setCurrentWorker(1);
        p.get(0).getCard().move(null,null,null);
    }
    @Test
    public void move_error()
    {
        ArrayList<Player> p = new ArrayList<>();
        p.add(new Player("player1",CardName.MINOTAUR,new Worker(2,3),new Worker(0,0)));
        p.add(new Player("player2",CardName.ARTEMIS,new Worker(3,4),new Worker(0,1)));
        p.add(new Player("player3",CardName.ATLAS,new Worker(2,2),new Worker(0,2)));
        p.get(0).setCurrentWorker(1);
        Board b = new Board();
        for(Cell c:b.getField()){
            if(c.getRow() == 1 && c.getColumn() == 2)
                c.setLevel(1);
            else if(c.getRow() == 1 && c.getColumn() == 3)
                c.setLevel(2);
            else if(c.getRow() == 1 && c.getColumn() == 4)
                c.setLevel(3);
            else if(c.getRow() == 2 && c.getColumn() == 4)
                c.setLevel(4);
            else if(c.getRow() == 3 && c.getColumn() == 2)
                c.setLevel(4);
            else if(c.getRow() == 3 && c.getColumn() == 3)
                c.setLevel(4);
            else if(c.getRow() == 3 && c.getColumn() == 4)
                c.setLevel(4);
            else if(c.getRow() == 2 && c.getColumn() == 2)
                c.setLevel(4);
        }
        p.get(0).getCard().move(p,b,b.getCell(1,4));
        assertEquals(p.get(0).getCurrentWorker().getRow(), 2);
        assertEquals(p.get(0).getCurrentWorker().getColumn(), 3);
    }
    @Test
    public void move_noAbility()
    {
        ArrayList<Player> p = new ArrayList<>();
        p.add(new Player("player1",CardName.MINOTAUR,new Worker(2,3),new Worker(0,0)));
        p.add(new Player("player2",CardName.ARTEMIS,new Worker(4,0),new Worker(0,1)));
        p.add(new Player("player3",CardName.ATLAS,new Worker(2,2),new Worker(0,2)));
        p.get(0).setCurrentWorker(1);
        Board b = new Board();
        for(Cell c:b.getField()){
            if(c.getRow() == 1 && c.getColumn() == 2)
                c.setLevel(1);
            else if(c.getRow() == 1 && c.getColumn() == 3)
                c.setLevel(2);
            else if(c.getRow() == 1 && c.getColumn() == 4)
                c.setLevel(3);
            else if(c.getRow() == 2 && c.getColumn() == 4)
                c.setLevel(4);
            else if(c.getRow() == 3 && c.getColumn() == 2)
                c.setLevel(4);
            else if(c.getRow() == 3 && c.getColumn() == 3)
                c.setLevel(4);
            else if(c.getRow() == 3 && c.getColumn() == 4)
                c.setLevel(1);
            else if(c.getRow() == 2 && c.getColumn() == 2)
                c.setLevel(4);
        }
        for(Cell cell:b.getField())
            if(cell.getRow() == 3 & cell.getColumn() == 4)
                p.get(0).getCard().move(p,b,cell);
        assertEquals(p.get(0).getCurrentWorker().getRow(), 3);
        assertEquals(p.get(0).getCurrentWorker().getColumn(), 4);
    }
    @Test
    public void move_abilityWorker1()
    {
        ArrayList<Player> p = new ArrayList<>();
        p.add(new Player("player1",CardName.MINOTAUR,new Worker(2,2),new Worker(2,1)));
        p.add(new Player("player2",CardName.ARTEMIS,new Worker(2,3),new Worker(1,2)));
        p.add(new Player("player3",CardName.ATLAS,new Worker(4,3),new Worker(4,4)));
        p.get(0).setCurrentWorker(1);
        Board b = new Board();
        for(Cell c:b.getField()){
            if(c.getRow() == 3)
                c.setLevel(3);
        }
        assertNotNull(p);
        assertNotNull(b);
        p.get(0).setCurrentWorker(0);
        p.get(0).getCard().move(p,b,b.getCell(2,3));
        assertEquals(p.get(0).getWorker1().getRow(), 2);
        assertEquals(p.get(0).getWorker1().getColumn(), 3);
        assertEquals(p.get(1).getWorker1().getRow(), 2);
        assertEquals(p.get(1).getWorker1().getColumn(), 4);
    }
    @Test
    public void move_abilityWorker2()
    {
        ArrayList<Player> p = new ArrayList<>();
        p.add(new Player("player1",CardName.MINOTAUR,new Worker(2,2),new Worker(2,1)));
        p.add(new Player("player2",CardName.ARTEMIS,new Worker(2,3),new Worker(1,2)));
        p.get(0).setCurrentWorker(1);
        Board b = new Board();
        for(Cell c:b.getField()){
            if(c.getRow() == 3)
                c.setLevel(3);
        }
        assertNotNull(p);
        assertNotNull(b);
        p.get(0).setCurrentWorker(0);

        ArrayList<Cell> available = p.get(0).getCard().checkMove(p,b);
        assertTrue(available.contains(b.getCell(1,2)));
        p.get(0).getCard().move(p,b,b.getCell(1,2));
        assertEquals(p.get(0).getWorker1().getRow(), 1);
        assertEquals(p.get(0).getWorker1().getColumn(), 2);
        assertEquals(p.get(1).getWorker2().getRow(), 0);
        assertEquals(p.get(1).getWorker2().getColumn(), 2);
    }
}
