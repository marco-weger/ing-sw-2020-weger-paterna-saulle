package it.polimi.ingsw.cards;

import it.polimi.ingsw.model.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class HephaestusTest {
    // checkBuild
    @Test
    public void checkBuild_paramsNull() {
        Card c = FactoryCard.getCard(CardName.HEPHASTUS);
        List<Player> p = new ArrayList<>();
        p.add(new Player("player1", CardName.HEPHASTUS, new Worker(0, 0), new Worker(0, 0)));
        p.add(new Player("player2", CardName.PAN, new Worker(0, 0), new Worker(0, 0)));
        p.add(new Player("player3", CardName.PROMETHEUS, new Worker(0, 0), new Worker(0, 0)));
        assertNotNull(c);
        assertEquals(c.checkBuild(null, new Board()).size(), 0);
        assertEquals(c.checkBuild(p, null).size(), 0);
    }

    @Test
    public void checkBuild_noCurrentPlayerWorker() {
        List<Player> p = new ArrayList<>();
        p.add(new Player("player1", CardName.HEPHASTUS, new Worker(0, 0), new Worker(0, 0)));
        p.add(new Player("player2", CardName.ARTEMIS, new Worker(0, 0), new Worker(0, 0)));
        p.add(new Player("player3", CardName.MINOTAUR, new Worker(0, 0), new Worker(0, 0)));
        assertEquals(p.get(0).getCard().checkBuild(p, new Board()).size(), 0);
    }

    @Test
    public void checkAndBuild_noActive() {
        List<Player> p = new ArrayList<>();
        p.add(new Player("player1", CardName.HEPHASTUS, new Worker(2, 2), new Worker(1, 1)));
        p.add(new Player("player2", CardName.ATHENA, new Worker(2, 1), new Worker(1, 2)));
        p.add(new Player("player3", CardName.PAN, new Worker(1, 3), new Worker(2, 3)));
        p.get(0).setCurrentWorker(1);
        p.get(0).getCard().setActive(false);
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
        List<Cell> building = p.get(0).getCard().checkBuild(p, b);
        assertEquals(building.size(), 3);
        for (Cell c : building)
            assertTrue(c.getRow() == 3 && c.getColumn() == 1 || c.getRow() == 3 && c.getColumn() == 2 || c.getRow() == 3 && c.getColumn() == 3);
    }

    @Test
    public void checkAndBuild_activeSuccess() {
        List<Player> p = new ArrayList<>();
        p.add(new Player("player1", CardName.HEPHASTUS, new Worker(2, 2), new Worker(1, 1)));
        p.add(new Player("player2", CardName.MINOTAUR, new Worker(0, 0), new Worker(1, 2)));
        p.add(new Player("player3", CardName.PAN, new Worker(1, 3), new Worker(4, 4)));
        p.get(0).setCurrentWorker(1);
        Board b = new Board();
        assertNotNull(p);
        assertNotNull(b);
        for (Cell c : b.getField()) {
            if (c.getRow() == 3 && c.getColumn() == 1)
                c.setLevel(1);
            else if (c.getRow() == 3 && c.getColumn() == 2)
                c.setLevel(2);
            else if (c.getRow() == 3 && c.getColumn() == 3)
                c.setLevel(3);
            else if (c.getRow() == 2 && c.getColumn() == 1)
                c.setLevel(0);
            else if (c.getRow() == 2 && c.getColumn() == 3)
                c.setLevel(4);
        }

        p.get(0).getCard().setActive(true);

        List<Cell> building = p.get(0).getCard().checkBuild(p, b);

        assertEquals(building.size(), 2);
        for (Cell c : building)
            assertTrue(c.getRow() == 2 && c.getColumn() == 1 || c.getRow() == 3 && c.getColumn() == 1);

        p.get(0).getCard().build(p, b, b.getCell(3, 1));
        assertEquals(3, b.getCell(3,1).getLevel());
    }

    @Test
    public void checkAndBuild_activeFailed() {
        List<Player> p = new ArrayList<>();
        p.add(new Player("player1", CardName.HEPHASTUS, new Worker(2, 2), new Worker(1, 1)));
        p.add(new Player("player2", CardName.MINOTAUR, new Worker(0, 0), new Worker(1, 2)));
        p.add(new Player("player3", CardName.PAN, new Worker(1, 3), new Worker(4, 4)));
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
                c.setLevel(2);
            else if (c.getRow() == 2 && c.getColumn() == 1)
                c.setLevel(3);
            else if (c.getRow() == 2 && c.getColumn() == 3)
                c.setLevel(4);
        }

        //p.get(0).getCard().setActive(true);

        List<Cell> building = p.get(0).getCard().checkBuild(p, b);

        assertEquals(building.size(), 4);
        for (Cell c : building)
            assertTrue(c.getRow() == 2 && c.getColumn() == 1 || c.getRow() == 3 && c.getColumn() == 1 || c.getRow() == 3 && c.getColumn() == 2 || c.getRow() == 3 && c.getColumn() == 3);

        p.get(0).getCard().build(p, b, b.getCell(3, 2));
        assertEquals(2, b.getCell(3,2).getLevel());

        /*
        building = p.get(0).getCard().checkBuild(p, b);
        assertEquals(building.size(), 3);
        for (Cell c : building)
            assertTrue(c.getRow() == 3 && c.getColumn() == 1 || c.getRow() == 3 && c.getColumn() == 2||c.getRow() == 3 && c.getColumn() == 3);
         */
    }

    //build
    @Test
    public void build_null() {
        Card c = FactoryCard.getCard(CardName.HEPHASTUS);
        assertNotNull(c);
        c.build(null, null, null);
    }

    @Test
    public void build_error() {
        List<Player> p = new ArrayList<>();
        Board b = new Board();
        p.add(new Player("player1", CardName.HEPHASTUS, new Worker(2, 2), new Worker(1, 1)));
        p.add(new Player("player2", CardName.DEMETER, new Worker(2, 1), new Worker(1, 2)));
        p.add(new Player("player3", CardName.APOLLO, new Worker(1, 3), new Worker(2, 3)));
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
        List<Player> p = new ArrayList<>();
        Board b = new Board();
        p.add(new Player("player1", CardName.HEPHASTUS, new Worker(2, 2), new Worker(1, 1)));
        p.add(new Player("player2", CardName.PROMETHEUS, new Worker(2, 1), new Worker(1, 2)));
        p.add(new Player("player3", CardName.ARTEMIS, new Worker(1, 3), new Worker(2, 3)));
        p.get(0).setCurrentWorker(1);
        List<Cell> b1 = p.get(0).getCard().checkBuild(p,b);
        for (Cell c : b1) {
            b.getCell(c.getRow(),c.getColumn()).setLevel(0);
            assertEquals(c.getLevel(),0);
            //default build
            p.get(0).getCard().setActive(false);
            p.get(0).getCard().build(p, b, c);
            assertEquals(b.getCell(c.getRow(),c.getColumn()).getLevel(), 1);
            //considering that the turn is the next one, the other player are NOT influencing the board in this version
            //thus, the test is only for correction of "build" purpose
            p.get(0).getCard().setActive(true);
            p.get(0).getCard().build(p, b, c);
            assertEquals(b.getCell(c.getRow(),c.getColumn()).getLevel(), 3);
        }
    }

    //TODO: test on the handler of exceptions
}
