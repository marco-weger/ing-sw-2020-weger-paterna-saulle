package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.commons.SnapCell;
import it.polimi.ingsw.model.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class CardTest {
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
        p.get(0).setCard(CardName.APOLLO,null);
        p.get(1).setCard(CardName.ARTEMIS,null);
        p.get(2).setCard(CardName.ATLAS,null);
    }
    // setter
    @Test
    public void setter()
    {
        Card c = FactoryCard.getCard(CardName.APOLLO,null);
        assertNotNull(c);
        c.setActive(true);
        assertTrue(c.isActive());
        assertFalse(c.isOpponent());
        assertFalse(c.isQuestion());
    }
    // getNextStatus
    @Test
    public void getNextStatus()
    {
        Card c = FactoryCard.getCard(CardName.APOLLO,null);
        assertNotNull(c);
        Status s;
        s = c.getNextStatus(Status.START);
        assertNotNull(s);
        assertEquals(Status.CHOSEN, s);
        s = c.getNextStatus(Status.CHOSEN);
        assertNotNull(s);
        assertEquals(Status.QUESTION_M, s);
        s = c.getNextStatus(Status.QUESTION_M);
        assertNotNull(s);
        assertEquals(Status.MOVED, s);
        s = c.getNextStatus(Status.MOVED);
        assertNotNull(s);
        assertEquals(Status.QUESTION_B, s);
        s = c.getNextStatus(Status.BUILT);
        assertNotNull(s);
        assertEquals(Status.START, s);
    }
    // checkWin
    @Test
    public void checkWin_cellNull()
    {
        Card c = FactoryCard.getCard(CardName.APOLLO,null);
        assertNotNull(c);
        assertFalse(c.checkWin(null,new Cell(0,0,0)));
        assertFalse(c.checkWin(new Cell(0,0,0),null));
    }
    @Test
    public void checkWin_cellOut()
    {
        Card c = FactoryCard.getCard(CardName.APOLLO,null);
        assertNotNull(c);
        assertFalse(c.checkWin(new Cell(5,5,0),new Cell(0,0,0)));
        assertFalse(c.checkWin(new Cell(0,0,0),new Cell(5,5,0)));
    }
    @Test
    public void checkWin_win()
    {
        Card c = FactoryCard.getCard(CardName.APOLLO,null);
        assertNotNull(c);
        assertTrue(c.checkWin(new Cell(4,4,2),new Cell(4,3,3)));
    }
    @Test
    public void checkWin_notWin()
    {
        Card c = FactoryCard.getCard(CardName.APOLLO,null);
        assertNotNull(c);
        assertFalse(c.checkWin(new Cell(4,4,3),new Cell(3,4,3)));
    }
    // checkBuild
    @Test
    public void checkBuild_paramsNull()
    {
        Card c = FactoryCard.getCard(CardName.APOLLO,null);
        initialize();
        assertNotNull(c);
        assertEquals(c.checkBuild(null,new Board()).size(),0);
        assertEquals(c.checkBuild(p,null).size(),0);
    }
    @Test
    public void checkBuild_noCurrentPlayerWorker()
    {
        Card c = FactoryCard.getCard(CardName.PAN,null);
        initialize();
        assertNotNull(c);
        assertEquals(c.checkBuild(p,new Board()).size(),0);
        assertEquals(c.checkBuild(p,new Board()).size(),0);
    }
    @Test
    public void checkBuild()
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
                c.setLevel(4);
        }
        assertNotNull(p);
        assertNotNull(b);
        ArrayList<Cell> ret = p.get(0).getCard().checkBuild(p,b);
        //for(Cell c:ret)
        //    System.out.println(c.getRow() + " - " + c.getColumn());
        assertEquals(ret.size(),3);
        for(Cell c:ret)
            assertTrue(c.getRow() == 1 && c.getColumn() == 2 || c.getRow() == 1 && c.getColumn() == 3 || c.getRow() == 1 && c.getColumn() == 4);
    }
    // getCheckBuild
    @Test
    public void getCheckBuild()
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
                c.setLevel(4);
        }
        assertNotNull(p);
        assertNotNull(b);
        ArrayList<SnapCell> ret = p.get(0).getCard().getCheckBuild(p,b);
        //for(Cell c:ret)
        //    System.out.println(c.getRow() + " - " + c.getColumn());
        assertEquals(ret.size(),3);
        for(SnapCell c:ret)
            assertTrue(c.row == 1 && c.column == 2 || c.row == 1 && c.column == 3 || c.row == 1 && c.column == 4);
    }
    // checkMove
    @Test
    public void checkMove_paramsNull()
    {
        Card c = FactoryCard.getCard(CardName.APOLLO,null);
        initialize();
        assertNotNull(c);
        ArrayList<Cell> ret = c.checkMove(p,null);
        assertEquals(ret.size(),0);
        ret = c.checkMove(null,new Board());
        assertEquals(0, ret.size());
    }
    @Test
    public void checkMove_noCurrentPlayerWorker()
    {
        Card c = FactoryCard.getCard(CardName.PAN,null);
        initialize();
        assertNotNull(c);
        assertEquals(c.checkMove(p,new Board()).size(),0);
        assertEquals(c.checkMove(p,new Board()).size(),0);
    }
    @Test
    public void checkMove_easyMove()
    {
        initialize();
        p.get(0).setWorker1(new Worker(2,3));
        p.get(0).setWorker2(new Worker(0,0));
        p.get(1).setWorker1(new Worker(4,0));
        p.get(1).setWorker2(new Worker(0,1));
        p.get(2).setWorker1(new Worker(1,0));
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
        assertNotNull(p);
        assertNotNull(b);
        ArrayList<Cell> ret = p.get(0).getCard().checkMove(p,b);
        //for(Cell c:ret)
        //    System.out.println(c.getRow() + " - " + c.getColumn());
        assertEquals(ret.size(),1);
        for(Cell c:ret){
            assertEquals(c.getRow() , 1);
            assertEquals(c.getColumn() , 2);
        }
    }
    @Test
    public void checkMove_downTo()
    {
        initialize();
        p.get(0).setWorker1(new Worker(0,0));
        p.get(0).setWorker2(new Worker(4,0));
        p.get(1).setWorker1(new Worker(4,1));
        p.get(1).setWorker2(new Worker(4,2));
        p.get(2).setWorker1(new Worker(4,3));
        p.get(2).setWorker2(new Worker(0,2));
        p.get(0).setCurrentWorker(1);
        Board b = new Board();
        for(Cell c:b.getField()){
            if(c.getRow() == 0 && c.getColumn() == 0)
                c.setLevel(3);
            else if(c.getRow() == 1 && c.getColumn() == 0)
                c.setLevel(0);
            else if(c.getRow() == 0 && c.getColumn() == 1)
                c.setLevel(1);
            else if(c.getRow() == 1 && c.getColumn() == 1)
                c.setLevel(4);
        }
        assertNotNull(p);
        assertNotNull(b);
        ArrayList<Cell> ret = p.get(0).getCard().checkMove(p,b);
        //for(Cell c:ret)
        //    System.out.println(c.getRow() + " - " + c.getColumn());
        assertEquals(ret.size(),2);
        for(Cell c:ret){
            assertEquals(c.getRow()+c.getColumn() , 1);
        }
    }
    // getCheckMove
    @Test
    public void getcheckMove_downTo()
    {
        initialize();
        p.get(0).setWorker1(new Worker(0,0));
        p.get(0).setWorker2(new Worker(4,0));
        p.get(1).setWorker1(new Worker(4,1));
        p.get(1).setWorker2(new Worker(4,2));
        p.get(2).setWorker1(new Worker(4,3));
        p.get(2).setWorker2(new Worker(0,4));
        p.get(0).setCurrentWorker(1);
        Board b = new Board();
        for(Cell c:b.getField()){
            if(c.getRow() == 0 && c.getColumn() == 0)
                c.setLevel(3);
            else if(c.getRow() == 1 && c.getColumn() == 0)
                c.setLevel(0);
            else if(c.getRow() == 0 && c.getColumn() == 1)
                c.setLevel(1);
            else if(c.getRow() == 1 && c.getColumn() == 1)
                c.setLevel(4);
        }
        assertNotNull(p);
        assertNotNull(b);
        ArrayList<SnapCell> ret = p.get(0).getCard().getCheckMove(p,b);
        //for(Cell c:ret)
        //    System.out.println(c.getRow() + " - " + c.getColumn());
        assertEquals(ret.size(),2);
        for(SnapCell c:ret){
            assertEquals(c.row+c.column , 1);
        }
    }
    // activeBlock
    @Test
    public void activeBlock()
    {
        Card c = FactoryCard.getCard(CardName.APOLLO,null);
        assertNotNull(c);
        assertEquals(c.activeBlock(null,new Board(), new Worker(0,0), Status.CHOSEN).size(),0);
    }
    // move
    @Test
    public void move_null()
    {
        Card c = FactoryCard.getCard(CardName.PAN,null);
        assertNotNull(c);
        c.move(null,null,null);
    }
    @Test
    public void move_error()
    {
        Card c = FactoryCard.getCard(CardName.PAN,null);
        initialize();
        p.get(0).setCard(CardName.PAN,null);
        p.get(1).setCard(CardName.HEPHAESTUS,null);
        p.get(2).setCard(CardName.ATHENA,null);
        p.get(0).setWorker1(new Worker(2,3));
        p.get(0).setWorker2(new Worker(0,0));
        p.get(1).setWorker1(new Worker(3,4));
        p.get(1).setWorker2(new Worker(0,1));
        p.get(2).setWorker1(new Worker(2,2));
        p.get(2).setWorker2(new Worker(0,2));
        p.get(0).setCurrentWorker(1);
        Board b = new Board();
        assertNotNull(c);
        assertFalse(c.move(p,b,b.getCell(3,4)));
        assertEquals(p.get(0).getCurrentWorker().getRow(), 2);
        assertEquals(p.get(0).getCurrentWorker().getColumn(), 3);
    }
    @Test
    public void move()
    {
        Card c = FactoryCard.getCard(CardName.APOLLO,null);
        initialize();
        p.get(0).setWorker1(new Worker(2,3));
        p.get(0).setWorker2(new Worker(0,0));
        p.get(1).setWorker1(new Worker(4,0));
        p.get(1).setWorker2(new Worker(0,1));
        p.get(2).setWorker1(new Worker(2,2));
        p.get(2).setWorker2(new Worker(0,2));
        p.get(0).setCurrentWorker(1);
        Board b = new Board();
        assertNotNull(c);
        for(Cell cell:b.getField())
            if(cell.getRow() == 3 & cell.getColumn() == 4)
                c.move(p,b,cell);
        assertEquals(p.get(0).getCurrentWorker().getRow(), 3);
        assertEquals(p.get(0).getCurrentWorker().getColumn(), 4);
    }
    // build
    @Test
    public void build_null()
    {
        Card c = FactoryCard.getCard(CardName.PAN,null);
        assertNotNull(c);
        c.build(null,null,null);
    }
    @Test
    public void build_error()
    {
        initialize();
        p.get(0).setWorker1(new Worker(2,3));
        p.get(0).setWorker2(new Worker(0,0));
        p.get(1).setWorker1(new Worker(3,4));
        p.get(1).setWorker2(new Worker(0,1));
        p.get(2).setWorker1(new Worker(2,2));
        p.get(2).setWorker2(new Worker(0,2));
        p.get(0).setCurrentWorker(1);
        p.get(0).getCard().build(p,new Board(),new Cell(3,4,0));
        assertEquals(p.get(0).getCurrentWorker().getRow(), 2);
        assertEquals(p.get(0).getCurrentWorker().getColumn(), 3);
    }
    @Test
    public void build()
    {
        Card c = FactoryCard.getCard(CardName.PAN,null);
        initialize();
        p.get(0).setCard(CardName.PAN,null);
        p.get(1).setCard(CardName.HEPHAESTUS,null);
        p.get(2).setCard(CardName.ATHENA,null);
        p.get(0).setWorker1(new Worker(2,3));
        p.get(0).setWorker2(new Worker(0,0));
        p.get(1).setWorker1(new Worker(1,4));
        p.get(1).setWorker2(new Worker(0,1));
        p.get(2).setWorker1(new Worker(2,2));
        p.get(2).setWorker2(new Worker(0,2));
        p.get(0).setCurrentWorker(1);
        Board b = new Board();
        assertNotNull(c);
        for(Cell cell:b.getField()){
            if(cell.getRow() == 3 & cell.getColumn() == 4){
                assertTrue(c.build(p,b,cell));
                assertEquals(cell.getLevel(), 1);
            }
        }
    }

    @Test
    public void hasLostFalse() {
        initialize();
        p.get(0).setCard(CardName.APOLLO,null);
        p.get(1).setCard(CardName.ARTEMIS,null);
        p.get(0).setWorker1(new Worker(0,0));
        p.get(0).setWorker2(new Worker(1,0));
        p.get(1).setWorker1(new Worker(1,4));
        p.get(1).setWorker2(new Worker(4,4));
        Board b = new Board();
        for(Cell ce :b.getField()){
            if(ce.getRow() == 0 && ce.getColumn() == 0)
                ce.setLevel(0);
            else if(ce.getRow() == 0 && ce.getColumn() == 1)
                ce.setLevel(0);
            else if(ce.getRow() == 0 && ce.getColumn() == 2)
                ce.setLevel(0);
            else if(ce.getRow() == 0 && ce.getColumn() == 3)
                ce.setLevel(0);
            else if(ce.getRow() == 0 && ce.getColumn() == 4)
                ce.setLevel(0);
            else if(ce.getRow() == 1 && ce.getColumn() == 0)
                ce.setLevel(0);
            else if(ce.getRow() == 1 && ce.getColumn() == 1)
                ce.setLevel(0);
            else if(ce.getRow() == 1 && ce.getColumn() ==2)
                ce.setLevel(0);
            else if(ce.getRow() == 1 && ce.getColumn() == 3)
                ce.setLevel(0);
            else if(ce.getRow() == 1 && ce.getColumn() == 4)
                ce.setLevel(0);
            else if(ce.getRow() == 2 && ce.getColumn() == 0)
                ce.setLevel(0);
            else if(ce.getRow() == 2 && ce.getColumn() == 1)
                ce.setLevel(0);
            else if(ce.getRow() == 2 && ce.getColumn() ==2)
                ce.setLevel(0);
            else if(ce.getRow() == 2 && ce.getColumn() == 3)
                ce.setLevel(0);
            else if(ce.getRow() == 2 && ce.getColumn() == 4)
                ce.setLevel(0);
            else if(ce.getRow() == 3 && ce.getColumn() == 0)
                ce.setLevel(0);
            else if(ce.getRow() == 3 && ce.getColumn() == 1)
                ce.setLevel(0);
            else if(ce.getRow() == 3 && ce.getColumn() ==2)
                ce.setLevel(0);
            else if(ce.getRow() == 3 && ce.getColumn() == 3)
                ce.setLevel(0);
            else if(ce.getRow() == 3 && ce.getColumn() == 4)
                ce.setLevel(0);
            else if(ce.getRow() == 4 && ce.getColumn() == 0)
                ce.setLevel(0);
            else if(ce.getRow() == 4 && ce.getColumn() == 1)
                ce.setLevel(0);
            else if(ce.getRow() == 4 && ce.getColumn() ==2)
                ce.setLevel(0);
            else if(ce.getRow() == 4 && ce.getColumn() == 3)
                ce.setLevel(0);
            else if(ce.getRow() == 4 && ce.getColumn() == 4)
                ce.setLevel(0);
        }
        assertFalse(p.get(0).getCard().hasLost(p, b));
    }


    @Test
    public void hasLostTrue() {
        initialize();
        p.get(0).setCard(CardName.PAN,null);
        p.get(1).setCard(CardName.ARTEMIS,null);
        p.get(0).setWorker1(new Worker(0,0));
        p.get(0).setWorker2(new Worker(1,0));
        p.get(1).setWorker1(new Worker(2,0));
        p.get(1).setWorker2(new Worker(0,1));
        Board b = new Board();
        for(Cell ce :b.getField()){
            if(ce.getRow() == 0 && ce.getColumn() == 0)
                ce.setLevel(0);
            else if(ce.getRow() == 0 && ce.getColumn() == 1)
                ce.setLevel(4);
            else if(ce.getRow() == 0 && ce.getColumn() == 2)
                ce.setLevel(0);
            else if(ce.getRow() == 0 && ce.getColumn() == 3)
                ce.setLevel(0);
            else if(ce.getRow() == 0 && ce.getColumn() == 4)
                ce.setLevel(0);
            else if(ce.getRow() == 1 && ce.getColumn() == 0)
                ce.setLevel(0);
            else if(ce.getRow() == 1 && ce.getColumn() == 1)
                ce.setLevel(4);
            else if(ce.getRow() == 1 && ce.getColumn() ==2)
                ce.setLevel(4);
            else if(ce.getRow() == 1 && ce.getColumn() == 3)
                ce.setLevel(0);
            else if(ce.getRow() == 1 && ce.getColumn() == 4)
                ce.setLevel(0);
            else if(ce.getRow() == 2 && ce.getColumn() == 0)
                ce.setLevel(0);
            else if(ce.getRow() == 2 && ce.getColumn() == 1)
                ce.setLevel(4);
            else if(ce.getRow() == 2 && ce.getColumn() ==2)
                ce.setLevel(0);
            else if(ce.getRow() == 2 && ce.getColumn() == 3)
                ce.setLevel(0);
            else if(ce.getRow() == 2 && ce.getColumn() == 4)
                ce.setLevel(0);
            else if(ce.getRow() == 3 && ce.getColumn() == 0)
                ce.setLevel(0);
            else if(ce.getRow() == 3 && ce.getColumn() == 1)
                ce.setLevel(0);
            else if(ce.getRow() == 3 && ce.getColumn() ==2)
                ce.setLevel(0);
            else if(ce.getRow() == 3 && ce.getColumn() == 3)
                ce.setLevel(0);
            else if(ce.getRow() == 3 && ce.getColumn() == 4)
                ce.setLevel(0);
            else if(ce.getRow() == 4 && ce.getColumn() == 0)
                ce.setLevel(0);
            else if(ce.getRow() == 4 && ce.getColumn() == 1)
                ce.setLevel(0);
            else if(ce.getRow() == 4 && ce.getColumn() ==2)
                ce.setLevel(0);
            else if(ce.getRow() == 4 && ce.getColumn() == 3)
                ce.setLevel(0);
            else if(ce.getRow() == 4 && ce.getColumn() == 4)
                ce.setLevel(0);
        }
        assertTrue(p.get(0).getCard().hasLost(p, b));
    }
   /* @Test
    public void hasLostTrue() {
        ArrayList<Player> p = new ArrayList<>();
        p.add(new Player("player1", CardName.APOLLO,new Worker(0,0),new Worker(1,0)));
        p.add(new Player("player2",CardName.ARTEMIS,new Worker(1,4),new Worker(4,4)));
        Match m = new Match(0, new Board(), p,false, p.get(0), Status.START);
        for(Cell c : m.getBoard().getField())
            if(!c.isOccupied(p))
                c.setLevel(4);
        m.setCurrentPlayer(p.get(0));
        assertTrue(m.checkCurrentPlayerLose());
        for(Cell c : m.getBoard().getField())
            if(c.getRow() == 2 && c.getColumn() == 0)
                c.setLevel(0);
        assertFalse(m.checkCurrentPlayerLose());
    }*/

}
