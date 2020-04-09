package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Worker;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class DemeterTest {
    ArrayList<Player> p = new ArrayList<>(Arrays.asList(
            new Player("player1","",null),
            new Player("player2","",null),
            new Player("player3","",null)
    ));

    public void initialize(){
        for(Player player:p){
            player.setWorker1(new Worker(0,0));
            player.setWorker2(new Worker(0,0));
        }
        p.get(0).setCard(CardName.DEMETER,null);
        p.get(1).setCard(CardName.ARTEMIS,null);
        p.get(2).setCard(CardName.ATLAS,null);
    }
    // checkBuild
    @Test
    public void checkBuild_paramsNull()
    {
        initialize();
        assertEquals(p.get(0).getCard().checkBuild(null,new Board()).size(),0);
        assertEquals(p.get(0).getCard().checkBuild(p,null).size(),0);
    }
    @Test
    public void checkBuild_noCurrentPlayerWorker()
    {
        initialize();
        assertEquals(p.get(0).getCard().checkBuild(p,new Board()).size(),0);
    }
    @Test
    public void checkBuild_noActive()
    {
        initialize();
        p.get(0).setWorker1(new Worker(2,3));
        p.get(0).setWorker2(new Worker(0,0));
        p.get(1).setWorker1(new Worker(4,0));
        p.get(1).setWorker2(new Worker(0,1));
        p.get(2).setWorker1(new Worker(2,2));
        p.get(2).setWorker2(new Worker(0,2));
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
        ArrayList<Cell> ret = p.get(0).getCard().checkBuild(p,b);
        assertEquals(ret.size(),3);
        for(Cell c:ret)
            assertTrue(c.getRow() == 1 && c.getColumn() == 2 || c.getRow() == 1 && c.getColumn() == 3 || c.getRow() == 1 && c.getColumn() == 4);
    }
    @Test
    public void checkBuild_active()
    {
        initialize();
        p.get(0).setWorker1(new Worker(2,3));
        p.get(0).setWorker2(new Worker(0,0));
        p.get(1).setWorker1(new Worker(4,0));
        p.get(1).setWorker2(new Worker(0,1));
        p.get(2).setWorker1(new Worker(2,2));
        p.get(2).setWorker2(new Worker(0,2));
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
        ArrayList<Cell> ret = p.get(0).getCard().checkBuild(p,b);
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
        Card c = FactoryCard.getCard(CardName.DEMETER,null);
        assertNotNull(c);
        c.build(null,null,null);
    }
    @Test
    public void build_error()
    {
        initialize();
        p.get(0).setWorker1(new Worker(2,3));
        p.get(0).setWorker2(new Worker(0,0));
        p.get(1).setWorker1(new Worker(3,4));
        p.get(1).setWorker2(new Worker(0,1));
        p.get(2).setWorker1(new Worker(2,2));
        p.get(2).setWorker2(new Worker(0,2));
        p.get(0).setCurrentWorker(1);
        p.get(0).getCard().build(p,new Board(),new Cell(3,4,0));
        assertEquals(p.get(0).getCurrentWorker().getRow(), 2);
        assertEquals(p.get(0).getCurrentWorker().getColumn(), 3);
    }
    @Test
    public void build()
    {
        initialize();
        p.get(0).setWorker1(new Worker(2,3));
        p.get(0).setWorker2(new Worker(0,0));
        p.get(1).setWorker1(new Worker(4,0));
        p.get(1).setWorker2(new Worker(0,1));
        p.get(2).setWorker1(new Worker(2,2));
        p.get(2).setWorker2(new Worker(0,2));
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
