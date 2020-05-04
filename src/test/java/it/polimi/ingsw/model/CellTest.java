package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.CardName;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class CellTest {
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
    // setter
    @Test
    public void setter()
    {
        Cell c = new Cell(1,1,1);
        c.setLevel(1);
        //c.setColumn(1);
        //c.setRow(1);
        assertEquals(1,c.getRow());
        assertEquals(1,c.getColumn());
        assertEquals(1,c.getLevel());
    }
    // isOccupied
    @Test
    public void isOccupied_listNull()
    {
        assertFalse(new Cell(0,0,0).isOccupied(null));
    }
    @Test
    public void isCellInBoard_workerNull()
    {
        assertFalse(new Cell(0,0,0).isOccupied(p));
    }
    @Test
    public void isCellInBoard_true()
    {
        initialize();
        p.get(0).setWorker1(new Worker(2,4));
        p.get(0).setWorker2(new Worker(1,1));
        p.get(1).setWorker1(new Worker(3,4));
        p.get(1).setWorker2(new Worker(1,4));
        p.get(2).setWorker1(new Worker(4,4));
        p.get(2).setWorker2(new Worker(1,2));
        assertTrue(new Cell(1,4,0).isOccupied(p));
    }
    @Test
    public void isCellInBoard_false()
    {
        initialize();
        assertFalse(new Cell(1,4,0).isOccupied(p));
    }
}
