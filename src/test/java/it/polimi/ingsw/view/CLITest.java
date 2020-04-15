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
        c.getWorkers().add(new SnapWorker(0,2,"MARCO",2));
        c.getWorkers().add(new SnapWorker(1,4,"GIULIO",1));
        c.getWorkers().add(new SnapWorker(3,4,"GIULIO",2));
        c.getWorkers().add(new SnapWorker(2,0,"FRA",1));
        c.getWorkers().add(new SnapWorker(0,1,"FRA",2));
        CLI view = new CLI(c);
        view.getPlayers().add("MARCO");
        view.getPlayers().add("GIULIO");
        view.getPlayers().add("FRA");
        view.boardPrint();
    }
}
