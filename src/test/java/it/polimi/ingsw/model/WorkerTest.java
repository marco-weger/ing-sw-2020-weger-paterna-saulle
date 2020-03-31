package it.polimi.ingsw.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class WorkerTest {
    // move
    @Test
    public void move(){
        Worker w = new Worker(0,0);
        w.move(3,4);
        assertEquals(w.getRow(),3);
        assertEquals(w.getColumn(),4);
    }

    // getLevel
    @Test
    public void getLevel_boardNull()
    {
        assertEquals(new Worker(0,0).getLevel(null),-1);
    }
    @Test
    public void getLevel()
    {
        Board b = new Board();
        for(Cell c:b.getField())
            if(c.getRow() == 0 && c.getColumn() == 0)
                c.setLevel(1);
        assertEquals(new Worker(0,0).getLevel(b),1);
    }

    @Test
    public void outOfTheBoard(){
        Board b = new Board();
        Worker w = new Worker(0,0);
        w.setColumn(10);
        w.setRow(10);
        assertFalse(w.getRow()>4||w.getRow()<0||w.getColumn()<0||w.getColumn()>4);
        w.setRow(3);
        w.setColumn(3);
        for(Cell c: b.getField()){
            if(c.getRow()==w.getRow()&&c.getColumn()==w.getColumn()){
                c.setLevel(-1);
                assertFalse(w.getLevel(b)<0);
                c.setLevel(5);
                assertFalse(w.getLevel(b)>4);
            }
        }

    }

   /* @Test
    public void activeWorker(){
        Match m = new Match();
        Player p = new Player("player1", CardName.PAN,null,null);
        m.getCurrentPlayer().equals(p);
        p.setCurrentWorker(1);
        Worker w = new Worker(p.getCurrentWorker().getRow(),p.getCurrentWorker().getColumn());
        //it should be done after the implementation of the controller, the test on "active" for acknowledge
        //that the Worker is actually getting activated by the Player.
        w.setActive(true);
        assertSame(p.getCurrentWorker(), w); //&& w.isActive()==true && p.getWorker1()==w);

    }*/

}
