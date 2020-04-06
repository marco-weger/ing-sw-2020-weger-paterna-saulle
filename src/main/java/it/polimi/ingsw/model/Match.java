package it.polimi.ingsw.model;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.commons.serverMessages.AvailableCardServer;
import it.polimi.ingsw.commons.serverMessages.CurrentStatusServer;
import it.polimi.ingsw.commons.serverMessages.SomeoneLoseServer;
import it.polimi.ingsw.commons.serverMessages.SomeoneWinServer;
import it.polimi.ingsw.model.cards.CardName;

import java.util.ArrayList;

public class Match extends Observable {

    /**
     * Unique id to discriminate the match
     */
    private int id;

    /**
     * The board
     */
    private Board board;

    /**
     * List of players (2 or 3)
     */
    private ArrayList<Player> players;

    /**
     * List of losers
     */
    private ArrayList<Player> losers;

    /**
     * Status of the match
     */
    private Status status;

    private boolean ended;

    private ArrayList<CardName> selectedCard;

    public Match(int id){
        this.id=id;
        this.board=new Board();
        this.ended=false;
        this.status=Status.CARD_CHOICE;
        this.players = new ArrayList<>();
        this.losers = new ArrayList<>();
        this.selectedCard = new ArrayList<>();
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isEnded() {
        return ended;
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
    }

    public Status getStatus() {
        return status;
    }

    /**
     * When the status changes the view will be notified with current one and current player
     * @param status the new status
     */
    public void setStatus(Status status) {
        this.status = status;
        if(getCurrentPlayer() != null)
            notifyObservers(new CurrentStatusServer(getCurrentPlayer().getName(),status));
        else
            notifyObservers(new CurrentStatusServer("",status));
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public ArrayList<Player> getLosers() {
        return losers;
    }

    /**
     * A method that add a player into the Loser List and remove it from the Active Player List
     * @param p select a player
     */
    //FIXME discutere su SetLoser, se è invocato solo da endGame e da startTurn va bene cosi
    public void setLosers(Player p) {
      /*  if(p.isActive())
            players.get((players.indexOf(getCurrentPlayer())+1)%3).setActive(true);
        getLosers().add(p);
        getPlayers().remove(p);*/

      getLosers().add(p);
      p.setActive(false);
        notifyObservers(new SomeoneLoseServer(p.getName()));
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public ArrayList<CardName> getSelectedCard(){ return selectedCard; }

    /**
     * A method that pick the card from the deck and notify the third player to choose.
     * @param selectedCard the list of card selected
     */
    public void setSelectedCards(ArrayList<CardName> selectedCard){
        this.selectedCard = new ArrayList<>();
        this.selectedCard.addAll(selectedCard);
        notifyObservers(new AvailableCardServer(selectedCard));
    }

    /**
     * Current player getter
     */
    public Player getCurrentPlayer(){
        for(Player p:players)
            if(p.isActive())
                return p;
        return null;
    }

    /**
     * A method to set next player
     */
    public void setNextPlayer() {
        if(getCurrentPlayer() != null){
            int i = players.indexOf(getCurrentPlayer());
            getCurrentPlayer().setActive(false);
            if (i < players.size()-1) {
                players.get(i + 1).setActive(true);
            } else {
                players.get(0).setActive(true);
            }
        }
        else{
            players.get(0).setActive(true);
        }
    }

    /**
     * @return It verifies if the current player win for other players defeat
     */
    //FIXME Meglio contare il numero dei player eliminati, che rimuovere i player dalla lista
    //FIXME altrimenti addio alla modalità spettatore, e il parametro active diventa inutile
    public boolean checkCurrentPlayerWin() {
        if(losers.size() == players.size()-1){
            notifyObservers(new SomeoneWinServer(players.get(0).getName()));
            return true;
        }
        return false;
    }
}

