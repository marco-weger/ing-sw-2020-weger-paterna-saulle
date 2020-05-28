package it.polimi.ingsw.controller;

import it.polimi.ingsw.Observer;
import it.polimi.ingsw.commons.ClientMessage;
import it.polimi.ingsw.commons.Status;
import it.polimi.ingsw.commons.clientmessages.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.CardName;
import it.polimi.ingsw.network.VirtualView;

import java.io.*;
import java.util.*;

public class Controller implements Observer, ClientMessageHandler {

    /**
     * The MODEL
     * The CONTROLLER is the only one allowed to modify the MODEL
     */
    private Match match;
    private VirtualView virtualView;

    public Controller(VirtualView virtualView) {
        this.virtualView = virtualView;
        boolean go = false;
        int id;
        do {
            id = 1 + Math.abs(new Random().nextInt(9999998));
            if(new File("saved-match").listFiles() != null)
                for(File file: Objects.requireNonNull(new File("saved-match").listFiles()))
                    if(file.getName().contains(String.format("%07d" , id)))
                        go = true;
        }while(go);
        this.match = new Match(id,virtualView);
    }

    /**
     * Constructor used for load game
     * @param virtualView the VIRTUALVIEW
     * @param match the MATCH
     */
    public Controller(VirtualView virtualView, Match match) {
        this.virtualView = virtualView;
        this.match = match;
        this.match.clearObserver();
        this.match.addObserver(virtualView);
        for(Player p : this.match.getPlayers()){
            p.clearObserver();
            p.addObserver(virtualView);
            p.getCard().clearObserver();
            p.getCard().addObserver(virtualView);
        }
    }

    public VirtualView getVirtualView() {
        return virtualView;
    }

    public void setVirtualView(VirtualView virtualView) {
        this.virtualView = virtualView;
    }

    public Match getMatch() {
        return this.match;
    }

    public void setMatch(Match match) { this.match = match; }

    @Override
    public void update(Object arg){
        if( ! (arg instanceof ClientMessage))
            throw new RuntimeException("This must be an ClientMessage object");

        ClientMessage cm = (ClientMessage) arg;
        cm.accept(this);
    }

    @Override
    public void handleMessage(ConnectionClient message) {

    }


    /**
     * Disconnection is handled by removing someone from the list of players
     * if it happens before the start of the game, else it counts as losing.
     * @param message sent when someone disconnects from server
     */
    @Override
    public void handleMessage(DisconnectionClient message) {
        if(this.match.getStatus().equals(Status.NAME_CHOICE)){
            this.match.removePlayer(message.name);
        }
        else{
            Player loser = null;
            for(Player p : match.getPlayers())
                if(p.getName().equals(message.name))
                    loser = p;
            match.setLosers(new ArrayList<>(Collections.singletonList(loser)),message.isTimesUp);
            startTurn(false);
        }
    }


    /**
     * Reconnection notifies that the player has returned to the game
     * @param message sent when someone reconnects on server
     */
    @Override
    public void handleMessage(ReConnectionClient message) {
        match.playerReConnection(message.name,message.type);
    }


    /**
     * An empty message to test the reachability of a Client
     * @param event
     */
    @Override
    public void handleMessage(PingClient event) {
    }


    /**
     * send the challenger cards picked from the deck
     * @param message contains the name of the challenger and the list of the cards picked from the deck
     */
    @Override
    public void handleMessage(ChallengerChoseClient message) {
        if(match.getStatus().compareTo(Status.CARD_CHOICE) == 0
                && message.name.equals(match.getPlayers().get(0).getName())
                && message.c.size() == match.getPlayers().size()
                && match.getSelectedCard().isEmpty()
        ){
            ArrayList<CardName> tmp = new ArrayList<>();
            for(int i = 0; i < match.getPlayers().size(); i++)
                tmp.add(message.c.get(i));

            match.setSelectedCards(tmp);
        }
    }


    /**
     * send the card chosen by the current player from the picked by he challenger during the initialize phase of the match
     * @param message contains the name of the current player and the chard chosen from the list picked by the challenger
     */
    @Override
    public void handleMessage(PlayerChoseClient message) {
        String who = "";
        for(Player p:match.getPlayers())
            if(p.getCard() == null)
                who = p.getName();

        if(match.getStatus().compareTo(Status.CARD_CHOICE) == 0
                && message.name.equals(who)
                && match.getSelectedCard().contains(message.c)
        )
        {
            match.getSelectedCard().remove(message.c);
            for (Player p:match.getPlayers()){
                if(p.getName().equals(who)){
                    p.setCard(message.c,virtualView);
                }
            }
            if(match.getSelectedCard().size() == 1){
                // start worker initializing
                match.getPlayers().get(0).setCard(match.getSelectedCard().get(0),virtualView);
                match.getSelectedCard().remove(match.getSelectedCard().get(0));

                if(match.getSelectedCard().isEmpty())
                    match.setStatus(Status.WORKER_CHOICE);
            }
            else {
                match.setSelectedCards(match.getSelectedCard());
            }
        }
    }


    /**
     * send the start posizion of the current worker chosen by the current player during the initialize state of the match
     * @param message contains the name of the current player and the start position chosen for the current worker
     */
    @Override
    public void handleMessage(WorkerInitializeClient message) {
        Player selected = null;
        int i=0;
        while(i<match.getPlayers().size()){
            if(match.getPlayers().get(i).getName().equals(message.name) && (match.getPlayers().get(i).getWorker1() == null || match.getPlayers().get(i).getWorker2() == null)){
                selected = match.getPlayers().get(i);
                i=match.getPlayers().size()+1;
            }
            else if(!match.getPlayers().get(i).getName().equals(message.name) && (match.getPlayers().get(i).getWorker1() == null || match.getPlayers().get(i).getWorker2() == null)){
                selected = null;
                i=match.getPlayers().size()+1;
            }
            else if(!match.getPlayers().get(i).getName().equals(message.name) && match.getPlayers().get(i).getWorker1() != null && match.getPlayers().get(i).getWorker2() != null){
                i++;
            }
        }
        if(selected != null){
            if(selected.getWorker1() == null)
                selected.setWorker1(new Worker(message.x,message.y));
            else selected.setWorker2(new Worker(message.x,message.y));
            if(match.getPlayers().get(match.getPlayers().size()-1).getWorker2() != null)
                startTurn(false);
        }
    }


    /**
     * send the current worker chosen by the current player
     * @param message contains the name of the current player and the current worker chosen
     */
    @Override
    public void handleMessage(WorkerChoseClient message) {
        if(match.getCurrentPlayer().getName().equals(message.name) && match.getStatus().compareTo(Status.START) == 0){
            match.setStatus(match.getCurrentPlayer().getCard().getNextStatus(match.getStatus()));
            match.getCurrentPlayer().setCurrentWorker(message.worker);

            if(match.getCurrentPlayer().getCard().isQuestion()
                    && match.getCurrentPlayer().getCard().getStatus().compareTo(match.getStatus()) == 0
                    && match.getCurrentPlayer().getCard().activable(match.getPlayers(),match.getBoard())
            )
            {
                match.getCurrentPlayer().doQuestion();
            }
            else{
                match.setStatus(match.getCurrentPlayer().getCard().getNextStatus(match.getStatus()));
                match.getCurrentPlayer().getCard().getCheckMove(match.getPlayers(),match.getBoard());
            }
        }
    }


    /**
     * send the answer to the player if the card need it (ability == true)
     * @param message contains the name of the current player, if the ability is passive or not and the current status
     */

    @Override
    public void handleMessage(AnswerAbilityClient message) {
        if(match.getCurrentPlayer().getName().compareTo(message.name) == 0 && match.getStatus().compareTo(message.type) == 0) {
            match.getCurrentPlayer().getCard().setActive(message.ability);
            match.setStatus(match.getCurrentPlayer().getCard().getNextStatus(match.getStatus()));
            if (match.getStatus().compareTo(Status.QUESTION_M) == 0)
                match.getCurrentPlayer().getCard().getCheckMove(match.getPlayers(), match.getBoard());
            else if (match.getStatus().compareTo(Status.QUESTION_B) == 0)
                match.getCurrentPlayer().getCard().getCheckBuild(match.getPlayers(), match.getBoard()); }
        //TODO rimuvoere dopo i test
        else{
            match.setStatus(match.getCurrentPlayer().getCard().getNextStatus(match.getStatus()));
        }
    }


    /**
     * If the player can move into that cell, with this method he can move into it.
     * after it, if the player have won, end the game
     * if the player God is Arthemis, ask the question to the players
     * @param message a message ClientToServer with the name of the player and the cell where him wants to move.
     */
    @Override
    public void handleMessage(MoveClient message) {
        if(match.getCurrentPlayer().getName().equals(message.name) && match.getStatus().compareTo(Status.QUESTION_M) == 0){
            Cell from = match.getBoard().getCell(match.getCurrentPlayer().getCurrentWorker().getRow(),match.getCurrentPlayer().getCurrentWorker().getColumn());
            if(match.getCurrentPlayer().getCard().move(match.getPlayers(),match.getBoard(),match.getBoard().getCell(message.x,message.y))){
                if(match.getCurrentPlayer().getCard().checkWin(from,match.getBoard().getCell(message.x,message.y))){
                    //caso with currentplayerwin
                    endMatch(match.getCurrentPlayer());
                    return;
                }
                match.setStatus(match.getCurrentPlayer().getCard().getNextStatus(match.getStatus()));
                if(match.getCurrentPlayer().getCard().isQuestion()
                        && match.getCurrentPlayer().getCard().getStatus().compareTo(match.getStatus()) == 0
                        && match.getCurrentPlayer().getCard().activable(match.getPlayers(),match.getBoard()))
                {
                    // Here i made BUILD question if necessary
                    match.getCurrentPlayer().doQuestion();
                }
                else
                {
                    match.getCurrentPlayer().getCard().getCheckBuild(match.getPlayers(),match.getBoard());
                    match.setStatus(match.getCurrentPlayer().getCard().getNextStatus(match.getStatus()));
                }
            }
        }
    }


    /**
     * If the player Can Build in the cell, build a tower.
     * After if the player have Prometehus card with the ability on goes to QUESTION_M
     * else, go to END and close the turn.
     * @param message a message ClientToServer with the name of the player and the cell where him wants to build.
     */
    @Override
    public void handleMessage(BuildClient message) {
        if(match.getCurrentPlayer().getName().equals(message.name) && match.getStatus().compareTo(Status.QUESTION_B) == 0
                && match.getCurrentPlayer().getCard().build(match.getPlayers(),match.getBoard(),match.getBoard().getCell(message.x,message.y))){
            match.setStatus(match.getCurrentPlayer().getCard().getNextStatus(match.getStatus()));
            if(match.getStatus().equals(Status.BUILT))

            {
                startTurn(true);
                return;
            }
            else if(match.getStatus().equals(Status.QUESTION_M))

            {
                match.getCurrentPlayer().getCard().getCheckMove(match.getPlayers(), match.getBoard());
                return;
            }
            else if(match.getStatus().equals(Status.QUESTION_B))
            {
                match.getCurrentPlayer().getCard().getCheckBuild(match.getPlayers(), match.getBoard());
                return;
            }
        }
        if(match.getCurrentPlayer().getCard().build(match.getPlayers(),match.getBoard(),match.getBoard().getCell(message.x,message.y))){
            startTurn(true);
        }

    }


    /**
     * If the lobby is "not full", add player to the current lobby
     * (the Lobby can't properly be full because the game starts immediately after the quota for the mode
     * is reached, thus it start a new Lobby when the current is full and start the game for the
     * ex-current Lobby).
     * @param message contains the name of the current player, and the mode that he has chosen
     */
    @Override
    public void handleMessage(ModeChoseClient message) {
        if(message.refused){
            String path = System.getProperty("user.dir")+File.separatorChar+"resources" +File.separatorChar+"saved-match" + File.separatorChar + String.format("%07d" , this.match.getId())+".santorini";
            File f = new File(path);
            if(f.exists())
                f.delete();
            this.match.setEnded(true);
        } else {
            if(match.getPlayers().size() < message.mode) {
                match.addPlayer(new Player(message.name,virtualView),message.forced);
            }

            if(match.getPlayers().size() == message.mode) {
                startMatch();
            }
        }
    }

    @Override
    public void handleMessage(EasterEggClient easterEggClient) {
        match.activeEasterEgg(easterEggClient.name);
    }


    /**
     * Move all the loser players into the loser list and call the end of the Match
     *@param winner the player who have win the match
     */
    private void endMatch(Player winner){
        ArrayList<Player> losers = new ArrayList<>();
        for(Player p : match.getPlayers())
            if (p.getName().compareTo(winner.getName()) != 0)
                losers.add(p);
        match.setLosers(losers,false);
    }


    /**
     * Set the match status to CARD_CHOICE
     */
    private void startMatch() {
        match.setStatus(Status.CARD_CHOICE);
    }


    /**
     *this method handle the turn, if the goOn is true, close the currentplayer turn and set the next player
     * instead, if the goOne is false, check if the current player have won --> (in case End the game)
     * if the GoOne is false, but the player doesn't have won, check if the player have lost --> (in case move the player into loser list)
     * @param goOn if true goes to the next player, if false initialize the turn
     */
    // FIXME this method must be private (to check tests)
    public void startTurn(boolean goOn){
        if(!match.isEnded()){
            if(goOn) match.setNextPlayer();

            if(match.getCurrentPlayer().getCard() != null)
                match.getCurrentPlayer().getCard().initializeTurn();
            if(match.checkCurrentPlayerWin()) {
                endMatch(match.getCurrentPlayer());
            }
            else{
                if(match.getCurrentPlayer().getCard().hasLost(match.getPlayers(),match.getBoard())){
                    match.setLosers(new ArrayList<>(Collections.singletonList(match.getCurrentPlayer())),false);
                    startTurn(false);
                }
                else{
                    match.setStatus(Status.START);
                }
            }
        }
    }
}
