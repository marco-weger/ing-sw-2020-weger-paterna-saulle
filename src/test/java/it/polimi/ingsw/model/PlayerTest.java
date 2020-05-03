package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.CardName;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class PlayerTest {
    // setter
    @Test
    public void setter()
    {
        Player p = new Player("player1",null);
        p.setName("Marco");
        assertEquals(p.getName(),"Marco");
        p.setWorker1(new Worker(2,2));
        p.setWorker2(new Worker(3,1));
        assertEquals(p.getWorker1().getRow(),2);
        assertEquals(p.getWorker1().getColumn(),2);
        assertEquals(p.getWorker2().getRow(),3);
        assertEquals(p.getWorker2().getColumn(),1);
        p.setCard(CardName.APOLLO,null);
        assertEquals(p.getCard().getName(), CardName.APOLLO);
    }
    // current worker
    @Test
    public void current_worker(){
        Player p = new Player("player1",null);
        p.setWorker1(new Worker(0,0));
        p.setWorker2(new Worker(1,1));
        p.setCurrentWorker(1);
        assertEquals(p.getWorker1(), p.getCurrentWorker());
        p.setCurrentWorker(2);
        assertEquals(p.getWorker2(), p.getCurrentWorker());
    }
}