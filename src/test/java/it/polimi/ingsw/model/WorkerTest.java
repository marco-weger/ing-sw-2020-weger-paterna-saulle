package it.polimi.ingsw.model;

import it.polimi.ingsw.cards.CardName;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class WorkerTest {
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
}
