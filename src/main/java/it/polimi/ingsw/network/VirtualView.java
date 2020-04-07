package it.polimi.ingsw.network;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.Observer;
import it.polimi.ingsw.commons.ClientMessage;
import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;

public class VirtualView extends Observable implements Observer {

    // TODO: the virtual view has to check name params of MESSAGE every time
    private ArrayList<Player> playersName;
    private Server server;
    private boolean ended;

    public VirtualView(Server server){
        this.server = server;
        this.ended = false;
        addObserver(new Controller(this));
    }

    /**
     * this method sends the message to the controller
     * @param message message to send
     */
    protected void notify(ClientMessage message) {
        if (!ended) {
            notifyObservers(message);
        }
    }

    @Override
    public void update(Object arg) {
        if( ! (arg instanceof ServerMessage))
            throw new RuntimeException("This must be a ServerMessage object");
        ServerMessage cm = (ServerMessage) arg;
        System.out.println("---> FROM MODEL TO CLI");
        System.out.println("Type: " + cm.toString());

        if(cm.name.equals("")){
            // TODO: send message to all
            System.out.println("Receiver: ALL (empty string)");
        }
        else {
            // TODO: send message to signle player
            System.out.println("Receiver: " + cm.name);
        }
        System.out.println("== : == : == : == : == : == : == : ==");
    }

}
