package it.polimi.ingsw.card;

import it.polimi.ingsw.cards.Card;
import it.polimi.ingsw.cards.CardName;
import it.polimi.ingsw.cards.FactoryCard;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Cell;
import org.junit.Test;

import static org.junit.Assert.*;

public class CardTest {
    // checkWin
    @Test
    public void checkWin_cellNull()
    {
        Card c = FactoryCard.getCard(CardName.APOLLO);
        assertNotNull(c);
        assertFalse(c.checkWin(null,new Cell(0,0,0)));
        assertFalse(c.checkWin(new Cell(0,0,0),null));
    }
    @Test
    public void checkWin_cellOut()
    {
        Card c = FactoryCard.getCard(CardName.APOLLO);
        assertNotNull(c);
        assertFalse(c.checkWin(new Cell(5,5,0),new Cell(0,0,0)));
        assertFalse(c.checkWin(new Cell(0,0,0),new Cell(5,5,0)));
    }
    @Test
    public void checkWin_win()
    {
        Card c = FactoryCard.getCard(CardName.APOLLO);
        assertNotNull(c);
        assertTrue(c.checkWin(new Cell(4,5,2),new Cell(5,5,3)));
    }
    @Test
    public void checkWin_lose()
    {
        Card c = FactoryCard.getCard(CardName.APOLLO);
        assertNotNull(c);
        assertTrue(c.checkWin(new Cell(4,5,3),new Cell(5,5,3)));
    }
}
