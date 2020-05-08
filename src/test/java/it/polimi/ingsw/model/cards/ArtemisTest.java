package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.commons.Status;
import it.polimi.ingsw.model.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ArtemisTest {
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
        p.get(0).setCard(CardName.ARTEMIS,null);
        p.get(1).setCard(CardName.HEPHAESTUS,null);
        p.get(2).setCard(CardName.DEMETER,null);
    }
    //checkMove
    @Test
    public void checkMove_nullAndZeros() {
        initialize();
        List<Cell> moving = p.get(0).getCard().checkMove(p, null);
        assertEquals(0,moving.size());
        moving = p.get(0).getCard().checkMove(null, new Board());
        assertEquals(0, moving.size());
    }

    @Test
    public void checkMove_firstRingFailed() {
        initialize();
        p.get(0).setWorker1(new Worker(1,1));
        p.get(0).setWorker2(new Worker(0,0));
        p.get(1).setWorker1(new Worker(2,0));
        p.get(1).setWorker2(new Worker(0,1));
        p.get(2).setWorker1(new Worker(2,2));
        p.get(2).setWorker2(new Worker(0,2));
        p.get(0).setCurrentWorker(1);
        Board b = new Board();
        assertNotNull(p);
        assertNotNull(b);
        for (Cell c : b.getField()) {
            if (c.getRow() == 1 && c.getColumn() == 2)
                c.setLevel(4);
            else if (c.getRow() == 2 && c.getColumn() == 1)
                c.setLevel(2);
            else if (c.getRow() == 1 && c.getColumn() == 0)
                c.setLevel(3);
        }
        List<Cell> moving = p.get(0).getCard().checkMove(p, b);
        assertEquals(0, moving.size());
        //trying changing the value in the table, expecting not to change in "moving"
        Cell c1 = b.getCell(1, 2);
        c1.setLevel(0);
        assertEquals(0, moving.size());
    }

    @Test
    public void checkMove_firstRingSuccess()
    {
        initialize();
        p.get(0).setWorker1(new Worker(1,1));
        p.get(0).setWorker2(new Worker(0,0));
        p.get(1).setWorker1(new Worker(2,0));
        p.get(1).setWorker2(new Worker(0,1));
        p.get(2).setWorker1(new Worker(2,2));
        p.get(2).setWorker2(new Worker(0,2));
        p.get(0).setCurrentWorker(1);
        p.get(0).getCard().setActive(false);
        Board b = new Board();
        assertNotNull(p);
        assertNotNull(b);
        for(Cell c:b.getField()){
            if(c.getRow() == 1 && c.getColumn() == 0)
                c.setLevel(4);
            else if(c.getRow() == 2 && c.getColumn() == 1)
                c.setLevel(4);
            else if(c.getRow() == 1 && c.getColumn() == 2)
                c.setLevel(0);
        }
        List<Cell> val = p.get(0).getCard().checkMove(p,b);
        assertEquals(1,val.size());
    }

    @Test
    public void checkMove_secondRingSuccess(){
        initialize();
        p.get(0).setWorker1(new Worker(1,1));
        p.get(0).setWorker2(new Worker(0,0));
        p.get(1).setWorker1(new Worker(2,0));
        p.get(1).setWorker2(new Worker(0,1));
        p.get(2).setWorker1(new Worker(2,2));
        p.get(2).setWorker2(new Worker(0,2));
        p.get(0).setCurrentWorker(1);
        p.get(0).getCard().setActive(true);
        Board b = new Board();
        assertNotNull(p);
        assertNotNull(b);
        for(Cell c:b.getField()){
            if(c.getRow() == 1 && c.getColumn() == 0)
                c.setLevel(4);
            else if(c.getRow() == 2 && c.getColumn() == 1)
                c.setLevel(4);
            else if(c.getRow() == 2 && c.getColumn() == 2)
                c.setLevel(0);
            else if(c.getRow() == 3 && c.getColumn() == 0)
                c.setLevel(4);
            else if(c.getRow() == 3 && c.getColumn() == 1)
                c.setLevel(4);
            else if(c.getRow() == 3 && c.getColumn() == 2)
                c.setLevel(4);
            else if(c.getRow() == 3 && c.getColumn() == 3)
                c.setLevel(4);
            else if(c.getRow() == 2 && c.getColumn() == 3)
                c.setLevel(4);
            else if(c.getRow() == 1 && c.getColumn() == 3)
                c.setLevel(0);
            else if(c.getRow() == 0 && c.getColumn() == 3)
                c.setLevel(4);
        }
        List<Cell> val = p.get(0).getCard().checkMove(p,b);
        //assertEquals(2,val.size());

    }

    @Test
    public void checkMove_secondRingFailed1(){
        initialize();
        p.get(0).setWorker1(new Worker(1,1));
        p.get(0).setWorker2(new Worker(0,0));
        p.get(1).setWorker1(new Worker(2,0));
        p.get(1).setWorker2(new Worker(0,1));
        p.get(2).setWorker1(new Worker(1,2));
        p.get(2).setWorker2(new Worker(0,2));
        p.get(0).setCurrentWorker(1);
        p.get(0).getCard().setActive(true);
        Board b = new Board();
        assertNotNull(p);
        assertNotNull(b);
        for(Cell c:b.getField()){
            if(c.getRow() == 1 && c.getColumn() == 0)
                c.setLevel(4);
            else if(c.getRow() == 2 && c.getColumn() == 1)
                c.setLevel(4);
            else if(c.getRow() == 2 && c.getColumn() == 2)
                c.setLevel(0);
            else if(c.getRow() == 3 && c.getColumn() == 0)
                c.setLevel(4);
            else if(c.getRow() == 3 && c.getColumn() == 1)
                c.setLevel(4);
            else if(c.getRow() == 3 && c.getColumn() == 2)
                c.setLevel(4);
            else if(c.getRow() == 3 && c.getColumn() == 3)
                c.setLevel(4);
            else if(c.getRow() == 2 && c.getColumn() == 3)
                c.setLevel(4);
            else if(c.getRow() == 1 && c.getColumn() == 3)
                c.setLevel(4);
            else if(c.getRow() == 0 && c.getColumn() == 3)
                c.setLevel(4);
        }
        List<Cell> val = p.get(0).getCard().checkMove(p,b);
        assertEquals(1,val.size());

    }

    @Test
    public void checkMove_secondRingFailed2(){
        initialize();
        p.get(0).setWorker1(new Worker(1,1));
        p.get(0).setWorker2(new Worker(0,0));
        p.get(1).setWorker1(new Worker(2,0));
        p.get(1).setWorker2(new Worker(0,1));
        p.get(2).setWorker1(new Worker(1,2));
        p.get(2).setWorker2(new Worker(0,2));
        p.get(0).setCurrentWorker(1);
        p.get(0).getCard().setActive(true);
        Board b = new Board();
        assertNotNull(p);
        assertNotNull(b);
        for(Cell c:b.getField()){
            if(c.getRow() == 1 && c.getColumn() == 0)
                c.setLevel(4);
            else if(c.getRow() == 2 && c.getColumn() == 1)
                c.setLevel(4);
            else if(c.getRow() == 2 && c.getColumn() == 2)
                c.setLevel(4);
            else if(c.getRow() == 3 && c.getColumn() == 0)
                c.setLevel(0);
            else if(c.getRow() == 3 && c.getColumn() == 1)
                c.setLevel(0);
            else if(c.getRow() == 3 && c.getColumn() == 2)
                c.setLevel(0);
            else if(c.getRow() == 3 && c.getColumn() == 3)
                c.setLevel(0);
            else if(c.getRow() == 2 && c.getColumn() == 3)
                c.setLevel(0);
            else if(c.getRow() == 1 && c.getColumn() == 3)
                c.setLevel(0);
            else if(c.getRow() == 0 && c.getColumn() == 3)
                c.setLevel(0);
        }
        List<Cell> val = p.get(0).getCard().checkMove(p,b);
        assertEquals(0,val.size());

    }

    // move
    @Test
    public void moveNull()
    {
        Card c = FactoryCard.getCard(CardName.ARTEMIS,null);
        assertNotNull(c);
        c.move(null,null,null);
    }
    @Test
    public void moveErrorWhitAbility()
    {
        initialize();
        p.get(0).setWorker1(new Worker(1,1));
        p.get(0).setWorker2(new Worker(0,0));
        p.get(1).setWorker1(new Worker(2,0));
        p.get(1).setWorker2(new Worker(0,1));
        p.get(2).setWorker1(new Worker(1,2));
        p.get(2).setWorker2(new Worker(0,2));
        p.get(0).setCurrentWorker(1);
        Board b = new Board();
        for(Cell c:b.getField()){
            if(c.getRow() == 1 && c.getColumn() == 0)
                c.setLevel(4);
            else if(c.getRow() == 2 && c.getColumn() == 1)
                c.setLevel(4);
            else if(c.getRow() == 2 && c.getColumn() == 2)
                c.setLevel(0);
            else if(c.getRow() == 3 && c.getColumn() == 0)
                c.setLevel(4);
            else if(c.getRow() == 3 && c.getColumn() == 1)
                c.setLevel(4);
            else if(c.getRow() == 3 && c.getColumn() == 2)
                c.setLevel(4);
            else if(c.getRow() == 3 && c.getColumn() == 3)
                c.setLevel(4);
            else if(c.getRow() == 2 && c.getColumn() == 3)
                c.setLevel(4);
            else if(c.getRow() == 1 && c.getColumn() == 3)
                c.setLevel(4);
            else if(c.getRow() == 0 && c.getColumn() == 3)
                c.setLevel(4);
        }
        Board b1 = new Board();
        b1.setField(p.get(0).getCard().checkMove(p,b));
        p.get(0).getCard().move(p,b1,b1.getCell(1,3));
        assertEquals(1,p.get(0).getCurrentWorker().getRow());
        assertEquals(1,p.get(0).getCurrentWorker().getColumn());
    }
    @Test
    public void moveNoAbility()
    {
        initialize();
        p.get(0).setWorker1(new Worker(1,1));
        p.get(0).setWorker2(new Worker(0,0));
        p.get(1).setWorker1(new Worker(2,0));
        p.get(1).setWorker2(new Worker(0,1));
        p.get(2).setWorker1(new Worker(1,2));
        p.get(2).setWorker2(new Worker(0,2));
        p.get(0).setCurrentWorker(1);
        p.get(0).getCard().setActive(false);
        Board b = new Board();
        for(Cell c:b.getField()){
            if(c.getRow() == 1 && c.getColumn() == 0)
                c.setLevel(4);
            else if(c.getRow() == 2 && c.getColumn() == 1)
                c.setLevel(4);
            else if(c.getRow() == 2 && c.getColumn() == 2)
                c.setLevel(0);
        }
        Board b1 = new Board();
        b1.setField(p.get(0).getCard().checkMove(p,b));
        p.get(0).getCard().move(p,b1,b1.getCell(2,2));
        assertEquals(2,p.get(0).getCurrentWorker().getRow());
        assertEquals(2,p.get(0).getCurrentWorker().getColumn());
    }
    @Test
    public void moveWhitAbility()
    {
        initialize();
        p.get(0).setWorker1(new Worker(1,1));
        p.get(0).setWorker2(new Worker(0,0));
        p.get(1).setWorker1(new Worker(2,0));
        p.get(1).setWorker2(new Worker(0,1));
        p.get(2).setWorker1(new Worker(1,2));
        p.get(2).setWorker2(new Worker(0,2));
        p.get(0).setCurrentWorker(1);
        p.get(0).getCard().setActive(false);
        Board b = new Board();
        for(Cell c:b.getField()){
            if(c.getRow() == 1 && c.getColumn() == 0)
                c.setLevel(4);
            else if(c.getRow() == 2 && c.getColumn() == 1)
                c.setLevel(4);
            else if(c.getRow() == 2 && c.getColumn() == 2)
                c.setLevel(0);
            else if(c.getRow() == 3 && c.getColumn() == 0)
                c.setLevel(4);
            else if(c.getRow() == 3 && c.getColumn() == 1)
                c.setLevel(4);
            else if(c.getRow() == 3 && c.getColumn() == 2)
                c.setLevel(4);
            else if(c.getRow() == 3 && c.getColumn() == 3)
                c.setLevel(4);
            else if(c.getRow() == 2 && c.getColumn() == 3)
                c.setLevel(4);
            else if(c.getRow() == 1 && c.getColumn() == 3)
                c.setLevel(0);
            else if(c.getRow() == 0 && c.getColumn() == 3)
                c.setLevel(4);
        }
        assertEquals(1,p.get(0).getCard().checkMove(p,b).size());
        p.get(0).getCard().move(p,b, b.getCell(2,2));
        assertTrue(p.get(0).getCard().activable(p,b));
        p.get(0).getCard().setActive(true);
        assertEquals(1,p.get(0).getCard().checkMove(p,b).size());

        //p.get(0).getCard().move(p,b1,b1.getCell(1,3));
        //assertEquals(p.get(0).getCurrentWorker().getRow(), 1);
        //ssertEquals(p.get(0).getCurrentWorker().getColumn(), 3);
    }

    @Test
    public void athenanextstatus(){
        initialize();
        p.get(0).getCard().setActive(false);
        assertEquals(Status.CHOSEN,p.get(0).getCard().getNextStatus(Status.START));
        p.get(0).getCard().setActive(true);
        assertEquals(Status.QUESTION_M,p.get(0).getCard().getNextStatus(Status.MOVED));
        assertEquals(Status.QUESTION_B,p.get(0).getCard().getNextStatus(Status.QUESTION_M));
        assertFalse(p.get(0).getCard().isActive());
        assertEquals(Status.BUILT,p.get(0).getCard().getNextStatus(Status.QUESTION_B));

    }
}






