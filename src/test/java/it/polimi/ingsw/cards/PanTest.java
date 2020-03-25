package it.polimi.ingsw.cards;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Worker;
import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class PanTest {


    @Test
    public void checkWin_JUMP() {

            Card c = FactoryCard.getCard(CardName.PAN);
            List<Player> p = new ArrayList<>();
            p.add(new Player("player1", CardName.PAN, new Worker(0, 0), new Worker(0, 1)));
            p.add(new Player("player2", CardName.ARTEMIS, new Worker(3, 0), new Worker(3, 3)));
            p.add(new Player("player3", CardName.ATLAS, new Worker(2, 0), new Worker(2, 2)));
            Assert.assertNotNull(c);
            p.get(0).setCurrentWorker(0);
            Board b = new Board();
            for (Cell ce : b.getField()) {
                if (ce.getRow() == 0 && ce.getColumn() == 0)
                    ce.setLevel(2);
                else if (ce.getRow() == 1 && ce.getColumn() == 0)
                    ce.setLevel(0);
            }
            assertTrue(c.checkWin(new Cell(0,0,2),new Cell(1,0,0)));


    }

    @Test
    public void checkNotWin_JUMP(){

            Card c = FactoryCard.getCard(CardName.PAN);
            List<Player> p = new ArrayList<>();
            p.add(new Player("player1", CardName.PAN, new Worker(0, 0), new Worker(0, 1)));
            p.add(new Player("player2", CardName.ARTEMIS, new Worker(3, 0), new Worker(3, 3)));
            p.add(new Player("player3", CardName.ATLAS, new Worker(2, 0), new Worker(2, 2)));
            Assert.assertNotNull(c);
            p.get(0).setCurrentWorker(0);
            Board b = new Board();
            for (Cell ce : b.getField()) {
                if (ce.getRow() == 0 && ce.getColumn() == 0)
                    ce.setLevel(2);
                else if (ce.getRow() == 1 && ce.getColumn() == 0)
                    ce.setLevel(1);
            }
            assertFalse(c.checkWin(new Cell(0,0,2),new Cell(1,0,1)));



    }



    @Test
    public void checkWin_WithoutJUMP(){

        Card c = FactoryCard.getCard(CardName.PAN);
        List<Player> p = new ArrayList<>();
        p.add(new Player("player1", CardName.PAN, new Worker(0, 0), new Worker(0, 1)));
        p.add(new Player("player2", CardName.ARTEMIS, new Worker(3, 0), new Worker(3, 3)));
        p.add(new Player("player3", CardName.ATLAS, new Worker(2, 0), new Worker(2, 2)));
        Assert.assertNotNull(c);
        p.get(0).setCurrentWorker(0);
        Board b = new Board();
        for (Cell ce : b.getField()) {
            if (ce.getRow() == 0 && ce.getColumn() == 0)
                ce.setLevel(2);
            else if (ce.getRow() == 1 && ce.getColumn() == 0)
                ce.setLevel(3);
        }
        assertTrue(c.checkWin(new Cell(0,0,2),new Cell(1,0,3)));



    }
}

