package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Worker;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class MinotaurTest {
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
        p.get(0).setCard(CardName.MINOTAUR,null);
        p.get(1).setCard(CardName.ARTEMIS,null);
        p.get(2).setCard(CardName.ATLAS,null);
    }
    // checkMove
    @Test
    public void checkMove_paramsNull()
    {
        initialize();
        List<Cell> ret = p.get(0).getCard().checkMove(p,null);
        assertEquals(0,ret.size());
        ret = p.get(0).getCard().checkMove(null,new Board());
        assertEquals(0, ret.size());
    }
    @Test
    public void checkMove_noCurrentPlayerWorker()
    {
        initialize();
        assertEquals(0,p.get(0).getCard().checkMove(p,new Board()).size());
        assertEquals(0,p.get(0).getCard().checkMove(p,new Board()).size());
    }
    @Test
    public void checkMove()
    {
        initialize();
        p.get(0).setWorker1(new Worker(2,2));
        p.get(0).setWorker2(new Worker(2,1));
        p.get(1).setWorker1(new Worker(2,3));
        p.get(1).setWorker2(new Worker(1,2));
        p.get(2).setWorker1(new Worker(0,2));
        p.get(2).setWorker2(new Worker(4,4));
        p.get(0).setCurrentWorker(1);
        Board b = new Board();
        for(Cell c:b.getField()){
            if(c.getRow() == 3)
                c.setLevel(3);
        }
        assertNotNull(p);
        assertNotNull(b);
        List<Cell> ret = p.get(0).getCard().checkMove(p,b);

        assertEquals(3,ret.size());
        for(Cell c:ret)
            assertTrue((c.getRow() == 1 && c.getColumn() == 1) || (c.getRow() == 1 && c.getColumn() == 3) || (c.getRow() == 2 && c.getColumn() == 3));
    }
    // move
    @Test
    public void move_null()
    {
        initialize();
        p.get(0).setWorker1(new Worker(2,3));
        p.get(0).setWorker2(new Worker(0,0));
        p.get(1).setWorker1(new Worker(4,0));
        p.get(1).setWorker2(new Worker(0,1));
        p.get(2).setWorker1(new Worker(2,2));
        p.get(2).setWorker2(new Worker(0,2));
        p.get(0).setCurrentWorker(1);
        p.get(0).getCard().move(null,null,null);
    }
    @Test
    public void move_error()
    {
        initialize();
        p.get(0).setWorker1(new Worker(2,3));
        p.get(0).setWorker2(new Worker(0,0));
        p.get(1).setWorker1(new Worker(3,4));
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
            else if(c.getRow() == 2 && c.getColumn() == 2)
                c.setLevel(4);
        }
        p.get(0).getCard().move(p,b,b.getCell(1,4));
        assertEquals(3,p.get(0).getCurrentWorker().getRow());
        assertEquals(3,p.get(0).getCurrentWorker().getColumn());
    }
    @Test
    public void move_noAbility()
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
                c.setLevel(1);
            else if(c.getRow() == 2 && c.getColumn() == 2)
                c.setLevel(4);
        }
        for(Cell cell:b.getField())
            if(cell.getRow() == 3 & cell.getColumn() == 4)
                p.get(0).getCard().move(p,b,cell);
        assertEquals(3,p.get(0).getCurrentWorker().getRow());
        assertEquals(4,p.get(0).getCurrentWorker().getColumn());
    }
    @Test
    public void move_abilityWorker1()
    {
        initialize();
        p.get(0).setWorker1(new Worker(2,2));
        p.get(0).setWorker2(new Worker(2,1));
        p.get(1).setWorker1(new Worker(2,3));
        p.get(1).setWorker2(new Worker(1,2));
        p.get(2).setWorker1(new Worker(4,3));
        p.get(2).setWorker2(new Worker(4,4));
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
        assertEquals(2,p.get(0).getWorker1().getRow());
        assertEquals(3,p.get(0).getWorker1().getColumn());
        assertEquals(3,p.get(1).getWorker1().getRow());
        assertEquals(4,p.get(1).getWorker1().getColumn());
    }
    @Test
    public void move_abilityWorker2()
    {
        initialize();
        p.get(0).setWorker1(new Worker(2,2));
        p.get(0).setWorker2(new Worker(2,1));
        p.get(1).setWorker1(new Worker(2,3));
        p.get(1).setWorker2(new Worker(1,2));
        p.get(0).setCurrentWorker(1);
        Board b = new Board();
        for(Cell c:b.getField()){
            if(c.getRow() == 3)
                c.setLevel(3);
        }
        assertNotNull(p);
        assertNotNull(b);
        p.get(0).setCurrentWorker(0);

        List<Cell> available = p.get(0).getCard().checkMove(p,b);
        assertTrue(available.contains(b.getCell(1,2)));
        p.get(0).getCard().move(p,b,b.getCell(1,2));
        assertEquals(1,p.get(0).getWorker1().getRow());
        assertEquals(2,p.get(0).getWorker1().getColumn());
        assertEquals(0,p.get(1).getWorker2().getRow());
        assertEquals(2,p.get(1).getWorker2().getColumn());
    }
}
