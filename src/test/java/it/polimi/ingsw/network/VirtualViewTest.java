package it.polimi.ingsw.network;

import it.polimi.ingsw.commons.clientMessages.*;
import it.polimi.ingsw.model.Match;

import it.polimi.ingsw.commons.Status;
import it.polimi.ingsw.model.cards.CardName;
import org.junit.Test;

import java.util.Arrays;
import java.util.ArrayList;

import static org.junit.Assert.*;


public class VirtualViewTest {

    VirtualView vv;
    //It tests match initialization and first turn by ATLAS
    @Test
    public void AtlasCompleteTurnTEST()
    {
        vv = new VirtualView(null);
        vv.c.setMatch(new Match(43,vv));
        assertNotNull(vv.c.getMatch());
        assertEquals(vv.c.getMatch().getId(),43);
        vv.notify(new ConnectionClient("Marco"));
        vv.notify((new ModeChoseClient("Marco",3)));
        vv.notify(new ConnectionClient("Giulio"));
        vv.notify((new ModeChoseClient("Giulio",3)));
        vv.notify(new ConnectionClient("Francesco"));
        vv.notify((new ModeChoseClient("Francesco",3)));
        assertEquals(vv.c.getMatch().getStatus(), Status.CARD_CHOICE);
        vv.notify(new ChallengerChoseClient("Marco", new ArrayList<>(Arrays.asList(CardName.MINOTAUR, CardName.ATLAS, CardName.DEMETER))));
        assertEquals(vv.c.getMatch().getSelectedCard().size(), 3);
        vv.notify(new PlayerChoseClient("Francesco", CardName.DEMETER));
        vv.notify(new PlayerChoseClient("Giulio", CardName.MINOTAUR));
        assertEquals(vv.c.getMatch().getSelectedCard().size(), 0);
        assertEquals(vv.c.getMatch().getPlayers().get(0).getCard().getName(), CardName.ATLAS);
        assertEquals(vv.c.getMatch().getPlayers().get(1).getCard().getName(), CardName.MINOTAUR);
        assertEquals(vv.c.getMatch().getPlayers().get(2).getCard().getName(), CardName.DEMETER);
        assertEquals(Status.WORKER_CHOICE,vv.c.getMatch().getStatus());
        vv.notify(new WorkerInitializeClient("Francesco", 0, 0));
        vv.notify(new WorkerInitializeClient("Marco", 0, 3));
        vv.notify(new WorkerInitializeClient("Marco", 3, 0));
        vv.notify(new WorkerInitializeClient("Giulio", 0, 1));
        vv.notify(new WorkerInitializeClient("Giulio", 1, 0));
        vv.notify(new WorkerInitializeClient("Francesco", 2, 0));
        vv.notify(new WorkerInitializeClient("Francesco", 0, 2));
        assertEquals(vv.c.getMatch().getPlayers().get(0).getWorker1().getRow() + vv.c.getMatch().getPlayers().get(0).getWorker1().getColumn(), 3);
        assertEquals(vv.c.getMatch().getPlayers().get(0).getWorker1().getRow() + vv.c.getMatch().getPlayers().get(0).getWorker1().getColumn(), 3);
        assertEquals(vv.c.getMatch().getPlayers().get(1).getWorker1().getRow() + vv.c.getMatch().getPlayers().get(1).getWorker1().getColumn(), 1);
        assertEquals(vv.c.getMatch().getPlayers().get(1).getWorker1().getRow() + vv.c.getMatch().getPlayers().get(1).getWorker1().getColumn(), 1);
        assertEquals(vv.c.getMatch().getPlayers().get(2).getWorker1().getRow() + vv.c.getMatch().getPlayers().get(2).getWorker1().getColumn(), 2);
        assertEquals(vv.c.getMatch().getPlayers().get(2).getWorker1().getRow() + vv.c.getMatch().getPlayers().get(2).getWorker1().getColumn(), 2);
        assertEquals(Status.START,vv.c.getMatch().getStatus());
        System.out.println(vv.c.getMatch().getCurrentPlayer().getName());
        vv.notify(new WorkerChoseClient("Marco",2));
        assertEquals(3,vv.c.getMatch().getCurrentPlayer().getCurrentWorker().getRow());
        assertEquals(Status.QUESTION_M,vv.c.getMatch().getStatus());
        vv.notify(new MoveClient("Marco",4,4));
        vv.notify(new MoveClient("Marco",3,1));
        assertEquals(3,vv.c.getMatch().getCurrentPlayer().getCurrentWorker().getRow());
        assertEquals(1,vv.c.getMatch().getCurrentPlayer().getCurrentWorker().getColumn());
        assertEquals(Status.MOVED,vv.c.getMatch().getStatus());
        vv.notify(new AnswerAbilityClient("Marco",true,Status.MOVED));
        assertTrue(vv.c.getMatch().getPlayers().get(0).getCard().isActive());
        assertEquals(Status.QUESTION_B,vv.c.getMatch().getStatus());
        vv.notify(new BuildClient("Marco",4,1));
        assertEquals(4,vv.c.getMatch().getBoard().getCell(4,1).getLevel());
        assertEquals(vv.c.getMatch().getStatus(),Status.START);
        assertEquals(vv.c.getMatch().getCurrentPlayer().getName(),"Giulio");
    }

}
