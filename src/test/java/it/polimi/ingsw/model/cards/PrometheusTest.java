package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PrometheusTest {

    //checkMove when active
    @Test
    public void checkMove_paramsNull()
    {
        Card c = FactoryCard.getCard(CardName.PROMETHEUS);
        List<Player> p = new ArrayList<>();
        p.add(new Player("player1",CardName.PROMETHEUS,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player2",CardName.ARTEMIS,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player3",CardName.ATLAS,new Worker(0,0),new Worker(0,0)));
        assertNotNull(c);
        List<Cell> ret = c.checkMove(p,null);
        assertEquals(ret.size(),0);
        ret = c.checkMove(null,new Board());
        assertEquals(0, ret.size());
    }

    @Test
    public void checkMove_isActive()
    {
        Card c = FactoryCard.getCard(CardName.PROMETHEUS);
        List<Player> p = new ArrayList<>();
        p.add(new Player("player1",CardName.PROMETHEUS,new Worker(0,0),new Worker(4,1)));
        p.add(new Player("player2",CardName.ARTEMIS,new Worker(2,3),new Worker(3,3)));
        p.add(new Player("player3",CardName.ATLAS,new Worker(4,4),new Worker(3,1)));
        p.get(0).setCurrentWorker(1);
        assertNotNull(c);
        p.get(0).getCard().setActive(true);
        Board b = new Board();
        for(Cell cell:b.getField()){
            if(cell.getRow() == 0 && cell.getColumn() == 0)
                cell.setLevel(1);
            else if(cell.getRow() == 0 && cell.getColumn() == 1)
                cell.setLevel(1);
            else if(cell.getRow() == 1 && cell.getColumn() == 1)
                cell.setLevel(0);
            else if(cell.getRow() == 1 && cell.getColumn() == 0)
                cell.setLevel(1);
        }
        assertNotNull(p);
        assertNotNull(b);
        List<Cell> ret = p.get(0).getCard().checkMove(p,b);
        assertEquals(2, ret.size());

        //ret = c.checkMove(null,new Board());
        //assertEquals(0, ret.size());
    }


}