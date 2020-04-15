package it.polimi.ingsw.view;

import it.polimi.ingsw.commons.SnapCell;
import it.polimi.ingsw.commons.SnapWorker;
import it.polimi.ingsw.network.Client;
import org.junit.Test;

public class CLITest {
    @Test
    public void printBoardTest(){
        Client c = new Client();
        for(SnapCell sc : c.getBoard())
            sc.level = sc.row;
        c.getWorkers().add(new SnapWorker(0,0,"MARCO",1));
        CLI view = new CLI(c);
        view.getPlayers().add("MARCO");
        view.boardPrint();
    }
}
