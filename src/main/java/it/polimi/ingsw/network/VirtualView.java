package it.polimi.ingsw.network;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.Observer;
import it.polimi.ingsw.commons.ClientMessage;
import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.commons.clientMessages.ConnectionClient;
import it.polimi.ingsw.commons.clientMessages.ReadyClient;
import it.polimi.ingsw.commons.serverMessages.CurrentStatusServer;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Status;

import java.util.ArrayList;

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
    private ArrayList<ServerClientHandler> connectedPlayers;

    /**
     * Current status of the game, start from NAME_CHOICE
     */
    private Status currentStatus;

    public VirtualView(Server server){
        this.server = server;
        this.ended = false;
        this.connectedPlayers = new ArrayList<>();
        this.currentStatus = Status.NAME_CHOICE;

        // FIXME remove Controller attribute
        c = new Controller(this);
        addObserver(c);
        //addObserver(new Controller(this));
    }

    // It is used to run some tests about VirtualView and Controller communication
    // FIXME remove
    @Deprecated
    public Controller c;

    protected ArrayList<ServerClientHandler> getConnectedPlayers(){ return connectedPlayers; }

    public Status getCurrentStatus() {
        return currentStatus;
    }

    /**
     * This method sends the message to the controller
     * @param message the message to send
     */
    protected void notify(ClientMessage message) {
        if(message instanceof ConnectionClient){ // if it is a new player i add to list
            connectedPlayers.add(((ConnectionClient) message).sch);
        }
        else if(message instanceof ReadyClient) // if it is ready i increment counter on Server
        {
            server.setReady(server.getReady()+1);
            if(server.getReady() == this.connectedPlayers.size() && server.getReady() > 1)
                ((ReadyClient) message).start = true;
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

        System.out.println("[SENT] - " + sm.toString().substring(sm.toString().lastIndexOf('.')+1,sm.toString().lastIndexOf('@')) + " - " + (sm.name.equals("") ? "ALL" : sm.name));

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
