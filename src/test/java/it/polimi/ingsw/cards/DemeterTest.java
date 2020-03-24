package it.polimi.ingsw.cards;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Worker;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DemeterTest {
    // checkBuild
    @Test
    public void checkBuild_paramsNull()
    {
        Card c = FactoryCard.getCard(CardName.DEMETER);
        List<Player> p = new ArrayList<>();
        p.add(new Player("player1",CardName.DEMETER,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player2",CardName.ARTEMIS,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player3",CardName.ATLAS,new Worker(0,0),new Worker(0,0)));
        assertNotNull(c);
        assertEquals(c.checkBuild(null,new Board()).size(),0);
        assertEquals(c.checkBuild(p,null).size(),0);
    }
    @Test
    public void checkBuild_noCurrentPlayerWorker()
    {
        List<Player> p = new ArrayList<>();
        p.add(new Player("player1",CardName.DEMETER,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player2",CardName.ARTEMIS,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player3",CardName.ATLAS,new Worker(0,0),new Worker(0,0)));
        assertEquals(p.get(0).getCard().checkBuild(p,new Board()).size(),0);
    }
    @Test
    public void checkBuild_noActive()
    {
        List<Player> p = new ArrayList<>();
        p.add(new Player("player1",CardName.DEMETER,new Worker(2,3),new Worker(0,0)));
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
                c.setLevel(4);
        }
        assertNotNull(p);
        assertNotNull(b);
        p.get(0).getCard().setActive(false);
        List<Cell> ret = p.get(0).getCard().checkBuild(p,b);
        assertEquals(ret.size(),3);
        for(Cell c:ret)
            assertTrue(c.getRow() == 1 && c.getColumn() == 2 || c.getRow() == 1 && c.getColumn() == 3 || c.getRow() == 1 && c.getColumn() == 4);
    }
    @Test
    public void checkBuild_active()
    {
        List<Player> p = new ArrayList<>();
        p.add(new Player("player1",CardName.DEMETER,new Worker(2,3),new Worker(0,0)));
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
                c.setLevel(4);
        }
        assertNotNull(p);
        assertNotNull(b);
        p.get(0).getCard().setActive(false);
        List<Cell> ret = p.get(0).getCard().checkBuild(p,b);
        assertEquals(ret.size(),3);
        for(Cell c:ret)
            assertTrue(c.getRow() == 1 && c.getColumn() == 2 || c.getRow() == 1 && c.getColumn() == 3 || c.getRow() == 1 && c.getColumn() == 4);
        p.get(0).getCard().build(p,b,b.getCell(1,2));
        p.get(0).getCard().setActive(true);
        ret = p.get(0).getCard().checkBuild(p,b);
        assertEquals(ret.size(),2);
        for(Cell c:ret)
            assertTrue(c.getRow() == 1 && c.getColumn() == 3 || c.getRow() == 1 && c.getColumn() == 4);
    }
    // build
    @Test
    public void build_null()
    {
        Card c = FactoryCard.getCard(CardName.DEMETER);
        assertNotNull(c);
        c.build(null,null,null);
    }
    @Test
    public void build_error()
    {
        List<Player> p = new ArrayList<>();
        p.add(new Player("player1",CardName.DEMETER,new Worker(2,3),new Worker(0,0)));
        p.add(new Player("player2",CardName.ARTEMIS,new Worker(3,4),new Worker(0,1)));
        p.add(new Player("player3",CardName.ATLAS,new Worker(2,2),new Worker(0,2)));
        p.get(0).setCurrentWorker(1);
        p.get(0).getCard().build(p,new Board(),new Cell(3,4,0));
        assertEquals(p.get(0).getCurrentWorker().getRow(), 2);
        assertEquals(p.get(0).getCurrentWorker().getColumn(), 3);
    }
    @Test
    public void build()
    {
        List<Player> p = new ArrayList<>();
        p.add(new Player("player1",CardName.DEMETER,new Worker(2,3),new Worker(0,0)));
        p.add(new Player("player2",CardName.ARTEMIS,new Worker(4,0),new Worker(0,1)));
        p.add(new Player("player3",CardName.ATLAS,new Worker(2,2),new Worker(0,2)));
        p.get(0).setCurrentWorker(1);
        Board b = new Board();
        for(Cell cell:b.getField()){
            if(cell.getRow() == 3 & cell.getColumn() == 4){
                p.get(0).getCard().build(p,b,cell);
                assertEquals(cell.getLevel(), 1);
                p.get(0).getCard().setActive(true);
                p.get(0).getCard().build(p,b,cell);
                assertEquals(cell.getLevel(), 1);
                p.get(0).getCard().setActive(false);
                p.get(0).getCard().build(p,b,cell);
                assertEquals(cell.getLevel(), 2);
            }
        }
    }
}
