package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Worker;
import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class PanTest {
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
        p.get(0).setCard(CardName.PAN,null);
        p.get(1).setCard(CardName.ARTEMIS,null);
        p.get(2).setCard(CardName.ATLAS,null);
    }

    @Test
    public void checkWin_JUMP() {
        initialize();
        p.get(0).setWorker1(new Worker(0,0));
        p.get(0).setWorker2(new Worker(0,1));
        p.get(1).setWorker1(new Worker(3,0));
        p.get(1).setWorker2(new Worker(3,3));
        p.get(2).setWorker1(new Worker(2,0));
        p.get(2).setWorker2(new Worker(2,2));
        p.get(0).setCurrentWorker(0);
        Board b = new Board();
        for (Cell ce : b.getField()) {
            if (ce.getRow() == 0 && ce.getColumn() == 0)
                ce.setLevel(2);
            else if (ce.getRow() == 1 && ce.getColumn() == 0)
                ce.setLevel(0);
        }
        assertTrue(p.get(0).getCard().checkWin(new Cell(0,0,2),new Cell(1,0,0)));


    }

    @Test
    public void checkNotWin_JUMP(){
        initialize();
        p.get(0).setWorker1(new Worker(0,0));
        p.get(0).setWorker2(new Worker(0,1));
        p.get(1).setWorker1(new Worker(3,0));
        p.get(1).setWorker2(new Worker(3,3));
        p.get(2).setWorker1(new Worker(2,0));
        p.get(2).setWorker2(new Worker(2,2));
        p.get(0).setCurrentWorker(0);
        Board b = new Board();
        for (Cell ce : b.getField()) {
            if (ce.getRow() == 0 && ce.getColumn() == 0)
                ce.setLevel(2);
            else if (ce.getRow() == 1 && ce.getColumn() == 0)
                ce.setLevel(1);
        }
        assertFalse(p.get(0).getCard().checkWin(new Cell(0,0,2),new Cell(1,0,1)));
    }



    @Test
    public void checkWin_WithoutJUMP(){
        initialize();
        p.get(0).setWorker1(new Worker(0,0));
        p.get(0).setWorker2(new Worker(0,1));
        p.get(1).setWorker1(new Worker(3,0));
        p.get(1).setWorker2(new Worker(3,3));
        p.get(2).setWorker1(new Worker(2,0));
        p.get(2).setWorker2(new Worker(2,2));
        p.get(0).setCurrentWorker(0);
        Board b = new Board();
        for (Cell ce : b.getField()) {
            if (ce.getRow() == 0 && ce.getColumn() == 0)
                ce.setLevel(2);
            else if (ce.getRow() == 1 && ce.getColumn() == 0)
                ce.setLevel(3);
        }
        assertTrue(p.get(0).getCard().checkWin(new Cell(0,0,2),new Cell(1,0,3)));
    }

    @Test
    public void cell_NULL(){
        initialize();
        p.get(0).setWorker1(new Worker(0,0));
        p.get(0).setWorker2(new Worker(0,1));
        p.get(1).setWorker1(new Worker(3,0));
        p.get(1).setWorker2(new Worker(3,3));
        p.get(2).setWorker1(new Worker(2,0));
        p.get(2).setWorker2(new Worker(2,2));
        p.get(0).setCurrentWorker(0);
        assertFalse(p.get(0).getCard().checkWin(null, null));

    }
}

