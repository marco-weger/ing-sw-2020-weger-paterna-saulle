package it.polimi.ingsw.network;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.Observer;
import it.polimi.ingsw.commons.ClientMessage;
import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.commons.Status;
import it.polimi.ingsw.commons.clientmessages.ModeChoseClient;
import it.polimi.ingsw.commons.servermessages.*;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;

public class VirtualView extends Observable implements Observer {

    /**
     * The main server
     */
    private final Server server;

    /**
     * True if the game is ended
     */
    private boolean ended;

    /**
     * All players in this game
     */
    private final HashMap<String, ServerClientHandler> connectedPlayers;

    /**
     * Current status of the game, start from NAME_CHOICE
     */
    private Status currentStatus;

    /**
     * Last message from server to client
     */
    private final ServerMessage lastMessage;

    /**
     * List of losers
     */
    private final List<String> losers;

    /**
     * Timer used to manage single turn
     */
    private Timer turn;

    // It is used to run some tests about VirtualView and Controller communication
    //@Deprecated
    //public Controller c;

    /**
     * Constructor used for a new game
     * @param server the SERVER
     */
    public VirtualView(Server server){
        this.server = server;
        this.ended = false;
        this.connectedPlayers = new HashMap<>();
        this.currentStatus = Status.NAME_CHOICE;
        this.lastMessage = null;
        //c = new Controller(this);
        //addObserver(c);
        addObserver(new Controller(this));
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
        for(Player p : match.getLosers())
            this.losers.add(p.getName());
    }

    /**
     * @return has map of connected players
     */
    protected HashMap<String, ServerClientHandler> getConnectedPlayers(){ return connectedPlayers; }

    /**
     * @return current match status
     */
    public Status getCurrentStatus() { return currentStatus; }

    /**
     * @return list of players who has lost
     */
    public List<String> getLosers() { return losers; }

    /**
     * @return true if the match is ended
     */
    public boolean isEnded() { return ended; }

    /**
     * @return the timer of a single turn
     */
    public Timer getTurn(){ return turn;}

    /**
     * This method sends the message to the controller
     * @param message the message to send
     */
    protected void notify(ClientMessage message) {
        if(message instanceof ModeChoseClient){ // if it is a new player i add to list
            connectedPlayers.put(message.name,((ModeChoseClient) message).sch);
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
        if(sm instanceof CurrentStatusServer){
            currentStatus = ((CurrentStatusServer) sm).status;

            if((currentStatus.equals(Status.START) || currentStatus.equals(Status.WORKER_CHOICE)) && server != null){
                ((CurrentStatusServer) sm).timer = server.getTurnTimer();
                timerHandler(((CurrentStatusServer) sm).player); // start timer
            } else if(currentStatus.equals(Status.END)){
                ended = true;
                try{
                    turn.cancel(); // Delete last timer if exists
                } catch (Exception ignored){}
            }
        }
        else if(sm instanceof AvailableCardServer || sm instanceof WorkerChosenServer){
            timerHandler(sm.name); // start timer
        }else if(sm instanceof SomeoneWinServer){
            for(String name : connectedPlayers.keySet())
                if(!name.equals(((SomeoneWinServer) sm).player))
                    losers.add(name);
        }else if(sm instanceof SomeoneLoseServer){
            for(String name : connectedPlayers.keySet())
                if(name.equals(((SomeoneLoseServer) sm).player))
                    losers.add(name);
        }

        if(server != null && currentStatus != Status.END){
            if(sm.name.equals("")){
                server.sendAll(sm,this);
            }
            else{
                server.send(sm,this);
            }
        }
    }

    /**
     * It cancels last timer and run a new timer
     * @param player of the timer to be started
     */
    public void timerHandler(String player){
        // TIMER - Now the timer starts at the beginning of the turn and it runs for all the turn
        try{
            turn.cancel(); // Delete last timer if exists
        } catch (Exception ignored){}
        turn = new Timer();
        for(ServerClientHandler sch : getConnectedPlayers().values())
            if(sch != null && sch.getName().equals(player) && sch.isStillConnected())
                turn.schedule(new TimerTurn(sch), server.getTurnTimer()*1000);
    }

}
