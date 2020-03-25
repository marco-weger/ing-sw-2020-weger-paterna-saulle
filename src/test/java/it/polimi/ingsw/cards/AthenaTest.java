package it.polimi.ingsw.cards;


import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Worker;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class AthenaTest{


    @Test
    public void inizializeTurn_turnoffpower()
    {
        Card c = FactoryCard.getCard(CardName.ATHENA);
        List<Player> p = new ArrayList<>();
        p.add(new Player("player1",CardName.APOLLO,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player2",CardName.ARTEMIS,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player3",CardName.ATLAS,new Worker(0,0),new Worker(0,0)));
        assertNotNull(c);
        p.get(0).getCard().setActive(true);
        c.inizializeTurn();
        assertFalse(c.isActive());
    }


    @Test
    public void inizializeTurn_NOpower()
    {
        Card c = FactoryCard.getCard(CardName.ATHENA);
        List<Player> p = new ArrayList<>();
        p.add(new Player("player1",CardName.APOLLO,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player2",CardName.ARTEMIS,new Worker(0,0),new Worker(0,0)));
        p.add(new Player("player3",CardName.ATLAS,new Worker(0,0),new Worker(0,0)));
        assertNotNull(c);
        p.get(0).getCard().setActive(false);
        c.inizializeTurn();
        assertFalse(c.isActive());
    }
}