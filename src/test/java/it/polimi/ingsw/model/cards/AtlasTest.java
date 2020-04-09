package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class AtlasTest {
    ArrayList<Player> p = new ArrayList<>(Arrays.asList(
            new Player("player1","",null),
            new Player("player2","",null),
            new Player("player3","",null)
    ));

    public void initialize(){
        for(Player player:p){
            player.setWorker1(new Worker(0,0));
            player.setWorker2(new Worker(0,0));
        }
        p.get(0).setCard(CardName.ATLAS,null);
        p.get(1).setCard(CardName.HEPHAESTUS,null);
        p.get(2).setCard(CardName.DEMETER,null);
    }

    // checkBuild
    @Test
    public void checkBuild_paramsNull() {
        initialize();
        assertEquals(p.get(0).getCard().checkBuild(null, new Board()).size(), 0);
        assertEquals(p.get(0).getCard().checkBuild(p, null).size(), 0);
    }

    @Test
    public void checkBuild_noCurrentPlayerWorker() {
        initialize();
        assertEquals(p.get(0).getCard().checkBuild(p, new Board()).size(), 0);
    }

    @Test
    public void checkAndBuild_noActive() {
        initialize();
        p.get(0).setWorker1(new Worker(2,2));
        p.get(0).setWorker2(new Worker(1,1));
        p.get(1).setWorker1(new Worker(2,1));
        p.get(1).setWorker2(new Worker(1,2));
        p.get(2).setWorker1(new Worker(1,3));
        p.get(2).setWorker2(new Worker(2,3));
        p.get(0).setCurrentWorker(1);
        p.get(0).getCard().setActive(false);
        Board b = new Board();
        ArrayList<Cell> building = p.get(0).getCard().checkBuild(p, b);
        assertEquals(building.size(), 3);
        for (Cell c : building)
            assertTrue(c.getRow() == 3 && c.getColumn() == 1 || c.getRow() == 3 && c.getColumn() == 2 || c.getRow() == 3 && c.getColumn() == 3);
        assertTrue(p.get(0).getCard().build(p,b,building.get(1)));
        assertEquals(building.get(1).getLevel(),1);
    }

    @Test
    public void checkAndBuild_active() {
        initialize();
        p.get(0).setWorker1(new Worker(2,2));
        p.get(0).setWorker2(new Worker(1,1));
        p.get(1).setWorker1(new Worker(2,1));
        p.get(1).setWorker2(new Worker(1,2));
        p.get(2).setWorker1(new Worker(1,3));
        p.get(2).setWorker2(new Worker(2,3));
        p.get(0).setCurrentWorker(1);
        Board b = new Board();
        assertNotNull(p);
        assertNotNull(b);
        for (Cell c : b.getField()) {
            if (c.getRow() == 3 && c.getColumn() == 1)
                c.setLevel(0);
            else if (c.getRow() == 3 && c.getColumn() == 2)
                c.setLevel(1);
            else if (c.getRow() == 3 && c.getColumn() == 3)
                c.setLevel(0);
        }
        ArrayList<Cell> building = p.get(0).getCard().checkBuild(p, b);
        assertEquals(building.size(), 3);
        for (Cell c : building)
            assertTrue(c.getRow() == 3 && c.getColumn() == 1 || c.getRow() == 3 && c.getColumn() == 2 || c.getRow() == 3 && c.getColumn() == 3);
        p.get(0).getCard().setActive(true);
        p.get(0).getCard().build(p, b, b.getCell(3, 2));
        building = p.get(0).getCard().checkBuild(p, b);
        assertEquals(building.size(), 2);
        for (Cell c : building)
            assertTrue(c.getRow() == 3 && c.getColumn() == 1 || c.getRow() == 3 && c.getColumn() == 3);
        for (Cell c : b.getField())
            if (c.getRow() == 3 && c.getColumn() == 2)
                assertEquals(4, c.getLevel());
    }

    //build
    @Test
    public void build_null() {
        Card c = FactoryCard.getCard(CardName.ATLAS,null);
        assertNotNull(c);
        c.build(null, null, null);
    }

    @Test
    public void build_error() {
        Board b = new Board();
        initialize();
        p.get(0).setWorker1(new Worker(2,2));
        p.get(0).setWorker2(new Worker(1,1));
        p.get(1).setWorker1(new Worker(2,1));
        p.get(1).setWorker2(new Worker(1,2));
        p.get(2).setWorker1(new Worker(1,3));
        p.get(2).setWorker2(new Worker(2,3));
        p.get(0).setCurrentWorker(1);
        p.get(0).getCard().setActive(true);
        p.get(0).getCard().build(p, b, new Cell(1, 3, 0));
        //the cell is occupied, thus the build fail
        //the exception is not handled, the level remains 0
        for (Cell c : b.getField())
            if (c.getRow() == 1 && c.getColumn() == 3)
                assertEquals(0, c.getLevel());
        assertEquals(p.get(0).getCurrentWorker().getRow(), 2);
        assertEquals(p.get(0).getCurrentWorker().getColumn(), 2);
    }

    @Test
    public void build() {
        Board b = new Board();
        initialize();
        p.get(0).setWorker1(new Worker(2,2));
        p.get(0).setWorker2(new Worker(1,1));
        p.get(1).setWorker1(new Worker(2,1));
        p.get(1).setWorker2(new Worker(1,2));
        p.get(2).setWorker1(new Worker(1,3));
        p.get(2).setWorker2(new Worker(2,3));
        p.get(0).setCurrentWorker(1);
        ArrayList<Cell> b1 = p.get(0).getCard().checkBuild(p,b);
        for (Cell c : b1) {
                b.getCell(c.getRow(),c.getColumn()).setLevel(0);
                assertEquals(c.getLevel(),0);
                p.get(0).getCard().setActive(false);
                p.get(0).getCard().build(p, b, c);
                assertEquals(b.getCell(c.getRow(),c.getColumn()).getLevel(), 1);
                p.get(0).getCard().setActive(true);
                p.get(0).getCard().build(p, b, c);
                assertEquals(b.getCell(c.getRow(),c.getColumn()).getLevel(), 4);
        }
    }
}



