package it.polimi.ingsw.network;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.Observer;
import it.polimi.ingsw.commons.ClientMessage;
import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.commons.clientMessages.ModeChoseClient;
import it.polimi.ingsw.commons.clientMessages.ReConnectionClient;
import it.polimi.ingsw.commons.serverMessages.CurrentStatusServer;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.commons.Status;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class VirtualView extends Observable implements Observer {

    /**
     * The main server
     */
    private Server server;

    /**
     * True if the game is ended
     */
    private boolean ended;

    /**
     * All players in this game
     */
    private HashMap<String, ServerClientHandler> connectedPlayers;

    /**
     * Current status of the game, start from NAME_CHOICE
     */
    private Status currentStatus;

    /**
     * Last message from server to client
     */
    private ServerMessage lastMessage;

    private ArrayList<String> losers;

    /**
     * Constructor used for a new game
     * @param server the SERVER
     */
    public VirtualView(Server server){
        this.server = server;

        this.ended = false;
        this.connectedPlayers = new HashMap<>();

        this.currentStatus = Status.NAME_CHOICE;

        // FIXME remove Controller attribute
        c = new Controller(this);
        addObserver(c);
        //addObserver(new Controller(this));

        this.losers = new ArrayList<>();
    }

    /**
     * Constructor used for load game
     * @param server the SERVER
     * @param match the MATCH
     */
    public VirtualView(Server server, Match match, ServerMessage lastMessage){
        this.server = server;
        this.ended = match.isEnded();

        this.connectedPlayers = new HashMap<>();
        for(Player p : match.getPlayers())
            connectedPlayers.put(p.getName(),null);

        this.currentStatus = match.getStatus();
        addObserver(new Controller(this,match));

        this.lastMessage=lastMessage;

        this.losers = new ArrayList<>();
    }

    // It is used to run some tests about VirtualView and Controller communication
    // FIXME remove
    @Deprecated
    public Controller c;

    protected HashMap<String, ServerClientHandler> getConnectedPlayers(){ return connectedPlayers; }

    public Status getCurrentStatus() {
        return currentStatus;
    }

    public ArrayList<String> getLosers() { return losers; }

    /**
     * This method sends the message to the controller
     * @param message the message to send
     */
    protected void notify(ClientMessage message) {
        if(message instanceof ModeChoseClient){ // if it is a new player i add to list
            connectedPlayers.put(message.name,((ModeChoseClient) message).sch);
        } else if(message instanceof ReConnectionClient){ // if it is a new player i add to list
            connectedPlayers.remove(message.name);
            connectedPlayers.put(message.name,((ReConnectionClient) message).sch);
            if(!this.getConnectedPlayers().containsValue(null)){
                this.update(lastMessage);
            }
        }

        if (!ended) {
            notifyObservers(message);
        }
    }

    /**
     * This method get a message from model and forward it property client
     * @param arg a ServerMessage instance
     */
    @Override
    public void update(Object arg) {
        if( ! (arg instanceof ServerMessage))
            throw new RuntimeException("This must be a ServerMessage object");

        ServerMessage sm = (ServerMessage) arg;
        if(sm instanceof CurrentStatusServer)
            currentStatus = ((CurrentStatusServer) sm).status;

        if(server != null){
            if(sm.name.equals("")){
                server.sendAll(sm,this);
            }
            else{
                server.send(sm,this);
            }
        }
    }

}
