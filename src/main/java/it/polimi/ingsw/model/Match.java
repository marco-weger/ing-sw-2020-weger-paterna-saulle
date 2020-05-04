package it.polimi.ingsw.model;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.commons.ServerMessage;
import it.polimi.ingsw.commons.Status;
import it.polimi.ingsw.commons.servermessages.*;
import it.polimi.ingsw.model.cards.CardName;
import it.polimi.ingsw.network.VirtualView;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Match extends Observable implements Serializable {

    /**
     * Unique id to discriminate the match
     */
    private final int id;

    /**
     * The board
     */
    private Board board;

    /**
     * List of players (2 or 3)
     */
    private List<Player> players;

    /**
     * List of losers
     */
    private final List<Player> losers;

    /**
     * Status of the match
     */
    private Status status;

    /**
     * True if the match is ended
     */
    private boolean ended;

    /**
     * This list is used during cards matching
     */
    private ArrayList<CardName> selectedCard;

    /**
     * The match is initialized with these;
     * - BOARD: 5x5 cell board with all cells at 0 level
     * - ENDED: false
     * - STATUS: CARD_CHOICE
     * - PLAYERS: empty list
     * - SELECTEDCARD: empty list
     * - LOSERS: empty list
     * @param id the unique match id
     */
    public Match(int id, VirtualView vv){
        this.id=id;
        this.board=new Board();
        this.ended=false;
        this.status=Status.NAME_CHOICE;
        this.players = new ArrayList<>();
        this.losers = new ArrayList<>();
        this.selectedCard = new ArrayList<>();
        if(vv != null)
            this.addObserver(vv);
    }

    public int getId() { return id; }

    public boolean isEnded() { return ended; }

    public void setEnded(boolean ended) { this.ended = ended; }

    public Status getStatus() { return status; }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public List<CardName> getSelectedCard(){ return selectedCard; }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Player> getLosers() {
        return losers;
    }

    /**
     * When the status changes the view will be notified with current one and current player
     * @param status the new status
     */
    public void setStatus(Status status) {
        this.status = status;

        if(status.equals(Status.WORKER_CHOICE) && getCurrentPlayer() == null)
            players.get(0).setCurrent(true);

        if(getCurrentPlayer() != null){
            CurrentStatusServer message = new CurrentStatusServer(getCurrentPlayer().getName(),status);
            if (status.equals(Status.START)){
                getCurrentPlayer().setCurrentWorker(1);
                message.worker1 = !getCurrentPlayer().getCard().checkMove(players,board).isEmpty();
                getCurrentPlayer().setCurrentWorker(2);
                message.worker2 = !getCurrentPlayer().getCard().checkMove(players,board).isEmpty();
                getCurrentPlayer().setCurrentWorker(0);
            }
            if(status.getNotify())
                notifyObservers(message);
        }
        else
            notifyObservers(new CurrentStatusServer("",status));

        if(status.equals(Status.CARD_CHOICE))
            notifyObservers(new AvailableCardServer(this.players.get(0).getName(),new ArrayList<>()));

    }

    /**
     * A method that add a player into the Loser List and remove it from the Active Player List
     * @param p select a player
     */
    public void setLosers(List<Player> p, boolean isTimesUp) {
        if(p.size() == 1 && p.get(0).isCurrent()){
            players.get((players.indexOf(getCurrentPlayer())+1)%(players.size())).setCurrent(true);
        }
        getLosers().addAll(p);
        getPlayers().removeAll(p);
        if(players.size() > 1)
            for (Player player : p)
                notifyObservers(new SomeoneLoseServer(player.getName(),isTimesUp));
        else notifyObservers(new SomeoneWinServer(players.get(0).getName()));

        if(players.size() == 1){
            setEnded(true);
            setStatus(Status.END);
        }
    }

    /**
     * A method that pick the card from the deck and notify the third player to choose.
     * @param selectedCard the list of card selected
     */
    public void setSelectedCards(List<CardName> selectedCard){
        this.selectedCard = new ArrayList<>();
        this.selectedCard.addAll(selectedCard);
        int i = this.getSelectedCard().size()-1;
        notifyObservers(new AvailableCardServer(this.getPlayers().get(i).getName(),selectedCard));
    }

    /**
     * Current player getter
     */
    public Player getCurrentPlayer(){
        for(Player p:players)
            if(p.isCurrent())
                return p;
        return null;
    }

    /**
     * This method set next player, disable all workers and set START status
     */
    public void setNextPlayer() {
        if(getCurrentPlayer() != null){
            int i = players.indexOf(getCurrentPlayer());
            getCurrentPlayer().setCurrent(false);
            if (i < players.size()-1) {
                players.get(i + 1).setCurrent(true);
            } else {
                players.get(0).setCurrent(true);
            }
        }
        else{
            players.get(0).setCurrent(true);
        }
        for(Player p:players){
            p.getWorker1().setActive(false);
            p.getWorker2().setActive(false);
        }
        status=Status.START;
    }

    /**
     * @return It verifies if the current player win for other players defeat
     */
    public boolean checkCurrentPlayerWin() {
        if(players.size() == 1){
            notifyObservers(new SomeoneWinServer(players.get(0).getName()));
            return true;
        }
        return false;
    }

    /**
     * When a player is added to the list players
     * it notifies the Lobby
     */

    public void addPlayer(Player p){
        getPlayers().add(p);
        ArrayList<String> names = new ArrayList<>();
        for(Player player:players)
            names.add(player.getName());
        notifyObservers(new LobbyServer(names));
    }

    /**
     * When a player is removed to the list players
     * on the Initial phase, it notifies the Lobby
     */
    public void removePlayer(String name){
        if(getStatus().equals(Status.NAME_CHOICE)){
            for(int i = 0; i<getPlayers().size(); i++)
                if(getPlayers().get(i).getName().equals(name))
                    getPlayers().remove(getPlayers().get(i));

            ArrayList<String> names = new ArrayList<>();
            for(Player player:players)
                names.add(player.getName());
            notifyObservers(new LobbyServer(names));
        }
    }

    /**
     * Saving the match in file for server persistence purpose
     */
    public void saveToFile(ServerMessage sm) {
        FileOutputStream out = null;
        ObjectOutputStream objOut;

        if(!new File("resources" +File.separatorChar+"saved-match").exists() && !new File("resources" +File.separatorChar+"saved-match").mkdir())
            return;

        try {

            String path = System.getProperty("user.dir")+File.separatorChar+"resources" +File.separatorChar+"saved-match" + File.separatorChar + String.format("%07d" , this.getId())+".santorini";
            File f = new File(path);
            out = new FileOutputStream(f);
            objOut = new ObjectOutputStream(out);
            objOut.writeObject(this);
            objOut.flush();
            objOut.writeObject(sm);
            objOut.flush();
            objOut.close();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playerReConnection(String name){
        for(Player p : this.players)
            if(p.getName().equals(name))
                this.notifyObservers(new ReConnectionServer(name));
    }
}

