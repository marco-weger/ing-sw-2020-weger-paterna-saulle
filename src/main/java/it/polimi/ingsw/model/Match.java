package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.CardName;

import java.util.ArrayList;

public class Match {

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

    public void setStatus(Status status) {
        this.status = status;
        // TODO: notifica del current status alla view ed eventualmente (?) di chi è il turno (CurrentStatusServer)
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

    public void setLosers(Player p) {
        getLosers().add(p);
        getPlayers().remove(p);
        // TODO: notify the loser (SomeoneLoseServer)
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public ArrayList<CardName> getSelectedCard(){ return selectedCard; }

    public void setSelectedCards(ArrayList<CardName> selectedCard){
        this.selectedCard = new ArrayList<>();
        this.selectedCard.addAll(selectedCard);
        // TODO: notifica chiedendo al giocatore in ultima posizione la scelta (ServerMessageHandler)
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
    public boolean checkCurrentPlayerWin() {
        int deads = 0;
        int k = (players.size() - 1);
        for (Player player : players) {
            if (getCurrentPlayer() != player) {
                if (!player.getWorker1().isActive() && !player.getWorker2().isActive()) {
                    deads++;
                }
            }
        }
        if(deads == k){
            // TODO: notify win (SomeoneWinServer)
            return true;
        }
        return false;
    }
}

