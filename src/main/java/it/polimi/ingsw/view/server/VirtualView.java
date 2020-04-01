package it.polimi.ingsw.view.server;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.Observer;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;

public class VirtualView extends Observable implements Observer {

    private ArrayList<Player> playersName;

    @Override
    public void notifyObserver(Object arg) {}

}
