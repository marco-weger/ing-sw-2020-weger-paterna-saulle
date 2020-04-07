package it.polimi.ingsw.network;

import com.sun.security.ntlm.Client;
import it.polimi.ingsw.Observable;
import it.polimi.ingsw.Observer;
import it.polimi.ingsw.commons.ClientMessage;
import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.commons.serverMessages.*;
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
    }

    /**
     * this method sends the message to the controller
     * @param message message to send
     */
    public void notify(ClientMessage message) {
        if (!ended) {
            System.out.println("---> FROM CLI TO CONTROLLER\n");
            System.out.println("Type: " + message.toString() + "\n");
            System.out.println("Sender: " + message.name + "\n");
            // TODO: implements setChanged and notify
            //setChanged();
            //notifyObservers(message);
            notifyObservers(message);
            System.out.println("== : == : == : == : == : == : == : ==");
        }
    }

    @Override
    public void update(Object arg) {
        if( ! (arg instanceof ServerMessage))
            throw new RuntimeException("This must be a ServerMessage object");
        ServerMessage cm = (ServerMessage) arg;
        System.out.println("---> FROM MODEL TO CLI\n");
        System.out.println("Type: " + cm.toString() + "\n");

        if(cm.name.equals("")){
            // TODO: send message to all
            System.out.println("Receiver: ALL (empty string)\n");
        }
        else {
            // TODO: send message to signle player
            System.out.println("Receiver: " + cm.name + "\n");
        }
    }

}
