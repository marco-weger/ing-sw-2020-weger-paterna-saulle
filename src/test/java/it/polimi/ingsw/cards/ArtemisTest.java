package it.polimi.ingsw.cards;

import it.polimi.ingsw.model.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ArtemisTest {

    //checkMove
    @Test
    public void checkMove_nullAndZeros() {
        Card c = FactoryCard.getCard(CardName.ARTEMIS);
        assertNotNull(c);
        List<Player> p = new ArrayList<>();
        p.add(new Player("player1", CardName.ARTEMIS, new Worker(0, 0), new Worker(0, 0)));
        p.add(new Player("player2", CardName.HEPHASTUS, new Worker(0, 0), new Worker(0, 0)));
        p.add(new Player("player3", CardName.DEMETER, new Worker(0, 0), new Worker(0, 0)));
        List<Cell> moving = c.checkMove(p, null);
        assertEquals(moving.size(), 0);
        moving = c.checkMove(null, new Board());
        assertEquals(0, moving.size());

    }

    @Test
    public void checkMove_firstRingFailed() {
        List<Player> p = new ArrayList<>();
        p.add(new Player("player1", CardName.ARTEMIS, new Worker(1, 1), new Worker(0, 0)));
        p.add(new Player("player2", CardName.APOLLO, new Worker(2, 0), new Worker(0, 1)));
        p.add(new Player("player3", CardName.ATLAS, new Worker(2, 2), new Worker(0, 2)));
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
        List<Player> p = new ArrayList<>();
        p.add(new Player("player1",CardName.ARTEMIS,new Worker(1,1),new Worker(0,0)));
        p.add(new Player("player2",CardName.APOLLO,new Worker(2,0),new Worker(0,1)));
        p.add(new Player("player3",CardName.ATLAS,new Worker(2,2),new Worker(0,2)));
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
        List<Player> p = new ArrayList<>();
        p.add(new Player("player1",CardName.ARTEMIS,new Worker(1,1),new Worker(0,0)));
        p.add(new Player("player2",CardName.APOLLO,new Worker(2,0),new Worker(0,1)));
        p.add(new Player("player3",CardName.ATLAS,new Worker(1,2),new Worker(0,2)));
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
        assertEquals(2,val.size());

    }

    @Test
    public void checkMove_secondRingFailed1(){
        List<Player> p = new ArrayList<>();
        p.add(new Player("player1",CardName.ARTEMIS,new Worker(1,1),new Worker(0,0)));
        p.add(new Player("player2",CardName.APOLLO,new Worker(2,0),new Worker(0,1)));
        p.add(new Player("player3",CardName.ATLAS,new Worker(1,2),new Worker(0,2)));
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
        List<Player> p = new ArrayList<>();
        p.add(new Player("player1",CardName.ARTEMIS,new Worker(1,1),new Worker(0,0)));
        p.add(new Player("player2",CardName.APOLLO,new Worker(2,0),new Worker(0,1)));
        p.add(new Player("player3",CardName.ATLAS,new Worker(1,2),new Worker(0,2)));
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
        Card c = FactoryCard.getCard(CardName.ARTEMIS);
        assertNotNull(c);
        c.move(null,null,null);
    }
    @Test
    public void moveErrorWhitAbility()
    {
        List<Player> p = new ArrayList<>();
        p.add(new Player("player1",CardName.ARTEMIS,new Worker(1,1),new Worker(0,0)));
        p.add(new Player("player2",CardName.APOLLO,new Worker(2,0),new Worker(0,1)));
        p.add(new Player("player3",CardName.ATLAS,new Worker(1,2),new Worker(0,2)));
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
        assertEquals(p.get(0).getCurrentWorker().getRow(), 1);
        assertEquals(p.get(0).getCurrentWorker().getColumn(), 1);
    }
    @Test
    public void moveNoAbility()
    {
        List<Player> p = new ArrayList<>();
        p.add(new Player("player1",CardName.ARTEMIS,new Worker(1,1),new Worker(0,0)));
        p.add(new Player("player2",CardName.APOLLO,new Worker(2,0),new Worker(0,1)));
        p.add(new Player("player3",CardName.ATLAS,new Worker(1,2),new Worker(0,2)));
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
        assertEquals(p.get(0).getCurrentWorker().getRow(), 2);
        assertEquals(p.get(0).getCurrentWorker().getColumn(), 2);
    }
    @Test
    public void moveWhitAbility()
    {
        List<Player> p = new ArrayList<>();
        p.add(new Player("player1",CardName.ARTEMIS,new Worker(1,1),new Worker(0,0)));
        p.add(new Player("player2",CardName.APOLLO,new Worker(2,0),new Worker(0,1)));
        p.add(new Player("player3",CardName.ATLAS,new Worker(1,2),new Worker(0,2)));
        p.get(0).setCurrentWorker(1);
        p.get(0).getCard().setActive(true);
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
        Board b1 = new Board();
        b1.setField(p.get(0).getCard().checkMove(p,b));
        p.get(0).getCard().move(p,b1,b1.getCell(1,3));
        assertEquals(p.get(0).getCurrentWorker().getRow(), 1);
        assertEquals(p.get(0).getCurrentWorker().getColumn(), 3);
    }
}






