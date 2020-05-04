package it.polimi.ingsw.model;
import org.junit.Test;

import static org.junit.Assert.*;

public class BoardTest{

    // isCellInBoard
    @Test
    public void isCellInBoard_cellNull()
    {
        Board b = new Board();
        assertFalse(b.isCellInBoard(null));
    }
    @Test
    public void isCellInBoard_cellIn()
    {
        Board b = new Board();
        assertTrue(b.isCellInBoard(new Cell(0,0,0)));
    }
    @Test
    public void isCellInBoard_cellOut()
    {
        Board b = new Board();
        assertFalse(b.isCellInBoard(new Cell(5,5,0)));
    }
    // build
    @Test
    public void build_cellNull()
    {
        Board b = new Board();
        b.build(null, 3);
        for(Cell c: b.getField())
            assertEquals(0,c.getLevel());
    }
    @Test
    public void build_cellOutOfBoard()
    {
        Board b = new Board();
        b.build(new Cell(5,5, 3), 3);
        for(Cell c: b.getField())
            assertEquals(0,c.getLevel());
    }
    @Test
    public void build_levelOutOfRange()
    {
        Board b = new Board();
        b.build(new Cell(0,0, 3), 5);
        for(Cell c: b.getField())
            if(c.getRow() == 0 && c.getColumn() == 0)
                assertEquals(0,c.getLevel());
    }
    @Test
    public void build_correctInput()
    {
        Board b = new Board();
        b.build(new Cell(0,0, 3), 3);
        for(Cell c: b.getField())
            if(c.getRow() == 0 && c.getColumn() == 0)
                assertEquals(3,c.getLevel());
    }
}
