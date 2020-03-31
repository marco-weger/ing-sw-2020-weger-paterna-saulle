package it.polimi.ingsw.model;
import it.polimi.ingsw.model.cards.CardName;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class BoardTest{

    // isCellInBoard
    @Test
    public void isCellInBoard_cellNull()
    {
        Board b = new Board();
        assertFalse(Board.isCellInBoard(null));
    }
    @Test
    public void isCellInBoard_cellIn()
    {
        Board b = new Board();
        assertTrue(Board.isCellInBoard(new Cell(0,0,0)));
    }
    @Test
    public void isCellInBoard_cellOut()
    {
        Board b = new Board();
        assertFalse(Board.isCellInBoard(new Cell(5,5,0)));
    }
    // build
    @Test
    public void build_cellNull()
    {
        Board b = new Board();
        b.build(null, 3);
        for(Cell c: b.getField())
            assertEquals(c.getLevel(),0);
    }
    @Test
    public void build_cellOutOfBoard()
    {
        Board b = new Board();
        b.build(new Cell(5,5, 3), 3);
        for(Cell c: b.getField())
            assertEquals(c.getLevel(),0);
    }
    @Test
    public void build_levelOutOfRange()
    {
        Board b = new Board();
        b.build(new Cell(0,0, 3), 5);
        for(Cell c: b.getField())
            if(c.getRow() == 0 && c.getColumn() == 0)
                assertEquals(c.getLevel(),0);
    }
    @Test
    public void build_correctInput()
    {
        Board b = new Board();
        b.build(new Cell(0,0, 3), 3);
        for(Cell c: b.getField())
            if(c.getRow() == 0 && c.getColumn() == 0)
                assertEquals(c.getLevel(),3);
    }
    @Test
    public void testClone() throws CloneNotSupportedException {
        Board b = new Board();
        Board b2 = b.clone();

        b2.getCell(0,0).setLevel(4);
        assertEquals(b.getCell(0,0).getLevel(),0);
        assertEquals(b2.getCell(0,0).getLevel(),4);
    }
}
