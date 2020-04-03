package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Worker;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ApolloTest {
    // checkMove
    @Test
    public void checkMove_paramsNull()
    {
        Card c = FactoryCard.getCard(CardName.APOLLO);
        ArrayList<Player> p = new ArrayList<>();
        p.add(new Player("player1",CardName.APOLLO,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player2",CardName.ARTEMIS,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player3",CardName.ATLAS,new Worker(0,0),new Worker(0,0)));
        assertNotNull(c);
        ArrayList<Cell> ret = c.checkMove(p,null);
        assertEquals(ret.size(),0);
        ret = c.checkMove(null,new Board());
        assertEquals(0, ret.size());
    }
    @Test
    public void checkMove_noCurrentPlayerWorker()
    {
        Card c = FactoryCard.getCard(CardName.PAN);
        ArrayList<Player> p = new ArrayList<>();
        p.add(new Player("player1",CardName.APOLLO,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player2",CardName.ARTEMIS,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player3",CardName.ATLAS,new Worker(0,0),new Worker(0,0)));
        assertNotNull(c);
        assertEquals(c.checkMove(p,new Board()).size(),0);
        assertEquals(c.checkMove(p,new Board()).size(),0);
    }
    @Test
    public void checkMove_swtichUnaviable()
    {
        ArrayList<Player> p = new ArrayList<>();
        p.add(new Player("player1",CardName.APOLLO,new Worker(2,3),new Worker(0,0)));
        p.add(new Player("player2",CardName.ARTEMIS,new Worker(4,0),new Worker(0,1)));
        p.add(new Player("player3",CardName.ATLAS,new Worker(2,2),new Worker(0,2)));
        p.get(0).setCurrentWorker(1);
        Board b = new Board();
        for(Cell c:b.getField()){
            if(c.getRow() == 1)
                c.setLevel(4);
            else if(c.getRow() == 3)
                c.setLevel(4);
            else if(c.getRow() == 2 && c.getColumn() == 1)
                c.setLevel(4);
        }
        assertNotNull(p);
        assertNotNull(b);
        ArrayList<Cell> ret = p.get(0).getCard().checkMove(p,b);
        //for(Cell c:ret)
        //    System.out.println(c.getRow() + " - " + c.getColumn());
        assertEquals(ret.size(),1);
        for(Cell c:ret)
            assertTrue(c.getRow() == 2 && c.getColumn()== 4);
    }
    @Test
    public void checkMove()
    {
        ArrayList<Player> p = new ArrayList<>();
        p.add(new Player("player1",CardName.APOLLO,new Worker(2,3),new Worker(0,0)));
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
        ArrayList<Cell> ret = p.get(0).getCard().checkMove(p,b);
        //for(Cell c:ret)
        //    System.out.println(c.getRow() + " - " + c.getColumn());
        assertEquals(ret.size(),2);
        for(Cell c:ret)
            assertTrue((c.getRow() == 1 && c.getColumn() == 2) || (c.getRow() == 2 && c.getColumn() == 2));
    }
    // move
    @Test
    public void move_null()
    {
        Card c = FactoryCard.getCard(CardName.PAN);
        assertNotNull(c);
        c.move(null,null,null);
    }
    @Test
    public void move_error()
    {
        ArrayList<Player> p = new ArrayList<>();
        p.add(new Player("player1",CardName.APOLLO,new Worker(2,3),new Worker(0,0)));
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
        p.add(new Player("player1",CardName.APOLLO,new Worker(2,3),new Worker(0,0)));
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
        Card c = FactoryCard.getCard(CardName.APOLLO);
        p.add(new Player("player1",CardName.APOLLO,new Worker(2,3),new Worker(0,0)));
        p.add(new Player("player2",CardName.ARTEMIS,new Worker(4,0),new Worker(0,1)));
        p.add(new Player("player3",CardName.ATLAS,new Worker(2,2),new Worker(0,2)));
        p.get(0).setCurrentWorker(1);
        Board b = new Board();
        assertNotNull(c);
        for(Cell cell:b.getField())
            if(cell.getRow() == 2 & cell.getColumn() == 2)
                c.move(p,b,cell);
        assertEquals(p.get(0).getCurrentWorker().getRow(), 2);
        assertEquals(p.get(0).getCurrentWorker().getColumn(), 2);
        assertEquals(p.get(2).getWorker1().getRow(), 2);
        assertEquals(p.get(2).getWorker1().getColumn(), 3);
    }
    @Test
    public void move_abilityWorker2()
    {
        ArrayList<Player> p = new ArrayList<>();
        p.add(new Player("player1",CardName.APOLLO,new Worker(0,0),new Worker(2,3)));
        p.add(new Player("player2",CardName.ARTEMIS,new Worker(0,1),new Worker(4,0)));
        p.add(new Player("player3",CardName.ATLAS,new Worker(0,2),new Worker(2,2)));
        p.get(0).setCurrentWorker(2);
        Board b = new Board();
        p.get(0).getCard().move(p,b,b.getCell(2,2));
        assertEquals(p.get(0).getCurrentWorker().getRow(), 2);
        assertEquals(p.get(0).getCurrentWorker().getColumn(), 2);
        assertEquals(p.get(2).getWorker2().getRow(), 2);
        assertEquals(p.get(2).getWorker2().getColumn(), 3);
    }
}
