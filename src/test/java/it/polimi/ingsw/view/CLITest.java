package it.polimi.ingsw.view;

import it.polimi.ingsw.commons.SnapCell;
import it.polimi.ingsw.commons.SnapWorker;
import it.polimi.ingsw.model.cards.CardName;
import it.polimi.ingsw.network.Client;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

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
        //c.getWorkers().add(new SnapWorker(2,0,"FRA",1));
        //c.getWorkers().add(new SnapWorker(0,1,"FRA",2));
        CLI view = new CLI(c);
        c.getPlayers().add(new SnapPlayer("MARCO"));
        c.getPlayers().add(new SnapPlayer("GIULIO"));
        c.getPlayers().get(0).symbol= "@";
        c.getPlayers().get(0).symbol= "+";
        // OK COLOR
        c.getPlayers().get(0).loser = true;
        //c.getPlayers().add(new SnapPlayer("FRA",c.getMyCode(),2));
        c.getPlayers().get(0).card = CardName.ARTEMIS;
        c.getPlayers().get(1).card = CardName.MINOTAUR;
        c.getPlayers().get(0).card = CardName.PAN;
        c.setUsername(c.getPlayers().get(0).name);
        view.printTable();
    }
    /*
    @Test
    public void printTitleBoardTest(){
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
        c.getPlayers().add(new SnapPlayer("MARCO",c.getMyCode(),0));
        c.getPlayers().add(new SnapPlayer("GIULIO",c.getMyCode(),1));
        c.getPlayers().add(new SnapPlayer("FRA",c.getMyCode(),2));
        c.getPlayers().get(2).card = CardName.ARTEMIS;
        c.getPlayers().get(1).card = CardName.MINOTAUR;
        //view.printTable();
        view.printTitle();
        //view.printTable();
    }
    @Test
    public void printCard(){
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
        c.getPlayers().add(new SnapPlayer("MARCO",c.getMyCode(),0));
        c.getPlayers().add(new SnapPlayer("GIULIO",c.getMyCode(),1));
        c.getPlayers().add(new SnapPlayer("FRA",c.getMyCode(),2));
        view.clear();
        view.printTitle();
        view.printLobby(false);
        CLI.printCard(new ArrayList<>(Arrays.asList(CardName.values())));
    }
     */
}
