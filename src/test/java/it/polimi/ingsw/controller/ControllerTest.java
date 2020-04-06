package it.polimi.ingsw.controller;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.CardName;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import static org.junit.Assert.*;

public class ControllerTest {

    Board b = new Board();

    ArrayList<Player> p = new ArrayList<>(Arrays.asList(
            new Player("Marco"),
            new Player("Francesco"),
            new Player("Giulio")
    ));

    ArrayList<CardName> s = new ArrayList<>(Arrays.asList(
            CardName.ATHENA,CardName.PAN,CardName.APOLLO
    ));

    //ended = false, losers = empty [TEST CONSTRUCTOR]
    Match m = new Match(42, b, Status.CARD_CHOICE, p , s);
    @Test
    public void update() {
    }

    @Test
    public void handleMessage() {
    }

    @Test
    public void testHandleMessage() {
    }

    @Test
    public void testHandleMessage1() {
    }

    @Test
    public void testHandleMessage2() {
    }

    @Test
    public void testHandleMessage3() {
    }

    @Test
    public void testHandleMessage4() {
    }

    @Test
    public void testHandleMessage5() {
    }

    @Test
    public void testHandleMessage6() {
    }

    @Test
    public void testHandleMessage7() {
    }

    @Test
    public void testHandleMessage8() {
    }

    @Test
    public void testHandleMessage9() {
    }

    @Test
    public void endGameTest() {
        Player winner = p.get(0);
    //    setMatch(m);
    //    m.endGame(winner);
     //   assertTrue(m.endGame(winner);
    }

    @Test
    public void inizializeMatch() {
    }

    @Test
    public void startTurn() {
    }
}