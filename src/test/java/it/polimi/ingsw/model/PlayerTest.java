package it.polimi.ingsw.model;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.CardName;
import it.polimi.ingsw.model.cards.FactoryCard;
import org.junit.Test;

import static org.junit.Assert.*;


public class PlayerTest {

    // setter
    @Test
    public void setter()
    {
        Player p = new Player("player1", CardName.HEPHASTUS,new Worker(0,0),new Worker(0,0));
        p.setName("Marco");
        assertEquals(p.getName(),"Marco");
        p.setWorker1(new Worker(2,2));
        p.setWorker2(new Worker(3,1));
        assertEquals(p.getWorker1().getRow(),2);
        assertEquals(p.getWorker1().getColumn(),2);
        assertEquals(p.getWorker2().getRow(),3);
        assertEquals(p.getWorker2().getColumn(),1);
        p.setLoser(true);
        assertTrue(p.isLoser());
        p.setCard(FactoryCard.getCard(CardName.APOLLO));
        assertEquals(p.getCard().getName(), CardName.APOLLO);
    }
    // current worker
    @Test
    public void current_worker(){
        Player p = new Player("player1", CardName.HEPHASTUS,new Worker(0,0),new Worker(0,0));
        p.setCurrentWorker(1);
        assertEquals(p.getWorker1(), p.getCurrentWorker());
        p.setCurrentWorker(2);
        assertEquals(p.getWorker2(), p.getCurrentWorker());
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        Player p = new Player("TEST",CardName.ARTEMIS,new Worker(0,0),new Worker(4,4));
        Player p2 = p.clone();
        p2.setCard(FactoryCard.getCard(CardName.APOLLO));
        p2.getWorker1().setColumn(1);
        assertEquals(p.getWorker1().getColumn(), 0);
        assertEquals(p2.getWorker1().getColumn(), 1);
        assertEquals(p.getCard().getName(), CardName.ARTEMIS);
    }
}