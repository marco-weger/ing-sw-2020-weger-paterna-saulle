package it.polimi.ingsw.model;
import it.polimi.ingsw.model.cards.CardName;
import org.junit.Test;

import static org.junit.Assert.*;


public class PlayerTest {

    @Test
    public void current_worker(){
        Player p = new Player("player1", CardName.HEPHASTUS,new Worker(0,0),new Worker(0,0));
        p.setCurrentWorker(1);
        assertEquals(p.getWorker1(), p.getCurrentWorker());
        p.setCurrentWorker(2);
        assertEquals(p.getWorker2(), p.getCurrentWorker());
    }


}