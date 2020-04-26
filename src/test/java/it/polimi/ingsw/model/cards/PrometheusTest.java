package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.commons.Status;
import it.polimi.ingsw.model.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class PrometheusTest {
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
        p.get(0).setCard(CardName.PROMETHEUS,null);
        p.get(1).setCard(CardName.ARTEMIS,null);
        p.get(2).setCard(CardName.ATLAS,null);
    }

    @Test
    public void checkMove_paramsNull()
    {
        initialize();
        ArrayList<Cell> ret = p.get(0).getCard().checkMove(p,null);
        assertEquals(ret.size(),0);
        ret = p.get(0).getCard().checkMove(null,new Board());
        assertEquals(0, ret.size());
    }

    @Test
    public void checkMove_isActive()
    {
        initialize();
        p.get(0).setWorker1(new Worker(0,0));
        p.get(0).setWorker2(new Worker(4,1));
        p.get(1).setWorker1(new Worker(2,3));
        p.get(1).setWorker2(new Worker(3,3));
        p.get(2).setWorker1(new Worker(4,4));
        p.get(2).setWorker2(new Worker(3,1));
        p.get(0).setCurrent(true);
        p.get(0).setCurrentWorker(1);
        p.get(0).getCard().setActive(true);
        Board b = new Board();
        for(Cell cell:b.getField()){
            if(cell.getRow() == 0 && cell.getColumn() == 0)
                cell.setLevel(1);
            else if(cell.getRow() == 0 && cell.getColumn() == 1)
                cell.setLevel(0);
            else if(cell.getRow() == 1 && cell.getColumn() == 1)
                cell.setLevel(2);
            else if(cell.getRow() == 1 && cell.getColumn() == 0)
                cell.setLevel(1);
        }
        assertNotNull(p);
        assertNotNull(b);
        ArrayList<Cell> ret = p.get(0).getCard().checkMove(p,b);
        assertEquals(2, ret.size());
        for(Cell c:ret)
            assertEquals(c.getRow()+c.getColumn(),1);
    }


    @Test
    public void checkMove_isActiveFalse()
    {
        initialize();
        p.get(0).setWorker1(new Worker(0,0));
        p.get(0).setWorker2(new Worker(4,1));
        p.get(1).setWorker1(new Worker(2,3));
        p.get(1).setWorker2(new Worker(3,3));
        p.get(2).setWorker1(new Worker(4,4));
        p.get(2).setWorker2(new Worker(3,1));
        p.get(0).setCurrentWorker(1);
        p.get(0).getCard().setActive(false);
        Board b = new Board();
        for(Cell cell:b.getField()){
            if(cell.getRow() == 0 && cell.getColumn() == 0)
                cell.setLevel(1);
            else if(cell.getRow() == 0 && cell.getColumn() == 1)
                cell.setLevel(1);
            else if(cell.getRow() == 1 && cell.getColumn() == 1)
                cell.setLevel(0);
            else if(cell.getRow() == 1 && cell.getColumn() == 0)
                cell.setLevel(1);
        }
        assertNotNull(p);
        assertNotNull(b);
        ArrayList<Cell> ret = p.get(0).getCard().checkMove(p,b);
        assertEquals(3, ret.size());

    }

    @Test
    public void setNextStatus_isActivetrue1()
    {
        initialize();
        p.get(0).setWorker1(new Worker(0,0));
        p.get(0).setWorker2(new Worker(4,1));
        p.get(0).getCard().setActive(true);
        assertEquals(Status.QUESTION_B, p.get(0).getCard().getNextStatus(Status.CHOSEN));

    }

    @Test
    public void setNextStatus_isActivetrue2()
    {
        initialize();
        p.get(0).setWorker1(new Worker(0,0));
        p.get(0).setWorker2(new Worker(4,1));
        p.get(0).getCard().setActive(true);
        assertEquals(Status.QUESTION_M, p.get(0).getCard().getNextStatus(Status.QUESTION_B));
    }

    @Test
    public void setNextStatus_isActive3OntoOff()
    {
        initialize();
        p.get(0).setWorker1(new Worker(0,0));
        p.get(0).setWorker2(new Worker(4,1));
        p.get(0).getCard().setActive(true);
        assertEquals(Status.QUESTION_M, p.get(0).getCard().getNextStatus(Status.QUESTION_B));
    }

    @Test
    public void setNextStatus_isActivefalse1()
    {
        initialize();
        p.get(0).setWorker1(new Worker(0,0));
        p.get(0).setWorker2(new Worker(4,1));
        p.get(0).getCard().setActive(false);
        assertEquals(Status.MOVED, p.get(0).getCard().getNextStatus(Status.QUESTION_M));
    }

    @Test
    public void setNextStatus_isActivefalse2()
    {
        initialize();
        p.get(0).setWorker1(new Worker(0,0));
        p.get(0).setWorker2(new Worker(4,1));
        p.get(0).getCard().setActive(false);
        assertEquals(Status.CHOSEN, p.get(0).getCard().getNextStatus(Status.START));
    }

    @Test
    public void setNextStatus_isNULL()
    {
        initialize();
        p.get(0).setWorker1(new Worker(0,0));
        p.get(0).setWorker2(new Worker(4,1));
        p.get(0).getCard().setActive(false);
        assertNull(p.get(0).getCard().getNextStatus(null));
    }

    @Test
    public void checkBuild_active(){
        initialize();
        p.get(0).setWorker1(new Worker(0,0));
        p.get(0).setWorker2(new Worker(4,1));
        p.get(0).setCurrentWorker(1);
        p.get(0).getCard().setActive(true);
        Board b = new Board();
        for(Cell cell:b.getField()){
            if(cell.getRow() == 0 && cell.getColumn() == 0)
                cell.setLevel(1);
            else if(cell.getRow() == 0 && cell.getColumn() == 1)
                cell.setLevel(3);
            else if(cell.getRow() == 1 && cell.getColumn() == 1)
                cell.setLevel(1);
            else if(cell.getRow() == 1 && cell.getColumn() == 0)
                cell.setLevel(3);
        }
        assertNotNull(p);
        assertNotNull(b);
        ArrayList<Cell> ret = p.get(0).getCard().checkBuild(p,b);
        assertEquals(2, ret.size());
        //for(Cell c:ret)
          //  assertEquals(c.getRow()+c.getColumn(),1);
    }

    @Test
    public void activable(){
        initialize();
        p.get(0).setWorker1(new Worker(0,0));
        p.get(0).setWorker2(new Worker(4,1));
        p.get(0).setCurrentWorker(1);
        p.get(0).getCard().setActive(true);
        Board b = new Board();
        for(Cell cell:b.getField()){
            if(cell.getRow() == 0 && cell.getColumn() == 0)
                cell.setLevel(1);
            else if(cell.getRow() == 0 && cell.getColumn() == 1)
                cell.setLevel(4);
            else if(cell.getRow() == 1 && cell.getColumn() == 1)
                cell.setLevel(1);
            else if(cell.getRow() == 1 && cell.getColumn() == 0)
                cell.setLevel(4);
        }
        assertNotNull(p);
        assertNotNull(b);
        assertFalse(p.get(0).getCard().activable(p,b));
        for(Cell cell:b.getField()){
            if(cell.getRow() == 0 && cell.getColumn() == 1)
                cell.setLevel(1);
            else if(cell.getRow() == 1 && cell.getColumn() == 0)
                cell.setLevel(3);
        }
        assertTrue(p.get(0).getCard().activable(p,b));
    }


    @Test
    public void activable2(){
        initialize();
        p.get(0).setWorker1(new Worker(0,0));
        p.get(0).setWorker2(new Worker(4,1));
        p.get(1).setWorker1(new Worker(0,2));
        p.get(1).setWorker2(new Worker(1,1));
        p.get(0).setCurrentWorker(1);
        p.get(0).getCard().setActive(true);
        Board b = new Board();
        for(Cell cell:b.getField()){
            if(cell.getRow() == 0 && cell.getColumn() == 0)
                cell.setLevel(0);
            else if(cell.getRow() == 0 && cell.getColumn() == 1)
                cell.setLevel(2);
            else if(cell.getRow() == 1 && cell.getColumn() == 1)
                cell.setLevel(0);
            else if(cell.getRow() == 1 && cell.getColumn() == 0)
                cell.setLevel(0);
        }
        assertNotNull(p);
        assertNotNull(b);
        assertTrue(p.get(0).getCard().activable(p,b));
        for(Cell cell:b.getField()){
            if(cell.getRow() == 0 && cell.getColumn() == 1)
                cell.setLevel(4);
        }
        assertFalse(p.get(0).getCard().activable(p,b));
    }
}