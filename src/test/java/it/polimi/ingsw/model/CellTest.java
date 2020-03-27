package it.polimi.ingsw.model;

import it.polimi.ingsw.cards.CardName;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CellTest {
    // isOccupied
    @Test
    public void isOccupied_listNull()
    {
        assertFalse(new Cell(0,0,0).isOccupied(null));
    }
    @Test
    public void isCellInBoard_workerNull()
    {
        List<Player> p = new ArrayList<>();
        p.add(new Player("player1",CardName.APOLLO,null,null));
        p.add(new Player("player2",CardName.ARTEMIS,null,null));
        p.add(new Player("player3",CardName.ATLAS,null,null));
        assertFalse(new Cell(0,0,0).isOccupied(p));
    }
    @Test
    public void isCellInBoard_true()
    {
        List<Player> p = new ArrayList<>();
        p.add(new Player("player1",CardName.APOLLO,new Worker(2,4),new Worker(1,1)));
        p.add(new Player("player2",CardName.ARTEMIS,new Worker(3,4),new Worker(1,4)));
        p.add(new Player("player3",CardName.ATLAS,new Worker(4,4),new Worker(1,2)));
        assertTrue(new Cell(1,4,0).isOccupied(p));
    }
    @Test
    public void isCellInBoard_false()
    {
        List<Player> p = new ArrayList<>();
        p.add(new Player("player1",CardName.APOLLO,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player2",CardName.ARTEMIS,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player3",CardName.ATLAS,new Worker(0,0),new Worker(0,0)));
        assertFalse(new Cell(1,4,0).isOccupied(p));
    }
}
